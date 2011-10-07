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
package ch.blackspirit.graphics.particle;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;

/**
 * @author Markus Koller
 */
public class Emitter<T extends Particle> {
	private List<Initializer<T>> initializers = new ArrayList<Initializer<T>>();
	
	private ParticleProducer<T> particleProducer;
	private ParticlePool<T> particlePool;
	
	private Vector2f position = new Vector2f();
	
//	<spawnInterval enabled="false" max="100.0" min="100.0"/>
//	<spawnCount enabled="false" max="5.0" min="5.0"/>
//	<emitCount enabled="true" max="1000.0" min="1000.0"/>

	public Emitter(ParticleProducer<T> particleProducer) {
		this.particleProducer = particleProducer;
	}
	
	public List<T> emit(long elapsedTime) {
		List<T> produced = particleProducer.getParticles(elapsedTime);
		
		for(int i = 0; i < produced.size(); i++) {
			T particle = produced.get(i);
//			particle.getVelocity().set(0,0);
//			particle.getPosition().set(0,0);
//			particle.getForce().set(0,0);
//			particle.getColor().set(0,0,0,0);
//			particle.getSize().set(0,0);
			for(int j = 0; j < initializers.size(); j++) {
				Initializer<T> initializer = initializers.get(j);
				initializer.initialize(particle);
			}
			particle.getPosition().add(position);
		}
		return produced;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	public void setPosition(float x, float y) {
		position.set(x, y);
	}
	
	public void addInitializer(Initializer<T> initializer) {
		initializers.add(initializer);
	}
	public boolean removeInitializer(Initializer<T> initializer) {
		return initializers.remove(initializer);
	}

	public ParticlePool<T> getParticlePool() {
		return particlePool;
	}
	void setParticlePool(ParticlePool<T> particlePool) {
		this.particlePool = particlePool;
		particleProducer.setParticlePool(particlePool);
	}
}
