package com.mec.package_scanner.test;

import com.mec.classes.Communication;
import com.mec.package_scanner.core.PackageScanner;

public class Test {
	public static void main(String[] args) {
		// 普通包扫描
		new PackageScanner() {

			@Override
			public void dealClass(Class<?> klass) {
				System.out.println(klass.getName());
			}
		}.packageScan("com.mec.classes");
		System.out.println("-------------------------------------------");
		
		// jar包扫描
		new PackageScanner() {

			@Override
			public void dealClass(Class<?> klass) {
				System.out.println(klass.getName());
			}
		}.packageScan("com.mec.util");
		System.out.println("-------------------------------------------");

		// 根据传入的类进行扫描
		new PackageScanner() {

			@Override
			public void dealClass(Class<?> klass) {
				System.out.println(klass.getName());
			}
		}.packageScan(Communication.class);
	}
}
