package com.languagehelp.akka.mapreduce.java.actors;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;

import com.languagehelp.akka.mapreduce.java.messages.MapData;
import com.languagehelp.akka.mapreduce.java.messages.ReduceData;
import com.languagehelp.akka.mapreduce.java.messages.Result;

@SuppressWarnings("deprecation")
public class MasterActor extends UntypedActor {

	ActorRef mapActor = getContext().actorOf(new RoundRobinRouter(5).props(Props.create(MapActor.class)), "map");

	ActorRef reduceActor = getContext().actorOf(new RoundRobinRouter(5).props(Props.create(ReduceActor.class)), "reduce");

	ActorRef aggregateActor = getContext().actorOf(Props.create(AggregateActor.class), "aggregate");

	@Override
	public void onReceive(final Object message) throws Exception {
		if (message instanceof String) {
			mapActor.tell(message, getSelf());
		} else if (message instanceof MapData) {
			reduceActor.tell(message, getSelf());
		} else if (message instanceof ReduceData) {
			aggregateActor.tell(message, getSelf());
		} else if (message instanceof Result) {
			aggregateActor.forward(message, getContext());
		} else {
			unhandled(message);
		}

	}

}
