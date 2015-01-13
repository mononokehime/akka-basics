package com.languagehelp.akka.response.java.actors;

import java.util.List;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;

import com.languagehelp.akka.mapreduce.java.messages.Result;
import com.languagehelp.akka.response.java.messages.ResponseData;

public class ResponseMasterActor extends UntypedActor {

	private static final String SKIP_VALUE = "HEAD /";
	private static final String SKIP_VALUE_2 = "/customerCare";
	ActorRef splitActor = getContext()
			.actorOf(
					new RoundRobinRouter(5).props(Props
							.create(SplitStringActor.class)), "split");

	ActorRef responseDataActor = getContext()
			.actorOf(
					new RoundRobinRouter(5).props(Props
							.create(ResponseDataActor.class)), "responseData");

	ActorRef aggregateActor = getContext().actorOf(
			Props.create(ResponseAggregateActor.class), "aggregate");

	public ResponseMasterActor(final int maxResponseTimeMS) {
		System.out.println("I'm alive!" + maxResponseTimeMS);
	}

	@Override
	public void onReceive(final Object message) throws Exception {
		if (message instanceof String) {
			// don't process URLs which are just ping values
			if (((String) message).indexOf(SKIP_VALUE) < 0
					&& ((String) message).indexOf(SKIP_VALUE_2) < 0) {
				splitActor.tell(message, getSelf());
			}
		} else if (message instanceof List<?>) {
			responseDataActor.tell(message, getSelf());
		} else if (message instanceof ResponseData) {
			aggregateActor.tell(message, getSelf());
		} else if (message instanceof Result) {
			aggregateActor.forward(message, getContext());
		} else {
			unhandled(message);
		}

	}

}
