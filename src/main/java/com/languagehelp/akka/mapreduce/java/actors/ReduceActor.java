package com.languagehelp.akka.mapreduce.java.actors;

import java.util.HashMap;
import java.util.List;

import akka.actor.UntypedActor;

import com.languagehelp.akka.mapreduce.java.messages.MapData;
import com.languagehelp.akka.mapreduce.java.messages.ReduceData;
import com.languagehelp.akka.mapreduce.java.messages.WordCount;

public class ReduceActor extends UntypedActor{

	@Override
	public void onReceive(final Object message) throws Exception {
		System.out.println("ReduceActor thread"+Thread.currentThread().getId());
		if (message instanceof MapData) {
			final MapData mapData = (MapData) message;
			// reduce the incoming data and forward the result to Master actor
			getSender().tell(reduce(mapData.getDataList()), getSelf());
		} else {
			unhandled(message);
		}

	}
	private ReduceData reduce(final List<WordCount> dataList) {
		final HashMap<String, Integer> reducedMap = new HashMap<String, Integer>();
		for (final WordCount wordCount : dataList) {
			if (reducedMap.containsKey(wordCount.getWord())) {
				Integer value = reducedMap.get(wordCount.getWord());
				value++;
				reducedMap.put(wordCount.getWord(), value);
			} else {
				reducedMap.put(wordCount.getWord(), Integer.valueOf(1));
			}
		}
		return new ReduceData(reducedMap);
	}
}
