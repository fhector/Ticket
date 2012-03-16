package com.railway.ticket.client.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.railway.ticket.client.api.bean.Contact;
import com.railway.ticket.client.api.bean.PassengerOrder;
import com.railway.ticket.client.api.bean.PassengerTicket;
import com.railway.ticket.client.api.bean.Result;
import com.railway.ticket.client.api.bean.TicketOrder;
import com.railway.ticket.client.api.bean.TrainVotes;
import com.railway.ticket.client.exception.RailWayException;

/**
 * 将页面结果解析为Result
 * @author ChenXiaohong
 */
public class ResultResolve {
	private static final Logger logger = LoggerFactory.getLogger(ResultResolve.class);
	
	/**
	 * 解析登录结果
	 * @param html
	 * @return {@link Result}
	 */
	public static Result resolveLogin(String html) {
		Result result = inspecService(true, html);
		String strIsLogin = HtmlUtils.getVar("isLogin", html);
		if(HtmlUtils.isNotEmpty(strIsLogin)) {
			if(Boolean.parseBoolean(strIsLogin)) {
				//Success
				result.setSuccess();;//
				
				JSONObject uinfo = new JSONObject();
				//用户名
				uinfo.put("u_name", HtmlUtils.getVar("u_name", html));
				//未支付的订单
				int orderCount = Integer.parseInt(HtmlUtils.getVar("unPayOrderCount", html));
				uinfo.put("orderCount", orderCount);
				//用户激活 Y/N(不分大小写)
				uinfo.put("isActive", "Y".equalsIgnoreCase(HtmlUtils.getVar("isActive", html)));
				//提示信息
				String[] pim_info = HtmlUtils.evaluateXPath(html, "//div[@class='pim_font']//p");
				if(uinfo.getBooleanValue("isActive"))
					pim_info[1] = "";
				if(uinfo.getIntValue("orderCount") == 0)
					pim_info[2] = "";
				result.setMsg(pim_info[0] + pim_info[1] +  pim_info[2] +  pim_info[3]);//提示信息
				result.setAttach(uinfo);
			} else {
				//Failed
				String msg = HtmlUtils.getMessage(html);
				result.setCode(Result.SC_FAIL);//
				//判断错误原因
				result.setCode(Result.toCode(msg));
				result.setMsg(msg);
			}
		}
		logger.debug("用户登录,状态:{}", result.getCode());
		return result;
	}
	
