package cestest.presenters;

import framework.OutBinding;
import framework.Presenter;

public class GCSPresenter extends Presenter<GCSBinding> {

	OutBinding<DroneBinding> child;

	@Override
	public void onBindingChanged(GCSBinding newBinding) {
		if (newBinding != null)
			child.set(new DroneBinding(newBinding.droneInitPos.getX(), newBinding.droneInitPos.getY()));
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
		child = createChild(DronePresenter.class);

		// Escuta e emite, re-escuta e re-emite etc.....
		// registerListener(DroneLocationChange.class, (event) -> push(event));
	}

}
