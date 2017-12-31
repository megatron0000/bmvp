package framework;

import java.util.List;

import framework.binding.Binding;

public abstract class BindingOwner {
	abstract List<OutBinding<? extends Binding>> getPresenterOutBindings();

	abstract OutBinding<? extends Binding> getViewOutBinding();

	abstract ChangeDetector getChangeDetector();

}
