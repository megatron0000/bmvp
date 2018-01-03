package xyz.baudelaplace.bmvp.framework;

import xyz.baudelaplace.bmvp.framework.binding.Binding;
import xyz.baudelaplace.bmvp.framework.binding.BindingConsumer;
import xyz.baudelaplace.bmvp.framework.events.Event;

public abstract class View<T extends Binding> implements BindingConsumer<T> {

	private LocalEventDispatcher dispatcher;

	protected final <E extends Event> void push(E event) {
		dispatcher.push(event);
	}

	final void setLocalDispatcher(LocalEventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
}
