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
package ch.blackspirit.graphics;

/**
 * Defines which part of the rendering should be visible in a canvas. 
 * @author Markus Koller
 */
public interface View {
	/**
	 * Sets the logical resolution that is visible through the view.<br/>
	 * This must be called before any drawing during a frame, otherwise unpredictive results might occur.
	 */
	public void setSize(float width, float height);
	public float getWidth();
	public float getHeight();

	/**
	 * Setting the camera specifies which coordinate will be centered in the view.</br>
	 * The angle (int degrees) additionally rotates the view clockwise.<br/>
	 * This must be called before any drawing during a frame, otherwise unpredictive results might occur.
	 */
	public void setCamera(float x, float y, float angle);
	public void setCameraPosition(float x, float y);
	public void setCameraAngle(float angle);
	public float getCameraX();
	public float getCameraY();
	public float getCameraAngle();
}
