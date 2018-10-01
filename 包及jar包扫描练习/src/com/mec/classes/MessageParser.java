package com.mec.classes;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.mec.classes.server.util.PropertyReader;

public class MessageParser {
	/*
		CommandMessage command = new CommandMessage();
		command.setFrom("SERVER");
		command.setTo(clientIp);
		command.setAction("ID");
		command.setType(INetMessage.COMMAND);
		command.setMessage(id);
		// type#to:action?from&key=value
	 * */
	private static Map<String, String> interfaceMethodNameList;
	
	static {
		interfaceMethodNameList = new HashMap<String, String>();
		Method[] methods = INetMessage.class.getDeclaredMethods();
		for(Method method : methods) {
			interfaceMethodNameList.put(method.getName(), method.getName());
		}
	}
	
	public static String messageToOgnl(INetMessage messageObject) {
		StringBuffer str = new StringBuffer();
		
		str.append(messageObject.getType())
				.append("#")
				.append(messageObject.getTo())
				.append(":")
				.append(messageObject.getAction())
				.append("?from=")
				.append(messageObject.getFrom())
				.append("&message=")
				.append(messageObject.getMessage());
		Class<?> klass = messageObject.getClass();
		Method[] methods = klass.getDeclaredMethods();
		for(Method method : methods) {
			String methodName = method.getName();
			
			if(methodName.startsWith("get")) {
				String orgName = interfaceMethodNameList.get(methodName);
				if(orgName != null) {
					continue;
				}
				str.append("&");
				String propertyName = methodName.substring(3, 3+1).toLowerCase()
						+ methodName.substring(4);
				str.append(propertyName);
				str.append("=");
				
				Object result;
				try {
					result = method.invoke(messageObject);
					str.append(result.toString());
				} catch (IllegalAccessException e) {
				} catch (IllegalArgumentException e) {
				} catch (InvocationTargetException e) {
				}
			}
		}
		
		return str.toString();
	}
	
	public static INetMessage ognlToMessage(String ognl) {
		INetMessage message = null;
		
		int sharpIndex = ognl.indexOf("#");
		if(sharpIndex == -1) {
			return null;
		}
		
		String typeNo = ognl.substring(0, sharpIndex);
		
		int colonIndex = ognl.indexOf(":");
		String to = ognl.substring(sharpIndex + 1, colonIndex);
		
		int questionIndex = ognl.indexOf("?");
		String action = ognl.substring(colonIndex + 1, questionIndex);
		
		String messageClassName = PropertyReader.getValue("MessageType" + typeNo);
		try {
			Class<?> messageClass = Class.forName(messageClassName);
			message = (INetMessage) messageClass.newInstance();
			
			try {
				Field toField = messageClass.getDeclaredField("to");
				toField.setAccessible(true);
				toField.set(message, to);
			} catch (NoSuchFieldException e1) {
			} catch (SecurityException e1) {
			}
			
			try {
				Field actionField = messageClass.getDeclaredField("action");
				actionField.setAccessible(true);
				actionField.set(message, action);
			} catch (NoSuchFieldException e1) {
			} catch (SecurityException e1) {
			}
			
			try {
				Field typeField = messageClass.getDeclaredField("type");
				typeField.setAccessible(true);
				typeField.set(message, Integer.valueOf(typeNo));
			} catch (NoSuchFieldException e1) {
			} catch (SecurityException e1) {
			}
			
			String rest = ognl.substring(questionIndex + 1);
			
			String[] keyValues = rest.split("&");
			for(String keyValue : keyValues) {
				int eqIndex = keyValue.indexOf("=");
				String key = keyValue.substring(0, eqIndex);
				String value = keyValue.substring(eqIndex + 1);
				
				try {
					Field field = messageClass.getDeclaredField(key);
					field.setAccessible(true);
					field.set(message, value);
				} catch (NoSuchFieldException e) {
				} catch (SecurityException e) {
				}
			}
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		
		return message;
	}
}
