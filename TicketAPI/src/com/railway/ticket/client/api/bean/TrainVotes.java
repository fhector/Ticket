/**
 * 类
 */
package com.railway.ticket.client.api.bean;

import java.io.Serializable;

/**
 * 车次余票信息
 * 
 * @author ChenXiaohong
 */
public class TrainVotes implements Serializable {
	private static final long serialVersionUID = 1L;

	/*** 车次 TXX */
	private String trainNo;
	/*** 车次编码 2XXXXXXTXX0C */
	private String trainCode;
	/*** 发站 */
	private String departureStation;
	/*** 发时 */
	private String departureTime;
	/*** 到站 */
	private String arrivalStation;
	/*** 到时 */
	private String arrivalTime;
	/*** 历时 */
	private String period;
	/*** 无座 */
	private int noneSet;
	/*** 硬座 */
	private int hardSeat;
	/*** 软座 */
	private int softSeat;
	/*** 一等座 */
	private int firstCalssSeat;
	/*** 二等座 */
	private int seacondSeat;
	/*** 硬卧 */
	private int hardSleeper;
	/*** 软卧 */
	private int softSleeper;
	/*** 高级软卧 */
	private int deluxeSoftSleeper;
	/*** 商务座 */
	private int businessSeat;
	/*** 特等座 */
	private int principalSeat;
	/*** 其它 */
	private int otherSeat; 
	/*** 预订信息,为<code>null</code>时表示车次票已售完,无法再进行预订 */
	private String bookingInfo; 
	// 用于车票预订
	/*** 发站编码 */
	private String departureStationCode; 
	/*** 到站编码 */
	private String arrivalStationCode; 
	/*** 乘车日期 */
	private String travelDate; 

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

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getArrivalStation() {
		return arrivalStation;
	}

	public void setArrivalStation(String arrivalStation) {
		this.arrivalStation = arrivalStation;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public int getNoneSet() {
		return noneSet;
	}

	public void setNoneSet(int noneSet) {
		this.noneSet = noneSet;
	}

	public int getHardSeat() {
		return hardSeat;
	}

	public void setHardSeat(int hardSeat) {
		this.hardSeat = hardSeat;
	}

	public int getSoftSeat() {
		return softSeat;
	}

	public void setSoftSeat(int softSeat) {
		this.softSeat = softSeat;
	}

	public int getFirstCalssSeat() {
		return firstCalssSeat;
	}

	public void setFirstCalssSeat(int firstCalssSeat) {
		this.firstCalssSeat = firstCalssSeat;
	}

	public int getSeacondSeat() {
		return seacondSeat;
	}

	public void setSeacondSeat(int seacondSeat) {
		this.seacondSeat = seacondSeat;
	}

	public int getHardSleeper() {
		return hardSleeper;
	}

	public void setHardSleeper(int hardSleeper) {
		this.hardSleeper = hardSleeper;
	}

	public int getSoftSleeper() {
		return softSleeper;
	}

	public void setSoftSleeper(int softSleeper) {
		this.softSleeper = softSleeper;
	}

	public int getDeluxeSoftSleeper() {
		return deluxeSoftSleeper;
	}

	public void setDeluxeSoftSleeper(int deluxeSoftSleeper) {
		this.deluxeSoftSleeper = deluxeSoftSleeper;
	}

	public int getBusinessSeat() {
		return businessSeat;
	}

	public void setBusinessSeat(int businessSeat) {
		this.businessSeat = businessSeat;
	}

	public int getPrincipalSeat() {
		return principalSeat;
	}

	public void setPrincipalSeat(int principalSeat) {
		this.principalSeat = principalSeat;
	}

	public int getOtherSeat() {
		return otherSeat;
	}

	public void setOtherSeat(int otherSeat) {
		this.otherSeat = otherSeat;
	}

	/**
	 * 预订信息,为<code>null</code>时表示车次票已售完,无法再进行预订
	 * 
	 * @return 包含预订车票时所需的信息,不能预订的车次没有该信息
	 */
	public String getBookingInfo() {
		return bookingInfo;
	}

	public void setBookingInfo(String bookingInfo) {
		this.bookingInfo = bookingInfo;
	}

	public String getDepartureStationCode() {
		return departureStationCode;
	}

	public void setDepartureStationCode(String departureStationCode) {
		this.departureStationCode = departureStationCode;
	}

	public String getArrivalStationCode() {
		return arrivalStationCode;
	}

	public void setArrivalStationCode(String arrivalStationCode) {
		this.arrivalStationCode = arrivalStationCode;
	}

	public String getTravelDate() {
		return travelDate;
	}

	public void setTravelDate(String travelDate) {
		this.travelDate = travelDate;
	}
}
