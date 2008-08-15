package ch.blackspirit.graphics;

import java.io.IOException;

import ch.blackspirit.graphics.jogl.BufferTypes;
import ch.blackspirit.graphics.jogl.CanvasFactory;

public class CacheUpdateTest {

	public static void main(String []args) throws IOException {
		CacheUpdateTest test = null;
		test = new CacheUpdateTest();
		test.test();
		System.exit(0);
	}
	
	static int imageWidth = 64;
	static int imageHeight = 64;
	
	public void test() throws IOException {
		CanvasFactory factory = new CanvasFactory();		
		RealtimeCanvas canvas = factory.createRealtimeCanvasWindow(800,600);
		
		final Image bw = canvas.getImageFactory().createBufferedImage(imageWidth, imageHeight, BufferTypes.RGB_3Byte);
		
		byte[] bytes = (byte[])bw.getBuffer();
		for(int x = 0; x < imageWidth; x++) {
			for(int y = 0; y < imageHeight; y++) {
				bytes[(y * imageWidth * 3) + (x * 3)] = (byte)0;
				bytes[(y * imageWidth * 3) + (x * 3) + 1] = (byte)0;
				bytes[(y * imageWidth * 3) + (x * 3) + 2] = (byte)0;
			}
		}
		
		canvas.getResourceManager().cacheImage(bw);
		bw.updateCache();
		
		System.out.println("last update");
		
		for(int i = 0; i< imageWidth; i++) {
			bytes[i * 3 + 1] = (byte)255;
		}
		for(int i = 0; i< imageHeight; i++) {
			bytes[i * imageWidth * 3 + 2] = (byte)255;
		}
		for(int i = 0; i< imageHeight; i++) {
			bytes[((imageWidth-1) * 3) + (i * imageWidth *3)] = (byte)255;
		}
		for(int i = 0; i< imageWidth; i++) {
			bytes[i * 3 + (imageWidth * 3 * (imageHeight - 1))] = (byte)255;
		}
		
//		bw.updateCache();
		bw.updateCache(0, 0, 10,10);

		System.gc();
		
        canvas.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics renderer) {
				renderer.clear();
				renderer.drawImage(bw, bw.getWidth(), bw.getHeight());
			}
			public void init(View view, Graphics renderer) {
				view.setSize(800, 600);			
			}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
        });
        
		long start = System.currentTimeMillis();
		long count = 0;

		for(int i = 0; i < 1000000; i++) {
			canvas.draw();
			
			count++;
			long current = System.currentTimeMillis();
			if(current - start > 1000) {
				start = current;
				System.out.println("FPS: " + count);
				count = 0;
			}
		}
	}
}
