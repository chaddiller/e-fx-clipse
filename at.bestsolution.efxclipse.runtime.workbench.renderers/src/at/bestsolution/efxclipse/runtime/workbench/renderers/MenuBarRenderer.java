/*******************************************************************************
 * Copyright (c) 2012 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl<tom.schindl@bestsolution.at> - initial API and implementation
 *******************************************************************************/
package at.bestsolution.efxclipse.runtime.workbench.renderers;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.MUILabel;

@SuppressWarnings("restriction")
public class MenuBarRenderer extends JFXRenderer {

	@Override
	public Object createWidget(MUIElement element, Object parent) {
		if (parent != null) {
			if (parent instanceof MenuBar) {
				String label = ((MUILabel) element).getLocalizedLabel();
				label = label.replaceAll("&", "_");
				Menu menu = new Menu(label);
				menu.setMnemonicParsing(true);
				return menu;
			}
		}

		MenuBar menuBar = new MenuBar();
		return menuBar;
	}

	@Override
	public void processContents(MElementContainer<MUIElement> container) {
		super.processContents(container);

		if (container.getWidget() instanceof MenuBar) {
			MenuBar menuBar = (MenuBar) container.getWidget();
			for (MUIElement e : container.getChildren()) {
				menuBar.getMenus().add((Menu) e.getWidget());
			}
		} else if (container.getWidget() instanceof Menu) {
			Menu menu = (Menu) container.getWidget();
			for (MUIElement e : container.getChildren()) {
				menu.getItems().add((MenuItem) e.getWidget());
			}
		}

	}
}
