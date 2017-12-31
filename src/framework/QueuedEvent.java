package framework;

import framework.events.Event;

public interface QueuedEvent {
	Presenter<?> getPresenter();
	Event getEvent();
}
