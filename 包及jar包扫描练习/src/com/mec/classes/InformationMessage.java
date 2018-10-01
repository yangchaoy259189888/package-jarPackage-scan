package com.mec.classes;

public class InformationMessage implements INetMessage {
	private String from;
	private String to;
	private int type;
	private String action;
	private String message;
	
	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String getFrom() {
		return from;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public String getTo() {
		return to;
	}

	@Override
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
