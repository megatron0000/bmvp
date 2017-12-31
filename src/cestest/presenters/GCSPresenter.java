package cestest.presenters;

import framework.Presenter;

public class GCSPresenter extends Presenter<GCSBinding> {

	@Override
	public void onBindingChanged(GCSBinding newBinding) {
	}

	@Override
	public void onInit() {

	}

	@Override
	public void onDestroy() {

	}

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		createChild(DronePresenter.class);
		
		// Escuta e emite, re-escuta e re-emite etc.....
		// registerListener(DroneLocationChange.class, (event) -> push(event));
	}

}
