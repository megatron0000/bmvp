package framework.binding;

/**
 * Care must be taken when implementing clone(), so not to share references
 * between Presenters, or between Presenter and View
 * 
 * @author vitor
 *
 */
public interface Binding extends Cloneable {
	Object clone() throws CloneNotSupportedException;
}
