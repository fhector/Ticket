/**
 * 
 */
package com.railway.ticket.client.api.bean;

import java.io.Serializable;

/**
 * 订单车票信息
 * 
 * @author ChenXiaohong
 */
public class PassengerTicket implements Serializable {
	private static final long serialVersionUID = 1L;

	/*** 姓名 */
	private String passengerName;
	/*** 证件类型 */
	private String identityType;
	/*** 证件号 */
	private String identityNum;
	/*** 发车日期 */
	private String departureDate;
	/*** 出发地 */
	private String departureStation;
	/*** 出发时间 */
	private String departureTime;
	/*** 目的地 */
	private String arrivalStation;
	/*** 到达时间 */
	private String arrivalTime;
	/*** 票种 */
	private String ticketType;
	/*** 车次 */
	private String trainNo;
	/*** 席别 */
	private String seatType;
	/*** 车厢 */
	private String carNo;
	/*** 席位号 */
	private String seatNumber;
	/*** 票价（元） */
	private String fare;
	/*** 支付状态 */
	private String paymentStatus;

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public String getIdentityNum() {
		return identityNum;
	}

	public void setIdentityNum(String identityNum) {
		this.identityNum = identityNum;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public String getDepartureStation() {
		return departureStation;
	}

	public void setDepartureStation(String departureStation) {
		this.departureStation = departureStation;
	}

	public String getArrivalStation() {
		return arrivalStation;
	}

	public void setArrivalStation(String arrivalStation) {
		this.arrivalStation = arrivalStation;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getFare() {
		return fare;
	}

	public void setFare(String fare) {
		this.fare = fare;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
}