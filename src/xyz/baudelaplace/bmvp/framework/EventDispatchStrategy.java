package xyz.baudelaplace.bmvp.framework;

import xyz.baudelaplace.bmvp.framework.binding.Binding;
import xyz.baudelaplace.bmvp.framework.events.Event;
import xyz.baudelaplace.bmvp.framework.utility.ReflectionUtil;

public enum EventDispatchStrategy {

	TREE_CLIMB {

		@SuppressWarnings("unchecked")
		@Override
		void run(
				QueuedEvent queuedEvent, Tree<Presenter<? extends Binding>> tree, EventDispatcher dispatcher
		) {
			dispatcher.dispatchOn(
					tree.findClosestAncestor(
							queuedEvent.getPresenter(), presenter -> dispatcher.isRegistered(
									presenter, ((Class<? extends Event>) ReflectionUtil
											.getClass(queuedEvent.getEvent())))),
					queuedEvent.getEvent());
		}
	},
	ROOT_ONLY {

		@Override
		void run(
				QueuedEvent queuedEvent, Tree<Presenter<? extends Binding>> tree, EventDispatcher dispatcher
		) {
			dispatcher.dispatchOn(tree.getRoot(), queuedEvent.getEvent());
		}
	};

	abstract void run(
			QueuedEvent queuedEvent, Tree<Presenter<? extends Binding>> tree, EventDispatcher dispatcher
	);
}
