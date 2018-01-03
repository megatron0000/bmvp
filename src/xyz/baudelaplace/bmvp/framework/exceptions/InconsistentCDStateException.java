package xyz.baudelaplace.bmvp.framework.exceptions;

public class InconsistentCDStateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InconsistentCDStateException(String reason) {
		super(reason);
	}

}
