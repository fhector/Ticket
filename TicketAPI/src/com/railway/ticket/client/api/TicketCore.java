package com.railway.ticket.client.api;

import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railway.ticket.client.api.bean.Contact;
import com.railway.ticket.client.api.bean.Result;
import com.railway.ticket.client.api.bean.TicketOrder;
import com.railway.ticket.client.api.bean.TrainVotes;
import com.railway.ticket.client.config.Config;
import com.railway.ticket.client.utils.HtmlUtils;
import com.railway.ticket.client.utils.HttpUtils;
import com.railway.ticket.client.utils.RequestParameter;
import com.railway.ticket.client.utils.ResultResolve;

/**
 * 对12306购票网站部分功能的封装.
 * 返回的数据均使用{@link Result}类封装
 * @author ChenXiaohong
 * @author chenxiaohong@mail.com
 * @version 1.0.0
 */
public class TicketCore {
	private static final Logger logger = LoggerFactory.getLogger(TicketCore.class);
	private HttpClient client = null;
	
	/**
	 * 构造方法.
	 * 自动初始化{@link HttpClient}对象
	 */
	public TicketCore() {
		//创建HttpClient对象
		client = HttpUtils.createClient();
	}
	
	/**
	 * 用户登录
	 * @param username 用户名
	 * @param password 密码
	 * @param randcode 验证码
	 * @return {@link Result}登录成功attach包含用户信息
	 */
	public Result login(String username, String password, String randcode) {
		logger.debug("用户登录");
		String response = HttpUtils.post(client, Config.LOGIN_URL, 
				RequestParameter.getLoginParams(username, password, randcode));
		//解析结果并返回Result对象
		return ResultResolve.resolveLogin(response);
	}
	
	/** 余票查询
	 * @param from 出发城市
	 * @param to 到达城市
	 * @param depart 乘车日期
	 * @param rang 乘车时间区间
	 * @param trainNo 车次
	 * @param trainClass 车次类型
	 * @param trainType 车次类型范围
	 * @return {@link Result}
	 */
	public Result queryVotes(String from, String to, String depart,
			String rang, String trainNo, String trainClass, String trainType) {
		logger.debug("查询余票信息");
		String response = HttpUtils.get(client, Config.ORDER_SINGLE_URL, 
				RequestParameter.getVotesParams(from, to, depart, rang, trainNo, trainClass, trainType));
		return ResultResolve.resolveVotes(depart, response);
	}
	
	/**
	 * 查询列车停靠站信息
	 * @param trainCode 车次编码
	 * @param fromCode 起始站
	 * @param toCode 到站
	 * @param departDate 出发日期
	 * @return {@link Result}
	 */
	public Result queryTrainStopInfo(String trainCode, String fromCode, String toCode, String departDate) {
		logger.debug("查询列车停靠站信息");
		String response = HttpUtils.get(client, Config.ORDER_SINGLE_URL, 
				RequestParameter.getTrainStopQuery(trainCode, fromCode, toCode, departDate));
		return ResultResolve.resolveTrainStopInfo(response);
	}
	
	/**
	 * 查询起始站和终到站之间的所有车次
	 * @param date 出发日期
	 * @param fromStationCode 起始站编码
	 * @param toStationCode 终到站编码
	 * @param startTime 乘车时间区间
	 * @return {@link Result}
	 */
	public Result queryStationAllTrain(String date, String fromStationCode, String toStationCode, String startTime) {
		logger.debug("查询始发终至车次信息");
		String response = HttpUtils.get(client, Config.ORDER_SINGLE_URL, 
				RequestParameter.getStationAllTrain(date, fromStationCode, toStationCode, startTime));
		return ResultResolve.resolveStationAllTrain(response);
	}
	
	/** 
	 * 请求确认订单
	 * @param train 车次信息
	 * @param traveRoundTime 乘车时段
	 * @return {@link Result}
	 */
	public Result requestOrder(TrainVotes train, String traveRoundTime) {
		logger.debug("确认订单信息");
		String response = HttpUtils.post(client, Config.ORDER_SINGLE_URL, 
				RequestParameter.getRequestOrderParams(train, traveRoundTime));
		return ResultResolve.resolveRequestOrder(response);
	}
	
	/**
	 * 提交订单
	 * @param order
	 * @param randcode 验证码
	 * @return {@link Result}
	 */
	public Result takeOrder(TicketOrder order, String randcode) {
		logger.debug("提交订单");
		String response = HttpUtils.post(client, Config.ORDER_SUBMIT_URL, 
				RequestParameter.getTakeOrderParams(order, randcode));
		return ResultResolve.resolveTakeOrder(response);
	}
	
	/**
	 * 取消订单
	 * @param orderNo 订单编号
	 * @param token
	 * @param batchNo 1#
	 * @param cancel_flag 2
	 * @return {@link Result}
	 */
	public Result cancelOrder(String orderNo, String token, String batchNo, String cancel_flag) {
		logger.debug("取消订单");
		String response = HttpUtils.post(client, Config.ORDER_CANCEL_URL, 
				RequestParameter.getCancelOrderParams(orderNo, token, batchNo, cancel_flag));
		return ResultResolve.resolveCancelOrder(response);
	}
	
