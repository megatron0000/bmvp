package framework.events;

import framework.EventDispatchStrategy;

public interface Event {
	default EventDispatchStrategy getStrategy() {
		return EventDispatchStrategy.TREE_CLIMB;
	}

	default boolean isSynchronous() {
		return false;
	}
}
