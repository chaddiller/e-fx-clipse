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
package at.bestsolution.efxclipse.tooling.ui.preview;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.embed.swt.FXCanvas;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Builder;
import javafx.util.BuilderFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.osgi.service.prefs.BackingStoreException;

import at.bestsolution.efxclipse.tooling.ui.preview.bundle.Activator;
import at.bestsolution.efxclipse.tooling.ui.preview.text.AnnotationAccess;
import at.bestsolution.efxclipse.tooling.ui.preview.text.ColorManager;
import at.bestsolution.efxclipse.tooling.ui.preview.text.XMLConfiguration;
import at.bestsolution.efxclipse.tooling.ui.preview.text.XMLPartitionScanner;

import com.google.inject.Inject;

public class LivePreviewPart extends ViewPart {
	public static final String PREF_LOAD_CONTROLLER = "PREF_LOAD_CONTROLLER";
	@Inject
	private LivePreviewSynchronizer synchronizer;

	private Node rootPane_new;

	private Text logStatement;

	private CTabFolder folder;

	private static final String IMAGE_OK = LivePreviewPart.class.getName() + ".IMAGE_OK";
	private static final String IMAGE_WARNING = LivePreviewPart.class.getName() + ".IMAGE_WARNING";
	private static final String IMAGE_ERROR = LivePreviewPart.class.getName() + ".IMAGE_ERROR";
	private static final String IMAGE_PREVIEW = LivePreviewPart.class.getName() + ".IMAGE_PREVIEW";

	private static final String IMAGE_TAB_ERROR = LivePreviewPart.class.getName() + ".IMAGE_TAB_ERROR";
	private static final String IMAGE_TAB_WARNING = LivePreviewPart.class.getName() + ".IMAGE_TAB_WARNING";
	private static final String IMAGE_TAB_NORMAL = LivePreviewPart.class.getName() + ".IMAGE_TAB_NORMAL";

	private static final String IMAGE_STATUS_ERROR = LivePreviewPart.class.getName() + ".IMAGE_STATUS_ERROR";
	private static final String IMAGE_STATUS_WARNING = LivePreviewPart.class.getName() + ".IMAGE_STATUS_WARNING";
	private static final String IMAGE_STATUS_OK = LivePreviewPart.class.getName() + ".IMAGE_STATUS_OK";
	private static final String IMAGE_STATUS_NOPREVIEW = LivePreviewPart.class.getName() + ".IMAGE_STATUS_NOPREVIEW";

	private static final String IMAGE_LOAD_CONTROLLER = LivePreviewPart.class.getName() + ".IMAGE_LOAD_CONTROLLER";
	private static final String IMAGE_REFRESH = LivePreviewPart.class.getName() + ".IMAGE_REFRESH";

	private static final String IMAGE_FXML_CONTENT = LivePreviewPart.class.getName() + ".IMAGE_FXML_CONTENT";

	private static final String NO_PREVIEW_TEXT = "No preview available";
	protected static final int VERTICAL_RULER_WIDTH = 20;

