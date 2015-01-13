package com.languagehelp.akka.start.java;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;

import com.languagehelp.akka.mapreduce.java.messages.Result;
import com.languagehelp.akka.response.java.actors.ResponseActorCreator;
import com.languagehelp.akka.response.java.messages.ResponseData;

final class AverageResults {

	private final String url;
	private final int numberOfRequests;
	private final int averageResponseTime;

	public AverageResults(final String url, final int numberOfRequests,
			final int averageResponseTime) {
		this.url = url;
		this.numberOfRequests = numberOfRequests;
		this.averageResponseTime = averageResponseTime;
	}

	public double getAverageResponseTime() {
		return averageResponseTime / 1000.0;
	}

	public int getNumberOfRequests() {
		return numberOfRequests;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return "The url " + url + " had " + numberOfRequests
				+ " requests and the  average response time was "
				+ getAverageResponseTime() + " seconds";
	}
}

public class ResponseTimeAggregatorApplication {

	private static void addLongResponse(final ResponseData data) {
		if (data.getTimeMillis() > 3000) {
			List<ResponseData> longResponseList;
			// System.out.println("processing long response");
			if (longResponseMap.containsKey(LONG_RESPONSE_KEY)) {
				longResponseList = longResponseMap.get(LONG_RESPONSE_KEY);
				longResponseList.add(data);
			} else {
				longResponseList = new ArrayList<ResponseData>();
				longResponseList.add(data);
				longResponseMap.put(LONG_RESPONSE_KEY, longResponseList);
			}
		}
	}

	private static List<AverageResults> aggregateResults(
			final Map<String, List<ResponseData>> results) {
		final List<AverageResults> resultsList = new ArrayList<AverageResults>();
		for (final String key : results.keySet()) {
			final List<ResponseData> resultForURL = results.get(key);
			// now calculate the average response time
			double totalResponseTimeForURL = 0;
			for (final ResponseData data : resultForURL) {
				addLongResponse(data);
				totalResponseTimeForURL += data.getTimeMillis();

			}

			// System.out.println("totalResponseTimeForURL:"
			// + totalResponseTimeForURL);
			final int averageForURL = (int) (totalResponseTimeForURL / resultForURL
					.size());
			// System.out.println(key + " shows average response time "
			// + averageForURL + " for " + resultForURL.size()
			// + " requests");
			TOTAL_REQUESTS += resultForURL.size();
			final AverageResults average = new AverageResults(key,
					resultForURL.size(), averageForURL);
			resultsList.add(average);
		}
		return resultsList;

	}

	public static void main(final String[] args) throws Exception {
		System.currentTimeMillis();
		final Timeout timeout = new Timeout(
				Duration.create(5, TimeUnit.SECONDS));
		final ActorSystem _system = ActorSystem.create("ResponseApp");
		// = _system.actorOf(Props.create(new MyActorCreator(1)), "master");
		final ActorRef master = _system.actorOf(
				Props.create(new ResponseActorCreator(1)), "master");
		// Open the file
		// final FileInputStream fstream = new
		// FileInputStream("C:/dev/test.txt");
		final FileInputStream fstream = new FileInputStream(
				"C:/dev/logs/IDJAH APP and INT LOGS/APP02/server_access_log.2015-01-04.txt");
		final BufferedReader br = new BufferedReader(new InputStreamReader(
				fstream));
		String strLine;

		// Read File Line By Line
		while ((strLine = br.readLine()) != null) {
			// Print the content on the console
			master.tell(strLine, master);
			// System.out.println (strLine);
		}

		// Close the input stream
		br.close();
		fstream.close();

		// master.tell("The quick brown fox tried to jump over the lazy dog and fell on the dog",
		// master);
		// master.tell("Dog is man's best friend", master);
		// master.tell("Dog and Fox belong to the same family", master);
		Thread.sleep(3000);
		final Future<Object> future = Patterns.ask(master, new Result(),
				timeout);
		final Map<String, List<ResponseData>> results = (Map<String, List<ResponseData>>) Await
				.result(future, timeout.duration());

		class ResponseTimeComparator implements Comparator<ResponseData> {
			@Override
			public int compare(final ResponseData arg0, final ResponseData arg1) {
				// TODO Auto-generated method stub
				return arg1.getTimeMillis() - arg0.getTimeMillis();
			}
		}

		class AverageTimeComparator implements Comparator<AverageResults> {
			@Override
			public int compare(final AverageResults arg0,
					final AverageResults arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAverageResponseTime() > arg0
						.getAverageResponseTime()) {
					return 1;
				} else if (arg1.getAverageResponseTime() == arg0
						.getAverageResponseTime()) {
					return 0;
				} else {
					return -1;
				}
			}
		}
		// final Map<String, List<ResponseData>> sorted = new TreeMap<String,
		// List<ResponseData>>(
		// (Map<? extends String, ? extends List<ResponseData>>) new
		// ResponseTimeComparator());
		final List<AverageResults> sortedResults = aggregateResults(results);

		Collections.sort(sortedResults, new AverageTimeComparator());
		for (final AverageResults data : sortedResults) {
			System.out.println(data);
		}
		final List<ResponseData> longResponses = longResponseMap
				.get(LONG_RESPONSE_KEY);
		Collections.sort(longResponses, new ResponseTimeComparator());
		System.out.println("*********************************");
		System.out.println("Out of " + TOTAL_REQUESTS + " requests, "
				+ longResponses.size()
				+ " requests took longer than 3 seconds.");
		for (final ResponseData data : longResponses) {
			System.out.println(data);
		}
		System.out.println("*********************************");
		_system.shutdown();
		System.currentTimeMillis();
	}

	private final static Map<String, List<ResponseData>> longResponseMap = new HashMap<String, List<ResponseData>>();
	private final static String LONG_RESPONSE_KEY = "longResponses";
	private static int TOTAL_REQUESTS = 0;
}