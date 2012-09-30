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
package ch.blackspirit.graphics.jogl2;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import ch.blackspirit.graphics.AWTCanvas;
import ch.blackspirit.graphics.DisplayMode;
import ch.blackspirit.graphics.RealtimeCanvas;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.ScreenMode;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * The canvas factory for the Blackspirit Graphics JOGL implementation.
 * @author Markus Koller
 */
public final class NewtCanvasFactory implements ch.blackspirit.graphics.CanvasFactory {
	private CanvasProperties properties;
	private Screen screen;
	
	private static AtomicBoolean textureIOSetup = new AtomicBoolean(false);
	
	public NewtCanvasFactory() {
		this.properties = new CanvasProperties();
		URL url = this.getClass().getResource("/bsgraphics.properties");
		if(url != null) {
			properties.load(url);
		}
		if (!textureIOSetup.getAndSet(true)) {
			TextureIO.addTextureProvider(new ImageIOTextureProvider());
		}
	}

	private Screen getScreen() {
		if (screen == null) {
			Display dpy = NewtFactory.createDisplay(null);
			int screenIdx = 0;
			// TODO Move screen creation to canvasfactory
			screen = NewtFactory.createScreen(dpy, screenIdx);
			screen.createNative();
		} 
		return screen;
	}
	
	public CanvasProperties getProperties() {
		return properties;
	}
	/**
	 * @param properties Properties to be used on creation of a canvas.
	 */
	public void setProperties(CanvasProperties properties) {
		this.properties = properties;
	}

	public AWTCanvas createAWTCanvas(boolean lightweight) {
		return new ch.blackspirit.graphics.jogl2.AWTCanvas(lightweight, properties);
	}
	
	public RealtimeCanvas createRealtimeCanvasFullscreen(DisplayMode displayMode) {
		return new ch.blackspirit.graphics.jogl2.NewtCanvas(getScreen(), displayMode, properties);
	}
	public RealtimeCanvas createRealtimeCanvasFullscreen() {
		return new ch.blackspirit.graphics.jogl2.NewtCanvas(getScreen(), null, properties);
	}
	public RealtimeCanvas createRealtimeCanvasWindow(int width, int height) {
		return new ch.blackspirit.graphics.jogl2.NewtCanvas(getScreen(), width, height, properties);
	}

	public DisplayMode getDisplayMode(int width, int height) {
		DisplayMode current = getDisplayMode();
		for(DisplayMode mode: getDisplayModes()) {
			if(mode.getWidth() == width &&
				mode.getHeight() == height &&
				mode.getColorDepth() == current.getColorDepth() &&
				mode.getRefreshRate() == current.getRefreshRate()) {
				return mode;
			}
		}
		return null;
	}
	public DisplayMode getDisplayMode(int width, int height, int colorDepth) {
		DisplayMode current = getDisplayMode();
		for(DisplayMode mode: getDisplayModes()) {
			if(mode.getWidth() == width &&
				mode.getHeight() == height &&
				mode.getColorDepth() == colorDepth &&
				mode.getRefreshRate() == current.getRefreshRate()) {
				return mode;
			}
		}
		return null;
	}
	public Set<DisplayMode> getDisplayModes() {
		ScreenMode currentMode = getScreen().getCurrentScreenMode();

		Screen screen = getScreen();
		Set<DisplayMode> modes = new HashSet<DisplayMode>();
		
		for(ScreenMode mode :screen.getScreenModes()) {
			if (mode.getRotation() == currentMode.getRotation()) {
				modes.add(new ch.blackspirit.graphics.jogl2.DisplayMode(mode.getRotatedWidth(), mode.getRotatedHeight(), 
						mode.getMonitorMode().getSurfaceSize().getBitsPerPixel(), mode.getMonitorMode().getRefreshRate()));
			}
		}
		return modes;
	}
	public DisplayMode getDisplayMode() {
		ScreenMode currentMode = getScreen().getCurrentScreenMode();
		
		for(DisplayMode mode: getDisplayModes()) {
			if(mode.getWidth() == currentMode.getRotatedWidth() &&
				mode.getHeight() == currentMode.getRotatedHeight() &&
				mode.getColorDepth() == currentMode.getMonitorMode().getSurfaceSize().getBitsPerPixel() &&
				mode.getRefreshRate() == currentMode.getMonitorMode().getRefreshRate()) {
				return mode;
			}
		}
		return null;
	}
}
