package framework;

import framework.binding.Binding;
import framework.binding.BindingConsumer;
import framework.exceptions.BindingNotCloneableException;
import framework.exceptions.BindingResetAfterRead;
import framework.exceptions.ChangedAfterCheckedException;

public class OutBinding<T extends Binding> {

	private T current;
	private BindingConsumer<T> consumer;
	private ChangeDetector consumerDetector;
	private CDStateEnum state;
	private boolean wasRead = false;

	OutBinding(BindingConsumer<T> consumer, ChangeDetector detector) {
		this.consumer = consumer;
		this.consumerDetector = detector;
		this.state = CDStateEnum.PRE_INIT;
	}

	public synchronized void set(T changedBinding) {
		if (getState() == CDStateEnum.CHECKED)
			throw new ChangedAfterCheckedException(
					"A binding had been checked in change detection and was changed later");

		current = changedBinding;
		if (getState() != CDStateEnum.PRE_INIT && getState() != CDStateEnum.CHECK_DISABLED) {
			setState(CDStateEnum.NEED_CHECK);
		}
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

	void setWaiting() {
		this.state = CDStateEnum.WAITING;
	}

	void setChecked() {
		this.state = CDStateEnum.CHECKED;
	}

	void setCheckDisabled() {
		this.state = CDStateEnum.CHECK_DISABLED;
	}

	private void setState(CDStateEnum state) {
		this.state = state;
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

	void updateConsumer() {
		getState().updateConsumer(this);
	}

	void onCDPhaseFinish() {
		getState().onCDPhaseFinish(this);
	}

	boolean hasImpendingChange() {
		return getState() == CDStateEnum.PRE_INIT || getState() == CDStateEnum.NEED_CHECK;
	}
}