	/**
	 * 解析车次查询结果
	 * @param html
	 * @return {@link Result}
	 */
	public static Result resolveVotes (String travelDate, String html) {
		Result result = inspecService(html);
		//替换空格和回车符
		html = html.replaceAll("&nbsp;", "").replace("\\n", "$");
		//分割
		String[] nodes = html.split("\\$");
		List<TrainVotes> votes = new LinkedList<TrainVotes>();
		TrainVotes vote = null;
		for (String node : nodes) {
			vote = new TrainVotes();
			vote.setTravelDate(travelDate);
			//解析并设置余票信息
			String[] vote_arr = HtmlUtils.clean(node).getText().toString().split(",");
			vote.setTrainNo(vote_arr[1]);// 车次
			String codeTag = HtmlUtils.evaluateXPathTag(node, "//span[@class='base_txtdiv']")[0].getAttributeByName("onmouseover");
			String[] trainCodeTag = HtmlUtils.splitByRegx(codeTag, ".*?\\('((\\w+)#(\\w+)#(\\w+))'\\)");
			vote.setTrainCode(trainCodeTag[2]);// 车次编码
			vote.setDepartureStationCode(trainCodeTag[3]);
			vote.setArrivalStationCode(trainCodeTag[4]);
			String[] depart = HtmlUtils.splitByRegx(vote_arr[2], "(.*?)(\\d{2}:\\d{2})");
			vote.setDepartureStation(depart[1]);// 发站
			vote.setDepartureTime(depart[2]);// 发时
			String[] arrival = HtmlUtils.splitByRegx(vote_arr[3], "(.*?)(\\d{2}:\\d{2})");
			vote.setArrivalStation(arrival[1]);// 到站
			vote.setArrivalTime(arrival[2]);// 到时
			vote.setPeriod(vote_arr[4]);// 历时
			vote.setBusinessSeat(HtmlUtils.toVote(vote_arr[5]));// 商务座
			vote.setPrincipalSeat(HtmlUtils.toVote(vote_arr[6]));// 特等座
			vote.setFirstCalssSeat(HtmlUtils.toVote(vote_arr[7]));// 一等座
			vote.setSeacondSeat(HtmlUtils.toVote(vote_arr[8]));// 二等座
			vote.setDeluxeSoftSleeper(HtmlUtils.toVote(vote_arr[9]));// 高级软卧
			vote.setSoftSleeper(HtmlUtils.toVote(vote_arr[10]));// 软卧
			vote.setHardSleeper(HtmlUtils.toVote(vote_arr[11]));// 硬卧
			vote.setSoftSeat(HtmlUtils.toVote(vote_arr[12]));// 软座
			vote.setHardSeat(HtmlUtils.toVote(vote_arr[13]));// 硬座
			vote.setNoneSet(HtmlUtils.toVote(vote_arr[14]));// 无座
			vote.setOtherSeat(HtmlUtils.toVote(vote_arr[15]));//其它
			TagNode[] bookingTag = HtmlUtils.evaluateXPathTag(node, "//input[@class='yuding_u']");
			if(bookingTag.length > 0) {
				String tag = bookingTag[0].getAttributeByName("onclick");
				vote.setBookingInfo(HtmlUtils.splitByRegx(tag, ".*?'(.*?)'")[1]);//预订信息
			}
			votes.add(vote);
		}
		if(votes.size() == 0) {
			result.setCode(Result.SC_NOCONENT);
			result.setMsg("没有符合条件的车次信息");
		} else {
			result.setSuccess();;
			result.setMsg("查找成功");
		}
		result.setAttach(votes);
		logger.debug("余票信息查询,状态:{}", result.getCode());
		return result;
	}
	
	/**
	 * 解析列车停靠站信息
	 * @param html
	 * @return {@link Result}
	 */
	public static Result resolveTrainStopInfo(String html) {
		Result result = inspecService(html);
		JSONArray trainInfo = JSONArray.parseArray(html);
		if(trainInfo.size() == 0) {
			//没有内容
			result.setCode(Result.SC_NOCONENT);
			result.setMsg("没有查询到车次的停靠站信息");
		} else {
			result.setSuccess();;
			result.setMsg("查询成功");
		}
		result.setAttach(trainInfo);
		logger.debug("列车停靠站信息查询,状态:{}", result.getCode());
		return result;
	}
	
	/**
	 * 解析始发终到站之间的所有车次信息查询结果
	 * @param html
	 * @return {@link Result}
	 */
	public static Result resolveStationAllTrain(String html) {
		Result result = inspecService(html);
		List<TrainVotes> train_list = new LinkedList<TrainVotes>();
		//转换为JSONObject数组
		JSONArray train_arr = (JSONArray)JSONObject.parse(html);
		JSONObject train_json;
		TrainVotes train;
		for (Object train_obj : train_arr) {
			train_json = (JSONObject) train_obj;
			train = new TrainVotes();
			train.setTrainNo(train_json.getString("id"));
			train.setTrainCode(train_json.getString("value"));
			train.setDepartureStation(train_json.getString("start_station_name"));
			train.setDepartureTime(train_json.getString("start_time"));
			train.setArrivalStation(train_json.getString("end_station_name"));
			train.setArrivalTime(train_json.getString("end_time"));
			train_list.add(train);
		}
		result.setSuccess();
		result.setAttach(train_list);
		result.setMsg("查询成功");
		logger.debug("始发终至列车信息查询,状态:{}", result.getCode());
		return result;
	}
	
