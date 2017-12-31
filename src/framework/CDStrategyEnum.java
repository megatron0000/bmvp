package framework;

import java.util.ArrayList;

import framework.binding.Binding;
import framework.views.ViewAdapter;

public enum CDStrategyEnum implements CDStrategy {

	Default {

		@Override
		public void run(BindingOwner ownerElement, ViewAdapter adapter) {

			ownerElement.getPresenterOutBindings().forEach(binding -> {
				binding.updateConsumer();
			});

			adapter.invoke(() -> {
				OutBinding<? extends Binding> binding = ownerElement.getViewOutBinding();

				// TODO This synchronization is "bugish". It allows agents from outside view
				// thread to call Outbinding .set() directly, without ViewAdapter.invoke()
				synchronized (binding) {
					binding.updateConsumer();

					// TODO Finish should only be invoked after ALL calls to updateConsumer (among
					// view bindings)
					binding.onCDPhaseFinish();
				}
			});

			ownerElement.getPresenterOutBindings().forEach(b -> {
				b.getConsumerDetector().run();
				b.onCDPhaseFinish();
			});

		}

	},

	OnPush {
		@Override
		public void run(BindingOwner ownerElement, ViewAdapter adapter) {

			ArrayList<OutBinding<? extends Binding>> changedBindings = new ArrayList<>();

			ownerElement.getPresenterOutBindings().forEach(binding -> {
				if (binding.hasImpendingChange())
					changedBindings.add(binding);

				binding.updateConsumer();
			});

			// TODO Finish should only be invoked after ALL calls to updateConsumer (of view
			// bindings)
			adapter.invoke(() -> {
				OutBinding<? extends Binding> binding = ownerElement.getViewOutBinding();
				binding.updateConsumer();
				binding.onCDPhaseFinish();
			});

			ownerElement.getPresenterOutBindings().forEach(b -> {
				if (changedBindings.contains(b))
					b.getConsumerDetector().run();
				b.onCDPhaseFinish();
			});

		}
	};

}
