package com.mec.classes;

public interface INetMessage {
	int INFORMATION = 1;
	int COMMAND = 2;
	
	String getFrom();
	String getTo();
	int getType();
	String getMessage();
	String getAction();
}
