package com.app.talk.common;

import java.io.Serializable;
import java.util.Scanner;

public class User implements Serializable {
	private static final long serialVersionUID = -8205683033807579298L;
	private String name;

	User(String userName) {
		this.name = userName;
	}
}
