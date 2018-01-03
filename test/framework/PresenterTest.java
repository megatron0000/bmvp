package framework;

import static org.junit.jupiter.api.Assertions.fail;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import xyz.baudelaplace.bmvp.framework.Presenter;
import xyz.baudelaplace.bmvp.framework.binding.Binding;

class PresenterTest {

	Mockery context;
	Presenter<Binding> presenterInstance;

	@BeforeEach
	final void setup() {
		context = new Mockery() {
			{
				setImposteriser(ClassImposteriser.INSTANCE);
			}
		};
		presenterInstance = new Presenter<Binding>() {

			@Override
			public void onInit() {
			}

			@Override
			public void onDestroy() {
			}

			@Override
			public void onBindingChanged(Binding newBinding) {
			}

			@Override
			protected void configure() {
			}
		};
	}

	@Test
	final void testAsBindingOwner() {
	}

	@Test
	final void testAsViewOwner() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testConfigure() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testInternalConfigure() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testCreateBinding() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testCreateChild() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testDeclareView() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testDeregisterAllListeners() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetChangeDetector() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testInternalOnDestroy() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testPush() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testRegisterListener() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testScheduleDestruction() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testChangeStrategy() {
		fail("Not yet implemented"); // TODO
	}

}
