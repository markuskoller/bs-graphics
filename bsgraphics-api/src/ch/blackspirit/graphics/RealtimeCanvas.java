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

import java.util.List;

/**
 * A <code>Canvas</code> for real-time applications.
 * Rendering is triggered by calling the <code>draw</code> method.
 * @author Markus Koller
 */
public interface RealtimeCanvas extends Canvas {
	public void addWindowListener(WindowListener windowListener);
	public void removeWindowListener(WindowListener windowListener);
	public List<WindowListener> getWindowListeners();
	
	/**
	 * Change to window mode with the given canvas size in pixels.
	 * @param width Width of the rendering canvas.
	 * @param height Height of the rendering canvas.
	 */
	public void setWindow(int width, int height);
	/**
	 * Set the title to be shown in window mode. 
	 * @param title Title to show in the title bar.
	 */
	public void setWindowTitle(String title);
	public String getWindowTitle();
	/**
	 * Change to fullscreen mode using the given display mode.
	 * @param displayMode The display mode to switch to.
	 */
	public void setFullscreen(DisplayMode displayMode);
	/**
	 * Change to fullscreen using the current display mode.
	 */
	public void setFullscreen();
	public boolean isFullscreen();
	
//	public void setMouseVisible(boolean visible);
//	public boolean getMouseVisible(boolean visible);

	public int getScreenLocationX();
	public int getScreenLocationY();
	
//	public int getMouseX();
//	public int getMouseY();
}
