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
package ch.blackspirit.graphics.demo;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

import ch.blackspirit.graphics.DisplayMode;
import ch.blackspirit.graphics.DrawingMode;
import ch.blackspirit.graphics.Flip;
import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.GraphicsContext;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.Line;
import ch.blackspirit.graphics.RealtimeCanvas;
import ch.blackspirit.graphics.ResourceManager;
import ch.blackspirit.graphics.View;
import ch.blackspirit.graphics.WindowListener;
import ch.blackspirit.graphics.anim.Animation;
import ch.blackspirit.graphics.anim.AnimationImpl;
import ch.blackspirit.graphics.anim.Frame;
import ch.blackspirit.graphics.anim.FrameImpl;
import ch.blackspirit.graphics.jogl.BufferTypes;
import ch.blackspirit.graphics.jogl.CanvasFactory;
import ch.blackspirit.graphics.shape.Shape;
import ch.blackspirit.graphics.util.ColorGradientFactory;
import ch.blackspirit.graphics.util.ShapeCreator;
import ch.blackspirit.graphics.util.ShapeFactory;
import ch.blackspirit.graphics.util.TextureMapper;

/**
 * @author Markus Koller
 */
public class ModeChangeDemo  {
	private final static int WIDTH = 800;
	private final static int HEIGHT = 600;
	private static boolean up = false;
	private static boolean down = false;
	private static boolean left = false;
	private static boolean right = false;
	private static float posX = 380;
	private static float posY = 450;
	private static boolean toggle = false;
	private static long toggleTime = 0;

	
	// Variables for drawing animation
	Animation<Frame> walk;
	int xOffset = 0;
	int lightShineRandom1 = 0;
	int lightShineRandom2 = 0;
	int lightShineRandom3 = 0;
	int lightShineRandom4 = 0;

	
	public static void main(String []args) throws IOException {
		ModeChangeDemo demo = new ModeChangeDemo();
		demo.start();
	}
	
