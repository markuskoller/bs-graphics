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
package ch.blackspirit.graphics.particle.pedigree;

import ch.blackspirit.graphics.particle.Particle;
import ch.blackspirit.graphics.particle.Updater;

/**
 * @author Markus Koller
 */
public class ForceUpdater<T extends Particle> implements Updater<T> {
	private float gravity = 0;
	private float wind = 0;

	public void update(T particle, long elapsedTime) {
		particle.getForce().x += wind;
		particle.getForce().y += gravity;
	}

	public float getGravity() {
		return gravity;
	}
	public void setGravity(float gravity) {
		this.gravity = gravity;
	}
	public float getWind() {
		return wind;
	}
	public void setWind(float wind) {
		this.wind = wind;
	}
}
