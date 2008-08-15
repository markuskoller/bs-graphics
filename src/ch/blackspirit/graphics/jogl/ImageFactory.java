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
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.blackspirit.graphics.BufferType;
import ch.blackspirit.graphics.Image;

/*
 * @author Markus Koller
 */
class ImageFactory implements ch.blackspirit.graphics.ImageFactory {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	private int maxSize = Integer.MAX_VALUE;
	
	private ResourceManager resourceManager;
	
	public ImageFactory(ResourceManager resourceManager) {
		super();
		this.resourceManager = resourceManager;
	}
	
	void setMaxTextureSize(int size) {
		maxSize = size;
		LOGGER.info("Maximum Texture Size: " + maxSize);
	}
	int getMaxTextureSize() {
		return maxSize;
	}

	public Image createImage(URL url, boolean forceInternalAlpha) throws IOException {
		if(url == null) throw new IllegalArgumentException("Url must not be null");
		return new ch.blackspirit.graphics.jogl.Image(url, resourceManager, false, forceInternalAlpha);
	}
	public Image createImage(int width, int height, boolean internalAlpha)	throws IOException {
		return new ch.blackspirit.graphics.jogl.Image(width, height, resourceManager, internalAlpha);
	}
	
	public Image createBufferedImage(URL url, boolean forceAlpha) throws IOException {
		if(url == null) throw new IllegalArgumentException("Url must not be null");
		if(forceAlpha) {
			return createBufferedImage(url, BufferTypes.RGBA_4Byte);
		} else {
			return new ch.blackspirit.graphics.jogl.Image(url, resourceManager, true, false);
		}
	}
	public Image createBufferedImage(URL url, BufferType bufferType) throws IOException {
		if(url == null) throw new IllegalArgumentException("Url must not be null");
		if(bufferType == null) throw new IllegalArgumentException("BufferType must not be null");
		return new ch.blackspirit.graphics.jogl.Image(url, resourceManager, bufferType);
	}
	
	public Image createBufferedImage(int width, int height, BufferType bufferType) {
		if(bufferType == null) throw new IllegalArgumentException("BufferType must not be null");
		if(width > maxSize || height > maxSize) {
			throw new IllegalArgumentException("Requested image ("+ width + "x" + height + ") exceeds maximum image size: " +maxSize + "x" + maxSize + ".");
		}
//	    if(LOGGER.isLoggable(Level.FINE)) LOGGER.fine("Created Empty Image: DemandedImage=" + width + "x" + height); 

	    return new ch.blackspirit.graphics.jogl.Image(width, height, resourceManager, bufferType);
	}

	public Image createBufferedImage(int width, int height, boolean alpha) {
		if(alpha) {
			return createBufferedImage(width, height, BufferTypes.RGBA_4Byte);
		} else {
			return createBufferedImage(width, height, BufferTypes.RGB_3Byte);
		}
	}	
}
