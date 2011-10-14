/*
 * Copyright 2008-2009 Markus Koller
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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.debug.TraceGraphics;

/**
 * @author Markus Koller
 */
final class CanvasGLEventListener extends AbstractGLEventListener {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	private CanvasGraphics graphics;
	private AbstractGraphicsContext canvas;
	private View view;
	private ch.blackspirit.graphics.jogl.ResourceManager resourceManager;
	private ImageFactory imageFactory;
	private boolean initiated = false;

	private boolean vsync = true;
	private boolean vsyncChanged = false;
	private boolean firstInitialization = true;
	
	private TraceGraphics traceGraphics = new TraceGraphics();
	
	private Error error;
	private RuntimeException runtimeException;

	public CanvasGLEventListener(AbstractGraphicsContext canvas, ch.blackspirit.graphics.jogl.ResourceManager resourceManager, ImageFactory imageFactory, 
			View view, CanvasGraphics graphics) {
		this.canvas = canvas;
		this.graphics = graphics;
		this.view = view;
		this.resourceManager = resourceManager;
		this.imageFactory = imageFactory;
	}
	
	public Error getError() {
		return error;
	}
	public RuntimeException getRuntimeException() {
		return runtimeException;
	}
	
	public void listenerInit(GLAutoDrawable drawable) {
		try {
			LOGGER.info("Initializing graphics listener");
	
			graphics.init();
			
			if(canvas.getGraphicsListener() != null) {
				Graphics userGraphics = graphics;
				if(isTrace()) {
					userGraphics = traceGraphics;
					traceGraphics.setDelegate(graphics);
					traceGraphics.setLevel(getTraceLevel());
				}
				canvas.getGraphicsListener().init(view, userGraphics);
			}
			
			initiated = true;
		} catch (RuntimeException e) {
			runtimeException = e;
			LOGGER.log(Level.SEVERE, "Error during initialisation", e);
		} catch (Error e) {
			error = e;
			LOGGER.log(Level.SEVERE, "Error during initialisation", e);
		}
	}
	public void init(GLAutoDrawable drawable) {
		try {
			debug(drawable);
			LOGGER.info("Initializing renderer");
	
			int[] size = new int[1];
			drawable.getGL().glGetIntegerv(GL.GL_MAX_TEXTURE_SIZE, size, 0);
			imageFactory.setMaxTextureSize(size[0]);
	
			applyVSync(drawable);
			if(!firstInitialization)resourceManager.refreshCache();
			
			listenerInit(drawable);
	
			firstInitialization = false;
		} catch (RuntimeException e) {
			runtimeException = e;
			LOGGER.log(Level.SEVERE, "Error during initialisation", e);
		} catch (Error e) {
			error = e;
			LOGGER.log(Level.SEVERE, "Error during initialisation", e);
		}
	}
	public void display(GLAutoDrawable drawable) {
		try {
			debug(drawable);
			canvas.startDrawing();
			
			
			if(!initiated) {
				listenerInit(drawable);
				if (error != null) return;
				else if (runtimeException != null) return;
			} else {
				// refresh settings for the case the context is used for image drawing
				graphics.init();
			}
			if(vsyncChanged) applyVSync(drawable);
			
			resourceManager.cleanup();
	
			if(canvas.getGraphicsListener() != null) {
				Graphics userGraphics = graphics;
				if(isTrace()) {
					userGraphics = traceGraphics;
					traceGraphics.setDelegate(graphics);
					traceGraphics.setLevel(getTraceLevel());
				}
				canvas.getGraphicsListener().draw(view, userGraphics);
			}
		} catch (RuntimeException e) {
			runtimeException = e;
			LOGGER.log(Level.SEVERE, "Error drawing", e);
		} catch (Error e) {
			error = e;
			LOGGER.log(Level.SEVERE, "Error drawing", e);
		} finally {
			graphics.endFrame();
			canvas.endDrawing();
		}
	}
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		try {
			debug(drawable);
			if(canvas.getGraphicsListener() != null) {
				canvas.getGraphicsListener().sizeChanged(canvas, view);
			}
		} catch (RuntimeException e) {
			runtimeException = e;
			LOGGER.log(Level.SEVERE, "Error during size change", e);
		} catch (Error e) {
			error = e;
			LOGGER.log(Level.SEVERE, "Error during size change", e);
		}

	}
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}

	public boolean isInitiated() {
		return initiated;
	}
	public void setInitiated(boolean initiated) {
		this.initiated = initiated;
	}
	public boolean getVSync() {
		return vsync;
	}

	public boolean setVSync(boolean enabled) {
		if(enabled != vsync) vsyncChanged = true;
		vsync = enabled;
		return true;
	}
	public void applyVSync(GLAutoDrawable drawable) {
		if(vsync) {
			drawable.getGL().setSwapInterval(1);
		} else {
			drawable.getGL().setSwapInterval(0);
		}
		vsyncChanged = false;
	}
}