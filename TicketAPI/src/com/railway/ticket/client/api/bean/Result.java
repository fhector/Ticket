/**
 * 类
 */
package com.railway.ticket.client.api.bean;

import java.io.Serializable;

/**
 * 结果信息
 * @author ChenXiaohong
 */
public class Result implements Serializable {
	private static final long serialVersionUID = 1L;
	/*** 状态编码*/
	private int code = SC_FAIL;
	/*** 提示信息*/
	private String msg;
	/*** 附加对象*/
	private Object attach;
	
	//: 编码定义-START
	/*** 成功*/
	public static final int SC_OK = 200;
	/*** 验证码错误*/
	public static final int SC_VERIFYCODE = 406;
	/*** 账户已锁定*/
	public static final int SC_LOCKED = 423;
	/*** 没有内容*/
	public static final int SC_NOCONENT = 204;
	/*** 失败*/
	public static final int SC_FAIL = 302;
	/*** 重复提交*/
	public static final int SC_DUPLICATE = 400;
	/*** 登录已失效或登录验证错误*/
	public static final int SC_UNAUTHORIZED = 401;
	/*** 因用户过多无法处理请求*/
	public static final int SC_FORBIDDEN = 403;
	/*** 请求超时*/
	public static final int SC_TIMEOUT = 408;
	/*** 无服务*/
	public static final int SC_UNSERVICE = 503;
	//:~编码定义-END
	
	/** 根据信息判断状态
	 * @param msg 信息
	 * @return 状态码
	 */
	public static int toCode(String msg) {
		int code = SC_FAIL;
		if(msg.contains("密码")) {
			code = Result.SC_UNAUTHORIZED;
		} else if(msg.contains("锁定")) {
			code = Result.SC_LOCKED;
		} else if(msg.contains("验证码")) {
			code = Result.SC_VERIFYCODE;
		} else if(msg.contains("过多")) {
			//请求过多无法处理
			code = Result.SC_FORBIDDEN;
		} else if(msg.contains("重复提交")) {
			code = Result.SC_DUPLICATE;
		} else if(msg.contains("维护")) {
			code = Result.SC_UNSERVICE;
		}
		return code;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public void setSuccess() {
		this.code = Result.SC_OK;
	}
	public String getMsg() {
		if(null == msg) {
			msg = "请求出现响应问题,请检查网络或稍后重新访问!";
		}
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getAttach() {
		return attach;
	}
	public void setAttach(Object attach) {
		this.attach = attach;
	}
	
	public boolean isSuccess() {
		return SC_OK <= code && code <= Result.SC_NOCONENT;
	}
}
