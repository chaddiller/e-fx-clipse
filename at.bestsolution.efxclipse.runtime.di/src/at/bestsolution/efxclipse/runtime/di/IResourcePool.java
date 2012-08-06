package at.bestsolution.efxclipse.runtime.di;

import javafx.scene.image.Image;

import org.eclipse.core.runtime.CoreException;

public interface IResourcePool {
	public Image getImage(String imageKey) throws CoreException;
	public Image getImageUnchecked(String imageKey);
}