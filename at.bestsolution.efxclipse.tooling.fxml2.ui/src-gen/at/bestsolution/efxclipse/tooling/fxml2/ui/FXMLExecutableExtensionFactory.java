/*
 * generated by Xtext
 */
package at.bestsolution.efxclipse.tooling.fxml2.ui;

import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

import com.google.inject.Injector;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class FXMLExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return at.bestsolution.efxclipse.tooling.fxml2.ui.internal.FXMLActivator.getInstance().getBundle();
	}
	
	@Override
	protected Injector getInjector() {
		return at.bestsolution.efxclipse.tooling.fxml2.ui.internal.FXMLActivator.getInstance().getInjector("at.bestsolution.efxclipse.tooling.fxml2.FXML");
	}
	
}