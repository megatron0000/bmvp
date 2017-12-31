package cestest.events;

import framework.events.Event;

public interface DroneLocationChange extends Event {
	double getX();
	double getY();
	Object getDrone();
}
