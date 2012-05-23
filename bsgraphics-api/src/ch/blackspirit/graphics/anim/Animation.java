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

import java.util.List;

import ch.blackspirit.graphics.Flip;
import ch.blackspirit.graphics.Graphics;

/**
 * An animation is a set of frames. The frame shown changes over time
 * based on the frames' display times.
 * Every frame must have the same width and height in pixels.
 * Width and height of a frame must not change after it has been added.
 * What format you use for time (nanoseconds, milliseconds) doesn't matter, but
 * the same format should be used for the frames' display time.
 * @author Markus Koller
 * @param <T> Type of <code>Frame</code> the animation uses.
 */
public interface Animation<T extends Frame> {
	/**
	 * Increases the animation time.
	 * @param elapsedTime Time elapsed since last rendering.
	 */
	public void update(long elapsedTime);

	public long getTime(); 
	
	/**
	 * @return The currently shown frame.
	 */
	public T getFrame();

	/**
	 * @return All frames in the order they're displayed.
	 */
	public List<T> getFrames();

	/**
	 * Add a frame to the end of the animation.
	 * @param frame Frame to add.
	 */
	public void addFrame(T frame);

	/**
	 * Sets the animation time back to 0.
	 */
	public void reset();

	/**
	 * @return <code>true</code> if the animation has reached its end and is not repeated.
	 */
	public boolean hasFinished();

	/**
	 * @return <code>true</code> if the animation starts over when it reaches its end.
	 */
	public boolean isRepeated();

	/**
	 * Set if the animation should start over when it reaches its end.
	 * @param repeated <code>true</code> if the animation should start over when it reaches its end.
	 */
	public void setRepeated(boolean repeated);

	/**
	 * @return Height of the animation in pixels.
	 */
	public int getHeight();

	/**
	 * @return Width of the animation in pixels.
	 */
	public int getWidth();
	
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
	public void draw(Graphics graphics, float width, float height, Flip flip);
	/**
	 * Draw the current frame of the animation. 
	 * @param graphics The graphics to draw the frame with.
	 * @param width Width to display the frame with.
	 * @param height Height to display the frame with.
	 * @param subImageX X coordinate of the top left corner of the sub image.
	 * @param subImageY Y coordinate of the top left corner of the sub image.
	 * @param subImageWidth Width of the sub image.
	 * @param subImageHeight Height of the sub image.
	 * @deprecated Create an animation using sub images instead.
	 */
	public void draw(Graphics graphics, float width, float height, 
			int subImageX, int subImageY, int subImageWidth, int subImageHeight);
	/**
	 * Draw the current frame of the animation. 
	 * @param graphics The graphics to draw the frame with.
	 * @param width Width to display the frame with.
	 * @param height Height to display the frame with.
	 * @param subImageX X coordinate of the top left corner of the sub image.
	 * @param subImageY Y coordinate of the top left corner of the sub image.
	 * @param subImageWidth Width of the sub image.
	 * @param subImageHeight Height of the sub image.
	 * @param flip Image flipping.
	 * @deprecated Create an animation using sub images instead.
	 */
	public void draw(Graphics graphics, float width, float height, 
			int subImageX, int subImageY, int subImageWidth, int subImageHeight, Flip flip);
}