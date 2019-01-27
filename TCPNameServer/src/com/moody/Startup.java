package com.moody;

public class Startup {

	public static void main(String[] args) {
		new NameServerHandler().start(5555);
	}

}
