package framework.exceptions;

public class BindingResetAfterRead extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BindingResetAfterRead(String reason) {
		super(reason);
	}

}
