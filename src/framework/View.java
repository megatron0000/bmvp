package framework;

import framework.binding.Binding;
import framework.binding.BindingConsumer;
import framework.events.Event;

public abstract class View<T extends Binding> implements BindingConsumer<T> {

	private LocalEventDispatcher dispatcher;

	protected final <E extends Event> void push(E event) {
		dispatcher.push(event);
	}

	final void setLocalDispatcher(LocalEventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
}
