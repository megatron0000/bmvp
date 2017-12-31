package framework;

import com.google.inject.Inject;
import com.google.inject.Injector;

import framework.binding.Binding;

/**
 * Encapsulates injection and initialization of Presenters (with
 * {@link Presenter#configure()}) and Views (with
 * {@link View#setLocalDispatcher(LocalEventDispatcher)})
 * 
 * @author vitor
 *
 */
public class ComponentProvider {

	private Injector injector;

	private EventDispatcher dispatcher;

	@Inject
	public ComponentProvider(Injector injector, EventDispatcher dispatcher) {
		this.injector = injector;
		this.dispatcher = dispatcher;
	}

	<K extends Binding, T extends Presenter<K>> Presenter<K> getPresenter(
			Class<T> presenterClass, Presenter<?> parent
	) {
		T presenter = injector.getInstance(presenterClass);
		presenter.internalConfigure(parent);
		return presenter;
	}

	<K extends Binding, T extends View<K>> View<K> getView(Class<T> viewClass, Presenter<?> ownerPresenter) {
		T view = injector.getInstance(viewClass);
		view.setLocalDispatcher(dispatcher.createLocalDispatcher(ownerPresenter));
		return view;
	}
}
