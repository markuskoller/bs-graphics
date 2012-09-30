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
import javax.vecmath.Vector2f;

/**
 * @author Markus Koller
 */
public interface Triangle {
	/**
	 * @param index Valid indexes are 0 to 2
	 * @return Point for the given index.
	 */
	public Vector2f getPoint(int index);
	/**
	 * @param index Valid indexes are 0 to 2
	 * @param point Point to set for the given index.
	 */
	public void setPoint(int index, Vector2f point);

	/**
	 * @param index Valid indexes are 0 to 2
	 * @return Color for the Point with the given index.
	 */
	public Color4f getColor(int index);
	/**
	 * @param index Valid indexes are 0 to 2
	 * @param color Color to set for the given index.
	 */
	public void setColor(int index, Color4f color);

	/**
	 * @param index Valid indexes are 0 to 2
	 * @return Texture coordinate for the Point with the given index.
	 */
	public Vector2f getTextureCoordinate(int index);
	/**
	 * @param index Valid indexes are 0 to 2
	 * @param coordinate Texture coordinates to set for the given index in pixels.
	 */
	public void setTextureCoordinate(int index, Vector2f coordinate);
}
