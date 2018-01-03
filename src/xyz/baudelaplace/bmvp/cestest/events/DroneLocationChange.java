package xyz.baudelaplace.bmvp.cestest.events;

import java.awt.geom.Point2D;

import xyz.baudelaplace.bmvp.framework.events.Event;

public interface DroneLocationChange extends Event {
	Point2D getPosition();
	Object getDrone();
}
