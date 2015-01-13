package com.languagehelp.akka.constructoractor.java.actors;

import akka.japi.Creator;

public class MyActorCreator implements Creator<MyActor> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int initialise;
	public MyActorCreator(int initialise){
	    this.initialise = initialise;
	  }	

	@Override
	public MyActor create() throws Exception {
		return new MyActor(initialise);
	}
	


}
