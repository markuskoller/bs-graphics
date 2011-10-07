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

/**
 * The canvas represents a region of the screen in which
 * drawing can take place.
 * @author Markus Koller
 */
public interface Canvas extends GraphicsContext {
	/**
	 * Enable, disable vertical sync. Enabling can prevent flicker, but
	 * possibly drops the framerate.<br/>
	 * Default is disabled (if available).<br/>
	 * As this feature can not always be implemented it is optional.
	 * @param enabled <code>true</code> to enable VSync.
	 * @return <code>true</code> if the call was successfull;
	 */
	public boolean setVSync(boolean enabled);
	public boolean getVSync();
	
	/**
	 * Provides access to the image factory used by the canvas.
	 * @return Image manager used by the canvas.
	 */
	public ImageFactory getImageFactory();
	/**
	 * Creates a new ImageGraphicsContext to draw on an image.<br/>
	 * @param image The image to draw on.
	 * @return A new ImageGraphicsContext to draw on an image.
	 */
	public ImageGraphicsContext createImageGraphicsContext(Image image);
	/**
	 * Provides access to the resource manager used by the canvas.
	 * @return Image manager used by the canvas.
	 */
	public ResourceManager getResourceManager();	
	
	/**
	 * Get a boolean properties value.<br/>
	 * Property names are implementation specific.
	 * @param property Name of the property to query
	 * @return Value of the property.
	 */
	public boolean getPropertyBoolean(String property);
	/**
	 * Get a long properties value.<br/>
	 * Property names are implementation specific.
	 * @param property Name of the property to query
	 * @return Value of the property.
	 */
	public long getPropertyLong(String property);
	/**
	 * Get a float properties value.<br/>
	 * Property names are implementation specific.
	 * @param property Name of the property to query
	 * @return Value of the property.
	 */
	public float getPropertyFloat(String property);
}