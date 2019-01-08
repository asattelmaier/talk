package com.app.talk.command;

import java.io.Serializable;

public class Context implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8406049557651554696L;

	
	private static int contentIDCounter = 0;
	
	final int id;

	public Context() {
		this.id = genID();
	}
	
	public int getId() {
		return id;
	}	
	
	private static int genID() {
		return contentIDCounter++;
	}
	
	@Override
	public boolean equals(Object obj) {
		Context context = (Context) obj;
		return context.getId() == this.getId();
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(this.getId());		
	}
}
