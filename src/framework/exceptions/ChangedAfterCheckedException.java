package framework.exceptions;

public class ChangedAfterCheckedException extends Error {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ChangedAfterCheckedException(String reason) {
		super(reason);
	}

}
