package xyz.baudelaplace.bmvp.framework.events;

import xyz.baudelaplace.bmvp.framework.EventDispatchStrategy;

public interface Event {
	default EventDispatchStrategy getStrategy() {
		return EventDispatchStrategy.TREE_CLIMB;
	}

	default boolean isSynchronous() {
		return false;
	}
}
