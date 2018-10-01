package com.mec.classes.server.util;

public class PropertyReader {
//	private static final Map<String, String> propertyMap;
	
//	static {
//		propertyMap = new HashMap<>();
//		
//		InputStream is = PropertyReader.class
//				.getResourceAsStream("/net_config.properties");
//		Properties properties = new Properties();
//		try {
//			properties.load(is);
//			@SuppressWarnings("unchecked")
//			Enumeration<String> names = (Enumeration<String>) properties
//					.propertyNames();
//			while(names.hasMoreElements()) {
//				String name = names.nextElement();
//				String value = properties.getProperty(name);
//				propertyMap.put(name, value);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				is.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	public static String getValue(String key) {
		return key;
//		return propertyMap.get(key);
	}
}
