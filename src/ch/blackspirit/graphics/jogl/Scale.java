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

import javax.media.opengl.GL;

/**
 * @author Markus Koller
 */
public final class Scale implements Transformation {
	private float scaleX = 1;
	private float scaleY = 1;
	
	public Scale() {};
	
	public float getScaleX() {
		return scaleX;
	}
	public void setScaleX(float scale) {
		this.scaleX = scale;
	}
	public float getScaleY() {
		return scaleY;
	}
	public void setScaleY(float scale) {
		this.scaleY = scale;
	}

	public void apply(GL gl) {
		gl.glScalef(scaleX, scaleY, 1);
	}

}
