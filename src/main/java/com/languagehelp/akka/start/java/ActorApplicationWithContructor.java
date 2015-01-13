package com.languagehelp.akka.start.java;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActorApplicationWithContructor {

	public static void main(final String[] args) throws Exception {
		final FileInputStream fstream = new FileInputStream("C:/dev/test.txt");
		final BufferedReader br = new BufferedReader(new InputStreamReader(
				fstream));
		String strLine;

		// Read File Line By Line
		while ((strLine = br.readLine()) != null) {
			// Print the content on the console
			// master.tell(strLine, master);
			System.out.println(strLine);
			final List<String> list = new ArrayList<String>();
			final Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*")
					.matcher(strLine);
			while (m.find()) {
				list.add(m.group(1).replace("\"", "")); // Add .replace("\\",
				// "") to remove
				// surrounding quotes.
			}
			final int response = new Integer(list.get(4)).intValue();
			final int length = new Integer(list.get(5)).intValue();
			final int responseTime = new Integer(list.get(6)).intValue();
			System.out.println("response:" + response);
			System.out.println("length:" + length);
			System.out.println("responseTime:" + responseTime);
			System.out.println(list);
			System.out.println(list.size());

		}

		// Close the input stream
		br.close();
		fstream.close();

	}

}
