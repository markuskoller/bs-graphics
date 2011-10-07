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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.shape.Triangle;

@SuppressWarnings("unchecked")
/**
 * @author Markus Koller
 */
public class ParticleSystem<T extends Particle> {
	private List<Emitter<T>> emitters = new ArrayList<Emitter<T>>();
	private Object[] particles;
	private int particleCount = 0;
	private List<Updater<T>> updaters = new ArrayList<Updater<T>>();

	private ParticlePool<T> particlePool;
	
	private Vector2f position = new Vector2f();

	private long time = 0;
	private long updatedTime = 0;
	
	private long stepSize = 10;
	
	private Image image;

	public ParticleSystem(ParticlePool<T> particlePool) {
		this.particlePool = particlePool;
		particles = new Object[10000];
	}
	public ParticleSystem(Class<? extends T> particleClazz) {
		this.particlePool = new DefaultParticlePool<T>(particleClazz);
		particles = new Object[10000];
	}
	public ParticleSystem(ParticlePool<T> particlePool, int maxParticles) {
		this.particlePool = particlePool;
		particles = new Object[maxParticles];
	}
	
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}

	public void start() {
		for(int i = 0; i < pool.length; i++) {
			pool[i] = new Triangle();
			pool[i].setColor(0, new Color4f());
			pool[i].setColor(1, new Color4f());
			pool[i].setColor(2, new Color4f());
			pool[i].setTextureCoordinate(0, new Vector2f());
			pool[i].setTextureCoordinate(1, new Vector2f());
			pool[i].setTextureCoordinate(2, new Vector2f());
		}
	}
	
	public void update(long elapsedTime) {
		int updaterCount = updaters.size();

		time += elapsedTime;
		for(int i = 0; i < emitters.size(); i++) {
			Emitter<T> emitter = emitters.get(i);
			List<T> emitted = emitter.emit(elapsedTime);
			for(int p = 0; p < emitted.size(); p++) {
				T particle = emitted.get(p);
				particle.getPosition().add(position);
				particles[particleCount] = particle;
				for(int u = 0; u < updaterCount; u++) {
					Updater<T> updater = updaters.get(u);
					updater.update(particle, 0);
				}
				particleCount++;
			}
		}
		
		// update particles
		
		while(time - updatedTime > stepSize) {
			updatedTime += stepSize;

			for(int u = 0; u < updaterCount; u++) {
				Updater<T> updater = updaters.get(u);
				for(int i = 0; i < particleCount; i++) {
					T particle = (T)particles[i];
					long updateTime = updatedTime - particle.getTime();
					if(updateTime > 0) updater.update(particle, updateTime);
				}
			}
			for(int i = 0; i < particleCount; i++) {
				T particle = (T)particles[i];
				if(updatedTime > particle.getTime()) particle.setTime(updatedTime);
			}
		}
		
		for(int i = 0; i < particleCount; i++) {
			T particle = (T)particles[i];
			if(particle.getEnergy() <= 0) {
				// move particles in array
				int numMoved = particleCount - i - 1;
				if (numMoved > 0)
				    System.arraycopy(particles, i+1, particles, i,
						     numMoved);
				particles[--particleCount] = null; // Let gc do its work

				i--;
				particlePool.freeParticle(particle);
			}
		}
	}
	
	public void addUpdater(Updater<T> updater) {
		updaters.add(updater);
	}
	public boolean removeUpdater(Updater<T> updater) {
		return updaters.remove(updater);
	}
	public void addEmitter(Emitter<T> emitter) {
		emitters.add(emitter);
		emitter.setParticlePool(particlePool);
	}
	public boolean removeEmitter(Emitter<T> emitter) {
		boolean removed = emitters.remove(emitter);
		if(removed) emitter.setParticlePool(null);
		return removed;
	}
	
	public void reset() {
		
	}
	int numTriangles = 100;
	Triangle[] temp = new Triangle[numTriangles];
	Triangle[] pool = new Triangle[numTriangles];
	Sorter<T> sorter = new Sorter<T>();
	public void draw(Graphics graphics) {
		// optional sorting for drawing
//		sorter.sort(particles, creationTimeComparator, 0, particleCount - 1);

		
		int triangles = 0;
		float imageWidth = image.getWidth();
		float imageHeight = image.getHeight();
		for(int i = 0; i < particleCount; i++) {
			T particle = (T)particles[i];
			float width = particle.getSize().x;
			float height = particle.getSize().y;
			float x = particle.getPosition().x - width / 2;
			float y = particle.getPosition().y - height / 2;
			Color4f color = particle.getColor();
			
			// TODO test in joglgraphicsdelegate if texturecoordinates are available
			Triangle t = pool[triangles];
			t.getPoint(0).set(x, y);
			t.getPoint(1).set(x + width, y);
			t.getPoint(2).set(x, y + height);
			t.getTextureCoordinate(0).set(0, 0);
			t.getTextureCoordinate(1).set(imageWidth, 0);
			t.getTextureCoordinate(2).set(0, imageHeight);
			t.getColor(0).set(color);
			t.getColor(1).set(color);
			t.getColor(2).set(color);
			temp[triangles] = t;
			triangles++;

			t = pool[triangles];
			t.getPoint(0).set(x + width, y);
			t.getPoint(1).set(x + width, y + width);
			t.getPoint(2).set(x, y + height);
			t.getTextureCoordinate(0).set(imageWidth, 0);
			t.getTextureCoordinate(1).set(imageWidth, imageHeight);
			t.getTextureCoordinate(2).set(0, imageHeight);
			t.getColor(0).set(color);
			t.getColor(1).set(color);
			t.getColor(2).set(color);
			temp[triangles] = t;
			triangles++;
			
			if(triangles == numTriangles) {
				graphics.fillTriangles(temp, true, image);
				triangles = 0;
			}
		}
		if(triangles > 0) {
			Arrays.fill(temp, triangles, temp.length - 1, null);
			graphics.fillTriangles(temp, true, image);
			triangles = 0;
		}
	}
	
	public long getParticleCount() {
		return particleCount;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	public void setPosition(float x, float y) {
		position.set(x, y);
	}
	
	public static class LifetimeComparator<T extends Particle> implements Comparator<T> {
		public int compare(T o1, T o2) {
			return (int)(o2.getEnergy() - o1.getEnergy());
		}
	}
	public static class CreationTimeComparator<T extends Particle> implements Comparator<T> {
		public int compare(T o1, T o2) {
			return (int)(o1.getCreationTime() - o2.getCreationTime());
		}
	}
}
