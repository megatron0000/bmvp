package cestest.presenters;

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
		// TODO Auto-generated method stub

	}

	@Override
	public void onInit() {
		DronePresenter self = this;
		// TODO Auto-generated method stub
		new Timer().scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				x = x + 20 * (new Random().nextDouble() - 0.5);
				y = y + 20 * (new Random().nextDouble() - 0.5);
				push(new DroneLocationChange() {

					@Override
					public double getY() {
						return y;
					}

					@Override
					public double getX() {
						// TODO Auto-generated method stub
						return x;
					}

					@Override
					public Object getDrone() {
						return self;
					}
				});

			}
		}, 4000, 1000 / 10);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void configure() {
	}

}
