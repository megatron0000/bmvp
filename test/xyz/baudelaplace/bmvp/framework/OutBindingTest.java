package xyz.baudelaplace.bmvp.framework;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import xyz.baudelaplace.bmvp.framework.CDStateEnum;
import xyz.baudelaplace.bmvp.framework.CDStrategy;
import xyz.baudelaplace.bmvp.framework.ChangeDetector;
import xyz.baudelaplace.bmvp.framework.OutBinding;
import xyz.baudelaplace.bmvp.framework.binding.Binding;
import xyz.baudelaplace.bmvp.framework.binding.BindingConsumer;
import xyz.baudelaplace.bmvp.framework.exceptions.BindingNotCloneableException;
import xyz.baudelaplace.bmvp.framework.exceptions.BindingResetAfterRead;

class OutBindingTest {

	BindingConsumer<Binding> bindingConsumer = new BindingConsumer<Binding>() {

		@Override
		public void onBindingChanged(Binding newBinding) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onInit() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub

		}
	};

	ChangeDetector changeDetector = new ChangeDetector() {

		@Override
		void setStrategy(CDStrategy strategy) {
			// TODO Auto-generated method stub

		}

		@Override
		void run() {
			// TODO Auto-generated method stub

		}

		@Override
		CDStrategy getStrategy() {
			// TODO Auto-generated method stub
			return null;
		}
	};

	OutBinding<Binding> outbinding;

	Binding emptyBinding = new Binding() {
		@Override
		public Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return super.clone();
		}
	};

	@BeforeEach
	void restoreBinding() {
		outbinding = new OutBinding<>(bindingConsumer, changeDetector);
	}

	@Test
	void testGetters() {
		assertEquals(bindingConsumer, outbinding.getConsumer());
		assertEquals(changeDetector, outbinding.getConsumerDetector());
		assertEquals(CDStateEnum.PRE_INIT, outbinding.getState());
	}

	@Test
	void testStateSetters() {
		outbinding.setWaiting();
		assertEquals(CDStateEnum.WAITING, outbinding.getState());
		outbinding.setCheckDisabled();
		assertEquals(CDStateEnum.CHECK_DISABLED, outbinding.getState());
		outbinding.setChecked();
		assertEquals(CDStateEnum.CHECKED, outbinding.getState());
	}

	@Test
	void shouldStartInPRE_INIT() {
		assertEquals(CDStateEnum.PRE_INIT, outbinding.getState());
	}

	@Test
	void shouldWorkWithNullBinding() {
		assertEquals(null, outbinding.get());
	}

	@Test
	void shouldNotThrowIfBindingIsCloneable() {
		try {
			outbinding.set(emptyBinding);
			outbinding.get();
		} catch (BindingNotCloneableException e) {
			fail("Should not throw when Binding is cloneable");
		}
		assertThrows(BindingNotCloneableException.class, () -> {
			outbinding.set(new Binding() {
				@Override
				public Object clone() throws CloneNotSupportedException {
					throw new CloneNotSupportedException();
				}
			});
			outbinding.synchronizeChanges();
			outbinding.get();
		});
	}

	@Test
	void shouldThrowIfSetWhenChecked() {
		// No longer throws because Outbinding is now lazy
		// assertThrows(ChangedAfterCheckedException.class, () -> {
		// outbinding.setChecked();
		// outbinding.set(null);
		// });
	}

	@Test
	void shouldStayInPRE_INITIfChangedInPRE_INIT() {
		assertEquals(CDStateEnum.PRE_INIT, outbinding.getState());
		outbinding.set(emptyBinding);
		assertEquals(CDStateEnum.PRE_INIT, outbinding.getState());
	}

	@Test
	void shouldGoToNEED_CHECKIfChangedOutsidePRE_INITAndCHECK_DISABLED() {
		outbinding.setWaiting();
		outbinding.set(emptyBinding);
		outbinding.synchronizeChanges();
		assertEquals(CDStateEnum.NEED_CHECK, outbinding.getState());
	}

	@Test
	void shouldStayInCHECK_DISABLEDIfChangedOnIt() {
		outbinding.setCheckDisabled();
		outbinding.set(emptyBinding);
		assertEquals(CDStateEnum.CHECK_DISABLED, outbinding.getState());
	}

	@Test
	void shouldThrowIfResetAfterRead() {
		assertThrows(BindingResetAfterRead.class, () -> {
			outbinding.get();
			outbinding.reset(bindingConsumer, changeDetector);
		});
		restoreBinding();
		try {
			outbinding.reset(bindingConsumer, changeDetector);
		} catch (Exception e) {
			fail("Outbinding should not have thrown when reset before read");
		}
	}
}
