/*
 * Copyright 2009-2012 Markus Koller
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
package ch.blackspirit.graphics.jogl2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.nativewindow.AbstractGraphicsDevice;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLException;
import javax.media.opengl.GLPbuffer;
import javax.media.opengl.GLProfile;
import javax.media.opengl.glu.GLU;

import ch.blackspirit.graphics.DisplayMode;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.ResourceManager;
import ch.blackspirit.graphics.WindowListener;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.ScreenMode;
import com.jogamp.newt.event.WindowUpdateEvent;
import com.jogamp.newt.opengl.GLWindow;

/**
 * @author Markus Koller
 */
final class NewtCanvas extends AbstractGraphicsContext implements ch.blackspirit.graphics.RealtimeCanvas, RuntimeProperties {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	
	private static final GLCapabilities CAPABILITIES = new GLCapabilities(GLProfile.get(GLProfile.GL2));
	static {
		CAPABILITIES.setDepthBits(0);
		CAPABILITIES.setAlphaBits(8);
		CAPABILITIES.setDoubleBuffered(true);
//		CAPABILITIES.setPBuffer(true);
		// FSAA 4x
//		CAPABILITIES.setSampleBuffers(true);
//		CAPABILITIES.setNumSamples(4);
	}

	static GLCapabilities PBUFFER_CAPABILITIES = new GLCapabilities(GLProfile.get(GLProfile.GL2));
	static {
		PBUFFER_CAPABILITIES.setDoubleBuffered(false);
		PBUFFER_CAPABILITIES.setPBuffer(true);
//		PBUFFER_CAPABILITIES.setAlphaBits(8);
	}
	
	private CanvasProperties properties = new CanvasProperties();
	
//	private JFrame frame;
	private String title = "";
	private boolean fullscreen = false;
	
	private boolean usePBuffer = true;

	private Screen screen;
	private GLWindow window;
//	private GLAutoDrawable imageDrawable;
	private RenderContext canvasRenderContext = new RenderContext();
	private RenderContext imageRenderContext = new RenderContext();
	
	private final View view = new View();
	private final ch.blackspirit.graphics.jogl2.ResourceManager resourceManager;
	private final ch.blackspirit.graphics.jogl2.ImageFactory imageFactory;
	private GraphicsDelegate delegate;
	private CanvasGraphics canvasGraphics;
	private GraphicsListener graphicsListener;
	private CanvasGLEventListener canvasGLEventListener;
	
	private GLExecutor glExecutor;

	private boolean isGlExtBlendSubtractSupported;
	private long maxImageDrawingWidth;
	private long maxImageDrawingHeight;
	
	private final ArrayList<WindowListener> windowListener = new ArrayList<WindowListener>(100);
	
	private NewtCanvas(Screen screen, CanvasProperties properties) {
		this.properties = properties;
		this.screen = screen;
		
		glExecutor = new RenderContextGLExecutor(imageRenderContext, properties);
		resourceManager = new ch.blackspirit.graphics.jogl2.ResourceManager(glExecutor, this);
		imageFactory = new ch.blackspirit.graphics.jogl2.ImageFactory(resourceManager);
		
		createWindow();
	}
	
	public NewtCanvas(Screen screen, DisplayMode displayMode, CanvasProperties properties) {
		this(screen, properties);
		
		// initialize view size and camera
		if(displayMode == null) {
			// use current mode to initialize
			ScreenMode mode = screen.getCurrentScreenMode();
			view.setSize(mode.getRotatedWidth(), mode.getRotatedHeight());
		} else { 
			view.setSize(displayMode.getWidth(), displayMode.getHeight());
		}
		view.setCamera(0, 0, 0);

		setFullscreen(displayMode);
		initializeGraphics();
	}
	public NewtCanvas(Screen screen, int width, int height, CanvasProperties properties) {
		this(screen, properties);

		// initialize view size and camera
		view.setSize(width, height);
		view.setCamera(0, 0, 0);
		
		setWindow(width, height);
		initializeGraphics();
	}
	
	public void createWindow() {
		window = GLWindow.create(screen, CAPABILITIES);
		window.addWindowListener(new DelegateWindowListener(windowListener));
		// set no action on close
		// set no mouse icon
		window.setPointerVisible(false);
//		window.confinePointer(mouseConfined);
		window.setTitle(title);
	}
	
	public int getScreenLocationX() {
		return window.getLocationOnScreen(new javax.media.nativewindow.util.Point(0,0)).getX();
	}
	public int getScreenLocationY() {
		return window.getLocationOnScreen(new javax.media.nativewindow.util.Point(0,0)).getY();
	}
	
