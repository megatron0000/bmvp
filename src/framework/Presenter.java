package framework;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import framework.annotations.Strategy;
import framework.binding.Binding;
import framework.binding.BindingConsumer;
import framework.events.Event;
import framework.events.EventListener;
import framework.events.PresenterCreatedEvent;
import framework.events.PresenterScheduledDestroyEvent;
import framework.exceptions.ViewAlreadyDeclaredException;
import framework.views.PlaceholderView;
import framework.views.ViewAdapter;

/**
 * @author vitor
 *
 */
public abstract class Presenter<B extends Binding> implements BindingConsumer<B> {

	private Presenter<B> self = this;

	@Inject
	private EventDispatcher dispatcher;

	@Inject
	private ViewAdapter adapter;

	@Inject
	private ComponentProvider compProvider;
	
	@Inject
	@Strategy.Default
	private CDStrategy strategy;

	private class PrivateBindingOwner extends BindingOwner {

		private final List<OutBinding<? extends Binding>> presenterOuts = new ArrayList<>();

		void addPresenterOutbinding(OutBinding<? extends Binding> out) {
			presenterOuts.add(out);
		}

		@Override
		ChangeDetector getChangeDetector() {
			return self.getChangeDetector();
		}

		@Override
		List<OutBinding<? extends Binding>> getPresenterOutBindings() {
			return presenterOuts;
		}

		@Override
		OutBinding<? extends framework.binding.Binding> getViewOutBinding() {
			return asViewOwner().getOutbinding();
		}

	}

	private class PrivateChangeDetector extends ChangeDetector {

		@Override
		CDStrategy getStrategy() {
			return strategy;
		};

		@Override
		void run() {
			strategy.run(asBindingOwner(), adapter);
		};

		@Override
		void setStrategy(CDStrategy strategy) {
			self.strategy = strategy;
		};

	}

	private class PrivateViewOwner extends ViewOwner {
		private final OutBinding<Binding> viewOut = new OutBinding<>(new PlaceholderView(), null);
		boolean viewWasDeclared = false;

		public PrivateViewOwner() {
			viewOut.setCheckDisabled();
		}

		@SuppressWarnings("unchecked")
		@Override
		<K extends Binding> OutBinding<K> declareView(Class<? extends View<K>> viewClass) {

			if (viewWasDeclared)
				throw new ViewAlreadyDeclaredException("Tried to declare a presenter's view twice");

			adapter.invoke(() -> {
				View<K> viewInstance = compProvider.<K, View<K>>getView((Class<View<K>>) viewClass, self);
				viewOut.reset(
						(BindingConsumer<Binding>) viewInstance, null);
			});

			return ((OutBinding<K>) viewOut);
		}

		@SuppressWarnings("unchecked")
		@Override
		<K extends Binding> OutBinding<K> getOutbinding() {
			return (OutBinding<K>) viewOut;
		}

	}

	private final PrivateBindingOwner bo = new PrivateBindingOwner();

	private final ChangeDetector cd = new PrivateChangeDetector();

	private final ViewOwner vo = new PrivateViewOwner();

	final BindingOwner asBindingOwner() {
		return bo;
	}

	final ViewOwner asViewOwner() {
		return vo;
	}

	protected abstract void configure();

	void internalConfigure(Presenter<?> parent) {
		push(new PresenterCreatedEvent() {

			@Override
			public Presenter<?> getNewPresenter() {
				return self;
			}

			@Override
			public Presenter<?> getParent() {
				return parent;
			}
		});
		configure();
	}

	final OutBinding<B> createBinding() {
		return new OutBinding<>(this, getChangeDetector());
	}

	protected final <
			K extends Binding, T extends Presenter<K>> OutBinding<K> createChild(Class<T> childClass) {
		OutBinding<K> outbinding = compProvider.getPresenter(childClass, self).createBinding();
		bo.addPresenterOutbinding(outbinding);
		return outbinding;
	}

	protected final <K extends Binding> OutBinding<K> declareView(Class<? extends View<K>> viewClass) {
		return asViewOwner().declareView(viewClass);
	}

	final void deregisterAllListeners() {
		this.dispatcher.deRegisterFromAll(this);
	}

	final ChangeDetector getChangeDetector() {
		return cd;
	}

	final void internalOnDestroy() {
		deregisterAllListeners();
		asBindingOwner().getPresenterOutBindings().forEach(binding -> {
			// TODO Ops, needed to hack a little to access Presenter.internalOnDestroy
			((Presenter<?>) binding.getConsumer()).internalOnDestroy();
		});
		// TODO Check whether these 2 statements should be inside forEach or outside
		onDestroy();
		adapter.invoke(() -> asBindingOwner().getViewOutBinding().getConsumer().onDestroy());
	}

	protected final <T extends Event> void push(T event) {
		this.dispatcher.push(this, event);
	}

	protected final <
			T extends Event> void registerListener(Class<T> eventType, EventListener<T> eventListener) {
		this.dispatcher.register(this, eventType, eventListener);
	}

	protected final void scheduleDestruction() {
		push(new PresenterScheduledDestroyEvent() {

			@Override
			public Presenter<?> getPresenter() {
				return self;
			}

		});
	}
	
	protected final void changeStrategy(CDStrategy newStrategy) {
		getChangeDetector().setStrategy(newStrategy);
	}

}
