package xyz.baudelaplace.bmvp.framework;

import com.google.inject.Inject;

import xyz.baudelaplace.bmvp.framework.annotations.Root;
import xyz.baudelaplace.bmvp.framework.binding.Binding;
import xyz.baudelaplace.bmvp.framework.events.PresenterCreatedEvent;
import xyz.baudelaplace.bmvp.framework.events.PresenterScheduledDestroyEvent;
import xyz.baudelaplace.bmvp.framework.views.ViewAdapter;

public class MasterPresenter extends Presenter<Binding> {

	private Tree<Presenter<?>> subtree;

	@Inject
	@Root
	private Class<? extends Presenter<?>> rootPresenter;

	@Inject
	ViewAdapter adapter;

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {

		createChild((Class<Presenter<Binding>>) rootPresenter);

		subtree = new Tree<Presenter<?>>(this);
		
		registerListener(PresenterCreatedEvent.class, event -> {
			if (event.getNewPresenter() == this)
				return;

			if (!subtree.contains(event.getParent()))
				throw new RuntimeException(
						"Tried to put a new Presenter on Tree, " + "but parent node did not exist yet");
			subtree.addChild(event.getParent(), event.getNewPresenter());
		});
		
		registerListener(PresenterScheduledDestroyEvent.class, event -> {
			subtree.removeSubtree(event.getPresenter());
			event.getPresenter().internalOnDestroy();
		});
	}

	Tree<Presenter<?>> getSubtree() {
		return subtree;
	}

	@Override
	public void onBindingChanged(Binding newBinding) {
	}

	@Override
	public void onDestroy() {
	}

	@Override
	public void onInit() {

	}

}
