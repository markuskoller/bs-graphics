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
package ch.blackspirit.graphics.util;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;

/**
 * Utility class to create various standard shape outlines.
 * @author Markus Koller
 */
public class ShapeOutlineFactory {
	private ShapeOutlineFactory() {}
	
	/**
	 * @param width Width of the rectangle.
	 * @param height Height of the rectangle.
	 * @return A rectangles outline.
	 */
	public static List<Vector2f> createRectangle(float width, float height) {
		ArrayList<Vector2f> points = new ArrayList<Vector2f>();
		points.add(new Vector2f(-width/2, -height/2));
		points.add(new Vector2f(+width/2, -height/2));
		points.add(new Vector2f(+width/2, +height/2));
		points.add(new Vector2f(-width/2, +height/2));
		
		return points;
	}
	
	/**
	 * @param width Width of the rounded rectangle.
	 * @param height Height of the rounded rectangle.
	 * @param cornerRadius Radius of the circle of which a quarter makes up each corner.
	 * @return A rounded rectangles outline.
	 */
	public static List<Vector2f> createRoundedRectangle(float width, float height, float cornerRadius) {
		return createRoundedRectangle(width, height, cornerRadius, 9);
	}
	/**
	 * @param width Width of the rounded rectangle.
	 * @param height Height of the rounded rectangle.
	 * @param cornerRadius Radius of the circle of which a quarter makes up each corner.
	 * @param pointsPerCorner Points to use for each corners outline.
	 * @return A rounded rectangles outline.
	 */
	public static List<Vector2f> createRoundedRectangle(float width, float height, float cornerRadius, int pointsPerCorner) {
		boolean widthOne = false;
		if(cornerRadius > width / 2) {
			cornerRadius = width / 2;
			widthOne = true;
		}
		boolean heightOne = false;
		if(cornerRadius > height / 2) {
			cornerRadius = height / 2;
			heightOne = true;
			widthOne = false;
		}

		float angleInc = ((float)Math.PI / 2) / (pointsPerCorner + 1);
		
		ArrayList<Vector2f> shape = new ArrayList<Vector2f>();

		// top line
		if(widthOne) {
			shape.add(new Vector2f(0, -height));
		} else {
			shape.add(new Vector2f(-width/2 + cornerRadius, -height/2));
			shape.add(new Vector2f(+width/2 - cornerRadius, -height/2));
		}
		// top right corner
		for(int i = 1; i <= pointsPerCorner; i++) {
			float angle = angleInc * i - ((float)Math.PI / 2);
			shape.add(new Vector2f(
					cornerRadius * (float)Math.cos(angle) + width/2 - cornerRadius,
					cornerRadius * (float)Math.sin(angle) - height/2 + cornerRadius
					));
		}

		// right line
		if(heightOne) {
			shape.add(new Vector2f(width, 0));
		} else {
			shape.add(new Vector2f(width/2, -height/2 + cornerRadius));
			shape.add(new Vector2f(width/2, +height/2 - cornerRadius));
		}
		// bottom right corner
		for(int i = 1; i <= pointsPerCorner; i++) {
			float angle = angleInc * i;
			shape.add(new Vector2f(
					cornerRadius * (float)Math.cos(angle) + width/2 - cornerRadius,
					cornerRadius * (float)Math.sin(angle) + height/2 - cornerRadius
					));
		}

		// bottom line
		if(widthOne) {
			shape.add(new Vector2f(0, height));
		} else {
			shape.add(new Vector2f(+width/2 - cornerRadius, height/2));
			shape.add(new Vector2f(-width/2 + cornerRadius, height/2));
		}
		// bottom left corner
		for(int i = 1; i <= pointsPerCorner; i++) {
			float angle = angleInc * i + ((float)Math.PI / 2);
			shape.add(new Vector2f(
					cornerRadius * (float)Math.cos(angle) - width/2 + cornerRadius,
					cornerRadius * (float)Math.sin(angle) + height/2 - cornerRadius
					));
		}

		// left line
		if(heightOne) {
			shape.add(new Vector2f(-width, 0));
		} else {
			shape.add(new Vector2f(-width/2, +height/2 - cornerRadius));
			shape.add(new Vector2f(-width/2, -height/2 + cornerRadius));
		}
		// top left corner
		for(int i = 1; i <= pointsPerCorner; i++) {
			float angle = angleInc * i + ((float)Math.PI);
			shape.add(new Vector2f(
					cornerRadius * (float)Math.cos(angle) - width/2 + cornerRadius,
					cornerRadius * (float)Math.sin(angle) - height/2 + cornerRadius
					));
		}

		return shape;
	}
	
