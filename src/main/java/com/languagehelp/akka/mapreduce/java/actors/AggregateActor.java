package com.languagehelp.akka.mapreduce.java.actors;

import java.util.HashMap;
import java.util.Map;

import akka.actor.UntypedActor;

import com.languagehelp.akka.mapreduce.java.messages.ReduceData;
import com.languagehelp.akka.mapreduce.java.messages.Result;

public class AggregateActor extends UntypedActor{
	private final Map<String, Integer> finalReducedMap = new HashMap<String, Integer>();

	private void aggregateInMemoryReduce(final Map<String, Integer> reducedList) {
		Integer count = null;
		for (final String key : reducedList.keySet()) {
			if (finalReducedMap.containsKey(key)) {
				count = reducedList.get(key) + finalReducedMap.get(key);
				finalReducedMap.put(key, count);
			} else {
				finalReducedMap.put(key, reducedList.get(key));
			}
		}
	}
	@Override
	public void onReceive(final Object message) throws Exception {
		System.out.println("AggregateActor thread"+Thread.currentThread().getId());
		if (message instanceof ReduceData) {
			final ReduceData reduceData = (ReduceData) message;
			aggregateInMemoryReduce(reduceData.getReduceDataList());
		} else if (message instanceof Result) {
			getSender().tell(finalReducedMap.toString(), getSelf());
		} else {
			unhandled(message);
		}
	}
}
