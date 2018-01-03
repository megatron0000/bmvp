package framework;

import framework.binding.Binding;
import framework.binding.BindingConsumer;
import framework.exceptions.BindingNotCloneableException;
import framework.exceptions.BindingResetAfterRead;

public class OutBinding<T extends Binding> {

	private T current;
	private T nullBindingInternal;
	private T waitingToBeCurrent;
	private BindingConsumer<T> consumer;
	private ChangeDetector consumerDetector;
	private CDStateEnum state;
	private boolean wasRead = false;

	@SuppressWarnings("unchecked")
	OutBinding(BindingConsumer<T> consumer, ChangeDetector detector) {
		this.consumer = consumer;
		this.consumerDetector = detector;
		this.state = CDStateEnum.PRE_INIT;
		
		nullBindingInternal = (T) new Binding() {
			@Override
			public Object clone() throws CloneNotSupportedException {
				return this;
			}
		};
		waitingToBeCurrent = nullBindingInternal;
	}

	/**
	 * The requested change is scheduled, but only executed when
	 * <code>synchronizeChanges()</code> is called
	 * 
	 * @param changedBinding
	 */
	public synchronized void set(T changedBinding) {

		waitingToBeCurrent = changedBinding;

		// if (getState() == CDStateEnum.CHECKED)
		// throw new ChangedAfterCheckedException(
		// "A binding had been checked in change detection and was changed later");
		//
		// current = changedBinding;
		// if (getState() != CDStateEnum.PRE_INIT && getState() !=
		// CDStateEnum.CHECK_DISABLED) {
		// setState(CDStateEnum.NEED_CHECK);
		// }
	}

	@SuppressWarnings("unchecked")
	T get() {
		try {
			wasRead = true;
			return current == null ? null : (T) (current).clone();
		} catch (CloneNotSupportedException e) {
			throw new BindingNotCloneableException(
					"Binding "
							+ this.toString()
							+ " is not cloneable. Your implementation must make it cloneable");
		}
	}

	BindingConsumer<T> getConsumer() {
		return consumer;
	}

	ChangeDetector getConsumerDetector() {
		return consumerDetector;
	}

	/**
	 * Should not be used (only for tests)
	 * 
	 * @return current state the Outbinding is in
	 */
	CDStateEnum getState() {
		return state;
	}

	/**
	 * Changes state immediatly
	 */
	void setWaiting() {
		this.state = CDStateEnum.WAITING;
	}

	/**
	 * Changes state immediatly
	 */
	void setChecked() {
		this.state = CDStateEnum.CHECKED;
	}

	/**
	 * Changes state immediatly
	 */
	void setCheckDisabled() {
		this.state = CDStateEnum.CHECK_DISABLED;
	}

	/**
	 * Absorbs bindings previously set().
	 */
	synchronized void synchronizeChanges() {
		if (waitingToBeCurrent == nullBindingInternal)
			return;

		current = waitingToBeCurrent;
		waitingToBeCurrent = nullBindingInternal;
		if (state != CDStateEnum.PRE_INIT && state != CDStateEnum.CHECK_DISABLED) {
			state = CDStateEnum.NEED_CHECK;
		}
	}

	void reset(BindingConsumer<T> consumer, ChangeDetector detector) {
		if (wasRead) {
			throw new BindingResetAfterRead(
					"Tried to reset an Outbinding whose get() had already been called");
		}

		this.consumer = consumer;
		this.consumerDetector = detector;
		this.state = CDStateEnum.PRE_INIT;
	}

	/**
	 * Calls onBindingChange of consumer (or not, depending on this object's
	 * CDState)
	 */
	void updateConsumer() {
		// if (getConsumer() instanceof View) {
		// System.out.println("---------------------");
		// System.out.println(getConsumer());
		// System.out.println(state);
		// System.out.println("---------------------");
		// }
		getState().updateConsumer(this);
	}

	void onCDPhaseFinish() {
		getState().onCDPhaseFinish(this);
	}

	boolean hasImpendingChange() {
		return getState() == CDStateEnum.PRE_INIT || getState() == CDStateEnum.NEED_CHECK;
	}
}
