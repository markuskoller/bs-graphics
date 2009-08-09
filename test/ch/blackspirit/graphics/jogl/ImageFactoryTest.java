package ch.blackspirit.graphics.jogl;

import java.io.IOException;

import javax.vecmath.Color4f;

import org.junit.Assert;
import org.junit.Test;

import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.GraphicsContext;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.View;

public class ImageFactoryTest extends JoglTestBase {
	
	private static final class Listener implements GraphicsListener {
		private ch.blackspirit.graphics.Image image;
		public Listener(ch.blackspirit.graphics.Image image) {
			this.image = image;
		}
		
		public void draw(View view, Graphics graphics) {
			graphics.setClearColor(new Color4f(0, 1, 0, 0));
			graphics.clear();
			graphics.translate(-400, -300);
			graphics.drawImage(image, image.getWidth() * 8, image.getHeight() * 8);
		}
		public void init(View view, Graphics graphics) {
			view.setCameraPosition(400, 300);
		}
		public void sizeChanged(GraphicsContext graphicsContext, View view) {}
	}
	
	@Test
	public void createImageFromPNGForceAlpha() throws IOException {
		final ch.blackspirit.graphics.Image image = 
			imageFactory.createImage(getClass().getResource("/test.png"), true);
	
		// TODO add to jogl.Image a way to check OpenGL buffer type
//		Assert.assertEquals(BufferTypes.RGBA_4Byte, image.getBufferType());
		
		context.setGraphicsListener(new Listener(image));
		
		context.draw();
	}
	@Test
	public void createImageFromPNGImageDependentAlphaWith() throws IOException {
		final ch.blackspirit.graphics.Image image = 
			imageFactory.createImage(getClass().getResource("/test.png"), true);
	
		// TODO add to jogl.Image a way to check OpenGL buffer type
//		Assert.assertEquals(BufferTypes.RGBA_4Byte, image.getBufferType());
		
		context.setGraphicsListener(new Listener(image));
		
		context.draw();
	}

	@Test
	public void createImageFromPNGImageDependentAlphaWithout() throws IOException {
		final ch.blackspirit.graphics.Image image = 
			imageFactory.createImage(getClass().getResource("/test_no_alpha.png"), true);
	
		// TODO add to jogl.Image a way to check OpenGL buffer type
//		Assert.assertEquals(BufferTypes.RGBA_4Byte, image.getBufferType());
		
		context.setGraphicsListener(new Listener(image));
		
		context.draw();
	}

	@Test
	public void createImageEmptyAlphaFalse() throws IOException {
		final ch.blackspirit.graphics.Image image = 
			imageFactory.createImage(2, 2, false);
	
		// TODO add to jogl.Image a way to check OpenGL buffer type
//		Assert.assertEquals(BufferTypes.RGBA_4Byte, image.getBufferType());
		
		context.setGraphicsListener(new Listener(image));
		
		context.draw();
	}
	@Test
	public void createImageEmptyAlphaTrue() throws IOException {
		final ch.blackspirit.graphics.Image image = 
			imageFactory.createImage(2, 2, true);
	
		// TODO add to jogl.Image a way to check OpenGL buffer type
//		Assert.assertEquals(BufferTypes.RGBA_4Byte, image.getBufferType());
		
		context.setGraphicsListener(new Listener(image));
		
		context.draw();
	}
	

	@Test
	public void createBufferedImageFromPNGForceAlpha() throws IOException {
		final ch.blackspirit.graphics.Image image = 
			imageFactory.createBufferedImage(getClass().getResource("/test.png"), true);
		
		Assert.assertTrue(image.isBuffered());
		Assert.assertEquals(BufferTypes.RGBA_4Byte, image.getBufferType());
		
		context.setGraphicsListener(new Listener(image));
		
		context.draw();
	}
	@Test
	public void createBufferedImageFromPNGImageDependentAlphaWith() throws IOException {
		final ch.blackspirit.graphics.Image image = 
			imageFactory.createBufferedImage(getClass().getResource("/test.png"), false);
		
		Assert.assertTrue(image.isBuffered());
		Assert.assertEquals(BufferTypes.RGBA_4Byte, image.getBufferType());
		
		context.setGraphicsListener(new Listener(image));
		
		context.draw();
	}
	@Test
	public void createBufferedImageFromPNGImageDependentAlphaWithout() throws IOException {
		final ch.blackspirit.graphics.Image image = 
			imageFactory.createBufferedImage(getClass().getResource("/test_no_alpha.png"), false);
		
		Assert.assertTrue(image.isBuffered());
		Assert.assertEquals(BufferTypes.RGBA_4Byte, image.getBufferType());
		
		context.setGraphicsListener(new Listener(image));
		
		context.draw();
	}
	@Test
	public void createBufferedRGBImageFromPNG() throws IOException {
		final ch.blackspirit.graphics.Image image = 
			imageFactory.createBufferedImage(getClass().getResource("/test.png"), BufferTypes.RGB_3Byte);
		
		Assert.assertTrue(image.isBuffered());
		Assert.assertEquals(BufferTypes.RGB_3Byte, image.getBufferType());
		
		context.setGraphicsListener(new Listener(image));
		
		context.draw();
	}
	@Test
	public void createBufferedRGBAImageFromPNG() throws IOException {
		final ch.blackspirit.graphics.Image image = 
			imageFactory.createBufferedImage(getClass().getResource("/test_no_alpha.png"), BufferTypes.RGBA_4Byte);
		
		Assert.assertTrue(image.isBuffered());
		Assert.assertEquals(BufferTypes.RGBA_4Byte, image.getBufferType());
		
		context.setGraphicsListener(new Listener(image));
		
		context.draw();
	}

	@Test
	public void createBufferedImageEmptyWithAlpha() {
		ch.blackspirit.graphics.Image image = imageFactory.createBufferedImage(2, 2, true);
		Assert.assertTrue(image.isBuffered());
		Assert.assertEquals(BufferTypes.RGBA_4Byte, image.getBufferType());
	}
	@Test
	public void createBufferedImageEmptyWithoutAlpha() {
		ch.blackspirit.graphics.Image image = imageFactory.createBufferedImage(2, 2, false);
		Assert.assertTrue(image.isBuffered());
		Assert.assertEquals(BufferTypes.RGB_3Byte, image.getBufferType());
	}
	@Test
	public void createBufferedRGBImageEmpty() {
		ch.blackspirit.graphics.Image image = imageFactory.createBufferedImage(2, 2, BufferTypes.RGB_3Byte);
		Assert.assertTrue(image.isBuffered());
		Assert.assertEquals(BufferTypes.RGB_3Byte, image.getBufferType());
	}
	@Test
	public void createBufferedRGBAImageEmpty() {
		ch.blackspirit.graphics.Image image = imageFactory.createBufferedImage(2, 2, BufferTypes.RGBA_4Byte);
		Assert.assertTrue(image.isBuffered());
		Assert.assertEquals(BufferTypes.RGBA_4Byte, image.getBufferType());
	}
	
}