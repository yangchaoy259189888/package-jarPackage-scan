package com.mec.classes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Communication implements Runnable, 
		ICommunicationSpeaker {
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private ICommunicationListener listener;
	
	private boolean goon;
	
	public Communication(Socket socket) throws IOException {
		this.socket = socket;
		this.dis = new DataInputStream(socket.getInputStream());
		this.dos = new DataOutputStream(socket.getOutputStream());
	}
	
	public void startListenning() {
		this.goon = true;
		new Thread(this).start();
	}

	public void sendNetMessage(String message) {
		try {
			dos.writeUTF(message);
		} catch (IOException e) {
			// 瀵规柟涓柇閫氫俊锛屽睘浜庡紓甯镐笅绾�(鎺夌嚎)锛涘簲璇ョ粓姝㈢嚎绋嬶紱
			transmit("瀵规柟寮傚父鎺夌嚎锛屽彂閫佷俊鎭け璐ワ紒");
			close();
		}
	}
	
	public void close() {
		goon = false;
		try {
			if(dis != null) {
				dis.close();
				dis = null;
			}
			if(dos != null) {
				dos.close();
				dos = null;
			}
			if(socket != null && !socket.isClosed()) {
				socket.close();
				socket = null;
			}
		} catch (IOException e) {}
	}
	
	@Override
	public void run() {
		String message = null;
		
		while(goon) {
			try {
				message = dis.readUTF();
				transmit(message);
			} catch (IOException e) {
				transmit("寮傚父鍙戠敓:" + e);
				close();
			}
		}
		close();
	}

	@Override
	public void addCommunicationListener(ICommunicationListener listener) {
		if(this.listener == null || this.listener != listener) {
			this.listener = listener;
		}
	}

	@Override
	public void removeCommunicationListener(ICommunicationListener listener) {
		if(this.listener == listener) {
			this.listener = null;
		}
	}

	@Override
	public void transmit(String message) {
		if(listener == null) {
			return;
		}
		listener.dealMessage(message);
	}

}
