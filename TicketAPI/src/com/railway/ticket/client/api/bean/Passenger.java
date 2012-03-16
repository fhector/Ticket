/**
 * 
 */
package com.railway.ticket.client.api.bean;

import java.io.Serializable;

/**
 * 乘客信息
 * @author ChenXiaohong
 *
 */
public class Passenger implements Serializable {
	private static final long serialVersionUID = 1L;

	/*** 硬卧:3 座位类型,直接使用外围类属性*/
	private String passengerSeat = "3";
	/*** 车票类型*/
	private String passengerTicket = "1";
	/*** 姓名*/
	private String passengerName;
	/*** 二代身份证:1 证件类型*/
	private String passengerCardtype;
	/*** 证件号码*/
	private String passengerCardno;
	/*** 手机号码*/
	private String passengerMobileno;
	
	public Passenger() {
		//
	}
	
	/** 
	 * @param contact 联系人信息
	 */
	public Passenger(Contact contact) {
		setPassengerName(contact.getName());
		setPassengerCardtype(contact.getCardType());
		setPassengerCardno(contact.getCardNo());
		setPassengerMobileno(contact.getMobileNo());
	}
	
	/**
	 * @return	座位类型,车票类型,姓名,证件类型,证件号码,手机号码,Y
	 */
	public String getPassengerTickets() {
		StringBuffer sbf = new StringBuffer();
		sbf.append(passengerSeat).append(",");
		sbf.append(passengerTicket).append(",");
		sbf.append(passengerName).append(",");
		sbf.append(passengerCardtype).append(",");
		sbf.append(passengerCardno).append(",");
		sbf.append(passengerMobileno).append(",");
		sbf.append("Y");
		return sbf.toString();
	}
	/**
	 * @return 姓名,证件类型,证件号码
	 */
	public String getOldPassengers() {
		StringBuffer sbf = new StringBuffer();
		sbf.append(passengerName).append(",");
		sbf.append(passengerCardtype).append(",").append(passengerCardno);
		return sbf.toString();
	}
	public String getPassengerSeat() {
		return passengerSeat;
	}
	public void setPassengerSeat(String passengerSeat) {
		this.passengerSeat = passengerSeat;
	}
	public String getPassengerTicket() {
		return passengerTicket;
	}
	public void setPassengerTicket(String passengerTicket) {
		this.passengerTicket = passengerTicket;
	}
	public String getPassengerName() {
		return passengerName;
	}
	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}
	public String getPassengerCardtype() {
		return passengerCardtype;
	}
	public void setPassengerCardtype(String passengerCardtype) {
		this.passengerCardtype = passengerCardtype;
	}
	public String getPassengerCardno() {
		return passengerCardno;
	}
	public void setPassengerCardno(String passengerCardno) {
		this.passengerCardno = passengerCardno;
	}
	public String getPassengerMobileno() {
		return passengerMobileno;
	}
	public void setPassengerMobileno(String passengerMobileno) {
		this.passengerMobileno = passengerMobileno;
	}
}
