package com.languagehelp.akka.mapreduce.java.messages;

public final class WordCount {
	private final String word;
	private final Integer count;

	public WordCount(final String word, final Integer count) {
		this.word = word;
		this.count = count;
	}
	public Integer getCount() {
		return count;
	}
	public String getWord() {
		return word;
	}

}
