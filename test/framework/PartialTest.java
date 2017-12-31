//package framework;
//
//import java.util.ArrayList;
//
//import org.jmock.Expectations;
//import org.jmock.Mockery;
//import org.jmock.lib.concurrent.Synchroniser;
//import org.jmock.lib.legacy.ClassImposteriser;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import framework.binding.Binding;
//import framework.binding.BindingConsumer;
//import framework.views.ViewAdapter;
//
//class PartialTest {
//
//	Mockery context;
//	BindingOwner parent;
//	OutBinding<Binding> bindingParentChild1;
//	OutBinding<Binding> bindingParentChild2;
//	BindingOwner child1;
//	BindingOwner child2;
//	BindingConsumer<Binding> consumer1;
//	BindingConsumer<Binding> consumer2;
//	ChangeDetector detector1;
//	ChangeDetector detector2;
//	ViewAdapter adapter;
//	OutBinding<Binding> viewOutbinding;
//
//	@SuppressWarnings("unchecked")
//	@BeforeEach
//	void setup() {
//		context = new Mockery() {
//			{
//				setImposteriser(ClassImposteriser.INSTANCE);
//				setThreadingPolicy(new Synchroniser());
//			}
//		};
//		parent = context.mock(BindingOwner.class, "owner-1");
//		child1 = context.mock(BindingOwner.class, "owner-2");
//		child2 = context.mock(BindingOwner.class, "owner-3");
//		adapter = context.mock(ViewAdapter.class, "adapter");
//		bindingParentChild1 = context.mock(OutBinding.class, "binding-12");
//		bindingParentChild2 = context.mock(OutBinding.class, "outbinding-13");
//		consumer1 = context.mock(BindingConsumer.class, "consumer-1");
//		consumer2 = context.mock(BindingConsumer.class, "consumer-2");
//		detector1 = context.mock(ChangeDetector.class, "detector-1");
//		detector2 = context.mock(ChangeDetector.class, "detector-2");
//		viewOutbinding = context.mock(OutBinding.class, "view-outbinding");
//		
//		
//		context.checking(new Expectations() {
//			{
//				allowing(parent).getPresenterOutBindings();
//				will(returnValue(new ArrayList<OutBinding<Binding>>() {
//					private static final long serialVersionUID = -4048810016725301383L;
//
//					{
//						add(bindingParentChild1);
//						add(bindingParentChild2);
//					}
//				}));
//				allowing(child1).getPresenterOutBindings();
//				will(returnValue(new ArrayList<OutBinding<Binding>>()));
//				allowing(child2).getPresenterOutBindings();
//				will(returnValue(new ArrayList<OutBinding<Binding>>()));
//				
//				allowing(bindingParentChild1).getConsumer();
//				will(returnValue(consumer1));
//				allowing(bindingParentChild2).getConsumer();
//				will(returnValue(consumer2));
//				
//				allowing(bindingParentChild1).getConsumerDetector();
//				will(returnValue(detector1));
//				allowing(bindingParentChild2).getConsumerDetector();
//				will(returnValue(detector2));
//				
//				allowing(parent).getViewOutBinding();
//				will(returnValue(viewOutbinding));
//				allowing(child1).getViewOutBinding();
//				will(returnValue(viewOutbinding));
//				allowing(child2).getViewOutBinding();
//				will(returnValue(viewOutbinding));
//				
//			}
//		});
//	}
//
//	@Test
//	final void testDefaultStrategy() {
//
//		context.checking(new Expectations() {
//			{
//				
//			}
//		});
//		CDStrategyEnum.Default.run(parent, adapter);
//	}
//
//}
