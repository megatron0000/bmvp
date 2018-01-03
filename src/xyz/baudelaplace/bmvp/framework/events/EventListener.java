package xyz.baudelaplace.bmvp.framework.events;

public interface EventListener<T extends Event> {
	void onEvent(T e);
}
