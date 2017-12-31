package framework.binding;

import javax.annotation.Nullable;

public interface BindingConsumer<T extends Binding> {
	void onBindingChanged(@Nullable T newBinding);
	void onInit();
	void onDestroy();
}
