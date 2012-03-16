package com.railway.ticket.client.utils;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.railway.ticket.client.api.bean.Contact;
import com.railway.ticket.client.api.bean.Passenger;
import com.railway.ticket.client.api.bean.TicketOrder;
import com.railway.ticket.client.api.bean.TrainVotes;
import com.railway.ticket.client.config.Config;

/**
 * 各请求参数设置
 * @author ChenXiaohong
 */
public class RequestParameter {
	/**
	 * 用户登录参数
	 * @param username 用户名
	 * @param password 密码
	 * @param randcode 验证码
	 * @return {@link List}<{@link NameValuePair}>参数列表
	 */
	public static List<NameValuePair> getLoginParams(String username, String password, String randcode) {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "login"));
		params.add(new BasicNameValuePair("loginUser.user_name", username));
		params.add(new BasicNameValuePair("nameErrorFocus", ""));
		params.add(new BasicNameValuePair("passwordErrorFocus", ""));
		params.add(new BasicNameValuePair("randCode", randcode));
		params.add(new BasicNameValuePair("randErrorFocus", ""));
		params.add(new BasicNameValuePair("user.password", password));
		return params;
	}
	
	/**
	 * 生成车次查询参数
	 * @param from 出发城市
	 * @param to 到达城市
	 * @param depart 乘车日期
	 * @param rang 乘车时间区间
	 * @param trainNo 车次
	 * @param trainClass 车次类型
	 * @param trainType 车次类型范围
	 * @return  {@link List}<{@link NameValuePair}>参数列表
	 */
	public static List<NameValuePair> getVotesParams(String from, String to, String depart,
			String rang, String trainNo, String trainClass, String trainType) {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "queryLeftTicket"));
		params.add(new BasicNameValuePair("includeStudent", "00"));
		params.add(new BasicNameValuePair("orderRequest.from_station_telecode", from));
		params.add(new BasicNameValuePair("orderRequest.start_time_str", rang));
		params.add(new BasicNameValuePair("orderRequest.to_station_telecode", to));
		params.add(new BasicNameValuePair("orderRequest.train_date", depart));
		params.add(new BasicNameValuePair("orderRequest.train_no", trainNo));
		params.add(new BasicNameValuePair("seatTypeAndNum", ""));
		params.add(new BasicNameValuePair("trainClass", trainClass));
		params.add(new BasicNameValuePair("trainPassType", trainType));
		return params;
	}
	
	/**
	 * 生成列车停靠站信息查询参数
	 * @param trainCode 车次编码
	 * @param from 起始站
	 * @param to 到站
	 * @param depart 出发日期
	 * @return {@link List}<{@link NameValuePair}>参数列表
	 */
	public static List<NameValuePair> getTrainStopQuery(String trainCode, String from, String to , String depart) {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "queryaTrainStopTimeByTrainNo"));
		params.add(new BasicNameValuePair("train_no", trainCode));
		params.add(new BasicNameValuePair("from_station_telecode", from));
		params.add(new BasicNameValuePair("to_station_telecode", to));
		params.add(new BasicNameValuePair("depart_date", depart));
		return params;
	}
	
	/**
	 * 生成起终点站之间的所有车次信息参数
	 * @param date 乘车日期
	 * @param fromStationCode 起点站编码
	 * @param toStationCode 终到站编码
	 * @param startTime 乘车时间区间
	 * @return {@link List}<{@link NameValuePair}>参数列表
	 */
	public static List<NameValuePair> getStationAllTrain(String date, String fromStationCode, String toStationCode, String startTime) {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("date", date));
		params.add(new BasicNameValuePair("fromstation", fromStationCode));
		params.add(new BasicNameValuePair("tostation", toStationCode));
		params.add(new BasicNameValuePair("starttime", startTime));
		return params;
	}
	
	/**
	 * 生成订单请求参数
	 * @param train 车次信息
	 * @param traveRoundTime 乘车时段
	 * @return {@link List}<{@link NameValuePair}>参数列表
	 */
	public static List<NameValuePair> getRequestOrderParams(TrainVotes train, String traveRoundTime) {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "submutOrderRequest"));
		params.add(new BasicNameValuePair("station_train_code", train.getTrainNo()));
		params.add(new BasicNameValuePair("train_date", train.getTravelDate()));
		params.add(new BasicNameValuePair("seattype_num",""));
		params.add(new BasicNameValuePair("from_station_telecode",train.getDepartureStationCode()));
		params.add(new BasicNameValuePair("to_station_telecode",train.getArrivalStationCode()));
		params.add(new BasicNameValuePair("include_student","00"));
		params.add(new BasicNameValuePair("from_station_telecode_name",train.getDepartureStation())); //设置编号
		params.add(new BasicNameValuePair("to_station_telecode_name",train.getArrivalStation())); //设置编号
		params.add(new BasicNameValuePair("round_train_date", HtmlUtils.getDate()));
		params.add(new BasicNameValuePair("round_start_time_str","00:00--24:00")); 
		params.add(new BasicNameValuePair("single_round_type","1"));
		params.add(new BasicNameValuePair("train_pass_type","QB"));
		params.add(new BasicNameValuePair("train_class_arr","QB#D#Z#T#K#QT#"));
		params.add(new BasicNameValuePair("start_time_str", traveRoundTime));	//乘车时间区间
		params.add(new BasicNameValuePair("lishi", train.getPeriod()));   
		params.add(new BasicNameValuePair("train_start_time",train.getDepartureTime()));
		params.add(new BasicNameValuePair("trainno",train.getTrainCode()));
		params.add(new BasicNameValuePair("arrive_time",train.getArrivalTime()));
		params.add(new BasicNameValuePair("from_station_name",train.getDepartureStation()));
		params.add(new BasicNameValuePair("to_station_name",train.getArrivalStation()));
		String[] bookngInfo = HtmlUtils.splitByRegx(train.getBookingInfo()+"#", "(.*?)#");
		params.add(new BasicNameValuePair("ypInfoDetail", bookngInfo[bookngInfo.length-1]));
		return params;
	}
	
	/** 订单提交参数
	 * @param order
	 * @param randcode
	 * @return {@link List}<{@link NameValuePair}>参数列表
	 */
	public static List<NameValuePair> getTakeOrderParams(TicketOrder order, String randcode) {
		List<NameValuePair> parameters = new LinkedList<NameValuePair>();
		parameters.add(new BasicNameValuePair("org.apache.struts.taglib.html.TOKEN", order.getToken()));
		parameters.add(new BasicNameValuePair("textfield", order.getTextField()));
		parameters.add(new BasicNameValuePair("checkbox0", order.getCheckbox0()));
		//车次信息
		parameters.add(new BasicNameValuePair("orderRequest.train_date", order.getTravelDate()));
		parameters.add(new BasicNameValuePair("orderRequest.train_no", order.getTrainNo()));
		parameters.add(new BasicNameValuePair("orderRequest.station_train_code", order.getTrainCode()));
		parameters.add(new BasicNameValuePair("orderRequest.from_station_telecode", order.getDepartureStationCode()));
		parameters.add(new BasicNameValuePair("orderRequest.to_station_telecode", order.getArrivalStationCode()));
		parameters.add(new BasicNameValuePair("orderRequest.seat_type_code", order.getSeatType()));
		parameters .add(new BasicNameValuePair("orderRequest.ticket_type_order_num", order.getTicketOrderNum()));
		parameters.add(new BasicNameValuePair("orderRequest.bed_level_order_num", order.getLevelOrderNum()));
		//发到站信息
		parameters.add(new BasicNameValuePair("orderRequest.start_time", order.getDepartureStationTime()));
		parameters.add(new BasicNameValuePair("orderRequest.end_time", order.getArrivalStationTime()));
		parameters.add(new BasicNameValuePair("orderRequest.from_station_name", order.getDepartureStation()));
		parameters.add(new BasicNameValuePair("orderRequest.to_station_name", order.getArrivalStation()));
		parameters.add(new BasicNameValuePair("orderRequest.cancel_flag", order.getCancelFlag()));
		parameters.add(new BasicNameValuePair("orderRequest.id_mode", order.getIdMode()));
		
		//设置乘车人信息
		List<Passenger> passengers = order.getPassengers();
		//一次最多只能购买5张车票
		for (int i = 0; i < Config.MAXPASSENGER; i++) {
			Passenger passenger = (passengers.size() >= (i+1)) ? passengers.get(i) : null;
			if(null != passenger) {
				parameters.add(new BasicNameValuePair("passengerTickets", passenger.getPassengerTickets()));
			}
			parameters.add(new BasicNameValuePair("oldPassengers", null != passenger ? passenger.getOldPassengers() : ""));
			if(null != passenger) {
				parameters.add(new BasicNameValuePair("passenger_"+(i+1)+"_seat", passenger.getPassengerSeat()));
				parameters.add(new BasicNameValuePair("passenger_"+(i+1)+"_ticket", passenger.getPassengerTicket()));
				parameters.add(new BasicNameValuePair("passenger_"+(i+1)+"_name", passenger.getPassengerName()));
				parameters.add(new BasicNameValuePair("passenger_"+(i+1)+"_cardtype", passenger.getPassengerCardtype()));
				parameters.add(new BasicNameValuePair("passenger_"+(i+1)+"_cardno", passenger.getPassengerCardno()));
				parameters.add(new BasicNameValuePair("passenger_"+(i+1)+"_mobileno", passenger.getPassengerMobileno()));
			}
			parameters.add(new BasicNameValuePair("checkbox9", "Y"));
		}
		parameters.add(new BasicNameValuePair("randCode", randcode));
		parameters.add(new BasicNameValuePair("orderRequest.reserve_flag", order.getReserveFlag()));
		return parameters;
	}
	
	/**
	 * 生成取消订单参数列表
	 * @param orderNo 订单编号
	 * @param token
	 * @param batchNo
	 * @param cancel_flag
	 * @return {@link List}<{@link NameValuePair}>参数列表
	 */
	public static List<NameValuePair> getCancelOrderParams(String orderNo, String token, String batchNo, String cancel_flag) {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("org.apache.struts.taglib.html.TOKEN", token));
		params.add(new BasicNameValuePair("sequence_no", orderNo));
		params.add(new BasicNameValuePair("batch_no", batchNo));
		params.add(new BasicNameValuePair("orderRequest.train_date", ""));
		params.add(new BasicNameValuePair("orderRequest.train_no", ""));
		params.add(new BasicNameValuePair("orderRequest.station_train_code", ""));
		params.add(new BasicNameValuePair("orderRequest.from_station_telecode", ""));
		params.add(new BasicNameValuePair("orderRequest.to_station_telecode", ""));
		params.add(new BasicNameValuePair("orderRequest.seat_type_code", ""));
		params.add(new BasicNameValuePair("orderRequest.ticket_type_order_num", ""));
		params.add(new BasicNameValuePair("orderRequest.bed_level_order_num", "000000000000000000000000000000"));
		params.add(new BasicNameValuePair("orderRequest.start_time", ""));
		params.add(new BasicNameValuePair("orderRequest.end_time", ""));
		params.add(new BasicNameValuePair("orderRequest.from_station_name", ""));
		params.add(new BasicNameValuePair("orderRequest.to_station_name", ""));
		params.add(new BasicNameValuePair("orderRequest.cancel_flag", cancel_flag));
		params.add(new BasicNameValuePair("orderRequest.id_mode", ""));
		return params;
	}
	
	/**
	 * 查询未完成的订单
	 * @return {@link List}<{@link NameValuePair}>参数列表
	 */
	public static List<NameValuePair> getNotCompleteParams() {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "queryMyOrderNotComplete"));
		params.add(new BasicNameValuePair("leftmenu", "Y"));
		return params;
	}
	
	/**
	 * 常用联系人查询参数
	 * @param pageIndex 当前页
	 * @param pageSize 页数量
	 * @param contactName 联系人名称
	 * @return {@link List}<{@link NameValuePair}>参数列表
	 */
	public static List<NameValuePair> getQueryContactParams(int pageIndex, int pageSize, String contactName) {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "queryPagePassenger"));
		params.add(new BasicNameValuePair("pageIndex", String.valueOf(pageIndex)));
		params.add(new BasicNameValuePair("pageSize", String.valueOf(pageSize)));
		params.add(new BasicNameValuePair("passenger_name", (null == contactName ? "请输入汉字或拼音首字母" : contactName)));
		return params;
	}

	/**
	 * 初始化联系人页面参数
	 * @return {@link List}<{@link NameValuePair}>参数列表
	 */
	public static List<NameValuePair> getInitContactParams() {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "initUsualPassenger"));
		return params;
	}
	
	/**
	 * 初始化添加联系人页面参数
	 * @param token 通过{@link RequestParameter#getInitContactParams()}获取参数发送GET请求获取页面Token
	 * @return {@link List}<{@link NameValuePair}>参数列表
	 */
	public static List<NameValuePair> getInitAddContactParams(String token) {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "initAddPassenger"));
		params.add(new BasicNameValuePair("org.apache.struts.taglib.html.TOKEN", token));
		params.add(new BasicNameValuePair("name", ""));
		params.add(new BasicNameValuePair("card_type", ""));
		params.add(new BasicNameValuePair("card_no", ""));
		params.add(new BasicNameValuePair("passenger_type", ""));
		params.add(new BasicNameValuePair("search", "请输入汉字或拼音首字母"));
		return params;
	}
	
	/** 生成添加联系人参数
	 * @return {@link List}<{@link NameValuePair}>参数列表
	 */
	public static List<NameValuePair> getCreateContactParams(String token, Contact contact) {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "savePassenger"));
		params.add(new BasicNameValuePair("org.apache.struts.taglib.html.TOKEN", token));
		params.add(new BasicNameValuePair("name",contact.getName()));
		params.add(new BasicNameValuePair("sex_code",contact.getGender()));                                                                     
		params.add(new BasicNameValuePair("born_date",contact.getBornDate()));                                                            
		params.add(new BasicNameValuePair("country_code",contact.getCountryCode()));                                                                    
		params.add(new BasicNameValuePair("card_type",contact.getCardType()));                                                                     
		params.add(new BasicNameValuePair("card_no",contact.getCardNo()));                                                    
		params.add(new BasicNameValuePair("passenger_type",contact.getPsgTypeCode()));
		params.add(new BasicNameValuePair("mobile_no",contact.getMobileNo()));
		params.add(new BasicNameValuePair("phone_no",contact.getPhoneNo()));
		params.add(new BasicNameValuePair("email",contact.getEmail()));                                                                                                           
		params.add(new BasicNameValuePair("address",contact.getAddress()));                                                                                                         
		params.add(new BasicNameValuePair("postalcode",contact.getPostalCode()));                                                                                                      
		params.add(new BasicNameValuePair("studentInfo.province_code","11"));
		params.add(new BasicNameValuePair("studentInfo.school_code",""));
		params.add(new BasicNameValuePair("studentInfo.school_name","简码/汉字"));
		params.add(new BasicNameValuePair("studentInfo.department",""));
		params.add(new BasicNameValuePair("studentInfo.school_class",""));
		params.add(new BasicNameValuePair("studentInfo.student_no",""));
		params.add(new BasicNameValuePair("studentInfo.school_system","4"));
		params.add(new BasicNameValuePair("studentInfo.enter_year","2001"));
		params.add(new BasicNameValuePair("studentInfo.preference_card_no",""));
		params.add(new BasicNameValuePair("studentInfo.preference_from_station_name","简码/汉字"));                                                             
		params.add(new BasicNameValuePair("studentInfo.preference_from_station_code",""));
		params.add(new BasicNameValuePair("studentInfo.preference_to_station_name","简码/汉字"));                                                             
		params.add(new BasicNameValuePair("studentInfo.preference_to_station_code",""));
		return params;
	}
	
	/**
	 * 生成删除联系人参数
	 * @param token 
	 * @param contactName 姓名
	 * @param cardType 证件类型
	 * @param cardNo 证件号码
	 * @param passengerType 联系人类型
	 * @return {@link List}<{@link NameValuePair}>参数列表
	 */
	public static List<NameValuePair> getDeleteContactParams(String token, String contactName, String cardType, String cardNo, String passengerType) {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("org.apache.struts.taglib.html.TOKEN", token));
		params.add(new BasicNameValuePair("method", "deletePassenger"));
		params.add(new BasicNameValuePair("name", contactName + "#"));
		params.add(new BasicNameValuePair("card_type", cardType));
		params.add(new BasicNameValuePair("card_no", cardNo + "#"));
		params.add(new BasicNameValuePair("passenger_type", passengerType + "#"));
		params.add(new BasicNameValuePair("search", "请输入汉字或拼音首字母"));
		return params;
	}
	
	/** 
	 * 密码修改页面参数
	 * @return {@link List}<{@link NameValuePair}>参数列表
	 */
	public static List<NameValuePair> getInitChangePwdParams() {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "initForChangePwd"));
		return params;
	}
	
	/** 生成密码修改参数列表
	 * @param token 页面TOKEN
	 * @param password 原密码
	 * @param newPassword 新密码
	 * @return {@link List}<{@link NameValuePair}>参数列表
	 */
	public static List<NameValuePair> getChangePasswordParams(String token, String password, String newPassword) {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "changePwd"));
		params.add(new BasicNameValuePair("org.apache.struts.taglib.html.TOKEN", token));
		params.add(new BasicNameValuePair("confirmPassWord", password));
		params.add(new BasicNameValuePair("user.password", password));
		params.add(new BasicNameValuePair("user.password_new", newPassword));
		return params;
	}
	
	/**
	 * 注销登录
	 * @return  {@link List}<{@link NameValuePair}>参数列表
	 */
	public static List<NameValuePair> getLogoutParams() {
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("method", "logout"));
		return params;
	}
	
}