	private void initializeGraphics() {
		delegate = new JOGLGraphicsDelegate(canvasRenderContext, resourceManager, this);
		canvasGraphics = new CanvasGraphics(delegate, view);
        canvasGLEventListener = new CanvasGLEventListener(this, resourceManager, imageFactory, view, canvasGraphics);
        canvasGLEventListener.setDebugGL(properties.isDebugGL());
        canvasGLEventListener.setTrace(properties.isTraceEnabled());
        canvasGLEventListener.setTraceLevel(properties.getTraceLogLevel());
		canvasRenderContext.setMainGLEventListener(canvasGLEventListener);
        
        
		SupportGLExecutable supportGLExecutable = new SupportGLExecutable();
		glExecutor.execute(supportGLExecutable);
		isGlExtBlendSubtractSupported = supportGLExecutable.isGlExtBlendSubtractSupported;
		LOGGER.info("Graphics Card info: " + supportGLExecutable.vendor + 
				" - " + supportGLExecutable.renderer + 
				" - " +	supportGLExecutable.version);
		LOGGER.info("DrawingMode.SUBTRACT supported: " + isGlExtBlendSubtractSupported);
	}
	
	private void initialize(int width, int height) {
		GLDrawableFactory drawableFactory = GLDrawableFactory.getFactory(GLProfile.get(GLProfile.GL2));
		AbstractGraphicsDevice graphicsDevice = drawableFactory.getDefaultDevice();

//		if(!drawableFactory.canCreateGLPbuffer(graphicsDevice)) {
//			throw new RuntimeException("PBuffer not supported but required in this implementation!");
//		}
		
		int imageDrawingWidth = properties.getImageDrawingWidth();
		int imageDrawingHeight = properties.getImageDrawingHeight();
		LOGGER.info("Requested image drawing size: " + imageDrawingWidth + "x" + imageDrawingHeight);
		
		if(!(imageRenderContext.getDrawable() instanceof GLPbuffer)) {
			boolean cannotCreatePBuffer = !drawableFactory.canCreateGLPbuffer(graphicsDevice);
			// debug fallback
			if(!properties.isPBuffer()) cannotCreatePBuffer = true;
			if(!usePBuffer) cannotCreatePBuffer = true;
			if(!cannotCreatePBuffer) {
				try {
					GLAutoDrawable imageDrawable = drawableFactory.createGLPbuffer(graphicsDevice, 
							PBUFFER_CAPABILITIES, null, imageDrawingWidth, imageDrawingHeight, window.getContext());
					imageRenderContext.setDrawable(imageDrawable);
					
					SupportGLExecutable supportGLExecutable = new SupportGLExecutable();
					glExecutor.execute(supportGLExecutable);
					String vendor = supportGLExecutable.vendor;
					if (vendor != null && vendor.toLowerCase().contains("intel")) {
						imageDrawable.getContext().destroy();
						imageDrawable = null;
						usePBuffer = false;
						cannotCreatePBuffer = true;
					}
				} catch(Exception e) {
					cannotCreatePBuffer = true;
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

		canvasRenderContext.setDrawable(window);

        if(imageRenderContext.getDrawable() == null) {
        	imageDrawingHeight = height;
        	imageDrawingWidth = width;
        	imageRenderContext.setDrawable(window);
        }

        LOGGER.info("Actual image drawing size: " + imageDrawingWidth + "x" + imageDrawingHeight);
        maxImageDrawingHeight = imageDrawingHeight;
        maxImageDrawingWidth = imageDrawingWidth;
	}
	
	public void setFullscreen(DisplayMode displayMode) {
		// TODO handle refresh rate and bitdepth
		ScreenMode setup = null;
		if (displayMode != null) {
			ScreenMode current = screen.getCurrentScreenMode();
			for (ScreenMode mode: screen.getScreenModes()) {
				if (current.getRotation() == mode.getRotation() &&
						mode.getRotatedWidth() == displayMode.getWidth() && 
						mode.getRotatedHeight() == displayMode.getHeight() &&
						mode.getMonitorMode().getRefreshRate() == displayMode.getRefreshRate() &&
						mode.getMonitorMode().getSurfaceSize().getBitsPerPixel() == displayMode.getColorDepth()) {
					setup = mode;
				}
			}
			if(setup == null) {
				LOGGER.severe("No Mode with resolution: " + displayMode.getWidth() + "x" + displayMode.getHeight() + " available. Choosing unavailable resolutions should not be possible.");
				throw new RuntimeException("No Mode with resolution: " + displayMode.getWidth() + "x" + displayMode.getHeight() + " available. Choosing unavailable resolutions should not be possible.");
			}
			LOGGER.info("Fullscreen: Width=" + setup.getRotatedWidth() + 
					" Height=" + setup.getRotatedHeight());
//					" BitDepth=" + 	setup.getBitDepth() + 
//					" RefreshRate=" + setup.getRefreshRate());
		}
		
		if (window.isVisible()) {
			window.setVisible(false);
		}
		window.setFullscreen(true);
		window.setVisible(true);
		
		boolean useCurrentScreenMode = true;
		if(displayMode!=null) {
			try {
				screen.setCurrentScreenMode(setup);
				useCurrentScreenMode = false;
			} catch(Exception e) {
				LOGGER.log(Level.SEVERE, "Failed to change display mode", e);
			}
		}
		if (useCurrentScreenMode) {
			setup = screen.getCurrentScreenMode();
		}
		initialize(setup.getRotatedWidth(), setup.getRotatedHeight());
//		frame.setUndecorated(true);
//		frame.setResizable(false);
//
		fullscreen = true;
	}	
	public void setWindow(int width, int height) {
		if (window.isVisible()) {
			window.setVisible(false);
		}
		window.setFullscreen(false);
		window.setSize(width, height);
		window.setVisible(true);
		
		initialize(width, height);
		fullscreen = false;

		LOGGER.info("Window: Width=" + width + 
				" Height=" + height);
	}
	public void setWindowTitle(String title) {
		this.title = title;
//		if(frame != null) {
//			frame.setTitle(title);
//		}
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
		return window.getWidth();
	}

	public int getHeight() {
		return window.getHeight();
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
		window.getScreen().setCurrentScreenMode(window.getScreen().getOriginalScreenMode());
		imageRenderContext.destroy();
		canvasRenderContext.destroy();
		window.destroy();
		imageRenderContext = null;
		canvasRenderContext = null;
		window = null;
	}
	public ResourceManager getResourceManager() {
		return resourceManager;
	}
	public void setFullscreen() {
		setFullscreen(null);
	}
	
	public void draw() {
		if(window.isVisible()) window.display();
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
		ch.blackspirit.graphics.jogl2.Image joglImage = (ch.blackspirit.graphics.jogl2.Image)image;
		return new ImageGraphicsContext(joglImage, imageRenderContext, resourceManager, this, properties);
	}

	
	private static final class DelegateWindowListener implements com.jogamp.newt.event.WindowListener{
		private ArrayList<WindowListener> listener;
		public DelegateWindowListener(ArrayList<WindowListener> listener) {
			this.listener = listener;
		}
		
//		public void windowDeiconified(WindowEvent e) {
//			synchronized ( listener) {
//				for(int i = 0; i < listener.size(); i++) {
//					listener.get(i).windowDeiconified();
//				}
//			}
//		}
//		public void windowIconified(WindowEvent e) {
//			synchronized ( listener) {
//				for(int i = 0; i < listener.size(); i++) {
//					listener.get(i).windowIconified();
//				}
//			}
//		}

		@Override
		public void windowMoved(com.jogamp.newt.event.WindowEvent arg0) {}
		@Override
		public void windowRepaint(WindowUpdateEvent arg0) {}
		@Override
		public void windowResized(com.jogamp.newt.event.WindowEvent arg0) {}
		@Override
		public void windowDestroyed(com.jogamp.newt.event.WindowEvent arg0) {}
		@Override
		public void windowDestroyNotify(com.jogamp.newt.event.WindowEvent arg0) {
			synchronized ( listener) {
				for(int i = 0; i < listener.size(); i++) {
					listener.get(i).windowClosing();
				}
			}
		}
		@Override
		public void windowGainedFocus(com.jogamp.newt.event.WindowEvent arg0) {
			synchronized ( listener) {
				for(int i = 0; i < listener.size(); i++) {
					listener.get(i).windowActivated();
				}
			}
		}
		@Override
		public void windowLostFocus(com.jogamp.newt.event.WindowEvent arg0) {
			synchronized ( listener) {
				for(int i = 0; i < listener.size(); i++) {
					listener.get(i).windowDeactivated();
				}
			}
		}
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
	
//	/**
//	 * It is important that no other thread displays the canvas.
//	 * Otherwise control over threading gets lost!
//	 */
//	private static class NoPaintGLCanvas extends GLCanvas {
//		private static final long serialVersionUID = 1L;
//		/*public NoPaintGLCanvas() {
//			super();
//		}*/
//		public NoPaintGLCanvas(GLCapabilities capabilities,
//				GLCapabilitiesChooser chooser, GLContext shareWith,
//				GraphicsDevice device) {
//			super(capabilities, chooser, shareWith, device);
//		}
//		/*public NoPaintGLCanvas(GLCapabilities capabilities) {
//			super(capabilities);
//		}*/
//		// Paint does nothing!
//		public void paint(Graphics g) {}
//	}
}
