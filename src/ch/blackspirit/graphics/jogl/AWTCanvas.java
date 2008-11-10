/*
 * Copyright 2008 Markus Koller
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.blackspirit.graphics.jogl;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.logging.Logger;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLJPanel;
import javax.media.opengl.GLPbuffer;
import javax.media.opengl.glu.GLU;
import javax.swing.SwingUtilities;

import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.ResourceManager;

/**
 * @author Markus Koller
 */
class AWTCanvas extends AbstractGraphicsContext implements ch.blackspirit.graphics.AWTCanvas, GLExecutor, GLEventListener, ComponentListener, RuntimeProperties {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	
	private static final class UpdateRunnable implements Runnable {
		private final GLAutoDrawable drawable;
		public UpdateRunnable(GLAutoDrawable drawable) {
			this.drawable = drawable;
		}
		public void run() {
			drawable.display();
		}
	}

	private static final GLCapabilities CAPABILITIES = new GLCapabilities();
	static {
		CAPABILITIES.setDepthBits(0);
		CAPABILITIES.setAlphaBits(8);
		CAPABILITIES.setDoubleBuffered(true);
	}

	static GLCapabilities PBUFFER_CAPABILITIES = new GLCapabilities();
	static {
		PBUFFER_CAPABILITIES.setDoubleBuffered(false);
		PBUFFER_CAPABILITIES.setAlphaBits(8);
	}
	
	private BSGraphicsProperties properties = new BSGraphicsProperties();
	
	private boolean lightweight;
	private GLContext glContext = null;
	private GLAutoDrawable imageDrawable;
	private RenderContext imageRenderContext = new RenderContext();
	protected GLAutoDrawable canvas;
	private Component component;
	private RenderContext canvasRenderContext = new RenderContext();
	private final View view = new View();
	private final ch.blackspirit.graphics.jogl.ResourceManager resourceManager = 
		new ch.blackspirit.graphics.jogl.ResourceManager(this, this);
	private final ch.blackspirit.graphics.jogl.ImageFactory imageFactory = 
		new ch.blackspirit.graphics.jogl.ImageFactory(resourceManager);
	private final GraphicsDelegate delegate;
	private final CanvasGraphics canvasGraphics;
	private UpdateRunnable updateRunnable;
	private final CanvasGLEventListener canvasGLEventListener;

	private boolean propertiesInitialized = false;
	
	private boolean isComponentDrawingSize;
	
	private boolean isGlExtBlendSubtractSupported = false;
	private long maxImageDrawingWidth = 0;
	private long maxImageDrawingHeight = 0;

	private GraphicsListener graphicsListener;

	public AWTCanvas(boolean lightweight) {
		this.lightweight = lightweight;
//		view.setSize(mode.getWidth(), mode.getHeight());
		view.setCamera(0, 0, 0);
		initialize(lightweight);

		delegate = new JOGLGraphicsDelegate(canvasRenderContext, resourceManager, this);
		canvasGraphics = new CanvasGraphics(delegate, view);
        canvasGLEventListener = new CanvasGLEventListener(this, resourceManager, imageFactory, view, canvasGraphics);
        canvasGLEventListener.setDebugGL(properties.isDebugGL());
        canvasGLEventListener.setTrace(properties.isTrace());
        canvasGLEventListener.setTraceLevel(properties.getTraceLogLevel());
        canvasRenderContext.setMainGLEventListener(canvasGLEventListener);

        executableListener.setDebugGL(properties.isDebugGL());
        executableListener.setTrace(properties.isTrace());
        executableListener.setTraceLevel(properties.getTraceLogLevel());
	}
	
	public GraphicsDelegate getGraphicsDelegate() {
		return delegate;
	}


	private GLExecutableGLEventListener executableListener = new GLExecutableGLEventListener();

	public boolean execute(GLExecutable glExecutable) {
		try {
			GL gl = GLU.getCurrentGL();
			glExecutable.execute(GLContext.getCurrent().getGLDrawable(), gl);
		} catch(GLException e) {
			// no context current
			try {
				imageDrawable.setAutoSwapBufferMode(false);
				imageRenderContext.setGLEventListener(executableListener);
				executableListener.executable = glExecutable;
				imageDrawable.display();
				imageDrawable.setAutoSwapBufferMode(true);
				imageRenderContext.resetGLEventListener();
			} catch(GLException e2) {
				// TODO This maybe is a real exception due to a problem not just because no canvas is visible
				return false;
			}
		}
		return true;
	}
	
