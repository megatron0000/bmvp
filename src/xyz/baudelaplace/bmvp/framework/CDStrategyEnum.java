package xyz.baudelaplace.bmvp.framework;

import java.util.ArrayList;

import xyz.baudelaplace.bmvp.framework.binding.Binding;
import xyz.baudelaplace.bmvp.framework.views.ViewAdapter;

public enum CDStrategyEnum implements CDStrategy {

	Default {

		@Override
		public void run(BindingOwner ownerElement, ViewAdapter adapter) {

			ownerElement.getPresenterOutBindings().forEach(binding -> {
				binding.synchronizeChanges();
				binding.updateConsumer();
			});

			// TODO Invoking several methods can be slow. Better if I reunited all update
			// operations and called all at once at the end of CD
			adapter.invoke(() -> {
				OutBinding<? extends Binding> binding = ownerElement.getViewOutBinding();
				binding.synchronizeChanges();

				binding.updateConsumer();
			});

			ownerElement.getPresenterOutBindings().forEach(b -> {
				b.getConsumerDetector().run();
				b.onCDPhaseFinish();
			});

			adapter.invoke(() -> ownerElement.getViewOutBinding().onCDPhaseFinish());

		}

	},

	OnPush {
		@Override
		public void run(BindingOwner ownerElement, ViewAdapter adapter) {

			ArrayList<OutBinding<? extends Binding>> changedBindings = new ArrayList<>();

			ownerElement.getPresenterOutBindings().forEach(binding -> {
				binding.synchronizeChanges();

				if (binding.hasImpendingChange())
					changedBindings.add(binding);

				binding.updateConsumer();
			});

			adapter.invoke(() -> {
				OutBinding<? extends Binding> binding = ownerElement.getViewOutBinding();
				binding.synchronizeChanges();
				binding.updateConsumer();
			});

			ownerElement.getPresenterOutBindings().forEach(b -> {
				if (changedBindings.contains(b))
					b.getConsumerDetector().run();
				b.onCDPhaseFinish();
			});

			adapter.invoke(() -> ownerElement.getViewOutBinding().onCDPhaseFinish());

		}
	};

}
