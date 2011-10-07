/*
 * Copyright 2009 Markus Koller
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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCapabilitiesChooser;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLException;
import javax.media.opengl.GLPbuffer;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import ch.blackspirit.graphics.DisplayMode;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.ResourceManager;
import ch.blackspirit.graphics.WindowListener;

/**
 * @author Markus Koller
 */
final class RealtimeCanvas extends AbstractGraphicsContext implements ch.blackspirit.graphics.RealtimeCanvas, GLExecutor, RuntimeProperties {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	
	private static final GLCapabilities CAPABILITIES = new GLCapabilities();
	static {
		CAPABILITIES.setDepthBits(0);
		CAPABILITIES.setAlphaBits(8);
		CAPABILITIES.setDoubleBuffered(true);
		// FSAA 4x
//		CAPABILITIES.setSampleBuffers(true);
//		CAPABILITIES.setNumSamples(4);
	}

	static GLCapabilities PBUFFER_CAPABILITIES = new GLCapabilities();
	static {
		PBUFFER_CAPABILITIES.setDoubleBuffered(false);
		PBUFFER_CAPABILITIES.setAlphaBits(8);
	}
	
	private CanvasProperties properties = new CanvasProperties();
	
	private JFrame frame;
	private String title = "";
	private boolean fullscreen = false;
	
	private boolean usePBuffer = true;

	private GLContext glContext = null;
	private GLCanvas canvas;
	private GLAutoDrawable imageDrawable;
	private RenderContext canvasRenderContext = new RenderContext();
	private RenderContext imageRenderContext = new RenderContext();
	
	private final View view = new View();
	private final ch.blackspirit.graphics.jogl.ResourceManager resourceManager = 
		new ch.blackspirit.graphics.jogl.ResourceManager(this, this);
	private final ch.blackspirit.graphics.jogl.ImageFactory imageFactory = 
		new ch.blackspirit.graphics.jogl.ImageFactory(resourceManager);
	private GraphicsDelegate delegate;
	private CanvasGraphics canvasGraphics;
	private GraphicsListener graphicsListener;
	private CanvasGLEventListener canvasGLEventListener;

	private boolean isGlExtBlendSubtractSupported;
	private long maxImageDrawingWidth;
	private long maxImageDrawingHeight;
	
	private GLExecutableGLEventListener executableListener = new GLExecutableGLEventListener();
	
	private final ArrayList<WindowListener> windowListener = new ArrayList<WindowListener>(100);
	
	public RealtimeCanvas(DisplayMode displayMode, CanvasProperties properties) {
		this.properties = properties;
		
		// initialize view size and camera
		if(displayMode == null) {
			// use current mode to initialize
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice dev = env.getDefaultScreenDevice();
			java.awt.DisplayMode mode = dev.getDisplayMode();
			view.setSize(mode.getWidth(), mode.getHeight());
		} else { 
			view.setSize(displayMode.getWidth(), displayMode.getHeight());
		}
		view.setCamera(0, 0, 0);

		setFullscreen(displayMode);
		initializeGraphics();
	}
	public RealtimeCanvas(int width, int height, CanvasProperties properties) {
		this.properties = properties;

		// initialize view size and camera
		view.setSize(width, height);
		view.setCamera(0, 0, 0);
		
		setWindow(width, height);
		initializeGraphics();
	}
	
	public int getScreenLocationX() {
		return canvas.getLocationOnScreen().x;
	}
	public int getScreenLocationY() {
		return canvas.getLocationOnScreen().y;
	}
	
