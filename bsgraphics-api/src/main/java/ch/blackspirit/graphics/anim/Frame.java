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
package ch.blackspirit.graphics.anim;

import ch.blackspirit.graphics.Flip;
import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.Image;

/**
 * A frame is one image (sub image) used in an animation.
 * Every frame has a display time. After that time the
 * next frame is shown. <br/>
 * What format you use for time (nanoseconds, milliseconds) doesn't matter, 
 * but the same format should be used in the animation.
 * @author Markus Koller
 */
public interface Frame {
	/**
	 * @return The image used in this frame.
	 * @deprecated Animation is now using draw method.
	 */
	public Image getImage();
	/**
	 * @return The display time.
	 */
	public long getDisplayTime();
	/**
	 * @return The sub images X coordinate.
	 * @deprecated Animation is now using draw method.
	 */
	public int getSubImageX();
	/**
	 * @return The sub images Y coordinate.
	 * @deprecated Animation is now using draw method.
	 */
	public int getSubImageY();
	/**
	 * @return The sub images width.
	 * @deprecated Animation is now using draw method.
	 */
	public int getSubImageWidth();
	/**
	 * @return The sub images height.
	 * @deprecated Animation is now using draw method.
	 */
	public int getSubImageHeight();
	
	/**
	 * @return
	 * @since Blackspirit Graphics 2.1
	 */
	public int getWidth();
	/**
	 * @return
	 * @since Blackspirit Graphics 2.1
	 */
	public int getHeight();
	
	/**
	 * Draw the current frame of the animation. 
	 * @param graphics The graphics to draw the frame with.
	 * @param width Width to display the frame with.
	 * @param height Height to display the frame with.
	 */
	public void draw(Graphics graphics, float width, float height);
	/**
	 * Draw the current frame of the animation. 
	 * @param graphics The graphics to draw the frame with.
	 * @param width Width to display the frame with.
	 * @param height Height to display the frame with.
	 * @param flip Image flipping.
	 */
	public void draw(Graphics graphics, float width, float height, Flip flip);}