	public void start() throws IOException {
		CanvasFactory factory = new ch.blackspirit.graphics.jogl.CanvasFactory();

//		Configurator configurator = new Configurator(factory);
//		configurator.show();
		
		// TODO create display mode (window, fullscreen, resolution) and capability (antialiasing) switcher
		
		// Create a fullscreen realtime canvas using the current display mode.
		RealtimeCanvas tempCanvas;// = factory.createRealtimeCanvasFullscreen();
		DisplayMode mode = factory.getDisplayMode(WIDTH, HEIGHT);
//		if(mode != null) {
//			tempCanvas = factory.createRealtimeCanvasFullscreen(mode);
//		} else {
//			tempCanvas = factory.createRealtimeCanvasFullscreen();
//		}
		tempCanvas = factory.createRealtimeCanvasWindow(WIDTH, HEIGHT);
		
		
		final RealtimeCanvas canvas = tempCanvas;

		// Add Escape and Q as quitting keys
		Toolkit t = Toolkit.getDefaultToolkit();
		t.addAWTEventListener(new AWTEventListener() {
				public void eventDispatched(AWTEvent event) {
					KeyEvent ke = (KeyEvent)event;
					if(ke.getKeyCode() == KeyEvent.VK_T) {
						long time = System.currentTimeMillis();
						if(time - toggleTime > 5000) {
							toggle = true;
							toggleTime = time;
						}
					}
					if(ke.getKeyCode() == KeyEvent.VK_ESCAPE ||
							ke.getKeyCode() == KeyEvent.VK_Q) {
						canvas.dispose();
						System.exit(0);
					}
					if(ke.getKeyCode() == KeyEvent.VK_LEFT) {
						if(ke.getID() == KeyEvent.KEY_PRESSED) left = true;
						else if(ke.getID() == KeyEvent.KEY_RELEASED) left = false; 
					}
					if(ke.getKeyCode() == KeyEvent.VK_RIGHT) {
						if(ke.getID() == KeyEvent.KEY_PRESSED) right = true;
						else if(ke.getID() == KeyEvent.KEY_RELEASED) right = false; 
					}
					if(ke.getKeyCode() == KeyEvent.VK_UP) {
						if(ke.getID() == KeyEvent.KEY_PRESSED) up = true;
						else if(ke.getID() == KeyEvent.KEY_RELEASED) up = false; 
					}
					if(ke.getKeyCode() == KeyEvent.VK_DOWN) {
						if(ke.getID() == KeyEvent.KEY_PRESSED) down = true;
						else if(ke.getID() == KeyEvent.KEY_RELEASED) down = false; 
					}
				}
			}, AWTEvent.KEY_EVENT_MASK);

		canvas.setVSync(false);
//		canvas.addWindowListener(WindowListener.EXIT_ON_CLOSE);
		canvas.addWindowListener(new WindowListener() {
			public void windowActivated() {}
			public void windowClosing() {canvas.dispose(); System.exit(0); }
		    public void windowClosed() {}
			public void windowDeactivated() {}
			public void windowDeiconified() {}
			public void windowIconified() {}
		});
		canvas.setWindowTitle("Image Render Demo");

		System.out.println("created");

		
		final Image campfire = canvas.getImageFactory().createImage(
				ModeChangeDemo.class.getResource("/sprites/campfire.png"), false);
		final Image animLeftImage = canvas.getImageFactory().createImage(
				ModeChangeDemo.class.getResource("/sprites/Crono - Walk (Left) 44x68.png"), false);
		final Image animFrontImage = canvas.getImageFactory().createImage(
				ModeChangeDemo.class.getResource("/sprites/Crono - Walk (Front) 40x70.png"), false);
		final Image animBackImage = canvas.getImageFactory().createImage(
				ModeChangeDemo.class.getResource("/sprites/Crono - Walk (Back) 36x70.png"), false);
		final Image leftImage = canvas.getImageFactory().createImage(
				ModeChangeDemo.class.getResource("/sprites/Crono (Left).png"), false);
		final Image frontImage = canvas.getImageFactory().createImage(
				ModeChangeDemo.class.getResource("/sprites/Crono (Front).png"), false);
		final Image backImage = canvas.getImageFactory().createImage(
				ModeChangeDemo.class.getResource("/sprites/Crono (Back).png"), false);
		final Image grass = canvas.getImageFactory().createImage(
				ModeChangeDemo.class.getResource("/sprites/grass.png"), false);
		
		final Shape circle = ShapeFactory.createCircle(30, 25);
		final Shape circularArc = ShapeFactory.createCircularArc(30, -135, 135, 25);
//		final Shape ellipse = ShapeFactory.createEllipse(75, 25, 25);
		final Shape ellipse = ShapeFactory.createRoundedRectangle(120, 70, 25);
		final Shape ellipsoidalArc = ShapeFactory.createEllipsoidalArc(75, 25, 45, 315, 25);
//		final Shape rectangle = ShapeFactory.createRectangle(75, 25, false, false);
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
		final Shape rectangle = ShapeCreator.create(rectOut, rectIns);
		
		final Image dark = canvas.getImageFactory().createImage(128, 128, true);
		final Image darktest = canvas.getImageFactory().createImage(128, 128, true);
//		final Image dark = canvas.getImageFactory().createBufferedImage(128, 128, BufferTypes.RGBA_4Byte);
		final Image gradient = canvas.getImageFactory().createBufferedImage(256, 256, BufferTypes.RGBA_4Byte);
		final Image light = canvas.getImageFactory().createBufferedImage(128, 128, BufferTypes.RGBA_4Byte);
		
		ResourceManager rm = canvas.getResourceManager();
		rm.cacheImage(dark);
		rm.cacheImage(campfire);
		rm.cacheImage(animLeftImage);
		rm.cacheImage(animFrontImage);
		rm.cacheImage(animBackImage);
		rm.cacheImage(leftImage);
		rm.cacheImage(frontImage);
		rm.cacheImage(backImage);
		rm.cacheImage(grass);	
 
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

		final Animation<Frame> animFire = new AnimationImpl<Frame>();
		animFire.addFrame(new FrameImpl(campfire, 120000000,1, 1, 64, 80));
		animFire.addFrame(new FrameImpl(campfire, 120000000, 66, 1, 64, 80));
		animFire.addFrame(new FrameImpl(campfire, 120000000, 131, 1, 64, 80));
		animFire.addFrame(new FrameImpl(campfire, 120000000,196, 1, 64, 80));
		animFire.setRepeated(true);

//		final Color4f red = new Color4f(1,0,0,1);
		final Color4f white = new Color4f(1,1,1,1);
				
		ColorGradientFactory gradientFactory = new ColorGradientFactory();
		gradientFactory.addSourceSegment(50, 0, 50, 255, 150, new Color4f(1, 0, 0, 1));
		gradientFactory.addSourceSegment(205, 0, 205, 255,150, new Color4f(0, 0, 1, 1));
//		gradientFactory.addSourceSegment(0, 0, 0, 255, 256, new Color4f(0, 0, 0, 1));
//		gradientFactory.addSourceSegment(255, 0, 255, 255, 256, new Color4f(1, 1, 1, 1));
		gradientFactory.drawGradient(gradient);
//		gradient.setDirty();

		gradientFactory.clearSources();
		gradientFactory.addSourcePoint(light.getWidth() / 2, light.getHeight() / 2, light.getWidth() / 2f * .9f, new Color4f(0,0,0,0));
		gradientFactory.setBaseColor(new Color4f(1,1,1,1));
		gradientFactory.drawGradient(light);
//		gradientFactory.addSourceSegment(0, 0, 255, 255, new Color4f(1, 1, 1, 1));

//		rm.cacheImage(gradient);
//		rm.cacheImage(light);

		final Line[] line1 = new Line[2];
		line1[0] = new ch.blackspirit.graphics.shape.Line(new Vector2f(100, 500), new Vector2f(200, 500));
		line1[1] = new ch.blackspirit.graphics.shape.Line(new Vector2f(200, 500), new Vector2f(300, 520));

		final Line[] line2 = new Line[2];
		line2[0] = new ch.blackspirit.graphics.shape.Line(new Vector2f(100, 550), new Vector2f(200, 550));
		line2[1] = new ch.blackspirit.graphics.shape.Line(new Vector2f(200, 550), new Vector2f(300, 570));
		line2[0].setColor(0, new Color4f(1,1,1,1));
		line2[0].setColor(1, new Color4f(0,1,0,1));
		line2[1].setColor(0, new Color4f(0,1,0,1));
		line2[1].setColor(1, new Color4f(1,0,0,1));
	
		GraphicsContext imageContext = canvas.createImageGraphicsContext(dark);
		walk = animFrontStill;
		GraphicsContext testContext = canvas.createImageGraphicsContext(darktest);
		
		// Draw the light shine
		imageContext.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.setClearColor(new Color4f(.0f,.0f,.0f,.98f));
				graphics.clear();
				drawLight(graphics, walk, light, posX + xOffset, posY, lightShineRandom3);
				drawLight(graphics, animFire, light, 150 - animFire.getWidth()/2, 100 - animFire.getHeight()/2, lightShineRandom1);
				drawLight(graphics, animFire, light, 650 - animFire.getWidth()/2, 100 - animFire.getHeight()/2, lightShineRandom2);
				drawLight(graphics, animFire, light, 150 - animFire.getWidth()/2, 450 - animFire.getHeight()/2, lightShineRandom3);
				drawLight(graphics, animFire, light, 650 - animFire.getWidth()/2, 450 - animFire.getHeight()/2, lightShineRandom4);
//				graphics.clearTransformation();
//				graphics.setColor(white);
//				graphics.drawImage(darktest, 128, 128);
			}

