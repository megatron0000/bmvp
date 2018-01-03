package cestest.events;

import java.awt.geom.Point2D;

import framework.events.Event;

public interface DroneLocationChange extends Event {
	Point2D getPosition();
	Object getDrone();
}