	private void initialize(boolean lightweight) {
		if(!GLDrawableFactory.getFactory().canCreateGLPbuffer()) {
			throw new RuntimeException("PBuffer not supported but required in this implementation!");
		}
		
		int imageDrawingWidth = properties.getImageDrawingWidth();
		int imageDrawingHeight = properties.getImageDrawingHeight();
		LOGGER.info("Requested image drawing size: " + imageDrawingWidth + "x" + imageDrawingHeight);
		
		if(!(imageDrawable instanceof GLPbuffer)) imageDrawable = null;
		if(imageDrawable == null) {
			boolean cannotCreatePBuffer = !GLDrawableFactory.getFactory().canCreateGLPbuffer();
			// debug fallback
			if(!properties.isPBuffer()) cannotCreatePBuffer = true;
			if(!cannotCreatePBuffer) {
				try {
					imageDrawable = GLDrawableFactory.getFactory().createGLPbuffer(PBUFFER_CAPABILITIES, null, imageDrawingWidth, imageDrawingHeight, glContext);
					glContext = imageDrawable.getContext();
					imageRenderContext.setDrawable(imageDrawable);
			        LOGGER.info("Actual image drawing size: " + imageDrawingWidth + "x" + imageDrawingHeight);
			        maxImageDrawingHeight = imageDrawingHeight;
			        maxImageDrawingWidth = imageDrawingWidth;
			        isComponentDrawingSize = false;
				} catch(Exception e) {
					cannotCreatePBuffer = true;
					imageDrawable = null;
					glContext = null;
				}
			}
			if(cannotCreatePBuffer)	{
				LOGGER.info("Unable to create pbuffer using frame buffer instead");
				imageRenderContext.setDelegateRenderContext(canvasRenderContext);
			}
		}

		
		if(lightweight) {
			canvas = new GLJPanel(CAPABILITIES, null, glContext);
		} else {
			canvas = new GLCanvas(CAPABILITIES, null, glContext, null);
		}
		canvas.addGLEventListener(this);
		
		component = (Component)canvas;
		component.addComponentListener(this);
		updateRunnable = new UpdateRunnable(canvas);
//		if(glContext == null) glContext = canvas.getContext();
		canvasRenderContext.setDrawable(canvas);
//        canvas.setAutoSwapBufferMode(true);
        
        if(imageDrawable == null) {
        	imageDrawable = canvas;
            LOGGER.info("Actual image drawing size corresponds to component size");
            isComponentDrawingSize = true;
        }
	}
	
	public int getWidth() {
		return canvas.getWidth();
	}

	public int getHeight() {
		return canvas.getHeight();
	}

	public ch.blackspirit.graphics.ImageFactory getImageFactory() {
		return imageFactory;
	}
	public boolean getVSync() {
		return canvasGLEventListener.getVSync();
	}

	public boolean setVSync(boolean enabled) {
		return canvasGLEventListener.setVSync(enabled);
	}
	
	public void dispose() {
		canvas.getContext().destroy();
	}
	public ResourceManager getResourceManager() {
		return resourceManager;
	}
	public void draw() {
		SwingUtilities.invokeLater(updateRunnable);
	}
	public GraphicsListener getGraphicsListener() {
		return graphicsListener;
	}
	public void setGraphicsListener(GraphicsListener listener) {
		this.graphicsListener = listener;
		canvasGLEventListener.setInitiated(false);
	}
	
	public ch.blackspirit.graphics.ImageGraphicsContext createImageGraphicsContext(Image image) {
		if (!(image instanceof Image)) throw new RuntimeException("Image has not been created by the JOGL Blackspirit Graphics implementation!");
		ch.blackspirit.graphics.jogl.Image joglImage = (ch.blackspirit.graphics.jogl.Image)image;
		return new ImageGraphicsContext(joglImage, imageRenderContext, resourceManager, this, properties);
	}

	public Component getComponent() {
		return component;
	}

	public boolean isLightweight() {
		return lightweight;
	}

	public boolean getPropertyBoolean(String property) {
		if(!propertiesInitialized) throw new RuntimeException("Using the AWTCanvas properties are not available before the first rendering cycle.");
		if(Properties.IS_DRAWING_MODE_SUBTRACT_SUPPORTED.equals(property)) {
			return isGlExtBlendSubtractSupported;
		}
		throw new IllegalArgumentException("No such property: " + property);
	}
	public float getPropertyFloat(String property) {
		if(!propertiesInitialized) throw new RuntimeException("Using the AWTCanvas properties are not available before the first rendering cycle.");
		throw new IllegalArgumentException("No such property: " + property);
	}
	public long getPropertyLong(String property) {
		if(!propertiesInitialized) throw new RuntimeException("Using the AWTCanvas properties are not available before the first rendering cycle.");
		if(Properties.MAX_IMAGE_DRAWING_HEIGHT.equals(property)) {
			return maxImageDrawingHeight;
		} else if(Properties.MAX_IMAGE_DRAWING_WIDTH.equals(property)) {
			return maxImageDrawingWidth;
		}
		throw new IllegalArgumentException("No such property: " + property);
	}	

	public void componentResized(ComponentEvent e) {
		if(isComponentDrawingSize) {
			maxImageDrawingHeight = e.getComponent().getHeight();
			maxImageDrawingWidth = e.getComponent().getWidth();
		}

	}
	public void componentHidden(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}

	public void init(GLAutoDrawable drawable) {
		SupportGLExecutable supportGLExecutable = new SupportGLExecutable();
		execute(supportGLExecutable);
		isGlExtBlendSubtractSupported = supportGLExecutable.isGlExtBlendSubtractSupported;
		propertiesInitialized = true;
	}
	public void display(GLAutoDrawable drawable) {}
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
}
