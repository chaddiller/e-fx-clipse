/**
 */
package at.bestsolution.efxclipse.gefx.scene;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parent</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link at.bestsolution.efxclipse.gefx.scene.Parent#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @see at.bestsolution.efxclipse.gefx.scene.ScenePackage#getParent()
 * @model abstract="true"
 * @generated
 */
public interface Parent extends Node {
	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link at.bestsolution.efxclipse.gefx.scene.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see at.bestsolution.efxclipse.gefx.scene.ScenePackage#getParent_Children()
	 * @model containment="true"
	 * @generated
	 */
	EList<Node> getChildren();

} // Parent