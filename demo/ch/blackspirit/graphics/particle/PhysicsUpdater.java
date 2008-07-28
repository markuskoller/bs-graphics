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

import javax.vecmath.Vector2f;

/**
 * @author Markus Koller
 */
public class PhysicsUpdater<T extends Particle> implements Updater<T> {
	private Vector2f positionChange = new Vector2f();
	private Vector2f velocityChange = new Vector2f();

	public void update(T particle, long elapsedTime) {
		float time = .001f * elapsedTime;

		velocityChange.set(particle.getForce());
		velocityChange.x *= time;
		velocityChange.y *= time;
		particle.getVelocity().add(velocityChange);

		positionChange.set(particle.getVelocity());
		positionChange.x *= time;
		positionChange.y *= time;
		particle.getPosition().add(positionChange);
	}

}
