/*
 * generated by Xtext
 */
package at.bestsolution.efxclipse.tooling.rrobot.dsl;

import org.eclipse.xtext.conversion.IValueConverterService;

import at.bestsolution.efxclipse.tooling.rrobot.dsl.conversion.RRobotConverters;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
public class RTaskRuntimeModule extends at.bestsolution.efxclipse.tooling.rrobot.dsl.AbstractRTaskRuntimeModule {
	@Override
	public Class<? extends IValueConverterService> bindIValueConverterService() {
		return RRobotConverters.class;
	}
}