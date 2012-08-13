/*******************************************************************************
 * Copyright (c) 2011 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl<tom.schindl@bestsolution.at> - initial API and implementation
 *******************************************************************************/
package at.bestsolution.efxclipse.tooling.css;

import java.util.List;

/**
 * @author ccaks
 *
 */
public interface CssExtendedDialectExtension extends CssDialectExtension {

	public static class CssProperty {
		public final String name;
		public final String doc;
		
		public CssProperty(String name, String doc) {
			this.name = name;
			this.doc = doc;
		}
	}
	
	
	public List<CssProperty> getPropertiesForSelector(String selector);
	
	
	public static class CssValuePart {
		public final String value;
		public final String doc;
		
		
		public CssValuePart(String value, String doc) {
			this.value = value;
			this.doc = doc;
		}
		
	}
	
	public List<CssProperty> getValuesForProperty(String propertyName);
	public List<CssProperty> getValuesForProperty(String propertyName, String... preceedingValueParts);
	
	public String getDocForProperty(String propertyName);
}
