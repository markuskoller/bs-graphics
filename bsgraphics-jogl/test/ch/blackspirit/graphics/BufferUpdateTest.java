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
package ch.blackspirit.graphics;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.vecmath.Color4f;

public class BufferUpdateTest  {
	private final static int WIDTH = 800;
	private final static int HEIGHT = 600;
	
	RealtimeCanvas canvas;
	
	public static void main(String []args) throws IOException {
		BufferUpdateTest demo = new BufferUpdateTest();
		demo.start();
	}
	
	public void start() throws IOException {
		CanvasFactory factory = new ch.blackspirit.graphics.jogl.CanvasFactory();

		// Create a fullscreen realtime canvas using the current display mode.
//		DisplayMode mode = factory.getDisplayMode(WIDTH, HEIGHT);
//		if(mode != null) {
//			canvas = factory.createRealtimeCanvasFullscreen(mode);
//		} else {
//			canvas = factory.createRealtimeCanvasFullscreen();
//		}
		canvas = factory.createRealtimeCanvasWindow(WIDTH, HEIGHT);
		
		// Add Escape and Q as quitting keys
		Toolkit t = Toolkit.getDefaultToolkit();
		t.addAWTEventListener(new AWTEventListener() {
				long lastVSyncChange = 0;
				public void eventDispatched(AWTEvent event) {
					KeyEvent ke = (KeyEvent)event;
					if(ke.getKeyCode() == KeyEvent.VK_S) {
						long time = System.currentTimeMillis();
						if(time - lastVSyncChange > 1000) {
							canvas.setVSync(!canvas.getVSync());
							lastVSyncChange = time;
						}
					}
					if(ke.getKeyCode() == KeyEvent.VK_ESCAPE ||
							ke.getKeyCode() == KeyEvent.VK_Q) {
						canvas.dispose();
						System.exit(0);
					}
				}
			}, AWTEvent.KEY_EVENT_MASK);

		canvas.setVSync(true);
		canvas.addWindowListener(WindowListener.EXIT_ON_CLOSE);
		canvas.setWindowTitle("Image Demo");

		final Image dark = canvas.getImageFactory().createBufferedImage(256, 256, true);
		
		ResourceManager rm = canvas.getResourceManager();
		rm.cacheImage(dark);

		GraphicsContext imageContext = canvas.createImageGraphicsContext(dark);
		

		
		canvas.setGraphicsListener(new GraphicsListener() {
			long start = System.nanoTime();
			long currTime = start;
			long count = 0;
			long fps = 0;
	
			public void draw(View view, Graphics renderer) {
				long elapsedTime = System.nanoTime() - currTime;
				currTime += elapsedTime;

				renderer.clear();
				
				// Draw darkness
				renderer.clearTransformation();
				renderer.drawImage(dark, 256, 256);
				
				// draw frames per second
//				renderer.setColor(red);
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
				view.setCamera(400, 300, 0);
				view.setSize(800, 600);
			}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {	}
		});
		
		// Cleaning up
		System.gc();

		imageContext.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.setClearColor(new Color4f(1.0f,.0f,.0f, 1f));
				graphics.clear();
				graphics.fillTriangle(80, 80, 180, 80, 130, 180);
			}
			public void init(View view, Graphics renderer) {
				view.setSize(256, 256);
				view.setCamera(0, 0, 0);
			}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		imageContext.draw();
//		dark.updateBuffer();
//		dark.updateBuffer(50, 50, 100, 100);
		dark.updateBuffer(1,1, 255, 255);
		imageContext.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.setClearColor(new Color4f(.0f,.0f,.0f, 1f));
				graphics.clear();
			}
			public void init(View view, Graphics renderer) {
				view.setCamera(0, 0, 0);
				view.setSize(256, 256);
			}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		imageContext.draw();
		dark.updateCache();
		
		while(true) {
//			dark.updateBuffer();
			canvas.draw();
		}
	}
}
