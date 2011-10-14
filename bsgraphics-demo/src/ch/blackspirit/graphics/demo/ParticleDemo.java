/*
 * Copyright 2009 Markus Koller
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
package ch.blackspirit.graphics.demo;

import java.io.IOException;

import javax.vecmath.Color4f;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Keyboard;
import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Controller.Type;
import ch.blackspirit.graphics.DrawingMode;
import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.GraphicsContext;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.RealtimeCanvas;
import ch.blackspirit.graphics.View;
import ch.blackspirit.graphics.WindowListener;
import ch.blackspirit.graphics.jogl.CanvasFactory;
import ch.blackspirit.graphics.particle.Emitter;
import ch.blackspirit.graphics.particle.Particle;
import ch.blackspirit.graphics.particle.ParticleSystem;
import ch.blackspirit.graphics.particle.PhysicsUpdater;
import ch.blackspirit.graphics.particle.pedigree.ForceUpdater;
import ch.blackspirit.graphics.particle.pedigree.Initializer;
import ch.blackspirit.graphics.particle.pedigree.ParticleProducer;
import ch.blackspirit.graphics.particle.pedigree.PropertyUpdater;

/**
 * @author Markus Koller
 */
public class ParticleDemo {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		ControllerEnvironment controllerEnv = ControllerEnvironment.getDefaultEnvironment();
		Keyboard keyboard = null;
		for(Controller controller: controllerEnv.getControllers()) {
			if(controller.getType() == Type.KEYBOARD) {
				keyboard = (Keyboard)controller;
				break;
			}
		}

		final RealtimeCanvas canvas = new CanvasFactory().createRealtimeCanvasWindow(800, 600);
		
		canvas.addWindowListener(WindowListener.EXIT_ON_CLOSE);
		canvas.setWindowTitle("Particle Demo");
		
		final Image image = canvas.getImageFactory().createImage(
				ParticleDemo.class.getResource("/ch/blackspirit/graphics/particle/particle.png"), false);
		canvas.getResourceManager().cacheImage(image);

		// Setup fire particle system
		final ParticleSystem<Particle> fireSystem = new ParticleSystem<Particle>(Particle.class);
		fireSystem.setImage(image);
		
		ParticleProducer<Particle> producer = new ParticleProducer<Particle>();
		producer.setIntervalMin(40);
		producer.setIntervalMax(40);
		producer.setCountMin(15);
		producer.setCountMax(15);
		
		Emitter<Particle> emitter = new Emitter<Particle>(producer);
		Initializer<Particle> initializer = new Initializer<Particle>();
		initializer.setSpeedMin(5);
		initializer.setSpeedMax(10);
		initializer.setSizeMin(40);
		initializer.setSizeMax(50);
		initializer.setAngleMin(-180f);
		initializer.setAngleMax(180f);
		initializer.setLifeMin(300);
		initializer.setLifeMax(2500);
		initializer.setXOffsetMin(-10);
		initializer.setXOffsetMax(10);
		initializer.setYOffsetMin(0);
		initializer.setYOffsetMax(0);
		initializer.setDistanceMax(10);
		initializer.setDistanceMin(0);
		emitter.addInitializer(initializer);
		fireSystem.addEmitter(emitter);
		
		ForceUpdater<Particle> force = new ForceUpdater<Particle>();
		force.setGravity(-.4f);
		fireSystem.addUpdater(force);
		
		fireSystem.addUpdater(new PhysicsUpdater<Particle>());
		
		PropertyUpdater<Particle> property = new PropertyUpdater<Particle>();
		property.setGrowthFactor(.2f);
		property.getR().addPoint(.6f, .02f);
		property.getG().addPoint(.6f, .02f);
		property.getB().addPoint(.6f, .01f);
		property.getA().addPoint(.6f, 1f);

		property.getR().addPoint(.7f, .3f);
		property.getG().addPoint(.7f, .15f);
		property.getB().addPoint(.7f, .05f);
		property.getA().addPoint(.7f, 1f);

		property.getR().addPoint(.8f, .2f);
		property.getG().addPoint(.8f, 0);
		property.getB().addPoint(.8f, 0);
		property.getA().addPoint(.8f, 1);

		property.getR().addPoint(1f, 0f);
		property.getG().addPoint(1f, 0);
		property.getB().addPoint(1f, 0);
		property.getA().addPoint(1f, 0);
		fireSystem.addUpdater(property);

		// Setup spring particle system
		final ParticleSystem<Particle> springSystem = new ParticleSystem<Particle>(Particle.class);
		springSystem.setImage(image);
		
		ParticleProducer<Particle> springProducer1 = new ParticleProducer<Particle>();
		springProducer1.setIntervalMin(3);
		springProducer1.setIntervalMax(3);
		springProducer1.setCountMin(1);
		springProducer1.setCountMax(1);
		
		Emitter<Particle> springEmitter1 = new Emitter<Particle>(springProducer1);
		Initializer<Particle> springInitializer1 = new Initializer<Particle>();
		springEmitter1.setPosition(-10, 0);
		springInitializer1.setSpeedMin(110);
		springInitializer1.setSpeedMax(150);
		springInitializer1.setSizeMin(3);
		springInitializer1.setSizeMax(5);
		springInitializer1.setAngleMin(9f);
		springInitializer1.setAngleMax(10f);
		springInitializer1.setLifeMin(2000);
		springInitializer1.setLifeMax(3000);
		springInitializer1.setXOffsetMin(0);
		springInitializer1.setXOffsetMax(0);
		springInitializer1.setYOffsetMin(0);
		springInitializer1.setYOffsetMax(0);
		springInitializer1.setDistanceMax(2);
		springInitializer1.setDistanceMin(0);
		springEmitter1.addInitializer(springInitializer1);
		springSystem.addEmitter(springEmitter1);

