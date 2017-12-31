package framework;

import framework.binding.Binding;
import framework.exceptions.InconsistentCDStateException;

public enum CDStateEnum {
	WAITING {
		@Override
		<T extends Binding> void updateConsumer(OutBinding<T> binding) {
			// Do nothing, since NEED_CHECK was not triggered since last CD run
			binding.setChecked();
		}

		@Override
		<T extends Binding> void onCDPhaseFinish(OutBinding<T> binding) {
			throw new InconsistentCDStateException("Binding pre-finished CD phase in WAITING");
		}
	},
	PRE_INIT {
		@Override
		<T extends Binding> void updateConsumer(OutBinding<T> binding) {
			binding.getConsumer().onBindingChanged(binding.get());
			binding.getConsumer().onInit();
			binding.setChecked();
		}

		@Override
		<T extends Binding> void onCDPhaseFinish(OutBinding<T> binding) {
			throw new InconsistentCDStateException("Binding pre-finished CD phase in PRE_INIT");
		}
	},
	NEED_CHECK {
		@Override
		<T extends Binding> void updateConsumer(OutBinding<T> binding) {
			binding.getConsumer().onBindingChanged(binding.get());
			binding.setChecked();
		}

		@Override
		<T extends Binding> void onCDPhaseFinish(OutBinding<T> binding) {
			throw new InconsistentCDStateException("Binding pre-finished CD phase in NEED_CHECK");
		}
	},
	CHECKED {
		@Override
		<T extends Binding> void updateConsumer(OutBinding<T> binding) {
			throw new InconsistentCDStateException("Binding started CD phase in CHECKED");
		}

		@Override
		<T extends Binding> void onCDPhaseFinish(OutBinding<T> binding) {
			binding.setWaiting();
		}
	},
	CHECK_DISABLED {
		@Override
		<T extends Binding> void updateConsumer(OutBinding<T> binding) {
			// Do nothing
		}

		@Override
		<T extends Binding> void onCDPhaseFinish(OutBinding<T> binding) {
			// Do nothing
		}
	};

	abstract <T extends Binding> void updateConsumer(OutBinding<T> binding);

	abstract <T extends Binding> void onCDPhaseFinish(OutBinding<T> binding);
}
