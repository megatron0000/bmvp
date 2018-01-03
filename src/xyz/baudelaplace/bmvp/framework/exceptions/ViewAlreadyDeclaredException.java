package xyz.baudelaplace.bmvp.framework.exceptions;

public class ViewAlreadyDeclaredException extends Error {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ViewAlreadyDeclaredException(String reason) {
		super(reason);
	}

}