	/**
	 * 解析订单请求结果
	 * @param html
	 * @return {@link Result}
	 */
	public static Result resolveRequestOrder(String html) {
		Result result = inspecService(html);
		//车次
		String trainCode = HtmlUtils.getVar("station_train_code_", html);
		if(HtmlUtils.isNotEmpty(trainCode)) {
			result.setSuccess();;
			TicketOrder order = new TicketOrder();
			TagNode tagNode = HtmlUtils.clean(html);
			order.setToken(HtmlUtils.getToken(tagNode));
			order.setTravelDate(HtmlUtils.getDateCST(HtmlUtils.evaluateXpath(tagNode, "//input[@id='start_date']", "value")));
			order.setTrainCode(HtmlUtils.evaluateXpath(tagNode, "//input[@id='station_train_code']", "value"));
			order.setTrainNo(HtmlUtils.evaluateXpath(tagNode, "//input[@id='train_no']", "value"));
			order.setDepartureStationCode(HtmlUtils.evaluateXpath(tagNode, "//input[@id='from_station_telecode']", "value"));
			order.setArrivalStationCode(HtmlUtils.evaluateXpath(tagNode, "//input[@id='to_station_telecode']", "value"));
			order.setSeatType(HtmlUtils.evaluateXpath(tagNode, "//input[@id='seat_type_code']", "value"));
			order.setTicketOrderNum(HtmlUtils.evaluateXpath(tagNode, "//input[@id='ticket_type_order_num']", "value"));
			order.setLevelOrderNum(HtmlUtils.evaluateXpath(tagNode, "//input[@id='bed_level_order_num']", "value"));
			order.setDepartureStationTime(HtmlUtils.getShortTimeCST(HtmlUtils.evaluateXpath(tagNode, "//input[@id='orderRequest_start_time']", "value")));
			order.setArrivalStationTime(HtmlUtils.getShortTimeCST(HtmlUtils.evaluateXpath(tagNode, "//input[@id='orderRequest_end_time']", "value")));
			order.setDepartureStation(HtmlUtils.evaluateXpath(tagNode, "//input[@id='orderRequest_from_station_name']", "value"));
			order.setArrivalStation(HtmlUtils.evaluateXpath(tagNode, "//input[@id='orderRequest_to_station_name']", "value"));
			order.setCancelFlag(HtmlUtils.evaluateXpath(tagNode, "//input[@id='cancel_flag']", "value"));
			order.setIdMode(HtmlUtils.evaluateXpath(tagNode, "//input[@id='orderRequest_id_mode']", "value"));
//			String lishi_ = HtmlUtils.getVar("lishi_", html);
//			String _type = HtmlUtils.evaluateXpath(tagNode, "//input[@id='_type']", "value");
//			String _train_date_str = HtmlUtils.evaluateXpath(tagNode, "//input[@id='_train_date_str']", "value");
//			String _station_train_code = HtmlUtils.evaluateXpath(tagNode, "//input[@id='_station_train_code']", "value");
			
			result.setAttach(order);
			result.setMsg("订单请求已受理");
		} else {
			result.setCode(Result.SC_FAIL);
			result.setMsg(HtmlUtils.getMessage(html));
		}
		logger.debug("确认订单,状态:{}", result.getCode());
		return result;
	}
	
