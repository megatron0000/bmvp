package cestest.presenters;

import java.awt.geom.Point2D;
import java.util.HashMap;

import com.google.inject.Inject;

import cestest.events.DroneCreationRequest;
import cestest.events.DroneLocationChange;
import cestest.views.UTMView;
import framework.OutBinding;
import framework.Presenter;
import framework.views.ViewAdapter;

public class UTMPresenter extends Presenter<UTMBinding> {

	@Inject
	ViewAdapter adapter;

	private OutBinding<UTMBinding> viewB;
	
	private HashMap<Object, Point2D> drones = new HashMap<>();
	
	private UTMBinding binding = new UTMBinding(drones);
	
	@Override
	public void onBindingChanged(UTMBinding newBinding) {
		System.out.println("UTMPresenter.onBindingChanged(UTMBinding newBinding)");
	}

	@Override
	public void onInit() {
		System.out.println("UTMPresenter.onInit()");
	}

	@Override
	public void onDestroy() {
		System.out.println("UTMPresenter.onDestroy()");
	}

	@Override
	protected void configure() {
		viewB = declareView(UTMView.class);
		createChild(GCSPresenter.class);
		createChild(GCSPresenter.class);

		registerListener(DroneLocationChange.class, (e) -> {
			drones.put(e.getDrone(), e.getPosition());
			viewB.set(binding);
		});

		registerListener(DroneCreationRequest.class, event -> {
			createChild(GCSPresenter.class).set(new GCSBinding(new Point2D() {

				@Override
				public void setLocation(double x, double y) {
					// TODO Auto-generated method stub

				}

				@Override
				public double getY() {
					return event.getY();
				}

				@Override
				public double getX() {
					return event.getX();
				}
			}));

		});
	}

}
