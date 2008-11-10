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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.opengl.GLAutoDrawable;

import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.debug.TraceGraphics;

/**
 * @author Markus Koller
 */
class ImageGraphicsContext extends AbstractGraphicsContext implements ch.blackspirit.graphics.ImageGraphicsContext {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	private RenderContext context;
	private ResourceManager resourceManager;
	private GraphicsListener graphicsListener;
	private ImageGLEventListener glEventListener;
	private JOGLGraphicsDelegate delegate;
	private ImageGraphics graphics;
	private Image image;
	private View view = new View();
	private boolean disposed = false;
	private boolean initiated = false;
	
	private TraceGraphics traceGraphics = new TraceGraphics();

	public ImageGraphicsContext(Image image, RenderContext context, ResourceManager resourceManager, RuntimeProperties runtimeProperties, BSGraphicsProperties properties) {
		super();
		this.image = image;
		this.context = context;
		this.resourceManager = resourceManager;
		this.delegate = new JOGLGraphicsDelegate(this.context, this.resourceManager, runtimeProperties);
		view.setSize(image.getWidth(), image.getHeight());
		view.setCamera(0, 0, 0);
		this.graphics = new ImageGraphics(image, delegate, this.resourceManager, view);
		this.glEventListener = new ImageGLEventListener(this);
		this.glEventListener.setDebugGL(properties.isDebugGL());
		this.glEventListener.setTrace(properties.isTrace());
		this.glEventListener.setTraceLevel(properties.getTraceLogLevel());
	}

	public GraphicsDelegate getGraphicsDelegate() {
		return delegate;
	}
	
	public void dispose() {
		this.disposed = true;
		
		this.context = null;
		this.resourceManager = null;
		this.graphicsListener = null;
		this.glEventListener = null;
		this.delegate = null;
		
		this.graphics.dispose();
		this.graphics = null;
		
		this.image = null;
		this.view = null;
	}

	public void draw() {
		if(disposed) throw new RuntimeException("Draw must not be called after disposal.");
		context.getDrawable().setAutoSwapBufferMode(false);
		context.setGLEventListener(glEventListener);
		context.getDrawable().display();
		context.getDrawable().setAutoSwapBufferMode(true);
		context.resetGLEventListener();
	}

	public GraphicsListener getGraphicsListener() {
		return this.graphicsListener;
	}

	public void setGraphicsListener(GraphicsListener listener) {
		this.graphicsListener = listener;
		initiated = false;
	}
	
	private class ImageGLEventListener extends AbstractGLEventListener {
		private ImageGraphicsContext context;
		
		public ImageGLEventListener(ImageGraphicsContext context) {
			this.context = context;
		}
		
		public void init(GLAutoDrawable drawable) {
			if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Image graphics context initializing");
			debug(drawable);
			LOGGER.info("Initializing image graphics context");
			if(context.graphicsListener != null) {
				Graphics userGraphics = graphics;
				if(isTrace()) {
					userGraphics = traceGraphics;
					traceGraphics.setDelegate(graphics);
					traceGraphics.setLevel(getTraceLevel());
				}
				context.graphicsListener.init(view, userGraphics);
			}			
			initiated = true;
		}
		public void display(GLAutoDrawable drawable) {
			debug(drawable);
			
			if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Image graphics context displaying");
			
			startDrawing();
			
			// initiate graphics every time as many image graphics contexts render on the same drawable
			graphics.init();
			
			if(!initiated) init(drawable);
			
			resourceManager.cleanup();

			if(context.graphicsListener != null) {
				Graphics userGraphics = graphics;
				if(isTrace()) {
					userGraphics = traceGraphics;
					traceGraphics.setDelegate(graphics);
					traceGraphics.setLevel(getTraceLevel());
				}
				context.graphicsListener.draw(view, userGraphics);
			}
			
			delegate.endFrame();
			if(image.texture == null) {
				try {
					resourceManager.cache(image);
				} catch (IOException e) {
					throw new RuntimeException("Error caching image. Do manual caching to prevent such errors during rendering.", e);
				}
			}

			image.texture.enable();
			image.texture.bind();	
			// copy buffer to image
			drawable.getGL().glCopyTexSubImage2D(image.texture.getTarget(), 0, 0, 0, 0, 0, image.getWidth(), image.getHeight());
			image.texture.disable();
			
			endDrawing();
		}
		public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
		public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}
    }

	public int getWidth() {
		return image.getWidth();
	}
	public int getHeight() {
		return image.getHeight();
	}
}
