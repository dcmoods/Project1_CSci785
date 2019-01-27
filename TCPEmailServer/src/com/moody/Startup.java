package com.moody;

public class Startup {

	public static void main(String[] args) {
		new EmailServerHandler().start(7777);
	}

}