	/**
	 * 解析订单提交结果
	 * @param html
	 * @return {@link Result}
	 */
	public static Result resolveTakeOrder(String html) {
		Result result = inspecService(html);
		int ticketNum = Integer.parseInt(HtmlUtils.getVar("successTicketNum", html, "0"));
		if(ticketNum > 0) {
			result.setSuccess();;
			TagNode docTagNode = HtmlUtils.clean(html);
			
			PassengerOrder order = new PassengerOrder();	//订单信息
			int requiredNum = Integer.parseInt(HtmlUtils.getVar("requiredNum", html, "0"));
			long loseTime = Long.parseLong(HtmlUtils.getVar("loseTime", html, "0"));
			long beginTime = Long.parseLong(HtmlUtils.getVar("beginTime", html, "0"));
			
			order.setToken(HtmlUtils.getToken(docTagNode));
			order.setSequenceNo(HtmlUtils.evaluateXpath(docTagNode, "//input[@name='sequence_no']", "value"));
			//总票价
			String price = HtmlUtils.evaluateXPath(docTagNode, "//div[@id='Ticket']//ul[@id='Num']//li[@class='red']")[0];
			order.setTotalPrice(Float.parseFloat(price.replace("总票价：", "").replace("元", "")));
			order.setBatchNo(HtmlUtils.evaluateXpath(docTagNode, "//input[@name='batch_no']", "value"));
			order.setTicketNum(ticketNum);
			order.setRequiredNum(requiredNum);
			order.setLoseTime(loseTime);
			order.setBeginTime(beginTime);
			order.setCancelFlag(HtmlUtils.evaluateXpath(docTagNode, "//input[@name='orderRequest.cancel_flag']", "value"));
			//支付Form信息
			TagNode[] epayForm = HtmlUtils.evaluateXPathTag(docTagNode, "//form[@id='epayForm']");
			order.setPayGateWay(epayForm[0].getAttributeByName("action"));
			order.setPayGateWayField(HtmlUtils.getInnerHtml(((TagNode)epayForm[0])));
			//车票信息
			TagNode[] trObjTags = HtmlUtils.evaluateXPathTag(docTagNode, "//table[@class='table_list']//tr");
			trObjTags = Arrays.copyOfRange(trObjTags, 2, trObjTags.length - 1);
			
			List<PassengerTicket> tickets = new LinkedList<PassengerTicket>();
			PassengerTicket ticket = null;
			for (TagNode ticketTagNode : trObjTags) {
				TagNode[] tdObjTag = HtmlUtils.evaluateXPathTag(ticketTagNode, "//td");
				ticket = new PassengerTicket();
				ticket.setPassengerName(HtmlUtils.getTagText(tdObjTag[0]));// 姓名
				ticket.setIdentityType(HtmlUtils.getTagText(tdObjTag[1]));// 证件类型
				ticket.setIdentityNum(HtmlUtils.getTagText(tdObjTag[2]));// 证件号
				ticket.setDepartureDate(HtmlUtils.getDateCST(HtmlUtils.getTagText(tdObjTag[3])));// 发车日期
				String[] departureInfo = HtmlUtils.evaluateXPath((TagNode)tdObjTag[4], "//p");
				ticket.setDepartureStation(departureInfo[0]); // 出发地
				ticket.setDepartureTime(HtmlUtils.getTimeCST(departureInfo[1].replace("开", "")));
				String[] arrivalInfo = HtmlUtils.evaluateXPath((TagNode)tdObjTag[5], "//p");
				ticket.setArrivalStation(arrivalInfo[0]); // 目的地
				ticket.setArrivalTime(HtmlUtils.getTimeCST(arrivalInfo[1].replace("开", "")));
				ticket.setTicketType(HtmlUtils.getTagText(tdObjTag[6]));// 票种
				ticket.setTrainNo(HtmlUtils.getTagText(tdObjTag[7])); // 车次
				ticket.setSeatType(HtmlUtils.getTagText(tdObjTag[8])); // 席别
				ticket.setCarNo(HtmlUtils.getTagText(tdObjTag[9])); // 车厢
				ticket.setSeatNumber(HtmlUtils.getTagText(tdObjTag[10])); // 席位号
				ticket.setFare(HtmlUtils.getTagText(tdObjTag[11])); // 票价（元）
				tickets.add(ticket);
			}
			order.setTickets(tickets);
			result.setAttach(order);
			result.setMsg(HtmlUtils.getTagText(HtmlUtils.evaluateXPathTag(docTagNode, "//div[@class='wc_fontn']//p[@class='f_blue']")[0]));
		} else {
			String msg = HtmlUtils.getMessage(html);
			result.setMsg(msg);
			result.setCode(Result.toCode(msg));
		}
		logger.debug("提交订单,状态:{}", result.getCode());
		return result;
	}
	
	/**
	 * 解析取消订单的结果
	 * @param html
	 * @return {@link Result}
	 */
	public static Result resolveCancelOrder(String html) {
		Result result = inspecService(html);
		String msg = HtmlUtils.getMessage(html);
		result.setMsg(msg);
		if(msg.contains("取消订单成功")) {
			result.setSuccess();;
			result.setMsg("订单:" + HtmlUtils.evaluateXpath(html, "//input[@name='sequence_no']", "value") + "取消成功!");
		} else {
			result.setCode(Result.SC_FAIL);
		}
		logger.debug("取消订单,状态:{}", result.getCode());
		return result;
	}
	
