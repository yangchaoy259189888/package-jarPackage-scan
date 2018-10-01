package com.mec.classes.server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mec.classes.CommandMessage;
import com.mec.classes.Communication;
import com.mec.classes.ICommunicationListener;
import com.mec.classes.INetMessage;
import com.mec.classes.MessageParser;

public class MecServer implements ICommunicationListener, Runnable,
		INetMessageSpeaker {
	private ServerSocket server;
	private int port;
	private boolean isStartup;
	private boolean continueListenning;
	
	private Map<String, Communication> communicationMap;
	
	private List<INetMessageListener> netMessageListenerList;
	
	private static final int DEFAULT_PORT = 54188;
//	Integer.valueOf(PropertyReader.getValue("port"));
	
	public MecServer() {
		this(DEFAULT_PORT);
	}
	
	public MecServer(int port) {
		this.netMessageListenerList = new ArrayList<>();
		this.communicationMap = new HashMap<>();
		
		this.isStartup = false;
		this.port = port;
	}
	
	public boolean isServerStartup() {
		return isStartup;
	}
	
	public void shutdown() {
		if(!isStartup) {
			uploadMessage("鏈嶅姟鍣ㄥ凡瀹曟満锛屼笉鑳藉啀娆″叧闂紒");
			
			return;
		}
		// TODO 鑻ユ湁鐢ㄦ埛鍦ㄧ嚎锛屽垯锛屼笉鑳藉叧闂�
		continueListenning = false;
		close();
		isStartup = false;
		uploadMessage("鏈嶅姟鍣ㄥ凡瀹曟満锛�");
		
		return;
	}
	
	private void close() {
		try {
			if(server != null && !server.isClosed()) {
				server.close();
				server = null;
			}
		} catch (IOException e) {}
	}
	
	public void startup() {
		if(isStartup) {
			uploadMessage("鏈嶅姟鍣ㄥ凡鍚姩锛屾棤闇�鍐嶆鍚姩锛�");
			return;
		}
		try {
			uploadMessage("寮�濮嬪惎鍔ㄦ湇鍔″櫒鈥︹��");
			server = new ServerSocket(port);
			uploadMessage("鏈嶅姟鍣ㄥ凡鍚姩锛�");
			isStartup = true;
			continueListenning = true;
			new Thread(this).start();
		} catch (IOException e) {
			server = null;
			isStartup = false;
			continueListenning = false;
		}
		
		return;
	}
	
	@Override
	public void run() {
		uploadMessage("寮�濮嬩睛鍚鎴风杩炴帴璇锋眰鈥︹��");
		while(continueListenning) {
			try {
				Socket client = server.accept();
				String clientIp = client.getInetAddress().getHostAddress();
				uploadMessage("渚﹀惉鍒癧" + clientIp + "]锛屽凡杩炴帴鈥︹��");
				
				Communication communication = new Communication(client);
				communication.addCommunicationListener(this);
				communication.startListenning();
				String id = String.valueOf(communication.hashCode());
				
				CommandMessage command = new CommandMessage();
				command.setFrom("SERVER");
				command.setTo(clientIp);
				command.setAction("ID");
				command.setType(INetMessage.COMMAND);
				command.setMessage(id);
				
				communication.sendNetMessage(MessageParser
						.messageToOgnl(command));
				communicationMap.put(id, communication);
			} catch (IOException e) {}
		}
	}

	@Override
	public void dealMessage(String message) {
		String clientIp;
		String mess;
		
		int colonIndex = message.indexOf(":");
		clientIp = message.substring(0, colonIndex);
		mess = message.substring(colonIndex + 1);
		
		if(mess.equals("exit")) {
			Communication communication = communicationMap.get(clientIp);
			communication.sendNetMessage("exit");
			
			communication.close();
			communicationMap.remove(clientIp);
			
			uploadMessage("瀹㈡埛绔��" + clientIp + "銆戞甯镐笅绾匡紒");
		} else {
			uploadMessage(message);
		}
	}

	@Override
	public void addNetMessageListener(INetMessageListener listener) {
		if(netMessageListenerList.contains(listener)) {
			return;
		}
		netMessageListenerList.add(listener);
	}

	@Override
	public void removeNetMessageListener(INetMessageListener listener) {
		if(!netMessageListenerList.contains(listener)) {
			return;
		}
		netMessageListenerList.remove(listener);
	}

	@Override
	public void uploadMessage(String message) {
		for(INetMessageListener listener : netMessageListenerList) {
			listener.onMessage(message);
		}
	}

}
