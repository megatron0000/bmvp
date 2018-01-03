package cestest.presenters;

import java.awt.geom.Point2D;
import java.util.HashMap;

import framework.binding.Binding;

public class UTMBinding implements Binding {

	public HashMap<Object, Point2D> drones;

	public UTMBinding(HashMap<Object, Point2D> drones) {
		this.drones = drones;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return this;
	}

}
