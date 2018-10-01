package com.mec.classes;

public interface ICommunicationSpeaker {
	void addCommunicationListener(ICommunicationListener listener);
	void removeCommunicationListener(ICommunicationListener listener);
	void transmit(String message);
}