	private void initializeGraphics() {
		delegate = new JOGLGraphicsDelegate(canvasRenderContext, resourceManager, this);
		canvasGraphics = new CanvasGraphics(delegate, view);
        canvasGLEventListener = new CanvasGLEventListener(this, resourceManager, imageFactory, view, canvasGraphics);
        canvasGLEventListener.setDebugGL(properties.isDebugGL());
        canvasGLEventListener.setTrace(properties.isTraceEnabled());
        canvasGLEventListener.setTraceLevel(properties.getTraceLogLevel());
		canvasRenderContext.setMainGLEventListener(canvasGLEventListener);
        
        executableListener.setDebugGL(properties.isDebugGL());
        executableListener.setTrace(properties.isTraceEnabled());
        executableListener.setTraceLevel(properties.getTraceLogLevel());
        
		SupportGLExecutable supportGLExecutable = new SupportGLExecutable();
		execute(supportGLExecutable);
		isGlExtBlendSubtractSupported = supportGLExecutable.isGlExtBlendSubtractSupported;
		LOGGER.info("Graphics Card info: " + supportGLExecutable.vendor + 
				" - " + supportGLExecutable.renderer + 
				" - " +	supportGLExecutable.version);
		LOGGER.info("DrawingMode.SUBTRACT supported: " + isGlExtBlendSubtractSupported);
	}
	
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

	private void initialize(int width, int height) {
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
			if(!usePBuffer) cannotCreatePBuffer = true;
			if(!cannotCreatePBuffer) {
				try {
					imageDrawable = GLDrawableFactory.getFactory().createGLPbuffer(PBUFFER_CAPABILITIES, null, imageDrawingWidth, imageDrawingHeight, glContext);
					glContext = imageDrawable.getContext();
					imageRenderContext.setDrawable(imageDrawable);
					
					SupportGLExecutable supportGLExecutable = new SupportGLExecutable();
					execute(supportGLExecutable);
					String vendor = supportGLExecutable.vendor;
					if (vendor != null && vendor.toLowerCase().contains("intel")) {
						imageDrawable.getContext().destroy();
						imageDrawable = null;
						glContext = null;
						usePBuffer = false;
						cannotCreatePBuffer = true;
					}
				} catch(Exception e) {
					cannotCreatePBuffer = true;
					imageDrawable = null;
					glContext = null;
				}
			}
			if(cannotCreatePBuffer)	{
				if (usePBuffer)
					LOGGER.info("Unable to create pbuffer. Using frame buffer for image drawing.");
				else
					LOGGER.info("Not using pbuffer with Intel graphics cards. Using frame buffer for image drawing.");
				imageRenderContext.setDelegateRenderContext(canvasRenderContext);
			}
		}

		if(canvas != null) canvas.getContext().destroy();
		canvas = new NoPaintGLCanvas(CAPABILITIES, null, glContext, null);
		if(glContext == null) glContext = canvas.getContext();
		canvasRenderContext.setDrawable(canvas);
        canvas.setFocusable(false);
        canvas.setFocusTraversalKeysEnabled(false);
        canvas.setEnabled(false);
        canvas.setAutoSwapBufferMode(true);

        if(imageDrawable == null) {
        	imageDrawingHeight = height;
        	imageDrawingWidth = width;
        	imageDrawable = canvas;
        }

