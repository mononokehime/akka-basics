package com.languagehelp.akka.response.java.messages;

import java.util.Date;

public final class ResponseData {
	private final String ip;
	private final Date time;
	private final String url;
	private final int response;
	private final int length;
	private final int timeMillis;

	public ResponseData(final String ip, final Date time, final String url,
			final int response, final int length, final int timeMillis) {
		this.ip = ip;
		this.time = time;
		this.url = url;
		this.response = response;
		this.length = length;
		this.timeMillis = timeMillis;
	}

	public String getIp() {
		return ip;
	}

	public int getLength() {
		return length;
	}

	public int getResponse() {
		return response;
	}

	public Date getTime() {
		return time;
	}

	public int getTimeMillis() {
		return timeMillis;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return "url requested=" + url + " on " + time
				+ " received response code=" + response + " and took="
				+ timeMillis / 1000.0 + " seconds to process";
	}

}
