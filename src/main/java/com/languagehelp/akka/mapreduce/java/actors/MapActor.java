package com.languagehelp.akka.mapreduce.java.actors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import akka.actor.UntypedActor;

import com.languagehelp.akka.mapreduce.java.messages.MapData;
import com.languagehelp.akka.mapreduce.java.messages.WordCount;



public class MapActor extends UntypedActor {
	private final String[] STOP_WORDS = { "a", "am", "an", "and", "are", "as", "at", "be","do", "go", "if", "in", "is", "it", "of", "on", "the", "to" };

	private final List<String> STOP_WORDS_LIST = Arrays.asList(STOP_WORDS);

	private MapData evaluateExpression(final String line) {
		System.out.println("MapActor thread"+Thread.currentThread().getId());
		final List<WordCount> dataList = new ArrayList<WordCount>();
		final StringTokenizer parser = new StringTokenizer(line);
		while (parser.hasMoreTokens()) {
			final String word = parser.nextToken().toLowerCase();
			if (!STOP_WORDS_LIST.contains(word)) {
				dataList.add(new WordCount(word,Integer.valueOf(1)));
			}
		}
		return new MapData(dataList);
	}
	@Override
	public void onReceive(final Object message) throws Exception {
		if (message instanceof String) {
			final String work = (String) message;
			// map the words in the sentence and send the result to the
			// MasterActor
			getSender().tell(evaluateExpression(work), getSelf());
		} else {
			unhandled(message);
		}

	}
}
