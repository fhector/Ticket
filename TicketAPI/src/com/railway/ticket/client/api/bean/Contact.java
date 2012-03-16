package com.railway.ticket.client.api.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/** 
 * 常用联系人
 * @author chenxiaohong
 */
public class Contact implements Serializable {
	private static final long serialVersionUID = 1L;
	String code;	//编号
	String name;	//姓名
	/*** M男 F女*/
	String gender;	//性别
	String bornDate;	//出生日期
	/*** CN 中国CHINA*/
	String countryCode;	//国家或地区	
	/*** 1:二代身份证 2:一代身份证 C:港澳通行证 G:台湾通行证 B:护照*/
	String cardType;	//证件类型
	String cardNo;	//证件号码
	/*** 1,成人 2,儿童 3,学生 4,伤残军人*/
	String psgTypeCode;	//旅客类型
	String mobileNo;	//手机号码
	String phoneNo;	//固定电话
	String email;	//电子邮件
	String address;	//地址
	String postalCode;	//邮编
	
	String seatType;	//席位类型(订单)
	
	/** 设置联系人信息
	 * @param contact
	 * @see #toString()
	 */
	public void toContact(String[] contact) {
		setCode(contact[1]);
		setName(contact[2]);
		setGender(contact[3]);
		setBornDate(contact[4]);
		setCountryCode(contact[5]);
		setCardType(contact[6]);
		setCardNo(contact[7]);
		setPsgTypeCode(contact[8]);
		setMobileNo(contact[9]);
		setPhoneNo(contact[10]);
		setEmail(contact[11]);
		setAddress(contact[12]);
		setPostalCode(contact[13]);
	}
	
	public String toString() {
		StringBuffer sbf = new StringBuffer();
		sbf.append(code).append(",");
		sbf.append(name).append(",");
		sbf.append(gender).append(",");
		sbf.append(bornDate).append(",");
		sbf.append(countryCode).append(",");
		sbf.append(cardType).append(",");
		sbf.append(cardNo).append(",");
		sbf.append(psgTypeCode).append(",");
		sbf.append(mobileNo).append(",");
		sbf.append(phoneNo).append(",");
		sbf.append(email).append(",");
		sbf.append(address).append(",");
		sbf.append(postalCode);
		return sbf.toString();
	}
	
	private static Map<String, String> cardTypeMap = new HashMap<String, String>();
	
	static {
		cardTypeMap.put("二代身份证", "1");
		cardTypeMap.put("一代身份证", "2");
		cardTypeMap.put("港澳通行证", "C");
		cardTypeMap.put("台湾通行证", "G");
		cardTypeMap.put("护照", "B");
	}
	
	public Contact() {
		this(null, null, null, null, null, null);
	}
	
	/**
	 * @param name 姓名
	 * @param gender 性别 M男 F女
	 * @param bornDate 出生日期
	 * @param cardType 证件类型 1:二代身份证 2:一代身份证 C:港澳通行证 G:台湾通行证 B:护照
	 * @param cardNo 证件号码
	 * @param mobileNo 手机号码
	 */
	public Contact(String name, String gender, String bornDate, String cardType, String cardNo, String mobileNo) {
		this.name = name;
		this.gender = gender;
		this.bornDate = bornDate;
		this.cardType = cardType;
		this.cardNo = cardNo;
		this.mobileNo = mobileNo;
		this.cardType = "1"; //二代身份证
		this.countryCode = "CN"; //中国
		this.psgTypeCode = "1"; //成人
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBornDate() {
		return bornDate;
	}
	public void setBornDate(String bornDate) {
		this.bornDate = bornDate;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		String type = cardTypeMap.get(cardType);
		if(null != type)
			cardType = type;
		this.cardType = cardType;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getPsgTypeCode() {
		return psgTypeCode;
	}
	public void setPsgTypeCode(String psgTypeCode) {
		this.psgTypeCode = psgTypeCode;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getSeatType() {
		return seatType;
	}
	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}
}