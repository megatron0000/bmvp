package cestest.presenters;

import framework.binding.Binding;

public class DroneBinding implements Binding {

	public double x;
	public double y;

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public DroneBinding(double x, double y) {
		this.x = x;
		this.y = y;
	}

}
