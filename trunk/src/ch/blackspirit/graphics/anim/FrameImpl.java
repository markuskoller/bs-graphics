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
package ch.blackspirit.graphics.anim;

import ch.blackspirit.graphics.Image;

/**
 * Basic Frame implementation covering all the features of the <code>Frame</code> interface.
 * @author Markus Koller
 */
public class FrameImpl implements Frame {
	private final Image image;
	private final long displayTime;
	private int subImageX;
	private int subImageY;
	private int subImageWidth;
	private int subImageHeight;

	/**
	 * Create a frame showing the complete image.
	 * @param image The image used in this frame.
	 * @param displayTime The time the frame is shown in nano seconds.
	 */
	public FrameImpl(final Image image, final long displayTime) {
		this(image, displayTime, 0, 0, 1, 1);
	}
	
	/**
	 * Create a frame showing only a part of the image.
	 * @param image The image used in this frame.
	 * @param displayTime The time the frame is shown in nano seconds.
	 * @param subImageX X offset of the sub image.
	 * @param subImageY Y offset of the sub image.
	 * @param subImageWidth Width of the sub image.
	 * @param subImageHeight Height of the sub image.
	 */
	public FrameImpl(final Image image, final long displayTime,
			int subImageX, int subImageY, int subImageWidth, int subImageHeight) {
		super();
		this.image = image;
		this.displayTime = displayTime;
		this.subImageX = subImageX;
		this.subImageY = subImageY;
		this.subImageWidth = subImageWidth;
		this.subImageHeight = subImageHeight;
	}
	
	public Image getImage() {
		return image;
	}
	public long getDisplayTime() {
		return displayTime;
	}
	public int getSubImageX() {
		return subImageX;
	}
	public int getSubImageY() {
		return subImageY;
	}
	public int getSubImageWidth() {
		return subImageWidth;
	}
	public int getSubImageHeight() {
		return subImageHeight;
	}
}
