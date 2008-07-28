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

import java.awt.Dimension;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.media.Buffer;
import javax.media.ConfigureCompleteEvent;
import javax.media.Control;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.PrefetchCompleteEvent;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.ResourceUnavailableEvent;
import javax.media.ResourceUnavailableException;
import javax.media.Time;
import javax.media.control.TrackControl;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.vecmath.Color4f;

import ch.blackspirit.graphics.BufferType;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.ImageFactory;
import ch.blackspirit.graphics.jogl.BufferTypes;

/**
 * @author Markus Koller
 */
public class VideoRenderer implements ControllerListener {
	private Processor processor;
	private BSGraphicsRenderer bsGraphicsRenderer; 
	private int[] waitSync = new int[0];
	private boolean stateTransOK = true;
	private Image image;
	private ImageFactory imageFactory;
	private int width;
	private int height;
	
	public VideoRenderer(ImageFactory imageFactory) {
		this.imageFactory = imageFactory;
		bsGraphicsRenderer = new BSGraphicsRenderer();
	}
   
	public Image getCurrentImage() {
		if(bsGraphicsRenderer.isUpdated()) {
			image = bsGraphicsRenderer.getImage();
		}
		return image;
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
    public boolean isUpdated() {
    	return bsGraphicsRenderer.isUpdated();
    }
	
	public boolean open(MediaLocator ml) {
		try {
		    processor = Manager.createProcessor(ml);
		} catch (Exception ex) {
		    System.out.println("failed to create a processor for movie " + ml);
		    return false;
		}

		processor.addControllerListener(this);
		processor.configure();
		
		if ( !waitForState(Processor.Configured)) {
		    System.out.println("Failed to configure the processor");
		    return false;
		}
		
		// use processor as a player
		processor.setContentDescriptor(null);
	
		// obtain the track control
		TrackControl[] tc = processor.getTrackControls();

		if ( tc == null ) {
		    System.out.println("Failed to get the track control from processor");
		    return false;
		}

		TrackControl vtc = null;
		
		for ( int i =0; i < tc.length; i++ ) {
		    if (tc[i].getFormat() instanceof VideoFormat ) {
			vtc = tc[i];
			break;
		    }
		    
		}

		if ( vtc == null ) {
		    System.out.println("can't find video track");
		    return false;
		}
		
		// Read video format
		VideoFormat f = (VideoFormat)vtc.getFormat();
		width = (int)f.getSize().getWidth();
		height = (int)f.getSize().getHeight();

		// create images
		bsGraphicsRenderer.setFrontBuffer(imageFactory.createBufferedImage((int)f.getSize().getWidth(), (int)f.getSize().getHeight(), BufferTypes.RGB_3Byte));
		bsGraphicsRenderer.setBackBuffer(imageFactory.createBufferedImage((int)f.getSize().getWidth(), (int)f.getSize().getHeight(), BufferTypes.RGB_3Byte));
		
		try {
		    vtc.setRenderer(bsGraphicsRenderer);
		} catch ( Exception ex) {
		    ex.printStackTrace();
		    System.out.println("the processor does not support effect");
		    return false;
		}


		return true;
    }
	
	public boolean start() {
		// prefetch
		processor.prefetch();
		if ( !waitForState(Processor.Prefetched)) {
		    System.out.println("Failed to prefech the processor");
		    return false;
		}

		processor.start();
		return true;
	}

	public boolean waitForState(int state) {
		synchronized (waitSync) {
		    try {
			while ( processor.getState() != state && stateTransOK ) {
			    waitSync.wait();
			}
		    } catch (Exception ex) {}
		    
		    return stateTransOK;
		}
	}
	public void controllerUpdate(ControllerEvent evt) {
		if ( evt instanceof ConfigureCompleteEvent ||
			     evt instanceof RealizeCompleteEvent ||
			     evt instanceof PrefetchCompleteEvent ) {
		    synchronized (waitSync) {
			stateTransOK = true;
			waitSync.notifyAll();
		    }
		} else if ( evt instanceof ResourceUnavailableEvent) {
		    synchronized (waitSync) {
			stateTransOK = false;
			waitSync.notifyAll();
		    }
		} else if ( evt instanceof EndOfMediaEvent) {
			// loop
	        processor.setMediaTime(new Time(0));
		    processor.start();
		    
		    // no loop
		    // p.close();
		}	
	}
	
	public static class BSGraphicsRenderer implements javax.media.renderer.VideoRenderer {
	    private Image frontBuffer;
	    private Image backBuffer;
	    private Lock imageLock = new ReentrantLock();
	    private boolean updated = false;
	    
	    private static final String name = "J3DRenderer";
	    
	    private RGBFormat inputFormat;
	    private RGBFormat supportedRGB;
	    private Format [] supportedFormats;
	    
	    private int       inWidth = 0;
	    private int       inHeight = 0;
	    private boolean started = false;

	    public BSGraphicsRenderer() {
			supportedRGB =  new RGBFormat(null,
						      Format.NOT_SPECIFIED,
						      Format.byteArray,
						      Format.NOT_SPECIFIED,
						      24,
						      3, 2, 1,
						      3, Format.NOT_SPECIFIED,
						      Format.TRUE,
						      Format.NOT_SPECIFIED);
			
			supportedFormats = new VideoFormat[] {supportedRGB };
	    }

	    public Object[] getControls() {
	        // No controls
	        return (Object[]) new Control[0];
	    }

	    @SuppressWarnings("unchecked")
		public Object getControl(String controlType) {
	       try {
	          Class cls = Class.forName(controlType);
	          Object cs[] = getControls();
	          for (int i = 0; i < cs.length; i++) {
	             if (cls.isInstance(cs[i]))
	                return cs[i];
	          }
	          return null;
	       } catch (Exception e) {   // no such controlType or such control
	         return null;
	       }
	    }

	    public String getName() {
	        return name;
	    }
	    
	    public void open() throws ResourceUnavailableException {}

	    public void reset() {}

	    public synchronized void close() {}

	    public void start() {
	        started = true;
	    }
	    public void stop() {
	        started = false;
	    }
	    public boolean isStarted() {
	    	return started;
	    }
	    
	    public Format [] getSupportedInputFormats() {
	        return supportedFormats;
	    }

	    /**
	     * Set the data input format.
	     */
	    public Format setInputFormat(Format format) {
	        if ( format != null && format instanceof RGBFormat &&
	             format.matches(supportedRGB)) {
	            
	            inputFormat = (RGBFormat) format;
	            System.out.println(inputFormat);
	            Dimension size = inputFormat.getSize();
	            inWidth = size.width;
	            inHeight = size.height;
	            
	            return format;
	        } else
	            return null;
	    }
	    
	    public boolean isUpdated() {
	       	imageLock.lock();
	        boolean u = updated;
	        imageLock.unlock();
	        return u;
	    }
	    
	    public Image getImage() {
	    	imageLock.lock();
	    	Image image = backBuffer;
	    	backBuffer = frontBuffer;
	    	frontBuffer = image;
	    	updated = false;
	    	imageLock.unlock();
	    	return image;
	    }
	    public void setFrontBuffer(Image frontBuffer) {
			this.frontBuffer = frontBuffer;
		}
		public void setBackBuffer(Image backBuffer) {
			this.backBuffer = backBuffer;
		}

		/**
	     * Processes the data and renders it to an image
	     */
	    public int process(Buffer buffer) {
			if ( buffer.getLength() <= 0 ) 
			    return BUFFER_PROCESSED_OK;
		
			byte[] rawData =(byte[])(buffer.getData());
		
		    imageLock.lock();

		    BufferType bufferType = backBuffer.getBufferType();
		
		    if(bufferType == BufferTypes.RGB_3Byte) {
		    	// Handle a RGB_3Byte buffer efficiently
		    	byte[] data = (byte[])backBuffer.getBuffer();
		    	int width = backBuffer.getWidth();
				int ip = 0;
			    for ( int y = 0; y < inHeight; y++ ) {
					for ( int x = 0; x < inWidth; x++) {
						int indexBase = (y * width + x) * 3;
						data[indexBase + 2] = rawData[ip++];
						data[indexBase + 1] = rawData[ip++];
						data[indexBase] = rawData[ip++];
					}
				} 
		    } else if(bufferType == BufferTypes.RGBA_4Byte) {
		    	// Handle a RGBA_4Byte buffer efficiently
		    	byte[] data = (byte[])backBuffer.getBuffer();
		    	int width = backBuffer.getWidth();
				int ip = 0;
			    for ( int y = 0; y < inHeight; y++ ) {
					for ( int x = 0; x < inWidth; x++) {
						int indexBase = (y * width + x) * 4;
						data[indexBase + 2] = rawData[ip++];
						data[indexBase + 1] = rawData[ip++];
						data[indexBase] = rawData[ip++];
						data[indexBase + 3] = (byte)255;
					}
				} 
		    } else {
		    	// Handling an unknown buffer will be pretty inefficient!
				int ip = 0;
				Color4f color = new Color4f();
				color.w = 1.0f;
				float v = 1.0f / 255;
			    for ( int y = 0; y < inHeight; y++ ) {
					for ( int x = 0; x < inWidth; x++) {
						color.z = v * (int)rawData[ip++];
						color.y = v * (int)rawData[ip++];
						color.x = v * (int)rawData[ip++];
						bufferType.setColor(backBuffer, x, y, color);
					}
				} 
		    }
			
			updated = true;
		    imageLock.unlock();
		
			return BUFFER_PROCESSED_OK;
	    }

	    
	    public java.awt.Component getComponent() {
	    	return null;
	    }
	    public boolean setComponent(java.awt.Component comp) {
	        return false;
	    }
	    public void setBounds(java.awt.Rectangle rect) {}
	    public java.awt.Rectangle getBounds() {
	        return null;
	    }
	}	
}
