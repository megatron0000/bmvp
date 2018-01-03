package xyz.baudelaplace.bmvp.framework.events;

import xyz.baudelaplace.bmvp.framework.Presenter;

public interface PresenterScheduledDestroyEvent extends Event {
	Presenter<?> getPresenter();
}
