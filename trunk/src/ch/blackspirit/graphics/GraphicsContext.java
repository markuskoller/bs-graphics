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
package ch.blackspirit.graphics;

/**
 * The graphics context is an abstract representation of an area to draw on. 
 * This could be the screen (canvas) or an image (image graphics context).<br/>
 * The drawing settings (color, transformations, etc.) are being held separately for each graphics context.
 * @author Markus Koller
 */
public interface GraphicsContext {
	/**
	 * Sets the GraphicsListener of this canvas which does all the drawing.
	 * @param listener The listener to be used for drawing.
	 */
	public void setGraphicsListener(GraphicsListener listener);
	public GraphicsListener getGraphicsListener();

	/**
	 * Updates the content of this canvas and therefore calls the drawing method on  
	 * the GraphicsListener. This method is guaranteed to be blocking and will return 
	 * after the GraphicsListeners display method has been called.
	 */
	public void draw();

	/**
	 * Releases all native resources and closes any automatically opened windows.
	 */
	public void dispose();
	
	/**
	 * Returns the width of the region that can be rendered on.
	 * @return Width of the region that can be rendered on.
	 */
	public int getWidth();
	/**
	 * Returns the height of the region that can be rendered on.
	 * @return Height of the region that can be rendered on.
	 */
	public int getHeight();

}
