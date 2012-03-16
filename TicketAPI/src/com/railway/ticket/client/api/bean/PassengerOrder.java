package com.railway.ticket.client.api.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订单信息
 * 
 * @author ChenXiaohong
 * 
 */
public class PassengerOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	/*** 页面TOKEN */
	private String token;
	/*** 订单号码 */
	private String sequenceNo;
	/*** 页面batchNo*/
	private String batchNo;
	/*** 订单取消标志 */
	private String cancelFlag;
	/*** 总票价 */
	private float totalPrice;
	/*** 失效时间 */
	private long loseTime;
	/*** 开始时间 */
	private long beginTime;
	/*** 车票张数 */
	private int ticketNum;
	/*** 欲购张数 */
	private int requiredNum;
	/*** 车票信息,包含乘客信息 */
	private List<PassengerTicket> tickets;
	/*** 支付网关路径 */
	private String payGateWay;
	/*** 支付Form内容 */
	private String payGateWayField;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getCancelFlag() {
		return cancelFlag;
	}

	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}

	public List<PassengerTicket> getTickets() {
		return tickets;
	}

	public void setTickets(List<PassengerTicket> tickets) {
		this.tickets = tickets;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public long getLoseTime() {
		return loseTime;
	}

	public void setLoseTime(long loseTime) {
		this.loseTime = loseTime;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public int getTicketNum() {
		return ticketNum;
	}

	public void setTicketNum(int ticketNum) {
		this.ticketNum = ticketNum;
	}

	public int getRequiredNum() {
		return requiredNum;
	}

	public void setRequiredNum(int requiredNum) {
		this.requiredNum = requiredNum;
	}

	public String getPayGateWay() {
		return payGateWay;
	}

	public void setPayGateWay(String payGateWay) {
		this.payGateWay = payGateWay;
	}

	public String getPayGateWayField() {
		return payGateWayField;
	}

	public void setPayGateWayField(String payGateWayField) {
		this.payGateWayField = payGateWayField;
	}
}
