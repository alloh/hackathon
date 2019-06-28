package com.saf.hackathon.pojo;

import java.io.Serializable;

/**
 * this object will handle the error response
 *
 * @author alagat
 *
 */
public class APIError implements Serializable {

	private static final long serialVersionUID = 1L;
	private String code;
	private String msgUser;
	private String msgDeveloper;

	public APIError() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsgUser() {
		return msgUser;
	}

	public void setMsgUser(String msgUser) {
		this.msgUser = msgUser;
	}

	public String getMsgDeveloper() {
		return msgDeveloper;
	}

	public void setMsgDeveloper(String msgDeveloper) {
		this.msgDeveloper = msgDeveloper;
	}

}
