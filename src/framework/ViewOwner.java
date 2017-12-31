package framework;

import framework.binding.Binding;

public abstract class ViewOwner {
	abstract <K extends Binding> OutBinding<K> declareView(Class<? extends View<K>> view);

	abstract <K extends Binding> OutBinding<K> getOutbinding();
}
