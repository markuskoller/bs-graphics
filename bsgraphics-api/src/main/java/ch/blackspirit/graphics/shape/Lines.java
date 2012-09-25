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
package ch.blackspirit.graphics.shape;

import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.Line;

/**
 * Part of the shape interface used to represent the shapes outline. 
 * @author Markus Koller
 */
public interface Lines {
	/**
	 * @return Lines representing the shapes outline
	 */
	public abstract Line[] getLines();
	/**
	 * Draw the lines optionally using the specified colors.
	 * @param graphics The graphics object to use for drawing.
	 * @param useColors Should the colors set on the triangles be used. 
	 */
	public void drawLines(Graphics graphics, boolean useColors);
}