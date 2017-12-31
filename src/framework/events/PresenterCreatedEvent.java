package framework.events;

import framework.EventDispatchStrategy;
import framework.Presenter;

public interface PresenterCreatedEvent extends Event {
	Presenter<?> getParent();

	Presenter<?> getNewPresenter();

	@Override
	default EventDispatchStrategy getStrategy() {
		return EventDispatchStrategy.ROOT_ONLY;
	}
}