	/**
	 * @param radius The radius of the circle.
	 * @return A circle outline.
	 */
	public static List<Vector2f> createCircle(float radius) {
		return createCircle(radius, 40);
	}
	/**
	 * @param radius The radius of the circle.
	 * @param points The number of points to use for generating the outline.
	 * @return A circle outline.
	 */
	public static List<Vector2f> createCircle(float radius, int points) {
		return createEllipse(radius*2, radius*2, points);
	}
	/**
	 * @param radius The radius of the circle.
	 * @param startAngle The start angle in degrees. 0° is horizontally to the right.
	 * @param endAngle The end angle in degrees. 0° is horizontally to the right.
	 * @return A circular arc outline. Like a piece of a pie.
	 */
	public static List<Vector2f> createCircularArc(float radius, float startAngle, float endAngle) {
		return createCircularArc(radius, startAngle, endAngle, (int)Math.abs(40f / 360f * (startAngle-endAngle)) + 1);
	}
	/**
	 * @param radius The radius of the circle.
	 * @param startAngle The start angle in degrees. 0° is horizontally to the right.
	 * @param endAngle The end angle in degrees. 0° is horizontally to the right.
	 * @param points The number of points to use for the outline.
	 * @return A circular arc outline. Like a piece of a pie.
	 */
	public static List<Vector2f> createCircularArc(float radius, float startAngle, float endAngle, int points) {
		return createEllipsoidalArc(radius*2, radius*2, startAngle, endAngle, points);
	}

	/**
	 * @param width Width of the ellipse.
	 * @param height Height of the ellipse.
	 * @return An ellipse outline.
	 */
	public static List<Vector2f> createEllipse(float width, float height) {
		return createEllipse(width, height, 40);
	}
	/**
	 * @param width Width of the ellipse.
	 * @param height Height of the ellipse.
	 * @param points The number of points to use for the outline.
	 * @return An ellipse outline.
	 */
	public static List<Vector2f> createEllipse(float width, float height, int points) {
		float angleInc = (float)(2 * Math.PI / points);
		float angle = 0;
		ArrayList<Vector2f> shape = new ArrayList<Vector2f>(points);
		for(int i = 0; i < points; i++) {
			shape.add(new Vector2f((width/2) * (float)Math.cos(angle), (height/2) * (float)Math.sin(angle)));
			angle += angleInc;
		}
		return shape;
	}

	/**
	 * @param width Width of the ellipse.
	 * @param height Height of the ellipse.
	 * @param startAngle The start angle in degrees. 0° is horizontally to the right.
	 * @param endAngle The end angle in degrees. 0° is horizontally to the right.
	 * @return An ellipsoidal arc outline. Like a piece of pie.
	 */
	public static List<Vector2f> createEllipsoidalArc(float width, float height, float startAngle, float endAngle) {
		return createEllipsoidalArc(width, height, startAngle, endAngle, (int)Math.abs(40f / 360f * (startAngle-endAngle)) + 1);
	}
	/**
	 * @param width Width of the ellipse.
	 * @param height Height of the ellipse.
	 * @param startAngle The start angle in degrees. 0° is horizontally to the right.
	 * @param endAngle The end angle in degrees. 0° is horizontally to the right.
	 * @param points The number of points to use for the outline.
	 * @return An ellipsoidal arc outline. Like a piece of pie.
	 */
	public static List<Vector2f> createEllipsoidalArc(float width, float height, float startAngle, float endAngle, int points) {
		float angleInc = (float)((endAngle - startAngle) / (points - 2));
		float angle = startAngle;
		ArrayList<Vector2f> shape = new ArrayList<Vector2f>(points);

		// Add center to connect
		shape.add(new Vector2f(0,0));
		
		// Add ellipsoidal arc outline
		for(int i = 0; i < (points - 1); i++) {
			float rad = (float)Math.toRadians(angle);
			shape.add(new Vector2f((width/2) * (float)Math.cos(rad), (height/2) * (float)Math.sin(rad)));
			angle += angleInc;
		}
		return shape;
	}
}