	/**
	 * 解析未完成的订单查询结果
	 * @param html
	 * @return {@link Result}
	 */
	public static Result resolveNotCompleteOrder(String html) {
		Result result = inspecService(html);
		//总价,时间
		int size = Integer.parseInt(HtmlUtils.getVar("size", html, "0"));
		if(size > 0) {
			result.setSuccess();;
			TagNode[] jdanTag = HtmlUtils.evaluateXPathTag(html, "//div[@class='jdan_tfont']//ul//li");
			int totalFare = Integer.parseInt(jdanTag[1].getText().toString().replace("总张数：", ""));
			String orderTime = HtmlUtils.getDateTimeCST(jdanTag[0].getText().toString().replace("订单时间： ", ""));
			String toF =  HtmlUtils.getMatcherValue(html, "总?\\s*票\\s*价:\\s(.*?)\\(")[0];
			result.setMsg(orderTime + "," + totalFare + (HtmlUtils.isNotEmpty(toF) ? "," + toF : ""));
			TagNode[] ticketTags = HtmlUtils.evaluateXPathTag(html, "//table[@class='table_clist']//tr");
			if(ticketTags.length == 0) {
				result.setCode(Result.SC_FAIL);
				result.setMsg(HtmlUtils.evaluateXPath(html, "//div[@class='sign_suc']")[0]);
				return result;
			}
			ticketTags = Arrays.copyOfRange(ticketTags, 1, ticketTags.length - 1);
			List<PassengerTicket> tickets = new LinkedList<PassengerTicket>();
			for (TagNode tagNode : ticketTags) {
				PassengerTicket passenger = new PassengerTicket();
				
				TagNode[] ticket = HtmlUtils.evaluateXPathTag(tagNode, "//td");
				String[] trainTag = ticket[0].getText().toString().trim().replaceAll("[,]?\\s{10,}", "|").split("\\|");
				
				passenger.setDepartureDate(HtmlUtils.getDateCST(trainTag[0])); // 发车日期
				passenger.setTrainNo(trainTag[1]); // 车次
				passenger.setDepartureStation(trainTag[2].split("—")[0]); // 出发地
				passenger.setArrivalStation(trainTag[2].split("—")[1]); // 目的地
				passenger.setDepartureTime(HtmlUtils.getTimeCST(trainTag[3].replace("开", "")));	//出发时间
				trainTag = ticket[1].getText().toString().trim().replaceAll("[,]?\\s{10,}", "|").split("\\|");
				passenger.setCarNo(trainTag[0]); // 车厢
				passenger.setSeatNumber(trainTag[1]); // 席位号
				passenger.setSeatType(trainTag[2]); // 席别
				passenger.setTicketType(trainTag[3]); // 票种
				passenger.setFare(trainTag[4].replace("元", "")); // 票价（元）
				trainTag = ticket[2].getText().toString().trim().replaceAll("[,]?\\s{10,}", "|").split("\\|");
				passenger.setPassengerName(trainTag[0]); // 姓名
				passenger.setIdentityType(trainTag[1]); // 证件类型
				trainTag = ticket[3].getText().toString().trim().replaceAll("[,]?\\s{10,}", "|").split("\\|");
				passenger.setPaymentStatus(trainTag[0]); //支付状态 
				tickets.add(passenger);
			}
			result.setAttach(tickets);
		} else {
			result.setCode(Result.SC_NOCONENT);
			result.setMsg(HtmlUtils.evaluateXPath(html, "//div[@class='sign_suc']")[0]);
		}
		logger.debug("查询未完成的订单,状态:{}", result.getCode());
		return result;
	}
	
	/** 解析联系人查询结果
	 * @param html
	 * @return {@link Result#getAttach()}获取联系人{@link List}
	 */
	public static Result resolveContactList(String html) {
		Result result = inspecService(html);
		List<Contact> contacts = new LinkedList<Contact>();
		//将结果解析为JSONObject数组
		JSONObject contact_obj = JSONObject.parseObject(html);
		JSONArray contact_arr = contact_obj.getJSONArray("rows");
		//读取信息并转换为Contact对象
		for (Object contact_object : contact_arr) {
			Contact contact = new Contact();
			JSONObject cont_obj = (JSONObject) contact_object;
			contact.setCode(cont_obj.getString("code"));
			contact.setName(cont_obj.getString("passenger_name"));
			contact.setGender(cont_obj.getString("sex_code")); //F M
			JSONObject bornDate = JSONObject.parseObject(cont_obj.getString("born_date"));
			contact.setBornDate(HtmlUtils.formatDate(bornDate.getString("time")));
			contact.setCountryCode(cont_obj.getString("country_code"));
			contact.setCardType(cont_obj.getString("passenger_type"));
			contact.setCardNo(cont_obj.getString("passenger_id_no"));
			contact.setPsgTypeCode(cont_obj.getString("passenger_id_type_code"));
			contact.setMobileNo(cont_obj.getString("mobile_no"));
			contact.setPhoneNo(cont_obj.getString("phone_no"));
			contact.setEmail(cont_obj.getString("email"));
			contact.setAddress(cont_obj.getString("address"));
			contact.setPostalCode(cont_obj.getString("postalcode"));
			contacts.add(contact);
		}
		result.setSuccess();
		result.setAttach(contacts);
		result.setMsg("查询成功");
		logger.debug("查询联系人信息,状态:{}", result.getCode());
		return result;
	}
	
