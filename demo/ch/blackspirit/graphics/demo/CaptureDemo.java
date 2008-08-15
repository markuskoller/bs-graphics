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
package ch.blackspirit.graphics.demo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.vecmath.Color4f;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Keyboard;
import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Controller.Type;
import ch.blackspirit.graphics.Flip;
import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.GraphicsContext;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.RealtimeCanvas;
import ch.blackspirit.graphics.View;
import ch.blackspirit.graphics.WindowListener;
import ch.blackspirit.graphics.anim.Animation;
import ch.blackspirit.graphics.anim.AnimationImpl;
import ch.blackspirit.graphics.anim.Frame;
import ch.blackspirit.graphics.anim.FrameImpl;
import ch.blackspirit.graphics.debug.TraceGraphics;
import ch.blackspirit.graphics.jogl.BufferTypes;
import ch.blackspirit.graphics.jogl.CanvasFactory;
import ch.blackspirit.graphics.util.AWTUtil;

/**
 * @author Markus Koller
 */
public class CaptureDemo  {
	private static boolean up = false;
	private static boolean down = false;
	private static boolean left = false;
	private static boolean right = false;
	private static float posX = 200;
	private static float posY = 50;
	
	private RealtimeCanvas canvas;

	private boolean doSave = false;
	
	public static void main(String []args) throws IOException {
		CaptureDemo demo = new CaptureDemo();
		demo.start();
	}
	public void start() throws IOException {
		ControllerEnvironment controllerEnv = ControllerEnvironment.getDefaultEnvironment();
		Keyboard keyboard = null;
		for(Controller controller: controllerEnv.getControllers()) {
			if(controller.getType() == Type.KEYBOARD) {
				keyboard = (Keyboard)controller;
				break;
			}
		}

		CanvasFactory factory = new ch.blackspirit.graphics.jogl.CanvasFactory();

		canvas = factory.createRealtimeCanvasWindow(400, 300);

		canvas.setVSync(true);
		canvas.addWindowListener(WindowListener.EXIT_ON_CLOSE);
		canvas.setWindowTitle("Capture Demo");
		
		final Image save = canvas.getImageFactory().createBufferedImage(400, 300, BufferTypes.RGB_3Byte);

		final Image animLeftImage = canvas.getImageFactory().createBufferedImage(
				CaptureDemo.class.getResource("/sprites/Crono - Walk (Left) 44x68.png"), false);
		Image animFrontImage = canvas.getImageFactory().createImage(
				CaptureDemo.class.getResource("/sprites/Crono - Walk (Front) 40x70.png"), false);
		Image animBackImage = canvas.getImageFactory().createImage(
				CaptureDemo.class.getResource("/sprites/Crono - Walk (Back) 36x70.png"), false);
		Image leftImage = canvas.getImageFactory().createImage(
				CaptureDemo.class.getResource("/sprites/Crono (Left).png"), false);
		Image frontImage = canvas.getImageFactory().createImage(
				CaptureDemo.class.getResource("/sprites/Crono (Front).png"), false);
		Image backImage = canvas.getImageFactory().createImage(
				CaptureDemo.class.getResource("/sprites/Crono (Back).png"), false);
		final Image grass = canvas.getImageFactory().createImage(
				CaptureDemo.class.getResource("/sprites/grass.png"), false);
		final Image wall = canvas.getImageFactory().createImage(
				CaptureDemo.class.getResource("/sprites/wall.png"), false);

		canvas.getResourceManager().cacheImage(animLeftImage);
		canvas.getResourceManager().cacheImage(animFrontImage);
		canvas.getResourceManager().cacheImage(animBackImage);
		canvas.getResourceManager().cacheImage(leftImage);
		canvas.getResourceManager().cacheImage(frontImage);
		canvas.getResourceManager().cacheImage(backImage);
		canvas.getResourceManager().cacheImage(grass);	
		canvas.getResourceManager().cacheImage(wall);

		// Setting up the animations
		final Animation<Frame> animLeft = new AnimationImpl<Frame>();
		animLeft.addFrame(new FrameImpl(animLeftImage, 160000000, 0, 0, 44, 68));
		animLeft.addFrame(new FrameImpl(animLeftImage, 160000000, 45, 0, 44, 68));
		animLeft.addFrame(new FrameImpl(animLeftImage, 160000000, 90, 0, 44, 68));
		animLeft.addFrame(new FrameImpl(animLeftImage, 160000000,135, 0, 44, 68));
		animLeft.addFrame(new FrameImpl(animLeftImage, 160000000,180, 0, 44, 68));
		animLeft.addFrame(new FrameImpl(animLeftImage, 160000000,225, 0, 44, 68));
		animLeft.setRepeated(true);
		
		final Animation<Frame> animFront = new AnimationImpl<Frame>();
		animFront.addFrame(new FrameImpl(animFrontImage, 160000000, 0, 0, 40, 70));
		animFront.addFrame(new FrameImpl(animFrontImage, 160000000, 41, 0, 40, 70));
		animFront.addFrame(new FrameImpl(animFrontImage, 160000000, 82, 0, 40, 70));
		animFront.addFrame(new FrameImpl(animFrontImage, 160000000,123, 0, 40, 70));
		animFront.addFrame(new FrameImpl(animFrontImage, 160000000,164, 0, 40, 70));
		animFront.addFrame(new FrameImpl(animFrontImage, 160000000,205, 0, 40, 70));
		animFront.setRepeated(true);
		
		
		final Animation<Frame> animLeftStill = new AnimationImpl<Frame>();
		animLeftStill.addFrame(new FrameImpl(leftImage, 160000000, 0, 0, 28, 68));
		animLeftStill.setRepeated(true);
		
		final Animation<Frame> animFrontStill = new AnimationImpl<Frame>();
		animFrontStill.addFrame(new FrameImpl(frontImage, 160000000, 0, 0, 32, 70));
		animFrontStill.setRepeated(true);
		
		final Animation<Frame> animBackStill = new AnimationImpl<Frame>();
		animBackStill.addFrame(new FrameImpl(backImage, 160000000, 0, 0, 32, 66));
		animBackStill.setRepeated(true);
		
		final Animation<Frame> animBack = new AnimationImpl<Frame>();
		animBack.addFrame(new FrameImpl(animBackImage, 160000000, 0, 0, 36, 70));
		animBack.addFrame(new FrameImpl(animBackImage, 160000000, 37, 0, 36, 70));
		animBack.addFrame(new FrameImpl(animBackImage, 160000000, 74, 0, 36, 70));
		animBack.addFrame(new FrameImpl(animBackImage, 160000000,111, 0, 36, 70));
		animBack.addFrame(new FrameImpl(animBackImage, 160000000,148, 0, 36, 70));
		animBack.addFrame(new FrameImpl(animBackImage, 160000000,185, 0, 36, 70));
		animBack.setRepeated(true);
		
		final Color4f white = new Color4f(1,1,1,1);
		final Color4f red = new Color4f(1,0,0,1);

		canvas.setGraphicsListener(new GraphicsListener() {
			long start = System.nanoTime();
			long currTime = start;
			long count = 0;
			long fps = 0;
	
			Animation<Frame> walk = animFrontStill;
			Flip flip = Flip.NONE;
			int xOffset = 0;

			public void draw(View view, Graphics renderer) {
				long elapsedTime = System.nanoTime() - currTime;
				currTime += elapsedTime;
				
				animLeft.update(elapsedTime);
				animFront.update(elapsedTime);
				animBack.update(elapsedTime);

				renderer.setColor(white);

				// Background
				for(int x = 0; x <= 400; x+=grass.getWidth() * 2) {
					for(int y = 0; y <= 300; y+=grass.getHeight() * 2) {
						renderer.translate(-x, -y);
						renderer.drawImage(grass, grass.getWidth() * 2, grass.getHeight() * 2);
						renderer.clearTransformation();
					}
				}
	
				for(int x = 0; x <= 400; x+=wall.getWidth() * 2) {
					renderer.translate(-x, -160);
					renderer.drawImage(wall, wall.getWidth() * 2, wall.getHeight() * 2);
					renderer.clearTransformation();
				}
			
				if(up && !down) {
					posY -= (float)elapsedTime / 11000000;
					walk = animBack;
					flip = Flip.NONE;
					xOffset = 0;
				} else if(down && !up) {
					posY += (float)elapsedTime / 11000000;
					walk = animFront;
					flip = Flip.NONE;
					xOffset = 0;
				} else if(left && !right) {
					posX -= (float)elapsedTime / 10000000;
					walk = animLeft;
					flip = Flip.NONE;
					xOffset = 0;
				} else if(right && !left) {
					posX += (float)elapsedTime / 10000000;
					walk = animLeft;
					flip = Flip.VERTICAL;
					xOffset = 0;
				} else {
					if(walk == animLeft) {
						walk = animLeftStill; 
						if(flip == Flip.NONE) xOffset = 4;
						else xOffset = 8;
					}
					if(walk == animFront) {
						walk = animFrontStill; 
						xOffset = 3;
					}
					if(walk == animBack) {
						walk = animBackStill; 
						xOffset = 1;
					}
				}
				
				renderer.clearTransformation();
				renderer.translate(-(posX + xOffset), -posY);
				walk.draw(renderer, walk.getWidth(), walk.getHeight(), flip);
				
				renderer.setColor(red);
				renderer.clearTransformation();
				renderer.translate(-250, -280);
				renderer.drawText("FPS:" + fps);
				
				if(doSave) {
					// Copy the content of the canvas
					renderer.copyToImage(save);
				}
				
				count++;
				if(currTime - start > 1000000000) {
					start = currTime;
					fps = count;
					count = 0;
					System.out.println(fps);
				}
			}

			public void init(View view, Graphics renderer) {
				view.setCamera(200, 150, 0);
				view.setSize(400, 300);
			}

			public void sizeChanged(GraphicsContext canvas, View view) {}
			
		});

	
		// Cleanup
		System.gc();
		
		// Starting the rendering
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
				
				// Character movement
				down = keyboard.isKeyDown(Key.DOWN);
				left = keyboard.isKeyDown(Key.LEFT);
				right = keyboard.isKeyDown(Key.RIGHT);
				up = keyboard.isKeyDown(Key.UP);

				doSave = keyboard.isKeyDown(Key.I);
			}

			
			canvas.draw();
			
			// Saving the image
			if(doSave) {
				// Update the image buffer
				save.updateBuffer();
				
				// Convert to BufferedImage
				BufferedImage awtSave = AWTUtil.readImageBuffer(save);
				
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);
				chooser.setFileFilter(new FileFilter() {
					@Override
					public boolean accept(File f) {
						return f.getName().endsWith(".png");
					}
					@Override
					public String getDescription() {
						return "Portable Network Graphics (.png)";
					}
				});
				if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					String fileName = chooser.getSelectedFile().getAbsolutePath();
					if(!fileName.endsWith(".png")) {
						fileName = fileName + ".png";
					}
					
					// Save using ImageIO
					ImageIO.write(awtSave, "png", new File(fileName));
				}
				doSave = false;
			}
		}
	}
}
