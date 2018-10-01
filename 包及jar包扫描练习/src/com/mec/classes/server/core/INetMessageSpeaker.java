package com.mec.classes.server.core;

public interface INetMessageSpeaker {
	void addNetMessageListener(INetMessageListener listener);
	void removeNetMessageListener(INetMessageListener listener);
	void uploadMessage(String message);
}
