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

/**
 * A display mode is a set of properties to control native fullscreen
 * resolution and color depth.
 * @author Markus Koller
 */
public interface DisplayMode {
	/**
	 * @return Height in pixels. 
	 */
	public int getHeight();
	/**
	 * @return Width in pixels. 
	 */
	public int getWidth();
	/**
	 * @return Color depth in bits. 
	 */
	public int getColorDepth();
	/**
	 * @return Color refresh rate in hz (1/s). 
	 */
	public int getRefreshRate();
}
