/*
 * generated by Xtext
 */
package at.bestsolution.efxclipse.tooling.efxbean.ui;

import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

import com.google.inject.Injector;

import at.bestsolution.efxclipse.tooling.efxbean.ui.internal.FXBeanActivator;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class FXBeanExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return FXBeanActivator.getInstance().getBundle();
	}
	
	@Override
	protected Injector getInjector() {
		return FXBeanActivator.getInstance().getInjector(FXBeanActivator.AT_BESTSOLUTION_EFXCLIPSE_TOOLING_EFXBEAN_FXBEAN);
	}
	
}