	/**
	 * 查询未完成的订单
	 * @return {@link Result}
	 */
	public Result queryNotCompleteOrder() {
		logger.debug("查询未完成的订单");
		String response = HttpUtils.get(client, Config.ORDER_QUERY_URL, RequestParameter.getNotCompleteParams());
		return ResultResolve.resolveNotCompleteOrder(response);
	}
	
	/** 查询常用联系人列表 
	 * @param pageIndex 页数(从0开始)
	 * @param pageSize
	 * @param contactName
	 * @return {@link Result}
	 */
	public Result queryContactList(int pageIndex, int pageSize, String contactName) {
		logger.debug("查询常用联系人列表");
		String response = HttpUtils.post(client, Config.CONTACT_MAINTAIN_URL, 
				RequestParameter.getQueryContactParams(pageIndex, pageSize, (null == contactName ? "" : contactName)));
		return ResultResolve.resolveContactList(response);
	}
	
	/**
	 * 添加常用联系人.
	 * 联系人添加需经过2次Http访问获取页面TOKEN后才能进行联系人的创建
	 * @param contact 联系人信息
	 * @return {@link Result}
	 */
	public Result createContact(Contact contact) {
		logger.debug("加载联系人页面..");
		String init_token = HtmlUtils.getToken(HttpUtils.get(client, Config.CONTACT_MAINTAIN_URL, 
				RequestParameter.getInitContactParams()));
		logger.debug("联系人页面加载成功,Token:{}。加载联系人添加页面..", init_token);
		String add_token = HtmlUtils.getToken(HttpUtils.get(client, Config.CONTACT_MAINTAIN_URL, 
				RequestParameter.getInitAddContactParams(init_token)));
		logger.debug("联系人添加页面加载成功,Token:{}。发送创建联系人请求..", add_token);
		String response = HttpUtils.post(client, Config.CONTACT_MAINTAIN_URL, 
				RequestParameter.getCreateContactParams(add_token, contact));
		return ResultResolve.resolveContactCreate(response);
	}
	
	/** 删除联系人
	 * @param contactName 姓名
	 * @param cardType 证件类型
	 * @param cardNo 证件号码
	 * @param passengerType 乘客类型
	 * @return {@link Result}
	 */
	public Result deleteContact(String contactName, String cardType, String cardNo, String passengerType) {
		logger.debug("加载联系人页面..");
		String init_token = HtmlUtils.getToken(HttpUtils.get(client, Config.CONTACT_MAINTAIN_URL, 
				RequestParameter.getInitContactParams()));
		logger.debug("联系人页面加载成功,Token:{}。加载联系人添加页面..", init_token);
		String del_token = HtmlUtils.getToken(HttpUtils.get(client, Config.CONTACT_MAINTAIN_URL, 
				RequestParameter.getInitAddContactParams(init_token)));
		logger.debug("联系人编辑页面加载成功,Token:{}。发送删除联系人请求..", del_token);
		String response = HttpUtils.post(client, Config.CONTACT_MAINTAIN_URL, 
				RequestParameter.getDeleteContactParams(del_token, contactName, cardType, cardNo, passengerType));
		return ResultResolve.resolveContactDelete(response);
	}
	
	/**
	 * 获取登录验证码
	 * @return 失败时数据长度为0
	 */
	public byte[] getLoginCode() {
		logger.debug("获取登录验证码");
		byte[] bytes = HttpUtils.get2byte(client, Config.LOGIN_CODE_URL);
		logger.debug("获取登录验证码,状态:{}", (null != bytes && bytes.length > 0) ? 
				Result.SC_OK : Result.SC_FAIL);
		return bytes;
	}
	
	/**
	 * 订单提交验证码
	 * @return {@link Result}
	 */
	public byte[] getOrderCode() {
		logger.debug("获取订单提交验证码");
		byte[] bytes = HttpUtils.get2byte(client, Config.ORDER_CODE_URL);
		logger.debug("获取订单提交验证码,状态:{}", (null != bytes && bytes.length > 0) ? 
				Result.SC_OK : Result.SC_FAIL);
		return bytes;
	}
	
	/**
	 * 修改用户密码
	 */
	public Result changePassword(String password, String newPassword) {
		logger.debug("加载密码修改页面..");
		String token =  HtmlUtils.getToken(HttpUtils.get(client, Config.USER_MAINTAIN_URL, RequestParameter.getInitChangePwdParams()));
		logger.debug("加载密码修改页面成功,Token:{}", token);
		String response = HttpUtils.post(client, Config.USER_MAINTAIN_URL, 
				RequestParameter.getChangePasswordParams(token, password, newPassword));
		return ResultResolve.resolveChangePassword(response);
	}
	
	/**
	 * 注销登录
	 * @return {@link Result} 返回值无需处理
	 */
	public Result logout() {
		Result result = new Result();
		logger.debug("注销登录");
		HttpUtils.get(client, Config.LOGIN_URL, RequestParameter.getLogoutParams());
		result.setCode(Result.SC_OK);
		result.setMsg("已注销登录");
		client.getConnectionManager().closeExpiredConnections();
		return result;
	}
}