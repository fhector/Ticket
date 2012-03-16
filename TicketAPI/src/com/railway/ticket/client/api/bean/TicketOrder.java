/**
 * 
 */
package com.railway.ticket.client.api.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订单信息
 * @author ChenXiaohong
 *
 */
public class TicketOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	/*** 页面TOKEN*/
	private String token;
	/*** 页面默认值(中文或拼音首字母)*/
	private String textField = "中文或拼音首字母";
	/*** */
	private String checkbox0 = "0";
	
	//列车信息
	/*** 出发日期*/
	private String travelDate;
	/*** 车次*/
	private String trainNo;
	/*** 车次编码*/
	private String trainCode;
	/*** 出发站*/
	private String departureStation;
	/*** 出发站编码*/
	private String departureStationCode;
	/*** 出发时间*/
	private String departureStationTime;
	/*** 终点站*/
	private String arrivalStation;
	/*** 终点站编码*/
	private String arrivalStationCode;
	/*** 终点站到达时间*/
	private String arrivalStationTime;
	/*** 席位类型*/
	private String seatType = "";
	/*** 车票张数*/
	private String ticketOrderNum = "";
	/*** */
	private String levelOrderNum = "000000000000000000000000000000";
	/*** */
	private String cancelFlag = "1";
	/*** */
	private String idMode = "Y";
	/**
	 * 支付方式:
	 * A 网上支付
	 * B 网上预订
	 */
	private String reserveFlag = "A";
	/*** 乘客信息列表 */
	private List<Passenger> passengers;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTextField() {
		return textField;
	}
	public void setTextField(String textField) {
		this.textField = textField;
	}
	public String getCheckbox0() {
		return checkbox0;
	}
	public void setCheckbox0(String checkbox0) {
		this.checkbox0 = checkbox0;
	}
	public String getTravelDate() {
		return travelDate;
	}
	public void setTravelDate(String travelDate) {
		this.travelDate = travelDate;
	}
	public String getTrainNo() {
		return trainNo;
	}
	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}
	public String getTrainCode() {
		return trainCode;
	}
	public void setTrainCode(String trainCode) {
		this.trainCode = trainCode;
	}
	public String getDepartureStation() {
		return departureStation;
	}
	public void setDepartureStation(String departureStation) {
		this.departureStation = departureStation;
	}
	public String getDepartureStationCode() {
		return departureStationCode;
	}
	public void setDepartureStationCode(String departureStationCode) {
		this.departureStationCode = departureStationCode;
	}
	public String getDepartureStationTime() {
		return departureStationTime;
	}
	public void setDepartureStationTime(String departureStationTime) {
		this.departureStationTime = departureStationTime;
	}
	public String getArrivalStation() {
		return arrivalStation;
	}
	public void setArrivalStation(String arrivalStation) {
		this.arrivalStation = arrivalStation;
	}
	public String getArrivalStationCode() {
		return arrivalStationCode;
	}
	public void setArrivalStationCode(String arrivalStationCode) {
		this.arrivalStationCode = arrivalStationCode;
	}
	public String getArrivalStationTime() {
		return arrivalStationTime;
	}
	public void setArrivalStationTime(String arrivalStationTime) {
		this.arrivalStationTime = arrivalStationTime;
	}
	public String getSeatType() {
		return seatType;
	}
	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}
	public String getTicketOrderNum() {
		return ticketOrderNum;
	}
	public void setTicketOrderNum(String ticketOrderNum) {
		this.ticketOrderNum = ticketOrderNum;
	}
	public String getLevelOrderNum() {
		return levelOrderNum;
	}
	public void setLevelOrderNum(String levelOrderNum) {
		this.levelOrderNum = levelOrderNum;
	}
	public String getCancelFlag() {
		return cancelFlag;
	}
	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}
	public String getIdMode() {
		return idMode;
	}
	public void setIdMode(String idMode) {
		this.idMode = idMode;
	}
	public List<Passenger> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}
	public String getReserveFlag() {
		return reserveFlag;
	}
	public void setReserveFlag(String reserveFlag) {
		this.reserveFlag = reserveFlag;
	}
}
