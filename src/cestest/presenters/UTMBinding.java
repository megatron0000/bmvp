package cestest.presenters;

import framework.binding.Binding;

public class UTMBinding implements Binding {

	public Object drone;
	public double x;
	public double y;

	public UTMBinding(Object drone, double x, double y) {
		this.drone = drone;
		this.x = x;
		this.y = y;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return this;
	}

}
