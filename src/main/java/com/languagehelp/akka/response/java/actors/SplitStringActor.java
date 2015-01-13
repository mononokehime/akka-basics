package com.languagehelp.akka.response.java.actors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import akka.actor.UntypedActor;

/**
 * Splits the string by space character while ignoring spaces within inverted
 * commas
 * 
 * @author MacDermotF
 *
 */
public class SplitStringActor extends UntypedActor {

	@Override
	public void onReceive(final Object message) throws Exception {
		if (message instanceof String) {
			final String work = (String) message;
			// map the words in the sentence and send the result to the
			// MasterActor
			getSender().tell(splitString(work), getSelf());
		} else {
			unhandled(message);
		}

	}

	/**
	 * Evaluate the string and split whilst ignoring spaces within inverted
	 * commas
	 * 
	 * @param message
	 *            the string to parse
	 * @return the list of strings
	 */
	private List<String> splitString(final String message) {
		final List<String> list = new ArrayList<String>();
		final Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(
				message);
		while (m.find()) {
			list.add(m.group(1).replace("\"", ""));
		}
		// Add .replace("\\", "") to remove surrounding escape characters.
		//
		return list;
	}
}
