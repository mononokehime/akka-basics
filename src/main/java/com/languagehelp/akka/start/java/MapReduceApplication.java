package com.languagehelp.akka.start.java;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;

import com.languagehelp.akka.mapreduce.java.actors.MasterActor;
import com.languagehelp.akka.mapreduce.java.messages.Result;

public class MapReduceApplication {

	public static void main(final String[] args) throws Exception{
		final long start = System.currentTimeMillis();
		final Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));
		final ActorSystem _system = ActorSystem.create("MapReduceApp");
		final ActorRef master = _system.actorOf(Props.create(MasterActor.class), "map");
		// Open the file
		final FileInputStream fstream = new FileInputStream("C:/dev/logs/BBM_11Dec2014/BBM_11Dec2014/INT05_astbbmIS.log");
		final BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;

		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   {
			// Print the content on the console
			master.tell(strLine, master);
			// System.out.println (strLine);
		}

		//Close the input stream
		br.close();
		fstream.close();

		//		master.tell("The quick brown fox tried to jump over the lazy dog and fell on the dog", master);
		//		master.tell("Dog is man's best friend", master);
		//		master.tell("Dog and Fox belong to the same family", master);
		Thread.sleep(5000);
		final Future<Object> future = Patterns.ask(master, new Result(), timeout);
		final String result = (String) Await.result(future, timeout.duration());
		System.out.println(result);
		_system.shutdown();
		final long end = System.currentTimeMillis();
		final long timeTook = end - start;
		System.out.println("timeTook:"+timeTook);
	}

}
