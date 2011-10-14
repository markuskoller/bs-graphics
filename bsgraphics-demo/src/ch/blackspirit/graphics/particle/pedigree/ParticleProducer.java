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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.blackspirit.graphics.particle.Particle;
import ch.blackspirit.graphics.particle.ParticlePool;

/**
 * @author Markus Koller
 */
public class ParticleProducer<T extends Particle> implements ch.blackspirit.graphics.particle.ParticleProducer<T> {
	private ParticlePool<T> particlePool;
	private Random random = new Random();
	
	private long currentTime = 0;

	private long lastSpawn = 0;
	private long nextSpawnInterval = 0;
	
	private long intervalMin;
	private long intervalMax;
	private long countMin;
	private long countMax;
	
//	<emitCount enabled="true" max="1000.0" min="1000.0"/>

	List<T> list = new ArrayList<T>();
	public List<T> getParticles(long elapsedTime) {
		this.currentTime += elapsedTime;
		list.clear();
	
		while(lastSpawn + nextSpawnInterval < currentTime) {
			long currentCount = countMin + (long)(random.nextFloat() * (float)(countMax - countMin));

			lastSpawn += nextSpawnInterval;
			
			for(int i = 0; i < currentCount; i++) {
				T particle = particlePool.getParticle();
				particle.getVelocity().set(0,0);
				particle.getPosition().set(0,0);
				particle.getForce().set(0,0);
				particle.getColor().set(0,0,0,0);
				particle.getSize().set(0,0);
	
				particle.setTime(lastSpawn);
				particle.setCreationTime(lastSpawn);
	
				list.add(particle);
			}
			
			nextSpawnInterval = intervalMin + (long)(random.nextFloat() * (float)(intervalMax - intervalMin));
		}
		return list;
	}
	
	public long getIntervalMin() {
		return intervalMin;
	}
	public void setIntervalMin(long intervalMin) {
		this.intervalMin = intervalMin;
	}
	public long getIntervalMax() {
		return intervalMax;
	}
	public void setIntervalMax(long intervalMax) {
		this.intervalMax = intervalMax;
	}
	public long getCountMin() {
		return countMin;
	}
	public void setCountMin(long countMin) {
		this.countMin = countMin;
	}
	public long getCountMax() {
		return countMax;
	}
	public void setCountMax(long countMax) {
		this.countMax = countMax;
	}

	public ParticlePool<T> getParticlePool() {
		return particlePool;
	}
	public void setParticlePool(ParticlePool<T> particlePool) {
		this.particlePool = particlePool;
	}
}
