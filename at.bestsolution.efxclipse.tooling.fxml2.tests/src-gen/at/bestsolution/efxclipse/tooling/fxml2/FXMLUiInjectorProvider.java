/*
* generated by Xtext
*/
package at.bestsolution.efxclipse.tooling.fxml2;

import org.eclipse.xtext.junit4.IInjectorProvider;

import com.google.inject.Injector;

public class FXMLUiInjectorProvider implements IInjectorProvider {
	
	public Injector getInjector() {
		return at.bestsolution.efxclipse.tooling.fxml2.ui.internal.FXMLActivator.getInstance().getInjector("at.bestsolution.efxclipse.tooling.fxml2.FXML");
	}
	
}