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

import ch.blackspirit.graphics.shape.Shape;

/**
 * Utility class to create various standard shapes.
 * @author Markus Koller
 */
public class ShapeFactory {
	
	private ShapeFactory() {}
	
	/**
	 * @param width Width of the rectangle.
	 * @param height Height of the rectangle.
	 * @return A rectangle.
	 */
	public static Shape createRectangle(float width, float height) {
		return ShapeCreator.create(ShapeOutlineFactory.createRectangle(width, height));
	}
	
	/**
	 * @param width Width of the rounded rectangle.
	 * @param height Height of the rounded rectangle.
	 * @param cornerRadius Radius of the circle of which a quarter makes up each corner.
	 * @return A rounded rectangle.
	 */
	public static Shape createRoundedRectangle(float width, float height, float cornerRadius) {
		return createRoundedRectangle(width, height, cornerRadius, 9);
	}
	/**
	 * @param width Width of the rounded rectangle.
	 * @param height Height of the rounded rectangle.
	 * @param cornerRadius Radius of the circle of which a quarter makes up each corner.
	 * @param pointsPerCorner Points to use for each corners outline.
	 * @return A rounded rectangle.
	 */
	public static Shape createRoundedRectangle(float width, float height, float cornerRadius, int pointsPerCorner) {
		return ShapeCreator.create(ShapeOutlineFactory.createRoundedRectangle(width, height, cornerRadius, pointsPerCorner));
	}
	
	/**
	 * @param radius The radius of the circle.
	 * @return A circle.
	 */
	public static Shape createCircle(float radius) {
		return createCircle(radius, 45);
	}
	/**
	 * @param radius The radius of the circle.
	 * @param points The number of points to use for generating the outline.
	 * @return A circle.
	 */
	public static Shape createCircle(float radius, int points) {
		return createEllipse(radius*2, radius*2, points);
	}

	/**
	 * @param radius The radius of the circle.
	 * @param startAngle The start angle in degrees. 0° is horizontally to the right.
	 * @param endAngle The end angle in degrees. 0° is horizontally to the right.
	 * @return A circular arc. Like a piece of a pie.
	 */
	public static Shape createCircularArc(float radius, float startAngle, float endAngle) {
		return createCircularArc(radius, startAngle, endAngle, (int)Math.abs(45f / 360f * (startAngle-endAngle)));
	}
	/**
	 * @param radius The radius of the circle.
	 * @param startAngle The start angle in degrees. 0° is horizontally to the right.
	 * @param endAngle The end angle in degrees. 0° is horizontally to the right.
	 * @param points The number of points to use for the outline.
	 * @return A circular arc. Like a piece of a pie.
	 */
	public static Shape createCircularArc(float radius, float startAngle, float endAngle, int points) {
		return createEllipsoidalArc(radius*2, radius*2, startAngle, endAngle, points);
	}

	/**
	 * @param width Width of the ellipse.
	 * @param height Height of the ellipse.
	 * @return An ellipse.
	 */
	public static Shape createEllipse(float width, float height) {
		return createEllipse(width, height, 45);
	}
	/**
	 * @param width Width of the ellipse.
	 * @param height Height of the ellipse.
	 * @param points The number of points to use for the outline.
	 * @return An ellipse.
	 */
	public static Shape createEllipse(float width, float height, int points) {
		return ShapeCreator.create(ShapeOutlineFactory.createEllipse(width, height, points));
	}

	/**
	 * @param width Width of the ellipse.
	 * @param height Height of the ellipse.
	 * @param startAngle The start angle in degrees. 0° is horizontally to the right.
	 * @param endAngle The end angle in degrees. 0° is horizontally to the right.
	 * @return An ellipsoidal arc. Like a piece of pie.
	 */
	public static Shape createEllipsoidalArc(float width, float height, float startAngle, float endAngle) {
		return createEllipsoidalArc(width, height, startAngle, endAngle, (int)Math.abs(45f / 360f * (startAngle-endAngle)));
	}
	/**
	 * @param width Width of the ellipse.
	 * @param height Height of the ellipse.
	 * @param startAngle The start angle in degrees. 0° is horizontally to the right.
	 * @param endAngle The end angle in degrees. 0° is horizontally to the right.
	 * @param points The number of points to use for the outline.
	 * @return An ellipsoidal arc. Like a piece of pie.
	 */
	public static Shape createEllipsoidalArc(float width, float height, float startAngle, float endAngle, int points) {
		return ShapeCreator.create(ShapeOutlineFactory.createEllipsoidalArc(width, height, startAngle, endAngle, points));
	}
}
