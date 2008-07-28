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
package ch.blackspirit.graphics.shape;

import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.Triangle;

/**
 * Part of the shape interface used to represent an area. 
 * @author Markus Koller
 */
public interface Area {
	/**
	 * @return The triangles covering the area.
	 */
	public abstract Triangle[] getTriangles();
	/**
	 * @return The texture set for texturing the area.
	 */
	public abstract Image getTexture();
	/**
	 * @param texture The texture to use for texturing the area.
	 */
	public abstract void setTexture(Image texture);
	/**
	 * Fill the area optionally using the colors specified and texturing
	 * @param graphics The graphics object to use for drawing.
	 * @param useColors Should the colors set on the triangles be used.
	 * @param texturing Should texturing information be used.
	 */
	public void fillArea(Graphics graphics, boolean useColors, boolean texturing);
}