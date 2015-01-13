package com.languagehelp.akka.response.java.actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import akka.actor.UntypedActor;

import com.languagehelp.akka.mapreduce.java.messages.Result;
import com.languagehelp.akka.response.java.messages.ResponseData;

public class ResponseAggregateActor extends UntypedActor {
	private final Map<String, List<ResponseData>> finalReducedMap = new HashMap<String, List<ResponseData>>();

	private void aggregateInMemoryReduce(final ResponseData responseData) {
		List<ResponseData> responseDataList = new ArrayList<ResponseData>();
		if (finalReducedMap.containsKey(responseData.getUrl())) {
			// System.out.println("adding" + responseData);
			responseDataList = finalReducedMap.get(responseData.getUrl());
			responseDataList.add(responseData);
		} else {
			// System.out.println("adding to new list" + responseData);

			responseDataList.add(responseData);
			finalReducedMap.put(responseData.getUrl(), responseDataList);
		}
	}

	@Override
	public void onReceive(final Object message) throws Exception {
		// System.out.println("AggregateActor thread"
		// + Thread.currentThread().getId());
		if (message instanceof ResponseData) {
			final ResponseData responseData = (ResponseData) message;
			aggregateInMemoryReduce(responseData);
		} else if (message instanceof Result) {
			getSender().tell(finalReducedMap, getSelf());
		} else {
			unhandled(message);
		}
	}
}