		ParticleProducer<Particle> springProducer2 = new ParticleProducer<Particle>();
		springProducer2.setIntervalMin(3);
		springProducer2.setIntervalMax(3);
		springProducer2.setCountMin(1);
		springProducer2.setCountMax(1);

		Emitter<Particle> springEmitter2 = new Emitter<Particle>(springProducer2);
		Initializer<Particle> springInitializer2 = new Initializer<Particle>();
		springEmitter2.setPosition(10, 0);
		springInitializer2.setSpeedMin(110);
		springInitializer2.setSpeedMax(150);
		springInitializer2.setSizeMin(3);
		springInitializer2.setSizeMax(5);
		springInitializer2.setAngleMin(-10f);
		springInitializer2.setAngleMax(-9f);
		springInitializer2.setLifeMin(2000);
		springInitializer2.setLifeMax(3000);
		springInitializer2.setXOffsetMin(0);
		springInitializer2.setXOffsetMax(0);
		springInitializer2.setYOffsetMin(0);
		springInitializer2.setYOffsetMax(0);
		springInitializer2.setDistanceMax(2);
		springInitializer2.setDistanceMin(0);
		springEmitter2.addInitializer(springInitializer2);
		springSystem.addEmitter(springEmitter2);

		ForceUpdater<Particle> springForce = new ForceUpdater<Particle>();
		springForce.setGravity(.9f);
		springSystem.addUpdater(springForce);
		
		springSystem.addUpdater(new PhysicsUpdater<Particle>());
		
		PropertyUpdater<Particle> springProperty = new PropertyUpdater<Particle>();
		springProperty.setGrowthFactor(1f);
		springProperty.getR().addPoint(0f, .35f);
		springProperty.getG().addPoint(0f, .35f);
		springProperty.getB().addPoint(0f, .4f);
		springProperty.getA().addPoint(0f, 1f);

		springProperty.getR().addPoint(.7f, .35f);
		springProperty.getG().addPoint(.7f, .35f);
		springProperty.getB().addPoint(.7f, .4f);
		springProperty.getA().addPoint(.7f, 1);

		springProperty.getR().addPoint(1f, .9f);
		springProperty.getG().addPoint(1f, .9f);
		springProperty.getB().addPoint(1f, .9f);
		springProperty.getA().addPoint(1f, 0);
		springSystem.addUpdater(springProperty);

		final Color4f white = new Color4f(1,1,1,1);
		final Color4f red = new Color4f(1,0,0,1);
		
		canvas.setGraphicsListener(new GraphicsListener() {
			long start = System.currentTimeMillis();
			long currTime = start;
			long count = 0;
			long fps = 0;
			
			public void draw(View view, Graphics renderer) {
				long elapsedTime = System.currentTimeMillis() - currTime;
				currTime += elapsedTime;

				renderer.setDrawingMode(DrawingMode.ALPHA_ADD);
				renderer.setColor(white);

				renderer.clear();
				renderer.clearTransform();
				renderer.translate(-200, -350);
				fireSystem.draw(renderer);

				renderer.clearTransform();
				renderer.translate(-600, -350);
				springSystem.draw(renderer);
				
				renderer.clearTransform();
				renderer.translate(-50, -50);
				renderer.drawText("Particle Demo");
				
				renderer.clearTransform();
				renderer.translate(-100, -500);
				renderer.drawText("Fire particles: " + fireSystem.getParticleCount());

				renderer.clearTransform();
				renderer.translate(-500, -500);
				renderer.drawText("Spring particles: " + springSystem.getParticleCount());

				// draw frames per second
				renderer.setColor(red);
				renderer.clearTransform();
				renderer.translate(-650, -580);
				renderer.drawText("FPS: " + fps);
							
				// calculate frames per second every second
				count++;
				if(currTime - start > 1000) {
					start = currTime;
					fps = count;
					count = 0;
					System.out.println(fps + " fps");
				}
			}
			public void init(View view, Graphics renderer) {
				view.setCamera(400, 300,0);
				view.setSize(800, 600);
			}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		
		fireSystem.start();
		springSystem.start();

		long currTime = System.currentTimeMillis();
		long lastVSyncChange = 0;
		while(true) {
			if(keyboard != null) {
				keyboard.poll();
			
				// End demo
				if(keyboard.isKeyDown(Key.Q) || keyboard.isKeyDown(Key.ESCAPE)) {
					canvas.dispose();
					System.exit(0);
				}
				// VSync
				if(keyboard.isKeyDown(Key.S)) {
					long time = System.currentTimeMillis();
					if(time - lastVSyncChange > 1000) {
						canvas.setVSync(!canvas.getVSync());
						lastVSyncChange = time;
					}
				}
			}
			long elapsedTime = System.currentTimeMillis() - currTime;
			currTime += elapsedTime;
			
			fireSystem.update(elapsedTime);
			springSystem.update(elapsedTime);
			canvas.draw();
		}
	}

}
