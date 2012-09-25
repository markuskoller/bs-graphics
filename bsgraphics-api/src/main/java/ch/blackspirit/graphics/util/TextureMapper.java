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
package ch.blackspirit.graphics.util;

import javax.vecmath.Vector2f;

import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.Triangle;
import ch.blackspirit.graphics.geometry.Transformations;
import ch.blackspirit.graphics.shape.Area;

/**
 * Utility class to map textures on shapes.
 * @author Markus Koller
 */
public class TextureMapper {
	private TextureMapper() {}
	
	/**
	 * Maps the image to shape and also sets the image as texture.
	 * @param shape The shape which should be textured.
	 * @param texture The image to use as texture.
	 * @param x The X coordinate of the textures center.
	 * @param y The Y coordinate of the textures center.
	 * @param rotationAngle The angle to rotate about the textures center in degrees.
	 * @param scale The amount of scale applied at the textures center. 1 = normal.
	 */
	public static void mapTexture(Area shape, Image texture, float x, float y, float rotationAngle, float scale) {
		float rad = (float)Math.toRadians(rotationAngle);
		for(int i = 0; i < shape.getTriangles().length; i++) {
			Triangle area = shape.getTriangles()[i];
			for(int j = 0; j < 3; j++) {
				Vector2f textureCoordinate = area.getTextureCoordinate(j);
				if(textureCoordinate == null) {
					textureCoordinate = new Vector2f();
					area.setTextureCoordinate(j, textureCoordinate);
				}
				
				// Get corresponding shape coordinate
				textureCoordinate.set(area.getPoint(j));
				
				// Rotate, scale, translate results in translation first, then scaling and then rotation
				Transformations.rotate(textureCoordinate, rad);
				textureCoordinate.scale(scale);
				textureCoordinate.x += x;
				textureCoordinate.y += y;
			}
		}
		shape.setTexture(texture);
	}
}
