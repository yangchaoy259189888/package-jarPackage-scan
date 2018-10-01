package com.mec.package_scanner.core;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class PackageScanner {
	public PackageScanner() {
	}

	public abstract void dealClass(Class<?> klass);

	private void scanPackage(String packageName, File currentFile) {
		// 利用文件过滤器FileFilter,把以.class结尾的文件和文件夹存入fileList
		File[] fileList = currentFile.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					return true;
				}
				return pathname.getName().endsWith(".class");
			}
		});

		for (File file : fileList) {
			// 是文件夹就递归
			if (file.isDirectory()) {
				scanPackage(packageName + "." + file.getName(), file);
			} else {
				String fileName = file.getName().replace(".class", "");
				String className = packageName + "." + fileName;
				// 利用反射机制反射出文件对应的类型
				try {
					Class<?> klass = Class.forName(className);
					if (klass.isAnnotation() || klass.isInterface() || klass.isEnum() || klass.isPrimitive()) {
						continue;
					}
					dealClass(klass);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void scanPackage(URL url) throws IOException {
		JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
		JarFile jarFile = urlConnection.getJarFile();
		Enumeration<JarEntry> jarEntries = jarFile.entries();
		while (jarEntries.hasMoreElements()) {
			JarEntry jarEntry = jarEntries.nextElement();
			String jarName = jarEntry.getName();
			/*
			 * jarName大概为这样： com/ com/mec/ com/mec/util/ com/mec/util/model/
			 * com/mec/util/test/ com/mec/util/Bytes.class com/mec/util/Bytes.java
			 */
			if (jarEntry.isDirectory() || !jarName.endsWith(".class")) {
				continue;
			}
			String className = jarName.replace(".class", "").replaceAll("/", ".");
			try {
				Class<?> klass = Class.forName(className);
				if (klass.isAnnotation() || klass.isInterface() || klass.isEnum() || klass.isPrimitive()) {
					continue;
				}
				dealClass(klass);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
	}

	public void packageScan(Class<?> klass) {
		packageScan(klass.getPackage().getName());
	}

	public void packageScan(String packageName) {
		// 根据包名得到对应路径名
		String packageOppPath = packageName.replace(".", "/");
		// 根据当前线程得到类加载器，进一步得到URL枚举
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try {
			Enumeration<URL> resources = classLoader.getResources(packageOppPath);
			while (resources.hasMoreElements()) {
				URL url = resources.nextElement();
				// 如果是jar包，则调用jar包扫描函数，如果是普通包且文件存在，调用普通包扫描函数
				if (url.getProtocol().equals("jar")) {
					scanPackage(url);
				} else {
					File file = new File(url.toURI());
					if (!file.exists()) {
						continue;
					}
					scanPackage(packageName, file);
				}
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

}