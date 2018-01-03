package cestest.events;

import framework.events.Event;

public interface DroneCreationRequest extends Event {
	double getX();
	double getY();
}
