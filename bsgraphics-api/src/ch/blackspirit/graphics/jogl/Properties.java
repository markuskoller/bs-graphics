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
package ch.blackspirit.graphics.jogl;

/**
 * The canvas properties available in the JOGL implementation of Blackspirit Graphics are
 * enumerated in this class.
 * @author Markus Koller
 */
public final class Properties {

	/**
	 * Boolean property
	 */
	public static final String IS_DRAWING_MODE_SUBTRACT_SUPPORTED = "ch.blackspirit.graphics.jogl.subtract.supported";
	/**
	 * Long property
	 */
	public static final String MAX_IMAGE_DRAWING_WIDTH = "ch.blackspirit.graphics.jogl.image.draw.width";
	/**
	 * Long property
	 */
	public static final String MAX_IMAGE_DRAWING_HEIGHT = "ch.blackspirit.graphics.jogl.image.draw.height";
}
