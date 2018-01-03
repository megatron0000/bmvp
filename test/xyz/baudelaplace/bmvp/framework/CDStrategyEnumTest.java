package xyz.baudelaplace.bmvp.framework;

import java.util.ArrayList;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.script.ScriptedAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import xyz.baudelaplace.bmvp.framework.BindingOwner;
import xyz.baudelaplace.bmvp.framework.CDStrategyEnum;
import xyz.baudelaplace.bmvp.framework.ChangeDetector;
import xyz.baudelaplace.bmvp.framework.OutBinding;
import xyz.baudelaplace.bmvp.framework.binding.Binding;
import xyz.baudelaplace.bmvp.framework.views.ViewAdapter;

class CDStrategyEnumTest {

	Mockery context;
	BindingOwner parent;
	OutBinding<Binding> bindingChild1;
	OutBinding<Binding> bindingChild2;
	OutBinding<Binding> bindingView;
	ChangeDetector detectorChild1;
	ChangeDetector detectorChild2;
	Sequence updateConsumerSequence;
	ViewAdapter adapter;

	@SuppressWarnings("unchecked")
	@BeforeEach
	final void setup() {
		context = new Mockery() {
			{
				setImposteriser(ClassImposteriser.INSTANCE);
			}
		};
		updateConsumerSequence = context.sequence("update-consumer-sequence");

		adapter = context.mock(ViewAdapter.class);
		parent = context.mock(BindingOwner.class);
		bindingChild1 = context.mock(OutBinding.class, "child1");
		bindingChild2 = context.mock(OutBinding.class, "child2");
		bindingView = context.mock(OutBinding.class, "view");
		detectorChild1 = context.mock(ChangeDetector.class, "detector-child-1");
		detectorChild2 = context.mock(ChangeDetector.class, "detector-child-2");

		context.checking(new Expectations() {
			{

				allowing(adapter).invoke(with(any(Runnable.class)));
				will(ScriptedAction.perform("$0.run()"));

				allowing(parent).getPresenterOutBindings();
				will(returnValue(new ArrayList<OutBinding<Binding>>() {
					private static final long serialVersionUID = 1L;

					{
						add(bindingChild1);
						add(bindingChild2);
					}
				}));

				allowing(parent).getViewOutBinding();
				will(returnValue(bindingView));

				allowing(bindingChild1).getConsumerDetector();
				will(returnValue(detectorChild1));
				allowing(bindingChild2).getConsumerDetector();
				will(returnValue(detectorChild2));

			}
		});
	}

	@Test
	final void testDefaultStrategy() {
		context.checking(new Expectations() {
			{

				oneOf(bindingChild1).synchronizeChanges();
				inSequence(updateConsumerSequence);
				
				oneOf(bindingChild1).updateConsumer();
				inSequence(updateConsumerSequence);
				
				oneOf(bindingChild2).synchronizeChanges();
				inSequence(updateConsumerSequence);
				
				oneOf(bindingChild2).updateConsumer();
				inSequence(updateConsumerSequence);
				
				oneOf(bindingChild1).onCDPhaseFinish();
				inSequence(updateConsumerSequence);
				
				oneOf(bindingChild2).onCDPhaseFinish();
				inSequence(updateConsumerSequence);

				oneOf(bindingView).synchronizeChanges();
				oneOf(bindingView).updateConsumer();
				oneOf(bindingView).onCDPhaseFinish();

				allowing(bindingChild1).hasImpendingChange();
				will(returnValue(false));
				oneOf(detectorChild1).run();

				allowing(bindingChild2).hasImpendingChange();
				will(returnValue(false));
				oneOf(detectorChild2).run();

			}
		});

		CDStrategyEnum.Default.run(parent, adapter);
		context.assertIsSatisfied();
	}

	@Test
	final void testOnPushStrategy() {
		context.checking(new Expectations() {
			{

				oneOf(bindingChild1).synchronizeChanges();
				inSequence(updateConsumerSequence);
				oneOf(bindingChild1).updateConsumer();
				inSequence(updateConsumerSequence);
				oneOf(bindingChild2).synchronizeChanges();
				inSequence(updateConsumerSequence);
				oneOf(bindingChild2).updateConsumer();
				inSequence(updateConsumerSequence);
				oneOf(bindingChild1).onCDPhaseFinish();
				inSequence(updateConsumerSequence);
				oneOf(bindingChild2).onCDPhaseFinish();
				inSequence(updateConsumerSequence);

				oneOf(bindingView).synchronizeChanges();
				oneOf(bindingView).updateConsumer();
				oneOf(bindingView).onCDPhaseFinish();

				allowing(bindingChild1).hasImpendingChange();
				will(returnValue(false));
				exactly(0).of(detectorChild1).run();

				allowing(bindingChild2).hasImpendingChange();
				will(returnValue(true));
				exactly(1).of(detectorChild2).run();

			}
		});

		CDStrategyEnum.OnPush.run(parent, adapter);
		context.assertIsSatisfied();
	}

}
