package com.mec.classes;

public class SpecialMessage implements INetMessage {
	private String action;
	private String message;
	private String special;
	private String common;
	
	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public String getCommon() {
		return common;
	}

	public void setCommon(String common) {
		this.common = common;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getFrom() {
		return "CLIENT";
	}

	@Override
	public String getTo() {
		return "SERVER";
	}

	@Override
	public int getType() {
		return 3;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getAction() {
		return action;
	}

	@Override
	public String toString() {
		return "SpecialMessage [action=" + action + ", message=" + message + ", special=" + special + ", common="
				+ common + "]";
	}
}
