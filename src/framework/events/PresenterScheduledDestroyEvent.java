package framework.events;

import framework.Presenter;

public interface PresenterScheduledDestroyEvent extends Event {
	Presenter<?> getPresenter();
}
