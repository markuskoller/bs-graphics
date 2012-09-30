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
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.Line;
import ch.blackspirit.graphics.Triangle;

/**
 * Simple shape implementation based on arrays.
 * @author Markus Koller
 */
public class SimpleShape implements Shape {
	private Line[] lines;
	private Triangle[] triangles;
	private Image texture;
	
	public SimpleShape(Triangle[] triangles, Line[] lines) {
		this.lines = lines;
		this.triangles = triangles;
	}
	
	public Line[] getLines() {
		return lines;
	}

	public Triangle[] getTriangles() {
		return triangles;
	}

	public Image getTexture() {
		return texture;
	}

	public void setTexture(Image texture) {
		this.texture = texture;
	}
	
	public void drawLines(Graphics graphics, boolean useColors) {
		graphics.drawLines(lines, useColors);
	}

	public void fillArea(Graphics graphics, boolean useColors, boolean texturing) {
		if(texturing) {
			graphics.fillTriangles(triangles, useColors, texture);
		} else {
			graphics.fillTriangles(triangles, useColors);
		}
	}

}
