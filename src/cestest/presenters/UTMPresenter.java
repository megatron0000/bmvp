package cestest.presenters;

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
			viewB.set(new UTMBinding(e.getDrone(), e.getX(), e.getY()));
		});

		registerListener(DroneCreationRequest.class, event -> createChild(GCSPresenter.class));
	}

}
