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
package ch.blackspirit.graphics.jogl;

import java.util.ArrayList;

/**
 * @author Markus Koller
 */
class View implements ch.blackspirit.graphics.View {
	private float width;
	private float height;
	private float cameraX;
	private float cameraY;
	private float cameraAngle;
	private ArrayList<ViewListener> viewListeners = new ArrayList<ViewListener>();
	
	public View() {
		super();
	}

	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
		fireViewSizeChanged();
	}

	
	public float getCameraX() {
		return cameraX;
	}
	public float getCameraY() {
		return cameraY;
	}
	public float getCameraAngle() {
		return cameraAngle;
	}

	public void setCamera(float x, float y, float angle) {
		this.cameraX = x;
		this.cameraY = y;
		this.cameraAngle = angle;
		fireCameraChanged();
	}
	public void setCameraAngle(float angle) {
		this.cameraAngle = angle;
		fireCameraChanged();
	}
	public void setCameraPosition(float x, float y) {
		this.cameraX = x;
		this.cameraY = y;
		fireCameraChanged();
	}

	public void addViewListener(ViewListener viewListener) {
		viewListeners.add(viewListener);
	}
	public boolean removeViewListener(ViewListener viewListener) {
		return viewListeners.remove(viewListener);
	}
	protected void fireViewSizeChanged() {
		for(int i = 0; i < viewListeners.size(); i++) {
			viewListeners.get(i).viewSizeChanged();
		}
	}
	protected void fireCameraChanged() {
		for(int i = 0; i < viewListeners.size(); i++) {
			viewListeners.get(i).cameraChanged();
		}
	}

}
