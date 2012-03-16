/**
 * 
 */
package com.railway.ticket.client.exception;

/**
 * 购票系统异常
 * 登录失效,系统维护.将抛出该异常
 * @author ChenXiaohong
 */
public class RailWayException extends IllegalStateException {
	private static final long serialVersionUID = 1L;

	public RailWayException() {
		super();
	}
	
	public RailWayException(String msg) {
		super(msg);
	}
	
	public RailWayException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public RailWayException(Throwable e) {
		super(e);
	}
}