	private IEclipsePreferences preference = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);

	private CTabItem logItem;

	private Label statusLabelIcon;

	private Label statusLabelText;

	private FXCanvas swtFXContainer;

	private IPartListener listener;

	private Spinner scale;

	private Map<IFile, Integer> scaleMap = new HashMap<IFile, Integer>();

	private IFile currentFile;

	private IDocument document;
	
	private ContentData currentData;
	
	private Scene currentScene;

	static {
		JFaceResources.getImageRegistry().put(IMAGE_OK, Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/16_16/security-high.png"));
		JFaceResources.getImageRegistry().put(IMAGE_WARNING, Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/16_16/security-medium.png"));
		JFaceResources.getImageRegistry().put(IMAGE_ERROR, Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/16_16/security-low.png"));
		JFaceResources.getImageRegistry().put(IMAGE_PREVIEW, Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/16_16/view-preview.png"));

		JFaceResources.getImageRegistry().put(IMAGE_TAB_NORMAL, Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/16_16/view-preview.png"));
		JFaceResources.getImageRegistry().put(IMAGE_TAB_ERROR, new DecorationOverlayIcon(JFaceResources.getImage(IMAGE_TAB_NORMAL), Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/ovr/error_co.gif"), IDecoration.BOTTOM_LEFT));
		JFaceResources.getImageRegistry().put(IMAGE_TAB_WARNING, new DecorationOverlayIcon(JFaceResources.getImage(IMAGE_TAB_NORMAL), Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/ovr/warning_co.gif"), IDecoration.BOTTOM_LEFT));

		JFaceResources.getImageRegistry().put(IMAGE_STATUS_ERROR, Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/16_16/dialog-error.png"));
		JFaceResources.getImageRegistry().put(IMAGE_STATUS_WARNING, Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/16_16/dialog-warning.png"));
		JFaceResources.getImageRegistry().put(IMAGE_STATUS_OK, Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/16_16/dialog-ok-apply.png"));
		JFaceResources.getImageRegistry().put(IMAGE_STATUS_NOPREVIEW, Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/16_16/dialog-information.png"));

		JFaceResources.getImageRegistry().put(IMAGE_LOAD_CONTROLLER, Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/16_16/system-switch-user.png"));
		JFaceResources.getImageRegistry().put(IMAGE_REFRESH, Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/16_16/edit-clear.png"));

		JFaceResources.getImageRegistry().put(IMAGE_FXML_CONTENT, Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/16_16/page_white_code_red.png"));

	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);

		listener = new IPartListener() {

			@Override
			public void partOpened(IWorkbenchPart part) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partDeactivated(IWorkbenchPart part) {
				if (part == LivePreviewPart.this && swtFXContainer != null) {
					swtFXContainer.setEnabled(false);
				}
			}

			@Override
			public void partClosed(IWorkbenchPart part) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partBroughtToTop(IWorkbenchPart part) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partActivated(IWorkbenchPart part) {
				if (part == LivePreviewPart.this && swtFXContainer != null) {
					swtFXContainer.setEnabled(true);
				}
			}
		};

		site.getWorkbenchWindow().getPartService().addPartListener(synchronizer);
		site.getWorkbenchWindow().getPartService().addPartListener(listener);
	}

	@Override
	public void createPartControl(final Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(3, false));

		folder = new CTabFolder(container, SWT.BOTTOM | SWT.BORDER);
		folder.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 3, 1));

		document = new Document();
		
		parent.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {

				{
					CTabItem item = new CTabItem(folder, SWT.NONE);

					item.setText("Preview");
					item.setImage(JFaceResources.getImage(IMAGE_PREVIEW));

					swtFXContainer = new FXCanvas(folder, SWT.NONE);
					swtFXContainer.setEnabled(false);

					item.setControl(swtFXContainer);

					rootPane_new = new BorderPane();
					Scene scene = new Scene((Parent) rootPane_new, 1000, 1000);
					currentScene = scene;
					swtFXContainer.setScene(scene);
				}

				{
					logItem = new CTabItem(folder, SWT.NONE);
					logItem.setText("Error log");
					logItem.setImage(JFaceResources.getImage(IMAGE_OK));

					logStatement = new Text(folder, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
					logStatement.setEditable(false);
					logItem.setControl(logStatement);

					Menu m = new Menu(logStatement);
					logStatement.setMenu(m);
					MenuItem clearItem = new MenuItem(m, SWT.PUSH);
					clearItem.setText("Clear Log");
					clearItem.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							logStatement.setText("");
						}
					});
				}

				{
					CTabItem fxmlContent = new CTabItem(folder, SWT.NONE);
					fxmlContent.setText("FXML-Source");
					fxmlContent.setImage(JFaceResources.getImage(IMAGE_FXML_CONTENT));

					final AnnotationModel model = new AnnotationModel();
					VerticalRuler verticalRuler = new VerticalRuler(VERTICAL_RULER_WIDTH, new AnnotationAccess());
					int styles = SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION;
					SourceViewer sourceViewer = new SourceViewer(folder, verticalRuler, styles);
					sourceViewer.configure(new XMLConfiguration(new ColorManager()));
					sourceViewer.setEditable(false);
					sourceViewer.getTextWidget().setFont(JFaceResources.getTextFont());

					IDocumentPartitioner partitioner = new FastPartitioner(new XMLPartitionScanner(), new String[] { XMLPartitionScanner.XML_TAG, XMLPartitionScanner.XML_COMMENT });
					partitioner.connect(document);
					document.setDocumentPartitioner(partitioner);
					sourceViewer.setDocument(document);
					verticalRuler.setModel(model);
					fxmlContent.setControl(sourceViewer.getControl());
				}

				folder.setSelection(0);

				statusLabelIcon = new Label(container, SWT.NONE);
				statusLabelIcon.setImage(JFaceResources.getImage(IMAGE_STATUS_NOPREVIEW));

				statusLabelText = new Label(container, SWT.NONE);
				statusLabelText.setText(NO_PREVIEW_TEXT);
				statusLabelText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

				Composite scaleControl = new Composite(container, SWT.NONE);
				scaleControl.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));
				scaleControl.setLayout(new GridLayout(2, false));

				Label l = new Label(scaleControl, SWT.NONE);
				l.setText("Zoom");

				scale = new Spinner(scaleControl, SWT.BORDER);
				scale.setMinimum(10);
				scale.setMaximum(500);
				scale.setIncrement(10);
				scale.setSelection(100);
				scale.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						rootPane_new.setScaleX(scale.getSelection() / 100.0);
						rootPane_new.setScaleY(scale.getSelection() / 100.0);
						if (currentFile != null) {
							scaleMap.put(currentFile, scale.getSelection());
						}
					}
				});
				
				parent.layout(true, true);
				
				if( currentData != null ) {
					refreshContent(currentData);
				}
			}
		});
		
		parent.layout(true, true);

		Action loadController = new Action("", IAction.AS_CHECK_BOX) {
			@Override
			public void run() {
				preference.putBoolean(PREF_LOAD_CONTROLLER, !preference.getBoolean(PREF_LOAD_CONTROLLER, false));
				try {
					preference.flush();
					synchronizer.refreshPreview();
				} catch (BackingStoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		loadController.setChecked(preference.getBoolean(PREF_LOAD_CONTROLLER, false));
		loadController.setImageDescriptor(JFaceResources.getImageRegistry().getDescriptor(IMAGE_LOAD_CONTROLLER));
		loadController.setToolTipText("Load the controller");

		Action refresh = new Action("", JFaceResources.getImageRegistry().getDescriptor(IMAGE_REFRESH)) {
			@Override
			public void run() {
				synchronizer.refreshPreview();
			}
		};
		refresh.setToolTipText("Force a refresh");

		getViewSite().getActionBars().getToolBarManager().add(refresh);
		getViewSite().getActionBars().getToolBarManager().add(loadController);
	}

	@Override
	public void dispose() {
		
		if( currentScene != null ) {
			currentScene.getStylesheets().clear();
		}
				
		getSite().getWorkbenchWindow().getPartService().removePartListener(synchronizer);
		getSite().getWorkbenchWindow().getPartService().addPartListener(listener);

		super.dispose();
	}

	@Override
	public void setFocus() {
		folder.setFocus();
	}

	private void refreshContent(final ContentData contentData) {
		this.currentData = contentData;
		if (folder != null && !folder.isDisposed()) {
			folder.getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {
					try {
						saveRefreshContent(contentData);	
					} catch(Throwable t) {
						t.printStackTrace();
					}
				}
			});
		}
	}

	private void saveRefreshContent(final ContentData contentData) {
		folder.setVisible(true);

		ClassLoader cl = null;

		FXMLLoader loader;
		if (contentData.extraJarPath != null && !contentData.extraJarPath.isEmpty() && swtFXContainer != null) {
			final URLClassLoader previewClassLoader = new PreviewURLClassloader(contentData.extraJarPath.toArray(new URL[0]), swtFXContainer.getClass().getClassLoader());

			cl = Thread.currentThread().getContextClassLoader();
				Thread.currentThread().setContextClassLoader(previewClassLoader);

				loader = new FXMLLoader();
				loader.setBuilderFactory(new BuilderFactory() {
					private BuilderFactory f = new JavaFXBuilderFactory(previewClassLoader);
					@Override
					public Builder<?> getBuilder(Class<?> type) {
						return f.getBuilder(type);
					}
				});
				loader.setClassLoader(previewClassLoader);
		} else {
			loader = new FXMLLoader();
		}

		String exception = null;

		try {
			currentFile = contentData.file;
			loader.setStaticLoad(!preference.getBoolean(PREF_LOAD_CONTROLLER, false));
			try {
				// TODO Should we set this to the bin-Folder??
				loader.setLocation(contentData.file.getParent().getLocation().toFile().getAbsoluteFile().toURI().toURL());
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (contentData.resourceBundle != null) {
				FileInputStream in = null;
				try {
					in = new FileInputStream(new File(contentData.resourceBundle));
					Properties p = new Properties();
					p.load(in);

					final Object[][] entries = new Object[p.entrySet().size()][];
					int i = 0;
					for (Entry<Object, Object> e : p.entrySet()) {
						entries[i++] = new Object[] { e.getKey(), e.getValue() };
					}

					ListResourceBundle b = new ListResourceBundle() {

						@Override
						protected Object[][] getContents() {
							return entries;
						}
					};
					loader.setResources(b);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

			try {
				document.set(contentData.contents);
				ByteArrayInputStream out = new ByteArrayInputStream(contentData.contents.getBytes());
				Object root = loader.load(out);
				out.close();

				Scene scene = null;
				if (root instanceof Scene) {
					scene = (Scene) root;
					rootPane_new = scene.getRoot();
				} else {
					if( contentData.previewSceneSetup != null ) {
						ByteArrayInputStream sceneOut = new ByteArrayInputStream(contentData.previewSceneSetup.getBytes());
						Object sceneRoot = loader.load(sceneOut);
						if( sceneRoot instanceof Scene ) {
							scene = (Scene) sceneRoot;
						}
					}
					
					rootPane_new = (Node) root;
					
					if( scene == null ) {
						BorderPane p = new BorderPane();
						p.setCenter(rootPane_new);
						scene = new Scene(p, 10000, 10000, Platform.isSupported(ConditionalFeature.SCENE3D));
						if( Platform.isSupported(ConditionalFeature.SCENE3D) ) {
							scene.setCamera(new PerspectiveCamera());
						}	
					} else {
						Node n = scene.getRoot().lookup("#previewcontainer");
						if( n == null || ! (n instanceof Parent) ) {
							n = scene.getRoot();
						}
						
						String previewFeature = null;
						
						if( n.getUserData() != null && n.getUserData() != null ) {
							previewFeature = n.getUserData().toString().trim();
						}
						
						if( previewFeature != null ) {
							try {
								String mName = "set" + Character.toUpperCase(previewFeature.charAt(0)) + previewFeature.substring(1);
								Method m = n.getClass().getMethod(mName, Node.class);
								m.invoke(n, rootPane_new);
							} catch(Throwable t) {
								String mName = "get" + Character.toUpperCase(previewFeature.charAt(0)) + previewFeature.substring(1);
								Method m = n.getClass().getMethod(mName);
								Object o = m.invoke(n);
								if( o instanceof List ) {
									((List<Object>)o).add(rootPane_new);
								}
							}
						} else {
							if( n instanceof BorderPane ) {
								((BorderPane)n).setCenter(rootPane_new);
							} else if( n instanceof Pane ) {
								((Pane)n).getChildren().add(rootPane_new);
							} else {
								throw new IllegalStateException("The parent in the scene is not a Pane. Set a preview-property styleclass on the control");
							}
						}
					}
				}

				if( rootPane_new != null ) {
					if (scaleMap.containsKey(currentFile)) {
						int value = scaleMap.get(currentFile).intValue();
						scale.setSelection(value);

						rootPane_new.setScaleX(value / 100.0);
						rootPane_new.setScaleY(value / 100.0);

					} else {
						scale.setSelection(100);
						rootPane_new.setScaleX(1);
						rootPane_new.setScaleY(1);
					}
				}
				
				// Force CSS-Reloading
				if( isJavaFX2() ) {
					ReflectiveInvoke.onStyleManagerClass(scene);
				}
				
				// In FX8 we need to remove the stylesheets on the old scene to force reloading them
				if( swtFXContainer.getScene() != null ) {
					swtFXContainer.getScene().getStylesheets().clear();	
				}
				
				scene.getStylesheets().addAll(contentData.cssFiles);
				currentScene = scene;
				swtFXContainer.setScene(scene);
				
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				exception = sw.toString();
			}
		} finally {
			if (cl != null) {
				Thread.currentThread().setContextClassLoader(cl);
			}
		}

		if (exception != null) {
			final String innerException = exception;
			if (folder.isDisposed()) {
				return;
			}

			folder.getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					if (innerException != null) {
						logItem.setImage(JFaceResources.getImage(IMAGE_ERROR));
						statusLabelIcon.setImage(JFaceResources.getImage(IMAGE_STATUS_ERROR));
						statusLabelText.setText(SimpleDateFormat.getTimeInstance().format(new Date()) + ": Error while updating preview");
						setTitleImage(JFaceResources.getImage(IMAGE_TAB_ERROR));
						folder.setSelection(logItem);
					}

					logStatement.setText("");
					logStatement.append("=================================================================" + logStatement.getLineDelimiter());
					logStatement.append("Preview loading @ " + new Date() + logStatement.getLineDelimiter());
					logStatement.append("=================================================================" + logStatement.getLineDelimiter());

					if (innerException != null) {
						logStatement.append("Exception:" + logStatement.getLineDelimiter());
						logStatement.append("----------" + logStatement.getLineDelimiter());
						logStatement.append(innerException + logStatement.getLineDelimiter());
						logStatement.append(logStatement.getLineDelimiter() + logStatement.getLineDelimiter());
						logStatement.setSelection(0);
					}
				}
			});

		} else {
			folder.getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					folder.setSelection(0);
					logItem.setImage(JFaceResources.getImage(IMAGE_OK));
					statusLabelIcon.setImage(JFaceResources.getImage(IMAGE_STATUS_OK));
					statusLabelText.setText(SimpleDateFormat.getTimeInstance().format(new Date()) + ": Preview updated");
					setTitleImage(JFaceResources.getImage(IMAGE_TAB_NORMAL));
				}
			});
		}
	}

	public void setContents(final ContentData contentData) {
		if (folder.isDisposed()) {
			return;
		}

		if (contentData != null && contentData.contents != null) {
			refreshContent(contentData);
		} else if (rootPane_new != null) {
			folder.getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {
					statusLabelIcon.setImage(JFaceResources.getImage(IMAGE_STATUS_NOPREVIEW));
					statusLabelText.setText(NO_PREVIEW_TEXT);
					folder.setVisible(false);
				}
			});
		}
	}
	
	private static boolean isJavaFX2() {
		return System.getProperty("javafx.version") != null && System.getProperty("javafx.version").startsWith("2");
	}

	public static class ContentData {
		public String contents;
		public String previewSceneSetup;
		public List<String> cssFiles;
		public String resourceBundle;
		public List<URL> extraJarPath;
		public IFile file;
		
		public ContentData(String contents, String previewSceneSetup, List<String> cssFiles, String resourceBundle, List<URL> extraJarPath, IFile file) {
			this.contents = contents;
			this.previewSceneSetup = previewSceneSetup;
			this.cssFiles = new ArrayList<String>(cssFiles);
			this.resourceBundle = resourceBundle;
			this.extraJarPath = extraJarPath;
			this.file = file;
		}
	}

	static class PreviewURLClassloader extends URLClassLoader {

		public PreviewURLClassloader(URL[] urls, ClassLoader parent) {
			super(urls, parent);
		}
		
		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			return super.loadClass(name);
		}
	}
}
