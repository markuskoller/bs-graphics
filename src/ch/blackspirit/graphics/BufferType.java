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

import javax.vecmath.Color4f;


/**
 * The <code>BufferType</code> describes the way image data is stored
 * in an images buffer.<br/>
 * Provides a standardized way to a access an images buffer. 
 * This is clearly not the fastest way to access the buffer, but the easiest and most reusable.
 * @author Markus Koller
 */
public interface BufferType {
	/**
	 * @param image The image to read a pixel from.
	 * @param x The pixels x coordinate.
	 * @param y The pixels y coordinate.
	 * @return The pixels red component as a float from 0 to 1.
	 */
	public abstract float getRed(Image image, int x, int y);
	/**
	 * @param image The image to read a pixel from.
	 * @param x The pixels x coordinate.
	 * @param y The pixels y coordinate.
	 * @return The pixels green component as a float from 0 to 1.
	 */
	public abstract float getGreen(Image image, int x, int y);
	/**
	 * @param image The image to read a pixel from.
	 * @param x The pixels x coordinate.
	 * @param y The pixels y coordinate.
	 * @return The pixels blue component as a float from 0 to 1.
	 */
	public abstract float getBlue(Image image, int x, int y);
	/**
	 * @param image The image to read a pixel from.
	 * @param x The pixels x coordinate.
	 * @param y The pixels y coordinate.
	 * @return The pixels alpha component as a float from 0 to 1.
	 */
	public abstract float getAlpha(Image image, int x, int y);
	/**
	 * @param image The image to set a pixel on.
	 * @param x The pixels x coordinate.
	 * @param y The pixels y coordinate.
	 * @param value The pixels new red component as a float from 0 to 1.
	 */
	public abstract void setRed(Image image, int x, int y, float value);
	/**
	 * @param image The image to set a pixel on.
	 * @param x The pixels x coordinate.
	 * @param y The pixels y coordinate.
	 * @param value The pixels new blue component as a float from 0 to 1.
	 */
	public abstract void setBlue(Image image, int x, int y, float value);
	/**
	 * @param image The image to set a pixel on.
	 * @param x The pixels x coordinate.
	 * @param y The pixels y coordinate.
	 * @param value The pixels new green component as a float from 0 to 1.
	 */
	public abstract void setGreen(Image image, int x, int y, float value);
	/**
	 * @param image The image to set a pixel on.
	 * @param x The pixels x coordinate.
	 * @param y The pixels y coordinate.
	 * @param value The pixels new alpha component as a float from 0 to 1.
	 */
	public abstract void setAlpha(Image image, int x, int y, float value);
	
	public void setColor(Image image, int x, int y, Color4f color);
	public void getColor(Image image, int x, int y, Color4f color);
}
