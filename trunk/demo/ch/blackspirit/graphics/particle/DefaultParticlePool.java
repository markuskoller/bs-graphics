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


/**
 * @author Markus Koller
 */
public class DefaultParticlePool<T extends Particle> implements ParticlePool<T> {
	private Object[] particles = new Object[10000];
	private int index = 0;
	
	private Class<? extends T> clazz;
	
	public DefaultParticlePool(Class<? extends T> clazz) {
		super();
		this.clazz = clazz;
	}
	
	@SuppressWarnings("unchecked")
	public T getParticle() {
		index--;
		if(index >= 0) {
			return (T)particles[index];
		} else {
			index++;
			try {
				return clazz.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException("Constructor without arguments must exist!", e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Constructor must be public!", e);
			}
		}
	}
	public void freeParticle(Particle particle) {
		if(index >= particles.length) {
			Object[] newArray = new Object[particles.length * 2];
			System.arraycopy(particles, 0, newArray, 0, particles.length);
			particles = newArray;
		}
		particles[index] = particle;
   		index++;
	}
}