        LOGGER.info("Actual image drawing size: " + imageDrawingWidth + "x" + imageDrawingHeight);
        maxImageDrawingHeight = imageDrawingHeight;
        maxImageDrawingWidth = imageDrawingWidth;
	}
	
	private JFrame createFrame() {
		JFrame frame = new JFrame();
		frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				Toolkit.getDefaultToolkit().getImage(""),
				new Point(0,0),
				"invisible"));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new DelegateWindowListener(windowListener));
		frame.setTitle(title);
		return frame;
	}
	
	public void setFullscreen(DisplayMode displayMode) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice dev = env.getDefaultScreenDevice();

		java.awt.DisplayMode setup = null;
		if(displayMode != null) {
			for(java.awt.DisplayMode mode :dev.getDisplayModes()) {
				if(mode.getWidth() == displayMode.getWidth() && 
						mode.getHeight() == displayMode.getHeight() &&
						mode.getBitDepth() == displayMode.getColorDepth() &&
						mode.getRefreshRate() == displayMode.getRefreshRate()) {
					setup = mode;
				}
			}
			if(setup == null) {
				LOGGER.severe("No Mode with resolution: " + displayMode.getWidth() + "x" + displayMode.getHeight() + " available. Choosing unavailable resolutions should not be possible.");
				throw new RuntimeException("No Mode with resolution: " + displayMode.getWidth() + "x" + displayMode.getHeight() + " available. Choosing unavailable resolutions should not be possible.");
			}
			LOGGER.info("Fullscreen: Width=" + setup.getWidth() + 
					" Height=" + setup.getHeight() + 
					" BitDepth=" + 	setup.getBitDepth() + 
					" RefreshRate=" + setup.getRefreshRate());
		}
		
		// Switch to new frame
		JFrame old = frame;
		frame = createFrame();
		if(old != null) {
			old.setVisible(false);
			old.getContentPane().remove(canvas);
			frame.getContentPane().add(canvas);
			old.dispose();
		} else {
			int width;
			int height;
			if(displayMode != null) {
				width = displayMode.getWidth();
				height = displayMode.getHeight();
			} else {
				java.awt.DisplayMode currentMode = dev.getDisplayMode();
				width = currentMode.getWidth();
				height = currentMode.getHeight();
			}
			initialize(width, height);
			frame.getContentPane().add(canvas);
		}
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);

		dev.setFullScreenWindow(frame);
		if(displayMode!=null) {
			try {
				if(dev.isDisplayChangeSupported()) dev.setDisplayMode(setup);
			} catch(Exception e) {
				LOGGER.log(Level.SEVERE, "Failed to change display mode", e);
			}
		}
		fullscreen = true;
	}	
	public void setWindow(int width, int height) {
		// Disable fullscreen rendering
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice dev = env.getDefaultScreenDevice();
		if(fullscreen) {
			dev.setFullScreenWindow(null);
		}
		
		// Switch to new frame
		JFrame old = frame;
		frame = createFrame();
		if(old != null) {
			old.setVisible(false);
			old.getContentPane().remove(canvas);
			frame.getContentPane().add(canvas);
			old.dispose();
		} else {
			initialize(width, height);
			frame.getContentPane().add(canvas);
		}
		frame.getContentPane().setPreferredSize(new Dimension(width, height));

		// Resizable must be set before pack for proper sizes
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	
		fullscreen = false;

		LOGGER.info("Window: Width=" + width + 
				" Height=" + height);
	}
	public void setWindowTitle(String title) {
		this.title = title;
		if(frame != null) {
			frame.setTitle(title);
		}
	}
	public String getWindowTitle() {
		return title;
	}
	public boolean isFullscreen() {
		return fullscreen;
	}
	
	
