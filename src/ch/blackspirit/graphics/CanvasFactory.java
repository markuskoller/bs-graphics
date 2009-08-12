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
package ch.blackspirit.graphics;

import java.util.Set;

/**
 * Provides methods for creation of a canvas and to get the available
 * display modes.
 * @author Markus Koller
 */
public interface CanvasFactory {
	/**
	 * Creates a new <code>RealtimeCanvas</code> fullscreen using the current display mode.
	 * @return A new <code>RealtimeCanvas</code>.
	 */
	public RealtimeCanvas createRealtimeCanvasFullscreen();
	/**
	 * Creates a new <code>RealtimeCanvas</code> fullscreen with the given display mode.
	 * @param displayMode The display mode to set.
	 * @return A new <code>RealtimeCanvas</code>.
	 */
	public RealtimeCanvas createRealtimeCanvasFullscreen(DisplayMode displayMode);
	/**
	 * Creates a new <code>RealtimeCanvas</code> as window with the given size.
	 * @param width The width of the canvas inside the window.
	 * @param height The height of the canvas inside the window.
	 * @return A new <code>RealtimeCanvas</code>.
	 */
	public RealtimeCanvas createRealtimeCanvasWindow(int width, int height);
	/**
	 * Creates a new <code>AWTCanvas</code>.
	 * Setting lightweight to <code>true</true> makes sure the canvas' Component can be properly
	 * integrated in a Swing application. 
	 * @param lightweight Must the canvas be a lightweight Swing component or not.
	 * @return A new <code>AWTCanvas</code>.
	 */
	public AWTCanvas createAWTCanvas(boolean lightweight);
	
	/**
	 * Returns all available fullscreen display modes.
	 * @return A set of available display modes.
	 */
	public Set<DisplayMode> getDisplayModes();
	/**
	 * If available returns the display mode with the currently used color depth and refresh rate which is matching the
	 * given resolution.
	 * @return The display mode matching the resolution, otherwise <code>null</code>.
	 */
	public DisplayMode getDisplayMode(int width, int height);
	/**
	 * Returns the requested fullscreen display mode with the currently used refresh rate if available.
	 * @return The display mode matching the criteria, otherwise <code>null</code>.
	 */
	public DisplayMode getDisplayMode(int width, int height, int colorDepth);
	/**
	 * Returns the current display mode.
	 * @return The current display mode.
	 */
	public DisplayMode getDisplayMode();
}
