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

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Keyboard;
import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Controller.Type;
import ch.blackspirit.graphics.DisplayMode;
import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.GraphicsContext;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.RealtimeCanvas;
import ch.blackspirit.graphics.View;
import ch.blackspirit.graphics.WindowListener;
import ch.blackspirit.graphics.jogl.BufferTypes;
import ch.blackspirit.graphics.jogl.CanvasFactory;
import ch.blackspirit.graphics.shape.Shape;
import ch.blackspirit.graphics.util.ColorGradientFactory;
import ch.blackspirit.graphics.util.ShapeCreator;
import ch.blackspirit.graphics.util.ShapeFactory;
import ch.blackspirit.graphics.util.ShapeOutlineFactory;
import ch.blackspirit.graphics.util.TextureMapper;

/**
 * @author Markus Koller
 */
public class ShapeDemo  {
	private final static int WIDTH = 800;
	private final static int HEIGHT = 600;

	RealtimeCanvas canvas;
	
	public static void main(String []args) throws IOException {
		ShapeDemo demo = new ShapeDemo();
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

		// Create a fullscreen realtime canvas using the current display mode.
//		DisplayMode mode = factory.getDisplayMode(WIDTH, HEIGHT);
//		if(mode != null) {
//			canvas = factory.createRealtimeCanvasFullscreen(mode);
//		} else {
			canvas = factory.createRealtimeCanvasFullscreen();
//		}

		canvas.setVSync(true);
		canvas.addWindowListener(WindowListener.EXIT_ON_CLOSE);
		canvas.setWindowTitle("Image Render Demo");
		
		final Image animLeftImage = canvas.getImageFactory().createImage(
				ModeChangeDemo.class.getResource("/sprites/Crono - Walk (Left) 44x68.png"), false);
		
		final Shape circle = ShapeFactory.createCircle(30, 25);
		final Shape circularArc = ShapeFactory.createCircularArc(30, -135, 135, 25);
		final Shape ellipse = ShapeFactory.createEllipse(75, 25, 25);
		final Shape ellipsoidalArc = ShapeFactory.createEllipsoidalArc(75, 25, 45, 315, 25);
		final Shape roundedRectangle = ShapeFactory.createRoundedRectangle(75, 40, 15);
		final Shape rectangle = ShapeFactory.createRectangle(75, 25);

		final Shape bscircle = ShapeFactory.createCircle(30, 25);

		List<Vector2f> rectOut = new ArrayList<Vector2f>();
		rectOut.add(new Vector2f(40, 10));
		rectOut.add(new Vector2f(-40, 10));
		rectOut.add(new Vector2f(-40, -10));
		rectOut.add(new Vector2f(40, -10));
		List<Vector2f> rectIn = new ArrayList<Vector2f>();
		rectIn.add(new Vector2f(20, 5));
		rectIn.add(new Vector2f(-20, 5));
		rectIn.add(new Vector2f(-20, -5));
		rectIn.add(new Vector2f(20, -5));
		List<List<Vector2f>> rectIns = new ArrayList<List<Vector2f>>();
		rectIns.add(rectIn);
		final Shape emptyRectangle = ShapeCreator.create(rectOut, rectIns);
		
		List<Vector2f> special1Outline = ShapeOutlineFactory.createRoundedRectangle(50, 40, 10, 3);
		List<Vector2f> special1Cutout1 = ShapeOutlineFactory.createRoundedRectangle(20, 30, 5, 3);

		List<List<Vector2f>> special1Cutouts = new ArrayList<List<Vector2f>>(2);
		special1Cutouts.add(special1Cutout1);
		
		final Shape manual1 = ShapeCreator.create(special1Outline, special1Cutouts);

		List<Vector2f> special2Outline = ShapeOutlineFactory.createCircle(30);
		List<Vector2f> special2Cutout1 = ShapeOutlineFactory.createCircle(10, 10);
		List<Vector2f> special2Cutout2 = ShapeOutlineFactory.createCircle(10, 10);
		for(Vector2f p: special2Cutout1) {
			p.x -= 15;
		}
		for(Vector2f p: special2Cutout2) {
			p.x += 15;
		}
		List<List<Vector2f>> special2Cutouts = new ArrayList<List<Vector2f>>(2);
		special2Cutouts.add(special2Cutout1);
		special2Cutouts.add(special2Cutout2);
		
		final Shape manual2 = ShapeCreator.create(special2Outline, special2Cutouts);

		
		final Image gradient = canvas.getImageFactory().createBufferedImage(256, 256, BufferTypes.RGBA_4Byte);
		
		final Color4f white = new Color4f(1,1,1,1);
		final Color4f red = new Color4f(1,0,0,1);
				
		ColorGradientFactory gradientFactory = new ColorGradientFactory();
		gradientFactory.addSourceSegment(50, 0, 50, 255, 150, new Color4f(1, 0, 0, 1));
		gradientFactory.addSourceSegment(205, 0, 205, 255,150, new Color4f(0, 0, 1, 1));
		gradientFactory.drawGradient(gradient);

		final Font fontNormal = new Font("SansSerif", Font.PLAIN, 24);
		final Font fontSmall = new Font("SansSerif", Font.PLAIN, 16);
		
		System.out.println("starting demo");
		canvas.setGraphicsListener(new GraphicsListener() {
			long start = System.nanoTime();
			long currTime = start;
			long count = 0;
			long fps = 0;
	
			float angle = 0;

			public void draw(View view, Graphics renderer) {
				long elapsedTime = System.nanoTime() - currTime;
				currTime += elapsedTime;
	
				angle += elapsedTime / 20000000f;
				TextureMapper.mapTexture(roundedRectangle, gradient, 128, 128, angle, 2f);
				TextureMapper.mapTexture(bscircle, animLeftImage, 80, 30, angle, 1);

				renderer.clear();
				
				renderer.setColor(white);

				renderer.translate(-50, -50);
				renderer.setFont(fontNormal);
				renderer.drawText("Shape Demo");

				renderer.setFont(fontSmall);
				renderer.clearTransformation();
				renderer.translate(-100, -100);
				renderer.drawText("Common Shapes");
				
				renderer.clearTransformation();
				renderer.translate(-100, -150);
				rectangle.fillArea(renderer, false, false);
				renderer.translate(-100, 0);
				roundedRectangle.fillArea(renderer, false, false);
				renderer.translate(-100, 0);
				circle.fillArea(renderer, false, false);
				renderer.translate(-100, 0);
				circularArc.fillArea(renderer, false, false);
				renderer.translate(-100, 0);
				ellipse.fillArea(renderer, false, false);
				renderer.translate(-100, 0);
				ellipsoidalArc.fillArea(renderer, false, false);

				renderer.clearTransformation();
				renderer.translate(-100, -230);
				renderer.drawTriangles(rectangle.getTriangles(), false);
				renderer.translate(-100, 0);
				renderer.drawTriangles(roundedRectangle.getTriangles(), false);
				renderer.translate(-100, 0);
				renderer.drawTriangles(circle.getTriangles(), false);
				renderer.translate(-100, 0);
				renderer.drawTriangles(circularArc.getTriangles(), false);
				renderer.translate(-100, 0);
				renderer.drawTriangles(ellipse.getTriangles(), false);
				renderer.translate(-100, 0);
				renderer.drawTriangles(ellipsoidalArc.getTriangles(), false);
				
				renderer.clearTransformation();
				renderer.translate(-100, -350);
				renderer.drawText("Manually Created Shapes");

				renderer.clearTransformation();
				renderer.translate(-100, -400);
				emptyRectangle.fillArea(renderer, false, false);
				renderer.translate(-100, 0);
				manual1.fillArea(renderer, false, false);
				renderer.translate(-100, 0);
				manual2.fillArea(renderer, false, false);

				renderer.clearTransformation();
				renderer.translate(-100, -480);
				renderer.drawTriangles(emptyRectangle.getTriangles(), false);
				renderer.translate(-100, 0);
				renderer.drawTriangles(manual1.getTriangles(), false);
				renderer.translate(-100, 0);
				renderer.drawTriangles(manual2.getTriangles(), false);

				renderer.clearTransformation();
				renderer.translate(-500, -350);
				renderer.drawText("Shape Texturing");

				renderer.clearTransformation();
				renderer.translate(-500, -400);
				bscircle.fillArea(renderer, false, true);
				renderer.translate(-100, 0);
				roundedRectangle.fillArea(renderer, false, true);
		
				// draw frames per second
				renderer.setColor(red);
				renderer.clearTransformation();
				renderer.translate(-650, -580);
				renderer.drawText("FPS:" + fps);
				
				// calculate frames per second every second
				count++;
				if(currTime - start > 1000000000) {
					start = currTime;
					fps = count;
					count = 0;
					System.out.println(fps);
				}
				
			}

			public void init(View view, Graphics renderer) {
				System.out.println("init");
				view.setCamera(400, 300, 0);
				view.setSize(800, 600);
				gradient.updateCache();
			}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {	}
			
		});
	
		// Cleaning up
		System.gc();
		
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
			
			canvas.draw();
		}
	}
}
