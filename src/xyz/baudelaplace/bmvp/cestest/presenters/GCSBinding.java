package xyz.baudelaplace.bmvp.cestest.presenters;

import java.awt.geom.Point2D;

import xyz.baudelaplace.bmvp.framework.binding.Binding;

public class GCSBinding implements Binding {
	@Override
	public Object clone() throws CloneNotSupportedException {
		return this;
	}
	
	public GCSBinding(Point2D dronePos) {
		droneInitPos = dronePos;
	}
	
	public Point2D droneInitPos;
}
