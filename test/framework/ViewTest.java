package framework;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import framework.binding.Binding;
import framework.events.Event;

class ViewTest {

	View<Binding> view;

	LocalEventDispatcher dispatcher;

	Mockery context;

	@BeforeEach
	void setup() {
		context = new Mockery() {
			{
				setImposteriser(ClassImposteriser.INSTANCE);
			}
		};
		view = new View<Binding>() {
			@Override
			public void onBindingChanged(Binding newBinding) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDestroy() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onInit() {
				// TODO Auto-generated method stub

			}
		};

		dispatcher = context.mock(LocalEventDispatcher.class);
	}

	@Test
	final void testPush() {
		view.setLocalDispatcher(dispatcher);
	}

	@Test
	final void testSetLocalDispatcher() {
		context.checking(new Expectations() {
			{
				oneOf(dispatcher).push(with(any(Event.class)));
			}
		});
		view.setLocalDispatcher(dispatcher);
		view.push(new Event() {
		});
		context.assertIsSatisfied();
	}

}
