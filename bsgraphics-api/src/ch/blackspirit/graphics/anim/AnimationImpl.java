/*
 * Copyright 2008-2011 Markus Koller
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

import java.util.ArrayList;
import java.util.List;

import ch.blackspirit.graphics.Flip;
import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.Image;

/**
 * Basic Animation implementation covering all the features of the <code>Animation</code> interface.
 * @author Markus Koller
 */
public class AnimationImpl<T extends Frame> implements Animation<T> {
	private long animTime = 0;
	private long totalTime = 0;
	private long runningTime = 0;
	
	protected int currentFrame = 0;
	
	private boolean repeated = false;
	
	protected List<T> frames = new ArrayList<T>();
	private List<Long> endTimes = new ArrayList<Long>();

	private int width = -1;
	private int height = -1;
	
	public void update(long elapsedNanoSeconds) {
		animTime += elapsedNanoSeconds;
		runningTime += elapsedNanoSeconds;
		
		if(hasFinished()) {
			currentFrame = frames.size() - 1;
		} else {
			if(animTime > totalTime) {
				animTime = animTime % totalTime;
				currentFrame = 0;
			}
			
			while( animTime > endTimes.get(currentFrame) ) {
				currentFrame++;
			}
		}
	}
	
	public T getFrame() {
		return frames.get(currentFrame);
	}
	
	public List<T> getFrames() {
		return frames;
	}

	public void addFrame(T frame) {
		if(frames.size() == 0) {
			width = frame.getSubImageWidth();
			height = frame.getSubImageHeight();
		} else {
			if(width != frame.getSubImageWidth() ||
					height != frame.getSubImageHeight()) {
				throw new IllegalArgumentException("All Frames in an Animation must have the same width and height!");
			}
		}
		
		totalTime += frame.getDisplayTime();
		endTimes.add(totalTime);
		frames.add(frame);
	}
	
	public void reset() {
		animTime = 0;
		currentFrame = 0;
		runningTime = 0;
	}
	
	public boolean hasFinished() {
		if(repeated) {
			return false;
		} else {
			return animTime > totalTime;
		}
	}
	
	public boolean isRepeated() {
		return repeated;
	}

	public void setRepeated(boolean repeated) {
		this.repeated = repeated;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	public void draw(Graphics graphics, float width, float height) {
		draw(graphics, width, height, Flip.NONE);
	}
	public void draw(Graphics graphics, float width, float height, Flip flip) {
        T frame = getFrame();
        Image image = frame.getImage();

        graphics.drawImage(image, 
				width, height, 
				frame.getSubImageX(), frame.getSubImageY(),
				frame.getSubImageWidth(), frame.getSubImageHeight(),
				flip);
	}
	public void draw(Graphics graphics, float width, float height, int subImageX, int subImageY, int subImageWidth, int subImageHeight) {
		draw(graphics, width, height, subImageX, subImageY, subImageWidth, subImageHeight, Flip.NONE);
	}
	public void draw(Graphics graphics, float width, float height, int subImageX, int subImageY, int subImageWidth, int subImageHeight, Flip flip) {
        T frame = getFrame();
        Image image = frame.getImage();

        graphics.drawImage(image, 
				width, height, 
				frame.getSubImageX() + subImageX, frame.getSubImageY() + subImageY,
				subImageWidth, subImageHeight,
				flip);
	}

	public long getTime() {
		return runningTime;
	}
}
