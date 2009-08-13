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

import java.awt.Font;
import java.io.IOException;
import java.util.Collection;

/**
 * The resource manager allows control over caching and releasing of resources (images, fonts).<br/>
 * This is useful to prevent resources from being cached during rendering.<br/>
 *  When a resource is used for drawing and is not yet cached, it will be cached automatically.
 * @author Markus Koller
 */
public interface ResourceManager {
	/**
	 * Remove the given image from the cache. All native resources get freed.
	 * @param image Image to be removed from the cache.
	 */
	public void freeImage(Image image);
	/**
	 * Remove all images from the cache and free all their native resources.
	 */
	public void freeImages();
	/**
	 * Return all cached images.
	 * @return The cached images.
	 */
	public Collection<Image> getCachedImages();
	/**
	 * Caches the image ready for rendering. At this point native resources get aquired.<br/>
	 * Calling this method results in a complete update of the cached image buffer.<br/>
	 * This method prevents time costly image caching operations from beeing performed 
	 * during the first rendering cycle. 
	 * @param image Image to cache.
	 * @throws IOException If the image could not be read.
	 * @return true if caching was successful
	 */
	public boolean cacheImage(Image image) throws IOException;
	
	/**
	 * Returns all cached fonts.
	 * @return All cached fonts.
	 */
	public Collection<Font> getCachedFonts();
	/**
	 * Removes all resources allocated to render text with the given font.
	 * @param font Font to free resources for.
	 */
	public void freeFont(Font font);
	/**
	 * Frees allocated resources for all cached fonts.
	 */
	public void freeFonts();
	/**
	 * Allocates all resources needed to render text with the given font.<br/>
	 * A derived font with for example different size needs different resources!
	 * @param font Font to be cached.
	 * @return true if caching was successful
	 */
	public boolean cacheFont(Font font);

}
