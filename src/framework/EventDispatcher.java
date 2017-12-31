package framework;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import com.google.common.collect.HashBasedTable;

import framework.events.Event;
import framework.events.EventListener;
import framework.utility.ReflectionUtil;

public class EventDispatcher {
	private HashBasedTable<Presenter<?>, Class<? extends Event>, EventListener<? extends Event>> table =
			HashBasedTable.<Presenter<?>, Class<? extends Event>, EventListener<? extends Event>>create();
	private ConcurrentLinkedQueue<QueuedEvent> queue = new ConcurrentLinkedQueue<>();

	private Runnable onStartRunnable = () -> {
	};
	private Runnable onQueueEmptyRunnable = () -> {
	};
	private Consumer<QueuedEvent> dispatchAlgorithm = (a) -> {
	};

	private ExecutorService executorService = Executors.newSingleThreadExecutor();

	EventDispatcher() {
		// TODO Auto-generated constructor stub
	}

	LocalEventDispatcher createLocalDispatcher(Presenter<?> agentElement) {
		return new LocalEventDispatcher(agentElement, this);
	}

	void deRegister(Presenter<?> p, Class<? extends Event> eventType) {
		table.remove(p, eventType);
	}

	void deRegisterFromAll(Presenter<?> p) {
		table.row(p).clear();
	}

	/**
	 * @param target
	 * @param event
	 * @return true if event could be called. false otherwise
	 */
	@SuppressWarnings("unchecked")
	boolean dispatchOn(Presenter<?> target, Event event) {
		if (table.get(target, ReflectionUtil.getClass(event)) != null) {
			((EventListener<Event>) (table.get(target, ReflectionUtil.getClass(event)))).onEvent(event);
			return true;
		}
		return false;
	}

	boolean isRegistered(Presenter<?> presenter, Class<? extends Event> eventClass) {
		return table.contains(presenter, eventClass);
	}

	/**
	 * The fact that events were created as anonymous classes led to some trouble.
	 * See {@link ReflectionUtil}
	 * 
	 * @param agent
	 * @param e
	 */
	<T extends Event> void push(Presenter<?> agent, T e) {
		synchronized (queue) {
			queue.add(new QueuedEvent() {

				@Override
				public Event getEvent() {
					return e;
				}

				@Override
				public Presenter<?> getPresenter() {
					return agent;
				}
			});
			queue.notify();
		}
	}

	<T extends Event> void register(Presenter<?> p, Class<T> eventType, EventListener<T> eventListener) {
		table.put(p, eventType, eventListener);
	}

	void setDispatchAlgorithm(Consumer<QueuedEvent> algorithm) {
		dispatchAlgorithm = algorithm;
	}

	void setOnQueueEmpty(Runnable onQueueEmpty) {
		onQueueEmptyRunnable = onQueueEmpty;
	}

	void setOnStart(Runnable onStart) {
		onStartRunnable = onStart;
	}

	Future<Void> start() throws InterruptedException, ExecutionException {
		return executorService.submit(() -> {
			onStartRunnable.run();
			while (true) {
				while (queue.isEmpty())
					synchronized (queue) {
						queue.wait();
					}
				while (!queue.isEmpty())
					dispatchAlgorithm.accept(queue.poll());
				onQueueEmptyRunnable.run();
			}
		});
	}

	void stop() {
		executorService.shutdownNow();
	}
}
