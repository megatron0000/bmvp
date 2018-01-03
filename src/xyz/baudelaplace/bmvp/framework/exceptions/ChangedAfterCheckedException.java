package xyz.baudelaplace.bmvp.framework.exceptions;

/**
 * @deprecated since Outbinding got to be lazy
 * @author vitor
 *
 */
@Deprecated
public class ChangedAfterCheckedException extends Error {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChangedAfterCheckedException(String reason) {
		super(reason);
	}

}
