package com.languagehelp.akka.mapreduce.java.messages;

import java.util.HashMap;

public final class ReduceData {
	private final HashMap<String, Integer> reduceDataList;

	public ReduceData(final HashMap<String, Integer> reduceDataList) {
		this.reduceDataList = reduceDataList;
	}

	public HashMap<String, Integer> getReduceDataList() {
		return reduceDataList;
	}

}
