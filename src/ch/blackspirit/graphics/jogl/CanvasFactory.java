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

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.sun.opengl.util.texture.TextureIO;

import ch.blackspirit.graphics.AWTCanvas;
import ch.blackspirit.graphics.DisplayMode;
import ch.blackspirit.graphics.RealtimeCanvas;

/**
 * The canvas factory for the Blackspirit Graphics JOGL implementation.
 * @author Markus Koller
 */
public final class CanvasFactory implements ch.blackspirit.graphics.CanvasFactory {
	private CanvasProperties properties;
	
	private static boolean textureIOSetup = false;
	
	public CanvasFactory() {
		this.properties = new CanvasProperties();
		URL url = this.getClass().getResource("/bsgraphics.properties");
		if(url != null) {
			properties.load(url);
		}
		if (textureIOSetup == false) {
			TextureIO.addTextureProvider(new ImageIOTextureProvider());
			textureIOSetup = true;
		}
	}

	public CanvasProperties getProperties() {
		return properties;
	}
	public void setProperties(CanvasProperties properties) {
		this.properties = properties;
	}

	public AWTCanvas createAWTCanvas(boolean lightweight) {
		return new ch.blackspirit.graphics.jogl.AWTCanvas(lightweight, properties);
	}
	public RealtimeCanvas createRealtimeCanvasFullscreen(DisplayMode displayMode) {
		return new ch.blackspirit.graphics.jogl.RealtimeCanvas(displayMode, properties);
	}
	public RealtimeCanvas createRealtimeCanvasFullscreen() {
		return new ch.blackspirit.graphics.jogl.RealtimeCanvas(null, properties);
	}
	public RealtimeCanvas createRealtimeCanvasWindow(int width, int height) {
		return new ch.blackspirit.graphics.jogl.RealtimeCanvas(width, height, properties);
	}

	public DisplayMode getDisplayMode(int width, int height) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice dev = env.getDefaultScreenDevice();
		java.awt.DisplayMode currentMode = dev.getDisplayMode();
		
		for(DisplayMode mode: getDisplayModes()) {
			if(mode.getWidth() == width &&
				mode.getHeight() == height &&
				mode.getColorDepth() == currentMode.getBitDepth() &&
				mode.getRefreshRate() == currentMode.getRefreshRate()) {
				return mode;
			}
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see ch.blackspirit.graphics.CanvasFactory#getDisplayMode(int, int, int)
	 */
	public DisplayMode getDisplayMode(int width, int height, int colorDepth) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice dev = env.getDefaultScreenDevice();
		java.awt.DisplayMode currentMode = dev.getDisplayMode();
		
		for(DisplayMode mode: getDisplayModes()) {
			if(mode.getWidth() == width &&
				mode.getHeight() == height &&
				mode.getColorDepth() == colorDepth &&
				mode.getRefreshRate() == currentMode.getRefreshRate()) {
				return mode;
			}
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see ch.blackspirit.graphics.CanvasFactory#getDisplayModes()
	 */
	public Set<DisplayMode> getDisplayModes() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice dev = env.getDefaultScreenDevice();

		Set<DisplayMode> modes = new HashSet<DisplayMode>();
		
		for(java.awt.DisplayMode mode :dev.getDisplayModes()) {
			modes.add(new ch.blackspirit.graphics.jogl.DisplayMode(mode.getWidth(), mode.getHeight(), mode.getBitDepth(), mode.getRefreshRate()));
		}
		return modes;
	}
	/* (non-Javadoc)
	 * @see ch.blackspirit.graphics.CanvasFactory#getDisplayMode()
	 */
	public DisplayMode getDisplayMode() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice dev = env.getDefaultScreenDevice();
		java.awt.DisplayMode currentMode = dev.getDisplayMode();
		
		for(DisplayMode mode: getDisplayModes()) {
			if(mode.getWidth() == currentMode.getWidth() &&
				mode.getHeight() == currentMode.getHeight() &&
				mode.getColorDepth() == currentMode.getBitDepth() &&
				mode.getRefreshRate() == currentMode.getRefreshRate()) {
				return mode;
			}
		}
		return null;
	}
}