	/**
	 * 解析联系人添加结果
	 * @param html
	 * @return {@link Result}
	 */
	public static Result resolveContactCreate(String html) {
		Result result = inspecService(html);
		String msg = HtmlUtils.getMessage(html);
		result.setMsg(msg);
		if(msg.contains("成功")) {
			result.setSuccess();
		} else {
			result.setCode(Result.SC_FAIL);
		}
		logger.debug("添加联系人,状态:{}", result.getCode());
		return result;
	}
	
	/**
	 * 解析联系人删除结果
	 * @param html
	 * @return {@link Result}
	 */
	public static Result resolveContactDelete(String html) {
		Result result = inspecService(html);
		String msg = HtmlUtils.getMessage(html);
		result.setMsg(msg);
		if(msg.contains("成功")) {
			result.setSuccess();
		} else {
			result.setCode(Result.SC_FAIL);
		}
		logger.debug("删除联系人,状态:{}", result.getCode());
		return result;
	}
	
	/**
	 * 解析密码修改结果
	 * @param html
	 * @return {@link Result}
	 */
	public static Result resolveChangePassword(String html) {
		Result result = inspecService(html);
		String msg = HtmlUtils.getMessage(html);
		result.setMsg(msg);
		if(msg.contains("成功")) {
			result.setSuccess();
		} else {
			result.setCode(Result.SC_FAIL);
		}
		logger.debug("删除联系人,状态:{}", result.getCode());
		return result;
	}
	
	
	/** 检查服务是否可用.
	 * 一般只在登录时进行调用该方法
	 * @param isLogin 是否为登录
	 * @param html
	 * @return {@link Result}
	 */
	public static Result inspecService(boolean isLogin, String html) {
		Result result = new Result();
		result.setCode(Result.SC_NOCONENT);
		//判断是否为维护时间
		String[] msgs = HtmlUtils.evaluateXPath(html, "//div[@class='sign_suc']");
		if(msgs.length > 0) {
			result.setCode(Result.SC_UNSERVICE);	//维护
			result.setMsg(msgs[0]);
		}
		//判断是否登录
		String strIsLogin = HtmlUtils.getVar("isLogin", html);
		if(HtmlUtils.isNotEmpty(strIsLogin)) {
			boolean stIsLogin = Boolean.parseBoolean(strIsLogin);
			if(!stIsLogin) {
				result.setCode(Result.SC_UNAUTHORIZED);
				result.setMsg(HtmlUtils.getMessage(html));
			}
		}
		if(!isLogin) {
			//判断是否应该抛出异常提醒
			if(result.getCode() == Result.SC_UNSERVICE || result.getCode() == Result.SC_UNAUTHORIZED) {
				throw new RailWayException(result.getMsg());
			}
		}
		return result;
	}
	
	/**
	 * 检查服务是否可用
	 * @param html
	 * @return {@link Result}
	 */
	public static Result inspecService(String html) {
		return inspecService(false, html);
	}
	
	/** 是否为成功
	 * @param result
	 * @return {@link Boolean}
	 */
	public static boolean isSuccess(Result result) {
		return result.isSuccess();
	}
	
	/**
	 * @param result
	 * @return {@link Boolean}
	 */
	public static boolean isSuccess(JSONObject result) {
		return isSuccess(JSONObject.toJavaObject(result, Result.class));
	}
}
