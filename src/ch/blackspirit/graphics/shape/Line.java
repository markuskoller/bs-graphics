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

import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

/**
 * Simple line implementation.
 * Colors are set to <code>null</code> by default.
 * @author Markus Koller
 */
public final class Line implements ch.blackspirit.graphics.Line {
	private Vector2f[] points = new Vector2f[2];
	private Color4f[] colors = new Color4f[2];
	
	public Line() {
		points[0] = new Vector2f();
		points[1] = new Vector2f();
	}
	/**
	 * @param p1 Point 1 of the line.
	 * @param p2 Point 2 of the line.
	 */
	public Line(Vector2f p1, Vector2f p2) {
		if(p1 == null || p2 == null) throw new IllegalArgumentException("Point must not be null");
		points[0] = p1;
		points[1] = p2;
	}
	
	public Vector2f getPoint(int index) {
		if(index < 0 || index > 1) throw new IllegalArgumentException("Index out of range (0 to 1)");
		return points[index];
	}
	public void setPoint(int index, Vector2f point) {
		if(index < 0 || index > 1) throw new IllegalArgumentException("Index out of range (0 to 1)");
		if(point == null) throw new IllegalArgumentException("Point must not be null");
		points[index] = point;
	}
	public Color4f getColor(int index) {
		if(index < 0 || index > 1) throw new IllegalArgumentException("Index out of range (0 to 1)");
		return colors[index];
	}
	public void setColor(int index, Color4f color) {
		if(index < 0 || index > 1) throw new IllegalArgumentException("Index out of range (0 to 1)");
		if(color == null) throw new IllegalArgumentException("Color must not be null");
		colors[index] = color;
	}

}
