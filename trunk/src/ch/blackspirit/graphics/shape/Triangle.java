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

import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

/**
 * Simple triangle implementation.
 * Texture coordinates and colors are set to <code>null</code> by default.
 * @author Markus Koller
 */
public final class Triangle implements ch.blackspirit.graphics.Triangle {
	private Vector2f[] points = new Vector2f[3];
	private Vector2f[] textureCoordinates = new Vector2f[3];
	private Color4f[] colors = new Color4f[3];
	
	public Triangle() {
		points[0] = new Vector2f();
		points[1] = new Vector2f();
		points[2] = new Vector2f();
	}
	public Triangle(Vector2f p1, Vector2f p2, Vector2f p3) {
		points[0] = p1;
		points[1] = p2;
		points[2] = p3;
	}
	
	public Vector2f getPoint(int index) {
		if(index < 0 || index > 2) throw new IllegalArgumentException("Index out of range (0 to 2)");
		return points[index];
	}
	public void setPoint(int index, Vector2f point) {
		points[index] = point;
	}

	public Vector2f getTextureCoordinate(int index) {
		if(index < 0 || index > 2) throw new IllegalArgumentException("Index out of range (0 to 2)");
		return textureCoordinates[index];
	}
	public void setTextureCoordinate(int index, Vector2f textureCoordinate) {
		textureCoordinates[index] = textureCoordinate;
	}

	public Color4f getColor(int index) {
		if(index < 0 || index > 2) throw new IllegalArgumentException("Index out of range (0 to 2)");
		return colors[index];
	}
	public void setColor(int index, Color4f color) {
		colors[index] = color;
	}

}
