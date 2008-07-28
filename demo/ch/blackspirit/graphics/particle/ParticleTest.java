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

import java.io.IOException;

import ch.blackspirit.graphics.DrawingMode;
import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.GraphicsContext;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.RealtimeCanvas;
import ch.blackspirit.graphics.View;
import ch.blackspirit.graphics.jogl.CanvasFactory;
import ch.blackspirit.graphics.particle.pedigree.ForceUpdater;
import ch.blackspirit.graphics.particle.pedigree.Initializer;
import ch.blackspirit.graphics.particle.pedigree.ParticleProducer;
import ch.blackspirit.graphics.particle.pedigree.PropertyUpdater;

/**
 * @author Markus Koller
 */
public class ParticleTest {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		RealtimeCanvas canvas = new CanvasFactory().createRealtimeCanvasWindow(800, 600);

//		final Image image = canvas.getImageFactory().createBufferedImage(32, 32, true);
//		ColorGradientFactory grad = new ColorGradientFactory();
//		grad.setBaseColor(new Color4f(1,1,1,0));
////		grad.setBaseColor(new Color4f(0,0,0,0));
//		grad.addSourcePoint(16, 16, 14, new Color4f(1,1,1,1));
//		grad.drawGradient(image);
//		image.updateCache();
		final Image image = canvas.getImageFactory().createImage(ParticleTest.class.getResource("particle.tga"), false);
		canvas.getResourceManager().cacheImage(image);

		ParticlePool<Particle> particlePool= new DefaultParticleManager();
		final ParticleSystem<Particle> system = new ParticleSystem<Particle>(particlePool);
		system.setImage(image);
		
		ParticleProducer<Particle> producer = new ParticleProducer<Particle>(particlePool);
		producer.setIntervalMin(50);
		producer.setIntervalMax(50);
		producer.setCountMin(20);
		producer.setCountMax(20);
		
		Emitter<Particle> emitter = new Emitter<Particle>(producer);
		Initializer<Particle> initializer = new Initializer<Particle>();
		initializer.setSpeedMin(0);
		initializer.setSpeedMax(10);
		initializer.setSizeMin(40);
		initializer.setSizeMax(50);
		initializer.setAngleMin(-180f);
		initializer.setAngleMax(180f);
		initializer.setLifeMin(2500);
		initializer.setLifeMin(2500);
		initializer.setXOffsetMin(-20);
		initializer.setXOffsetMax(20);
//		initializer.setYOffsetMin(-6);
//		initializer.setYOffsetMax(6);
		initializer.setDistanceMax(10);
		emitter.addInitializer(initializer);
		system.addEmitter(emitter);
		
		ForceUpdater<Particle> force = new ForceUpdater<Particle>();
		force.setGravity(-.5f);
		system.addUpdater(force);
		
		system.addUpdater(new PhysicsUpdater<Particle>());
		
		PropertyUpdater<Particle> property = new PropertyUpdater<Particle>();
		property.setGrowthFactor(.3f);
		property.getR().addPoint(.6f, .02f);
		property.getG().addPoint(.6f, .02f);
		property.getB().addPoint(.6f, .01f);
		property.getA().addPoint(.6f, 1f);

		property.getR().addPoint(.8f, .2f);
		property.getG().addPoint(.8f, 0);
		property.getB().addPoint(.8f, 0);
		property.getA().addPoint(.8f, 1);

		property.getR().addPoint(1f, 0f);
		property.getG().addPoint(1f, 0);
		property.getB().addPoint(1f, 0);
		property.getA().addPoint(1f, 0);
		system.addUpdater(property);
		
		canvas.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics renderer) {
				renderer.clear();
				system.draw(renderer);
			}
			public void init(View view, Graphics renderer) {
				renderer.setDrawingMode(DrawingMode.ALPHA_ADD);
				view.setCamera(0,0,0);
				view.setSize(800, 600);
			}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		
		system.start();

		long start = System.currentTimeMillis();
		long currTime = start;
		long count = 0;
		long fps = 0;
		
		while(true) {
			long elapsedTime = System.currentTimeMillis() - currTime;
			currTime += elapsedTime;
			
			system.update(elapsedTime);
			canvas.draw();

			// calculate frames per second every second
			count++;
			if(currTime - start > 1000) {
				start = currTime;
				fps = count;
				count = 0;
				System.out.println(fps);
				System.out.println(system.getParticleCount());
			}
		}
	}

}
