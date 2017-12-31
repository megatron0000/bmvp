package cestest.views;

import java.awt.Color;
import java.util.HashMap;

import org.piccolo2d.PNode;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

import cestest.events.DroneCreationRequest;
import cestest.presenters.UTMBinding;
import framework.View;

public class UTMView extends View<UTMBinding> {

	private GridExample grid;

	private UTMView self = this;

	HashMap<Object, PNode> drones = new HashMap<>();

	@Override
	public void onBindingChanged(UTMBinding newBinding) {
		if (newBinding == null)
			return;

		Object drone = newBinding.drone;
		double x = newBinding.x;
		double y = newBinding.y;

		if (!drones.containsKey(drone)) {
			PNode rect = new PNode();
			rect.setBounds(x, y, 40, 40);
			rect.setPaint(Color.RED);
			drones.put(drone, rect);
			grid.getCanvas().getLayer().addChild(rect);
		} else {
			drones.get(drone).setBounds(x, y, 40, 40);
		}
	}

	@Override
	public void onInit() {
		grid = new GridExample();
		grid.getCanvas().addInputEventListener(new PBasicInputEventHandler() {
			@Override
			public void mouseClicked(PInputEvent event) {
				System.out.println("click");
				self.push(new DroneCreationRequest() {
				});
			}
		});
	}

	@Override
	public void onDestroy() {
	}

}