//	public void init(GLAutoDrawable arg0) {
//	    gl.glLoadIdentity();
//
//		// Lighting
//		gl.glShadeModel(GL.GL_SMOOTH);
//		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, new float[] {1.0f, 1.0f, 1.0f, 1.0f}, 0);
//		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, new float[] {100.0f, 100.0f, 50.0f, 0.1f}, 0);
//		gl.glEnable(GL.GL_LIGHT0);
//	}

	public int getWidth() {
		return canvas.getWidth();
	}

	public int getHeight() {
		return canvas.getHeight();
	}

	public ch.blackspirit.graphics.ImageFactory getImageFactory() {
		return imageFactory;
	}
	public void addWindowListener(WindowListener windowListener) {
		synchronized (this.windowListener) {
			this.windowListener.add(windowListener);
		}
	}
	public void removeWindowListener(WindowListener windowListener) {
		synchronized (this.windowListener) {
			this.windowListener.remove(windowListener);
		}
	}
	public List<WindowListener> getWindowListeners() {
		return Collections.unmodifiableList(this.windowListener);
	}
	
	public boolean getVSync() {
		return canvasGLEventListener.getVSync();
	}

	public boolean setVSync(boolean enabled) {
		return canvasGLEventListener.setVSync(enabled);
	}
	
	public void dispose() {
		canvas.getContext().destroy();
		imageDrawable.getContext().destroy();
		frame.dispose();
		canvas = null;
		frame = null;
		imageDrawable = null;
	}
	public ResourceManager getResourceManager() {
		return resourceManager;
	}
	public void setFullscreen() {
		setFullscreen(null);
	}
	
	public void draw() {
		if(frame.isVisible()) canvas.display();
		if (canvasGLEventListener.getError() != null) {
			throw canvasGLEventListener.getError();
		} else if (canvasGLEventListener.getRuntimeException() != null) {
			throw canvasGLEventListener.getRuntimeException();
		}
	}
	public GraphicsListener getGraphicsListener() {
		return graphicsListener;
	}
	public void setGraphicsListener(GraphicsListener listener) {
		this.graphicsListener = listener;
		this.canvasGLEventListener.setInitiated(false);
	}
	
	public ch.blackspirit.graphics.ImageGraphicsContext createImageGraphicsContext(Image image) {
		if (!(image instanceof Image)) throw new RuntimeException("Image has not been created by the JOGL Blackspirit Graphics implementation!");
		ch.blackspirit.graphics.jogl.Image joglImage = (ch.blackspirit.graphics.jogl.Image)image;
		return new ImageGraphicsContext(joglImage, imageRenderContext, resourceManager, this, properties);
	}

	
	private static final class DelegateWindowListener implements java.awt.event.WindowListener{
		private ArrayList<WindowListener> listener;
		public DelegateWindowListener(ArrayList<WindowListener> listener) {
			this.listener = listener;
		}
		public void windowActivated(WindowEvent e) {
			synchronized ( listener) {
				for(int i = 0; i < listener.size(); i++) {
					listener.get(i).windowActivated();
				}
			}
		}
		public void windowClosed(WindowEvent e) {}
		public void windowClosing(WindowEvent e) {
			synchronized ( listener) {
				for(int i = 0; i < listener.size(); i++) {
					listener.get(i).windowClosing();
				}
			}
		}
		public void windowDeactivated(WindowEvent e) {
			synchronized ( listener) {
				for(int i = 0; i < listener.size(); i++) {
					listener.get(i).windowDeactivated();
				}
			}
		}
		public void windowDeiconified(WindowEvent e) {
			synchronized ( listener) {
				for(int i = 0; i < listener.size(); i++) {
					listener.get(i).windowDeiconified();
				}
			}
		}
		public void windowIconified(WindowEvent e) {
			synchronized ( listener) {
				for(int i = 0; i < listener.size(); i++) {
					listener.get(i).windowIconified();
				}
			}
		}
		public void windowOpened(WindowEvent e) {}
	}

	public boolean getPropertyBoolean(String property) {
		if(Properties.IS_DRAWING_MODE_SUBTRACT_SUPPORTED.equals(property)) {
			return isGlExtBlendSubtractSupported;
		}
		throw new IllegalArgumentException("No such property: " + property);
	}
	public float getPropertyFloat(String property) {
		throw new IllegalArgumentException("No such property: " + property);
	}
	public long getPropertyLong(String property) {
		if(Properties.MAX_IMAGE_DRAWING_HEIGHT.equals(property)) {
			return maxImageDrawingHeight;
		} else if(Properties.MAX_IMAGE_DRAWING_WIDTH.equals(property)) {
			return maxImageDrawingWidth;
		}
		throw new IllegalArgumentException("No such property: " + property);
	}
	
	/**
	 * It is important that no other thread displays the canvas.
	 * Otherwise control over threading gets lost!
	 */
	private static class NoPaintGLCanvas extends GLCanvas {
		private static final long serialVersionUID = 1L;
		/*public NoPaintGLCanvas() {
			super();
		}*/
		public NoPaintGLCanvas(GLCapabilities capabilities,
				GLCapabilitiesChooser chooser, GLContext shareWith,
				GraphicsDevice device) {
			super(capabilities, chooser, shareWith, device);
		}
		/*public NoPaintGLCanvas(GLCapabilities capabilities) {
			super(capabilities);
		}*/
		// Paint does nothing!
		public void paint(Graphics g) {}
	}
}
