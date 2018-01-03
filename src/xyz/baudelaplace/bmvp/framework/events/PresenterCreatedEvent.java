package xyz.baudelaplace.bmvp.framework.events;

import xyz.baudelaplace.bmvp.framework.EventDispatchStrategy;
import xyz.baudelaplace.bmvp.framework.Presenter;

public interface PresenterCreatedEvent extends Event {
	Presenter<?> getParent();

	Presenter<?> getNewPresenter();

	@Override
	default EventDispatchStrategy getStrategy() {
		return EventDispatchStrategy.ROOT_ONLY;
	}
}
