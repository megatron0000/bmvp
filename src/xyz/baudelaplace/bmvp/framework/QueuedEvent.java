package xyz.baudelaplace.bmvp.framework;

import xyz.baudelaplace.bmvp.framework.events.Event;

public interface QueuedEvent {
	Presenter<?> getPresenter();
	Event getEvent();
}
