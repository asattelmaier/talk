package com.app.talk.command;

import java.io.Serializable;

public class Context implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4393494720187101376L;

	
	final int id;

	public Context(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}	
}
