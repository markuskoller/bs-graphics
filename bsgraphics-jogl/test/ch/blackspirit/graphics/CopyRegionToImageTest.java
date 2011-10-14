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

public class CopyRegionToImageTest  {
	private final static int WIDTH = 800;
	private final static int HEIGHT = 600;
	
	RealtimeCanvas canvas;
	
	public static void main(String []args) throws IOException {
		CopyRegionToImageTest demo = new CopyRegionToImageTest();
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

		final Image dark = canvas.getImageFactory().createImage(512, 512, true);
		
		ResourceManager rm = canvas.getResourceManager();
		rm.cacheImage(dark);

		canvas.setGraphicsListener(new GraphicsListener() {
			long start = System.nanoTime();
			long currTime = start;
			long count = 0;
			long fps = 0;
	
			public void draw(View view, Graphics renderer) {
				long elapsedTime = System.nanoTime() - currTime;
				currTime += elapsedTime;

				renderer.clearTransformation();
				renderer.setClearColor(new Color4f(0,1,0,1));
				renderer.clear();
				
				// Draw darkness
				
				renderer.fillTriangle(100, 100, 200, 100, 150, 200);
				renderer.copyToImage(dark, 100, 100, 100, 100);
				
				renderer.setColor(new Color4f(1,0,0,1));
//				renderer.drawLine(201, 100, 201, 200);
				renderer.fillTriangle(201, 100, 501, 100, 201, 400);
				renderer.setColor(new Color4f(1,1,1,1));

				renderer.translate(-202, -100);
				renderer.drawImage(dark, 512, 512);

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
		
		while(true) {
			canvas.draw();
		}
	}
}
