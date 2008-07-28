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
package ch.blackspirit.graphics.particle;

import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

/**
 * @author Markus Koller
 */
public class Particle {
	private Vector2f position = new Vector2f(0,0);
	private Vector2f velocity = new Vector2f(0,0);
	private Vector2f force = new Vector2f(0,0);
	
	private Vector2f size = new Vector2f(0,0);
	private Vector2f initialSize = new Vector2f(0,0);
	
	// energy instead of lifetime (not limited to time related concept for existence)
	private long energy;
	private long initialEnergy;
	
	private long time;
	private long creationTime;
	private Color4f color = new Color4f(1,1,1,1);
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public long getEnergy() {
		return energy;
	}
	public void setEnergy(long energy) {
		this.energy = energy;
	}
	public Vector2f getPosition() {
		return position;
	}
	public Vector2f getSize() {
		return size;
	}
	public Color4f getColor() {
		return color;
	}
	public Vector2f getVelocity() {
		return velocity;
	}
	public Vector2f getForce() {
		return force;
	}
	public long getInitialEnergy() {
		return initialEnergy;
	}
	public void setInitialEnergy(long initialEnergy) {
		this.initialEnergy = initialEnergy;
	}
	public long getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	public Vector2f getInitialSize() {
		return initialSize;
	}
}	
