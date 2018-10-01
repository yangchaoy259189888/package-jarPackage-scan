package com.mec.classes.server.test;

import java.util.Scanner;

import com.mec.classes.server.core.INetMessageListener;
import com.mec.classes.server.core.MecServer;

public class MecServerMonitor implements Runnable, INetMessageListener {
	private MecServer mecServer;
	private Scanner scanner;
	
	public MecServerMonitor() {
		mecServer = new MecServer();
		mecServer.addNetMessageListener(this);
		scanner = new Scanner(System.in);
	}
	
	public void beginServerMonitor() {
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		String command;
		
		System.out.println("寮�濮嬭緭鍏ユ湇鍔″櫒鎺у埗鍛戒护:");
		while(true) {
			command = scanner.next();
			if(command.equalsIgnoreCase("exit")) {
				if(mecServer.isServerStartup()) {
					System.out.println("鏈嶅姟鍣ㄤ负瀹曟満锛屼笉鑳介��鍑猴紒");
				} else {
					break;
				}
			} else if(command.equalsIgnoreCase("startup")) {
				mecServer.startup();
			} else if(command.equalsIgnoreCase("shutdown")) {
				mecServer.shutdown();
			}
		}
		
		System.out.println("鍐嶈锛�");
	}

	@Override
	public void onMessage(String message) {
		System.out.println(message);
	}

}
