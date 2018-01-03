package xyz.baudelaplace.bmvp.framework;

import java.util.List;

import xyz.baudelaplace.bmvp.framework.binding.Binding;

public abstract class BindingOwner {
	abstract List<OutBinding<? extends Binding>> getPresenterOutBindings();

	abstract OutBinding<? extends Binding> getViewOutBinding();

	abstract ChangeDetector getChangeDetector();

}
