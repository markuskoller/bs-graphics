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

import java.io.File;
import java.io.IOException;

import javax.media.Codec;
import javax.media.Demultiplexer;
import javax.media.MediaLocator;
import javax.media.PlugInManager;
import javax.swing.JFileChooser;
import javax.vecmath.Color4f;

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
import ch.blackspirit.graphics.jogl.CanvasFactory;

/**
 * @author Markus Koller
 */
public class VideoDemo {
	private final static int WIDTH = 800;
	private final static int HEIGHT = 600;

	RealtimeCanvas canvas;
	
	public static void main(String []args) throws IOException, InterruptedException {
		VideoDemo demo = new VideoDemo();
		demo.start();
	}
	
	public void start() throws IOException, InterruptedException {
		ControllerEnvironment controllerEnv = ControllerEnvironment.getDefaultEnvironment();
		Keyboard keyboard = null;
		for(Controller controller: controllerEnv.getControllers()) {
			if(controller.getType() == Type.KEYBOARD) {
				keyboard = (Keyboard)controller;
				break;
			}
		}

		// Getting video to show
		JFileChooser filechooser = new JFileChooser();
		filechooser.setMultiSelectionEnabled(false);
		if(filechooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return;
		File file = filechooser.getSelectedFile();
		
		CanvasFactory factory = new ch.blackspirit.graphics.jogl.CanvasFactory();

		// Create a fullscreen realtime canvas using the current display mode.
		DisplayMode mode = factory.getDisplayMode(WIDTH, HEIGHT);
		if(mode != null) {
			canvas = factory.createRealtimeCanvasFullscreen(mode);
		} else {
			canvas = factory.createRealtimeCanvasFullscreen();
		}
		
		canvas.setVSync(true);
		canvas.addWindowListener(WindowListener.EXIT_ON_CLOSE);
		canvas.setWindowTitle("Video Demo");

		
		// Registering FOBS JMF plugin
		try {
			String FFMPEG_VIDEO_NATIVE = "com.omnividea.media.codec.video.NativeDecoder"; 
		    Codec videoNative = (Codec) Class.forName(FFMPEG_VIDEO_NATIVE).newInstance();
		    PlugInManager.addPlugIn(FFMPEG_VIDEO_NATIVE,
		            videoNative.getSupportedInputFormats(),
		            videoNative.getSupportedOutputFormats(null),
		            PlugInManager.CODEC);

		    String FFMPEG_AUDIO_NATIVE = "com.omnividea.media.codec.audio.NativeDecoder";
		    Codec audioNative = (Codec) Class.forName(FFMPEG_AUDIO_NATIVE).newInstance();
		    PlugInManager.addPlugIn(FFMPEG_AUDIO_NATIVE,
		    		audioNative.getSupportedInputFormats(),
		    		audioNative.getSupportedOutputFormats(null),
		            PlugInManager.CODEC);

//		    for(Format format: audioNative.getSupportedInputFormats()) {
//		    	System.out.println(format);
//		    }
		    
//		    String FFMPEG_VIDEO = "com.omnividea.media.codec.video.JavaDecoder"; 
//		    Codec video = (Codec) Class.forName(FFMPEG_VIDEO).newInstance();
//		    PlugInManager.addPlugIn(FFMPEG_VIDEO,
//		            video.getSupportedInputFormats(),
//		            video.getSupportedOutputFormats(null),
//		            PlugInManager.CODEC);
//
//			String FFMPEG_AUDIO = "com.omnividea.media.codec.audio.JavaDecoder";
//		    Codec audio= (Codec) Class.forName(FFMPEG_AUDIO).newInstance();
//		    PlugInManager.addPlugIn(FFMPEG_AUDIO,
//		    		audio.getSupportedInputFormats(),
//		    		audio.getSupportedOutputFormats(null),
//		            PlugInManager.CODEC);

//			String MP3 = "com.sun.media.codec.audio.mp3.JavaDecoder";
//		    Codec mp3 = (Codec) Class.forName(MP3).newInstance();
//		    PlugInManager.addPlugIn(MP3,
//		            mp3.getSupportedInputFormats(),
//		            mp3.getSupportedOutputFormats(null),
//		            PlugInManager.CODEC);

			String FFMPEG_DEMUX = "com.omnividea.media.parser.video.Parser"; 
			Demultiplexer demux = (Demultiplexer) Class.forName(FFMPEG_DEMUX).newInstance();
		    PlugInManager.addPlugIn(FFMPEG_DEMUX,
		    		demux.getSupportedInputContentDescriptors(),
		    		null,
		            PlugInManager.DEMULTIPLEXER);
		    
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		// Creating video renderer
		final VideoRenderer videoRenderer = new VideoRenderer(canvas.getImageFactory());
		MediaLocator mediaLocator = new MediaLocator(file.toURI().toURL());
		if(!videoRenderer.open(mediaLocator)) System.exit(0);

		canvas.setGraphicsListener(new GraphicsListener() {
			long start = System.nanoTime();
			long currTime = start;
			long count = 0;
			long fps = 0;

			Color4f red = new Color4f(1,0,0,1);
			Color4f white = new Color4f(1,1,1,1);
			float factor = 800f / videoRenderer.getWidth();
			Image image = null;
			
			public void draw(View view, Graphics graphics) {
				long elapsedTime = System.nanoTime() - currTime;
				currTime += elapsedTime;

				graphics.clear();
				graphics.setColor(white);
				graphics.clearTransformation();
				
				// Getting current video image
				if(videoRenderer.isUpdated()) {
					image = videoRenderer.getCurrentImage();
					image.updateCache();
				}
				
				// Drawing video image
				if(image != null) {
					graphics.translate(400, videoRenderer.getHeight() * factor / 2);
					graphics.drawImage(image, image.getWidth() * factor, image.getHeight() * factor);
				}
				
				// draw frames per second
				graphics.setColor(red);
				graphics.clearTransformation();
				graphics.translate(-250, -280);
				graphics.drawText("FPS:" + fps);
							
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
			}

			public void sizeChanged(GraphicsContext canvas, View view) {}
		});
		
		if(!videoRenderer.start()) System.exit(0);
		
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
			
			// On some systems vsync blocks video decoding, so we give it some time
			Thread.sleep(10);
			
			canvas.draw();
		}
	}

}
