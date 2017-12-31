package framework;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.jmock.Mockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import framework.binding.Binding;
import framework.events.Event;
import net.jodah.concurrentunit.ConcurrentTestCase;

class EventDispatcherTest extends ConcurrentTestCase {

	interface PrivateEvent extends Event {

	}

	EventDispatcher dispatcher;

	private int counter;

	private Mockery context;

	@BeforeEach
	void setup() {
		dispatcher = new EventDispatcher();
		counter = 0;
		context = new Mockery() {
			{
				setImposteriser(ClassImposteriser.INSTANCE);
				setThreadingPolicy(new Synchroniser());
			}
		};
	}

	Presenter<Binding> doRegister() {
		@SuppressWarnings("unchecked")
		Presenter<Binding> p = context.mock(Presenter.class);
		dispatcher.register(p, Event.class, event -> {
			counter++;
		});
		dispatcher.setDispatchAlgorithm(
				queuedEvent -> dispatcher.dispatchOn(p, queuedEvent.getEvent()));
		return p;
	}

	@Test
	final void testRegister() {
		Presenter<Binding> p = doRegister();
		dispatcher.dispatchOn(p, new Event() {

		});
		assertEquals(1, counter);
	}

	@Test
	final void testDeRegister() {
		@SuppressWarnings("unchecked")
		Presenter<Binding> p = context.mock(Presenter.class);
		dispatcher.deRegister(p, Event.class);
		// returns false when presenter does not listen to Event.class
		assertTrue(!dispatcher.dispatchOn(p, new Event() {
		}));
	}

	@Test
	final void testDeRegisterFromAll() {
		@SuppressWarnings("unchecked")
		Presenter<Binding> p = context.mock(Presenter.class);
		dispatcher.register(p, Event.class, event -> {
			throw new RuntimeException();
		});
		dispatcher.register(p, PrivateEvent.class, event -> {
			throw new RuntimeException();
		});
		dispatcher.deRegisterFromAll(p);
		// returns false when presenter does not listen to Event.class
		assertTrue(!dispatcher.dispatchOn(p, new Event() {
		}));
		// returns false when presenter does not listen to Event.class
		assertTrue(!dispatcher.dispatchOn(p, new PrivateEvent() {
		}));
	}

	@Test
	final void testPush() {
		Presenter<Binding> p = doRegister();
		dispatcher.setOnQueueEmpty(() -> {
			threadAssertEquals(1, counter);
			resume();
		});
		try {
			dispatcher.start();
		} catch (InterruptedException | ExecutionException e) {
			fail("Event dispatcher failed when it should not");
		}
		dispatcher.push(p, new Event() {
		});
		try {
			await(2000);
			dispatcher.stop();
		} catch (TimeoutException e) {
			fail("Awaiting worker thread timed out");
		}
	}

	@Test
	final void testSetOnStart() {
		dispatcher.setOnStart(() -> resume());
		try {
			dispatcher.start();
		} catch (InterruptedException | ExecutionException e) {
			fail("Dispatcher failed when it should not");
		}
		try {
			await(1000);
		} catch (TimeoutException e) {
			fail("Dispatcher wait timed out");
		}
	}

	@Test
	final void testSetOnQueueEmpty() {
		Presenter<Binding> p = doRegister();
		dispatcher.setOnQueueEmpty(() -> resume());
		try {
			dispatcher.start();
			dispatcher.push(p, new Event() {
			});
		} catch (InterruptedException | ExecutionException e) {
			fail("Dispatcher failed when it should not");
		}
		try {
			await(1000);
		} catch (TimeoutException e) {
			fail("Dispachter await timed out");
		}
	}

	@Test
	final void testSetDispatchAlgorithm() {
		@SuppressWarnings("unchecked")
		Presenter<Binding> p = context.mock(Presenter.class);
		dispatcher.setDispatchAlgorithm(queuedEvent -> resume());
		try {
			dispatcher.start();
		} catch (InterruptedException | ExecutionException e) {
			fail("Dispatcher failed when it should not");
		}
		dispatcher.push(p, new Event() {
		});
		try {
			await(1000);
		} catch (TimeoutException e) {
			fail("Dispachter await timed out");
		}
	}

	@Test
	final void testStart() {
		@SuppressWarnings("unchecked")
		Presenter<Binding> p = context.mock(Presenter.class);
		dispatcher.setDispatchAlgorithm(queuedEvent -> counter++);
		dispatcher.setOnStart(() -> counter++);
		dispatcher.setOnQueueEmpty(() -> {
			threadAssertEquals(2, counter);
			resume();
		});
		try {
			dispatcher.start();
		} catch (InterruptedException | ExecutionException e) {
			fail("Dispatcher failed when it should not");
		}
		dispatcher.push(p, new Event() {
		});
		try {
			await(1000);
		} catch (TimeoutException e) {
			fail("Dispatcher await timed out");
		}
	}

	@Test
	final void testDispatchOn() {
		assertFalse(dispatcher.dispatchOn(context.mock(Presenter.class, "presenter-2"), new Event() {
		}));
		Presenter<Binding> p = doRegister();
		assertTrue(dispatcher.dispatchOn(p, new Event() {
		}));
	}

	@Test
	final void testIsRegistered() {
		Presenter<Binding> p = doRegister();
		assertTrue(dispatcher.isRegistered(p, Event.class));
		assertFalse(dispatcher.isRegistered(p, PrivateEvent.class));
	}

	@Test
	final void testCreateLocalDispatcher() {
		@SuppressWarnings("unchecked")
		Presenter<Binding> p = context.mock(Presenter.class);
		dispatcher.register(p, Event.class, event -> counter++);
		dispatcher.setOnQueueEmpty(() -> {
			threadAssertEquals(1, counter);
			resume();
		});
		dispatcher.setDispatchAlgorithm(
				queuedEvent -> dispatcher.dispatchOn(queuedEvent.getPresenter(), queuedEvent.getEvent()));
		LocalEventDispatcher localDispatcher = dispatcher.createLocalDispatcher(p);
		try {
			dispatcher.start();
		} catch (InterruptedException | ExecutionException e) {
			fail("Dispatcher failed when it should not");
		}
		localDispatcher.push(new Event() {
		});
		try {
			await(1000);
		} catch (TimeoutException e) {
			fail("Dispatcher await timed out");
		}

	}

}
