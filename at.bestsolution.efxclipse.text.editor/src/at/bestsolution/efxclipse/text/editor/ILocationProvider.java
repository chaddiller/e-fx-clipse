package at.bestsolution.efxclipse.text.editor;

import org.eclipse.core.runtime.IPath;


/**
 * This class gets the location for a given
 * object.
 * <p>
 * In order to provided backward compatibility for clients of
 * <code>ILocationProvider</code>, extension interfaces are used to provide a means
 * of evolution. The following extension interfaces exist:
 * <ul>
 * <li>{@link org.eclipse.ui.editors.text.ILocationProviderExtension} since version 3.3
 * 		allowing to get the location as <code>URI</code>.</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.ui.editors.text.ILocationProviderExtension
 * @since 3.0
 */
public interface ILocationProvider {

	/**
	 * Returns the location of the given object or <code>null</code>.
	 * <p>
	 * The provided location is either a full path of a workspace resource or
	 * an absolute path in the local file system.
	 * </p>
	 *
	 * @param element the object for which to get the location
	 * @return the location of the given object or <code>null</code>
	 */
	IPath getPath(Object element);
}
