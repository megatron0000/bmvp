package xyz.baudelaplace.bmvp.cestest.events;

import xyz.baudelaplace.bmvp.framework.events.Event;

public interface DroneCreationRequest extends Event {
	double getX();
	double getY();
}
