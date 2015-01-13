package com.languagehelp.akka.constructoractor.java.actors;

import akka.actor.UntypedActor;

public class MyActor extends UntypedActor {
	public MyActor(final int initialise)
	{
		System.out.println("I'm alive!"+initialise);	
	}

	@Override
	public void onReceive(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
