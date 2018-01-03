package xyz.baudelaplace.bmvp.framework;

import xyz.baudelaplace.bmvp.framework.events.Event;

public class LocalEventDispatcher {

	private Presenter<?> presenter;

	private EventDispatcher dispatcher;

	LocalEventDispatcher(Presenter<?> handlingElement, EventDispatcher dispatcher) {
		presenter = handlingElement;
		this.dispatcher = dispatcher;
	}

	<T extends Event> void push(T eventInstance) {
		dispatcher.push(presenter, eventInstance);
	}

}
