package com.languagehelp.akka.response.java.actors;

import java.util.Date;
import java.util.List;

import akka.actor.UntypedActor;

import com.languagehelp.akka.response.java.messages.ResponseData;

public class ResponseDataActor extends UntypedActor {

	private static final int COLUMNS = 7;

	private ResponseData buildResponseData(final List<String> work)
			throws Exception {
		if (work.size() != COLUMNS) {
			throw new Exception("Invalid number of entries! It was "
					+ work.size() + " on: " + work);
		}
		final int response = new Integer(work.get(4)).intValue();
		final int length = new Integer(work.get(5)).intValue();
		final int responseTime = new Integer(work.get(6)).intValue();
		String url = work.get(3);
		// strip off any query parameters
		if (url.indexOf("?") > 0) {
			url = url.substring(0, url.indexOf("?"));
		}
		// strip off anything after MSISDN
		if (url.indexOf("/MSISDN") > 0) {
			url = url.substring(0, url.indexOf("/MSISDN") + 7);
		}
		// strip off anything after transaction
		if (url.indexOf("/transaction") > 0) {
			url = url.substring(0, url.indexOf("/transaction") + 12);
		}
		// strip off anything after alias
		if (url.indexOf("/alias") > 0) {
			url = url.substring(0, url.indexOf("/alias") + 6);
		}
		if (url.indexOf("HTTP/1.1") > 0) {
			url = url.substring(0, url.indexOf("HTTP/1.1") - 1);
		}
		if (url.indexOf("/payment") > 0) {
			url = url.substring(0, url.indexOf("/payment") + 8);
		}
		return new ResponseData(work.get(0), new Date(), url, response, length,
				responseTime);
	}

	@Override
	public void onReceive(final Object message) throws Exception {
		if (message instanceof List<?>) {
			@SuppressWarnings("unchecked")
			final List<String> work = (List<String>) message;
			// map the words in the sentence and send the result to the
			// MasterActor
			getSender().tell(buildResponseData(work), getSelf());
		} else {
			unhandled(message);
		}
	}
}
