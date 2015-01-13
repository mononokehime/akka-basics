package com.languagehelp.akka.mapreduce.java.messages;

import java.util.List;

public final class MapData {
	private final List<WordCount> dataList;

	public MapData(final List<WordCount> dataList) {
		this.dataList = dataList;
	}

	public List<WordCount> getDataList() {
		return dataList;
	}
}
