package framework;

import static org.junit.Assert.assertSame;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.inject.Injector;

import framework.binding.Binding;

class ComponentProviderTest {

	Mockery context;
	ComponentProvider provider;
	Injector injector;
	EventDispatcher dispatcher;
	Presenter<Binding> presenterInstance;
	View<Binding> viewInstance;
	LocalEventDispatcher localDispatcher;

	@SuppressWarnings("unchecked")
	@BeforeEach
	final void setup() {
		context = new Mockery() {
			{
				setImposteriser(ClassImposteriser.INSTANCE);
			}
		};
		presenterInstance = context.mock(Presenter.class);
		dispatcher = context.mock(EventDispatcher.class);
		injector = context.mock(Injector.class);
		provider = new ComponentProvider(injector, dispatcher);
	}

	@SuppressWarnings("unchecked")
	@Test
	final void testGetPresenter() {
		context.checking(new Expectations() {
			{
				allowing(injector).getInstance(Presenter.class);
				will(returnValue(presenterInstance));

				oneOf(presenterInstance).internalConfigure(null);
			}
		});

		assertSame(presenterInstance, provider.getPresenter(Presenter.class, null));

		context.assertIsSatisfied();
	}

	@SuppressWarnings("unchecked")
	@Test
	final void testGetView() {
		localDispatcher = context.mock(LocalEventDispatcher.class);
		viewInstance = context.mock(View.class);
		context.checking(new Expectations() {
			{

				allowing(injector).getInstance(View.class);
				will(returnValue(viewInstance));

				allowing(dispatcher).createLocalDispatcher(presenterInstance);
				will(returnValue(localDispatcher));

				allowing(viewInstance).setLocalDispatcher(localDispatcher);

			}
		});

		assertSame(viewInstance, provider.getView(View.class, presenterInstance));

		context.assertIsSatisfied();
	}

}
