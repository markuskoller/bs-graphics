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

import java.net.URL;

/**
 * For performance reasons Blackspirit Graphics implementation may need different image implementations.<br/>
 * Therefore an image must only be drawn using the canvas it has been created with.
 * @author Markus Koller
 */
public interface Image {
	/**
	 * Returns the URL this image has been created from or <code>null</code> if
	 * it has not been created from a URL.
	 * @return Returns the URL this image has been created from or <code>null</code>.
	 */
	public URL getURL();
	/**
	 * Get the images height.
	 * @return The images height.
	 */
	public int getHeight();
	/**
	 * Get the images width.
	 * @return The images width.
	 */
	public int getWidth();

	/**
	 * Does this image have a buffer to manipulate it.
	 * @return <code>true</code> if this image has a buffer.
	 */
	public boolean isBuffered();
	
	/**
	 * {@link BufferType BufferType} of this image.
	 * @return {@link BufferType BufferType} of this image, or <code>null</code> if it is not buffered.
	 */
	public BufferType getBufferType();
	
	/**
	 * The buffer of this image which could for example be a byte array.
	 * Changes in the images data are not automaticely visible when drawing.
	 * To tell the system about changes to the buffer invoke {@link #updateCache() updateCache}.<br/>
	 * @return The buffer of this image or <code>null</code> if it is not buffered.
	 */
	public Object getBuffer();
	/**
	 * Tells the system that the complete buffer has changed and the cache needs to be
	 * updated. If possible this is done immediately.<br/>
	 * For this operation the image must be cached, if it isn't already it will
	 * be cached first.<br/>
	 * Only available if isBuffered() is <code>true</code>.
	 */
	public void updateCache();
	/**
	 * Tells the system that the passed region of the buffer has changed and the cache needs to be
	 * updated. If possible this is done immediately.<br/>
	 * For this operation the image must be cached, if it isn't already it will
	 * be cached first, which results in a complete buffer update!<br/>
	 * Only available if isBuffered() is <code>true</code>.
	 */
	public void updateCache(int xOffset, int yOffset, int width, int height);

	public void updateBuffer();
	public void updateBuffer(int xOffset, int yOffset, int width, int height);
	
	// FEATURE release buffer
//	public void releaseBuffer();
	// FEATURE create buffer
//	public void createBuffer();
}