			public void init(View view, Graphics renderer) {
				view.setCamera(0, 0, 0);
				view.setSize(256, 256);
			}

			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		testContext.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.setClearColor(new Color4f(.1f,.0f,.0f,.98f));
				graphics.clear();
			}

			public void init(View view, Graphics renderer) {
				view.setCamera(0, 0, 0);
				view.setSize(256, 256);
			}

			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
	
		
		System.out.println("starting demo");
		canvas.setGraphicsListener(new GraphicsListener() {
			// Variables for fire simulation
			Random random = new Random();
			long lightShineTime = 0;

			long start = System.nanoTime();
			long currTime = start;
			long count = 0;
			long fps = 0;
	
			Flip flip = Flip.NONE;
			float angle = 0;

			public void draw(View view, Graphics renderer) {
				long elapsedTime = System.nanoTime() - currTime;
				currTime += elapsedTime;
				lightShineTime += elapsedTime;
	
				angle += elapsedTime / 20000000f;
				TextureMapper.mapTexture(circularArc, gradient, 128, 128, angle, 2f);
				TextureMapper.mapTexture(circle, animLeftImage, 80, 30, angle, 1);

				// clear
				renderer.clear();
				
				// update animation
				animLeft.update(elapsedTime);
				animFront.update(elapsedTime);
				animBack.update(elapsedTime);
				animFire.update(elapsedTime);

				renderer.setColor(white);

				for(int x = 0; x <= 800; x+=grass.getWidth() * 2) {
					for(int y = 0; y <= 600; y+=grass.getHeight() * 2) { 
						renderer.translate(-x, -y);
						renderer.drawImage(grass, grass.getWidth() * 2, grass.getHeight() * 2);
						renderer.clearTransformation();
					}
				}

				// Walking
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
				renderer.translate(-(posX + xOffset), -posY);
				walk.draw(renderer, walk.getWidth(), walk.getHeight(), flip);
				renderer.clearTransformation();


//				textRenderer.draw("Stencil Demo", 200, 370, 2);
				
				// Fires
				drawFire(renderer, animFire, light, 150, 100, lightShineRandom1);
				drawFire(renderer, animFire, light, 650, 100, lightShineRandom2);
				drawFire(renderer, animFire, light, 150, 450, lightShineRandom3);
				drawFire(renderer, animFire, light, 650, 450, lightShineRandom4);


				renderer.translate(-100, -100);
//				renderer.scale(4, 4);
				renderer.drawText("Scene Demo");

				
				// Draw darkness
				renderer.clearTransformation();
				renderer.drawImage(dark, 1024, 1024);
				
				
				// Makes it all go wrong!!
				renderer.setColor(white);
				
				renderer.translate(-100, -100);
				renderer.setColor(new Color4f(1,1,1,.4f));
//				renderer.setColor(red);
				rectangle.fillArea(renderer, false, false);
				renderer.translate(-100, 0);
//				renderer.setColor(white);
				circle.fillArea(renderer, true, true);
//				renderer.setColor(red);
				renderer.translate(-100, 0);
//				renderer.setColor(white);
				circularArc.fillArea(renderer, true, true);
//				renderer.setColor(red);
				renderer.translate(-100, 0);
				ellipse.fillArea(renderer, false, false);
//				ellipse.drawLines(renderer, false);
				renderer.translate(-100, 0);
				ellipsoidalArc.fillArea(renderer, false, false);
				
				renderer.setColor(white);
				renderer.translate(400, -100);
				renderer.drawImage(gradient, gradient.getWidth(), gradient.getHeight());
				
				renderer.clearTransformation();
				renderer.drawLines(line1, false);
				renderer.drawLines(line2, true);
				
				// draw frames per second
//				textRenderer.draw("FPS:" + fps, 650, 580, 1);
							
				// calculate frames per second every second
				count++;
				if(currTime - start > 1000000000) {
					start = currTime;
					fps = count;
					count = 0;
					System.out.println(fps);
				}
				
				// Simulate flickering of light
				if(lightShineTime > 120000000) {
					lightShineRandom1 = random.nextInt(10);
					lightShineRandom2 = random.nextInt(10);
					lightShineRandom3 = random.nextInt(10);
					lightShineRandom4 = random.nextInt(10);
					lightShineTime = 0;
				}

			}

			public void init(View view, Graphics renderer) {
				view.setCamera(400, 300, 0);
				view.setSize(800, 600);
				gradient.updateCache();
			}

			public void sizeChanged(GraphicsContext graphicsContext, View view) {	}
			
		});
		

	
		// Cleaning up
		System.gc();
		while(true) {
			if(toggle) {
				if(canvas.isFullscreen()) {
					canvas.setWindow(WIDTH, HEIGHT);
				} else {
					if(mode != null) {
						canvas.setFullscreen(mode);
					} else {
						canvas.setFullscreen();
					}
				}
				toggle = false;
			}
//			testContext.draw();
			imageContext.draw();
			canvas.draw();
		}
	}
	public static void drawLight(Graphics graphics, final Animation<Frame> anim, final Image light, final float x, final float y, int lightShine) {
		final int shineRadX = 128 + lightShine;
		final int shineRadY = 96 + lightShine;
		final float lightPosX = x + anim.getWidth()/2 - shineRadX;
		final float lightPosY = y + anim.getHeight()/4*3 - shineRadY;

		graphics.setColor(new Color4f(.0f,.0f,.0f,1f));
		graphics.setDrawingMode(DrawingMode.MULTIPLY);
//		graphics.setColorMask(false, false, false, true);
		graphics.translate(-lightPosX/4, -lightPosY/4);
		graphics.drawImage(light,shineRadX*2/4, shineRadY*2/4);
		graphics.clearTransformation();
		graphics.setDrawingMode(DrawingMode.ALPHA_BLEND);
//		graphics.setColorMask(true, true, true, true);

	}
	
	public static void drawFire(Graphics renderer, final Animation<Frame> animFire, final Image light, final float fireX, final float fireY, final int lightShine) {
		// Draw the fire
		renderer.setColor(new Color4f(1f,1f,1f,1f));
		
		renderer.translate(-(fireX - animFire.getWidth()/2), -(fireY - animFire.getHeight()/2));
		animFire.draw(renderer, animFire.getWidth(), animFire.getHeight());			
		renderer.clearTransformation();
	}

}
