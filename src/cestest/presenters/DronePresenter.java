package cestest.presenters;

import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cestest.events.DroneLocationChange;
import framework.Presenter;

public class DronePresenter extends Presenter<DroneBinding> {

	double x = 200;
	double y = 200;

	@Override
	public void onBindingChanged(DroneBinding newBinding) {
		if (newBinding == null)
			return;
		
		x = newBinding.x;
		y = newBinding.y;
	}

	@Override
	public void onInit() {
		DronePresenter self = this;
		
		// TODO Auto-generated method stub
		new Timer().scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				x = x + 5 * (new Random().nextDouble() - 0.5);
				y = y + 5 * (new Random().nextDouble() - 0.5);
				push(new DroneLocationChange() {

					@Override
					public Point2D getPosition() {
						return new Point2D() {
							@Override
							public double getX() {
								return x;
							}
							@Override
							public double getY() {
								return y;
							}
							@Override
							public void setLocation(double x, double y) {
							}
						};
					}
					
					@Override
					public Object getDrone() {
						return self;
					}
					
					@Override
					public boolean isSynchronous() {
						return true;
					}
					
				});

			}
		}, 0, 1000 / 60);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void configure() {
	}

}
