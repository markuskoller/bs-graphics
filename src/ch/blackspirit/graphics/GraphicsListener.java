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
 * To do the actual drawing a graphics listener must be implemented
 * which gets triggered when a graphics context gets redrawn.<br/>
 * It also notices when a graphics context gets initialised or the size changes.
 * @author Markus Koller
 */
public interface GraphicsListener {
	/**
	 * Called on the first rendering cycle after the GraphicsListener 
	 * has been set on a graphics context.<br/>
	 * Also called when the graphics context has lost its settings.
	 */
	public void init(View view, Graphics graphics);	
	/**
	 * Do all the drawing here.<br/>
	 * All settings (including transformations) are kept from the last rendering cycle.<br/>
	 * The buffer is in an unspecified state (content could be anything), 
	 * so it should be cleared or completely overriden<br/>
	 * Changes to the view must be done before any drawing.
	 * There is no specified behaviour for view changes during rendering.
	 */
	public void draw(View view, Graphics graphics);
	
	/**
	 * Notifies that the size of the graphics context changed.
	 */
	public void sizeChanged(GraphicsContext graphicsContext, View view);
}
