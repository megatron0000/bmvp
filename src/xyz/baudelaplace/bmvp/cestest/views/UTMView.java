package xyz.baudelaplace.bmvp.cestest.views;

import java.awt.Color;
import java.util.HashMap;

import org.piccolo2d.PNode;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

import xyz.baudelaplace.bmvp.cestest.events.DroneCreationRequest;
import xyz.baudelaplace.bmvp.cestest.presenters.UTMBinding;
import xyz.baudelaplace.bmvp.framework.View;

public class UTMView extends View<UTMBinding> {

	private GridExample grid;

	private UTMView self = this;

	HashMap<Object, PNode> drones = new HashMap<>();

	/**
	 * Initially, not checking for PNode initialization led to ghost drones (the
	 * ones created at startup disappeared)
	 */
	@Override
	public void onBindingChanged(UTMBinding newBinding) {
		if (newBinding == null || !grid.getIsInitialized())
			return;

		// System.out.print("view begin: " + grid);
		newBinding.drones.forEach((drone, pos) -> {

			double x = pos.getX();
			double y = pos.getY();

			if (!drones.containsKey(drone)) {
				PNode rect = new PNode();
				// System.out.println("view x2: " + x);
				rect.setBounds(x, y, 40, 40);
				rect.setPaint(Color.RED);
				drones.put(drone, rect);
				// System.out.println("view layer : " + grid.getCanvas().getLayer());
				grid.getCanvas().getLayer().addChild(rect);
				// System.out.println("view end");
			} else {
				drones.get(drone).setBounds(x, y, 40, 40);
			}

		});
	}

	@Override
	public void onInit() {
		grid = new GridExample();
		grid.getCanvas().addInputEventListener(new PBasicInputEventHandler() {
			@Override
			public void mouseClicked(PInputEvent event) {
				System.out.println("click");
				self.push(new DroneCreationRequest() {
					@Override
					public double getX() {
						return event.getPosition().getX();
					}

					@Override
					public double getY() {
						return event.getPosition().getY();
					}
				});
			}
		});
	}

	@Override
	public void onDestroy() {
	}

}
