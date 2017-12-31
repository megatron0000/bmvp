package framework.exceptions;

public class BindingNotCloneableException extends Error {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BindingNotCloneableException(String reason) {
		super(reason);
	}

}
