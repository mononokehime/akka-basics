package com.languagehelp.akka.response.java.actors;

import akka.japi.Creator;

public class ResponseActorCreator implements Creator<ResponseMasterActor> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int maxResponseTimeMS;
	public ResponseActorCreator(int maxResponseTimeMS){
	    this.maxResponseTimeMS = maxResponseTimeMS;
	  }	

	@Override
	public ResponseMasterActor create() throws Exception {
		return new ResponseMasterActor(maxResponseTimeMS);
	}
	


}
