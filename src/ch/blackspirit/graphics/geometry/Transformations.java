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
package ch.blackspirit.graphics.geometry;

import javax.vecmath.Vector2f;

/**
 * @author Markus Koller
 */
public class Transformations {
	public static void rotate(Vector2f point, Vector2f rotationCenter, float angleRad) {
		point.sub(rotationCenter);
		float x = point.x;
		float y = point.y;
		double sin = Math.sin(angleRad);
		double cos = Math.cos(angleRad);
		point.x = (float)(x * cos - y * sin);
		point.y = (float)(x * sin + y * cos);
		point.add(rotationCenter);
	}
	public static void rotate(Vector2f point, float angleRad) {
		float x = point.x;
		float y = point.y;
		double sin = Math.sin(angleRad);
		double cos = Math.cos(angleRad);
		point.x = (float)(x * cos - y * sin);
		point.y = (float)(x * sin + y * cos);
	}

}
