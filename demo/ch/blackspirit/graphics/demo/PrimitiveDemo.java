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

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Keyboard;
import net.java.games.input.Component.Identifier.Key;
import net.java.games.input.Controller.Type;
import ch.blackspirit.graphics.CanvasFactory;
import ch.blackspirit.graphics.DisplayMode;
import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.GraphicsContext;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.RealtimeCanvas;
import ch.blackspirit.graphics.View;
import ch.blackspirit.graphics.WindowListener;
import ch.blackspirit.graphics.shape.Line;
import ch.blackspirit.graphics.shape.Triangle;

/**
 * @author Markus Koller
 */
public class PrimitiveDemo  {
	
	RealtimeCanvas canvas;
	
	public static void main(String []args) throws IOException {
		PrimitiveDemo demo = new PrimitiveDemo();
		demo.start();
	}
	private void start() {
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
		DisplayMode mode = factory.getDisplayMode(800, 600);
		if(mode != null) {
			canvas = factory.createRealtimeCanvasFullscreen(mode);
		} else {
			canvas = factory.createRealtimeCanvasFullscreen();
		}
		canvas.setVSync(true);
		canvas.addWindowListener(WindowListener.EXIT_ON_CLOSE);

		final Font font24 = new Font("SansSerif", Font.PLAIN, 24);
		final Font font16 = new Font("SansSerif", Font.PLAIN, 16);
	
		final Color4f white = new Color4f(1,1,1,1);
		final Color4f red = new Color4f(1,0,0,1);
				
		// Cleaning up
		System.gc();
		
		// Starting the rendering
		// Generate random set of triangles with random color
		final Random random = new Random();
		
		final int pointCount = 1000;
		final ArrayList<Vector2f> points = new ArrayList<Vector2f>();

		final int lineCount = 200;
		final ArrayList<Line> lines = new ArrayList<Line>();
		final Line[] lineA = new Line[lineCount];

		final int triangleCount = 100;
		final ArrayList<Triangle> triangles = new ArrayList<Triangle>(triangleCount);
		final Triangle[] triangleA = new Triangle[triangleCount];
		
		canvas.setGraphicsListener(new GraphicsListener() {
			long start = System.nanoTime();
			long currTime = start;
			long count = 0;
			long fps = 0;

			int alphaC = 0;

			public void draw(View view, Graphics renderer) {
				long elapsedTime = System.nanoTime() - currTime;
				currTime += elapsedTime;

				renderer.clear();

				renderer.clearTransformation();
				renderer.setFont(font24);
				renderer.setColor(white);
				renderer.translate(-50, -50);
				renderer.drawText("Primitive Demo");

				// Random Points
				float randomPointsX1 = 50;
				float randomPointsY1 = 130;
				float randomPointsX2 = 250;
				float randomPointsY2 = 230;
				renderer.setFont(font16);
				renderer.clearTransformation();
				renderer.translate(-randomPointsX1, -randomPointsY1 + 30);
				renderer.drawText("" + pointCount + " Random Points");
				renderer.clearTransformation();
				float randomPointsWidth = randomPointsX2 - randomPointsX1;
				float randomPointsHeight = randomPointsY2 - randomPointsY1;
				Vector2f point;
				if(points.size() == pointCount) {
					point = points.remove(0);
				} else {
					point = new Vector2f();
				}
				points.add(point);
				point.x = random.nextFloat() * randomPointsWidth + randomPointsX1;
				point.y = random.nextFloat() * randomPointsHeight + randomPointsY1;
				
				for(int i = 0; i < points.size(); i++) {
					Vector2f p = points.get(i);
					renderer.drawPoint(p.x, p.y);
				}
				
				// Random Lines
				float randomLinesX1 = 50;
				float randomLinesY1 = 330;
				float randomLinesX2 = 250;
				float randomLinesY2 = 530;
				renderer.setFont(font16);
				renderer.clearTransformation();
				renderer.translate(-randomLinesX1, -randomLinesY1 + 30);
				renderer.setColor(white);
				renderer.drawText("" + lineCount + " Random Lines");
				renderer.clearTransformation();
				float randomLinesWidth = randomLinesX2 - randomLinesX1;
				float randomLinesHeight = randomLinesY2 - randomLinesY1;
				Line line;
				Color4f lineColor = null;
				if(lines.size() == lineCount) {
					line = lines.remove(0);
					lineColor = line.getColor(0);
				} else {
					line = new Line();
					lineColor = new Color4f();
					line.setColor(0, lineColor);
					line.setColor(1, lineColor);
				}
				lines.add(line);
				for(int i = 0; i < 2; i++) {
					line.getPoint(i).x = random.nextFloat() * randomLinesWidth + randomLinesX1;
					line.getPoint(i).y = random.nextFloat() * randomLinesHeight + randomLinesY1;
				}
				float linealpha = 1;
				if(alphaC == 5) linealpha = random.nextFloat();
				lineColor.set(random.nextFloat(), random.nextFloat(), random.nextFloat(), linealpha);

				lines.toArray(lineA);
				renderer.drawLines(lineA, true);
				
				// Random Triangles
				float randomTriangleX1 = 300;
				float randomTriangleY1 = 130;
				float randomTriangleX2 = 700;
				float randomTriangleY2 = 530;
				float randomTriangleWidth = randomTriangleX2 - randomTriangleX1;
				float randomTriangleHeight = randomTriangleY2 - randomTriangleY1;
				// Update random triangles
				alphaC++;
				Triangle triangle = null;
				Color4f triangleColor = null;
				if(triangles.size() == triangleCount) {
					triangle = triangles.remove(0);
					triangleColor = triangle.getColor(0);
				} else {
					triangle = new Triangle();
					triangleColor = new Color4f();
					triangle.setColor(0, triangleColor);
					triangle.setColor(1, triangleColor);
					triangle.setColor(2, triangleColor);
				}
				float alpha = 1;
				if(alphaC == 5){
					alpha = random.nextFloat();
					alphaC = 0;
				}
				triangleColor.set(random.nextFloat(), random.nextFloat(), random.nextFloat(), alpha);
				for(int i = 0; i < 3; i++) {
					triangle.getPoint(i).x = random.nextFloat() * randomTriangleWidth + randomTriangleX1;
					triangle.getPoint(i).y = random.nextFloat() * randomTriangleHeight + randomTriangleY1;
				}
				triangles.add(triangle);
				
				// Triangles
				renderer.setColor(white);

				renderer.setFont(font16);
				renderer.translate(-randomTriangleX1, -randomTriangleY1 + 30);
				renderer.drawText("" + triangleCount + " Random Triangles");
				
				renderer.clearTransformation();
				
				triangles.toArray(triangleA);
				renderer.fillTriangles(triangleA, true);
				
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
				view.setSize(800, 600);
				view.setCamera(400, 300, 0);
			}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		
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
