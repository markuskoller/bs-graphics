package ch.blackspirit.graphics.jogl;

import java.io.IOException;

import javax.vecmath.Color4f;

import org.junit.Test;

import ch.blackspirit.graphics.Flip;
import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.GraphicsContext;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.View;

public class ImageTest extends JoglTestBase {
	@Test
	public void drawImage() throws IOException {
		final ch.blackspirit.graphics.Image image = imageFactory.createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawImage(image, image.getWidth(), image.getHeight());
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}
	@Test
	public void drawImageFlipped() throws IOException {
		final ch.blackspirit.graphics.Image image = imageFactory.createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawImage(image, image.getWidth(), image.getHeight(), Flip.BOTH);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}
	
	@Test
	public void drawImageNotFlipped() throws IOException {
		final ch.blackspirit.graphics.Image image = imageFactory.createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawImage(image, image.getWidth(), image.getHeight(), Flip.NONE);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}

	@Test
	public void drawSubImage() throws IOException {
		final ch.blackspirit.graphics.Image image = imageFactory.createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawImage(image, image.getWidth(), image.getHeight(), 
						image.getWidth() / 4, image.getHeight() / 4, image.getWidth() / 2, image.getHeight() / 2);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}
	@Test
	public void drawSubImageFlipped() throws IOException {
		final ch.blackspirit.graphics.Image image = imageFactory.createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawImage(image, image.getWidth(), image.getHeight(), 
						image.getWidth() / 4, image.getHeight() / 4, image.getWidth() / 2, image.getHeight() / 2, Flip.BOTH);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}
	
	@Test
	public void drawSubImageNotFlipped() throws IOException {
		final ch.blackspirit.graphics.Image image = imageFactory.createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawImage(image, image.getWidth(), image.getHeight(), 
						image.getWidth() / 4, image.getHeight() / 4, image.getWidth() / 2, image.getHeight() / 2, Flip.NONE);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}
	
	@Test
	public void drawImageColored() throws IOException {
		final ch.blackspirit.graphics.Image image = imageFactory.createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.setColor(new Color4f(1, 0, 0, 1));
				graphics.drawImage(image, image.getWidth(), image.getHeight());
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}

	@Test
	public void drawSubImageColored() throws IOException {
		final ch.blackspirit.graphics.Image image = imageFactory.createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.setColor(new Color4f(1, 0, 0, 1));
				graphics.drawImage(image, image.getWidth(), image.getHeight(), 
						image.getWidth() / 4, image.getHeight() / 4, image.getWidth() / 2, image.getHeight() / 2);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void drawImageNull() throws IOException {
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawImage(null, 640, 480);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}
	@Test (expected=IllegalArgumentException.class)
	public void drawFlippedImageNull() throws IOException {
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawImage(null, 640, 480, Flip.BOTH);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}
	@Test (expected=IllegalArgumentException.class)
	public void drawSubImageNull() throws IOException {
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawImage(null, 640, 480, 0, 0, 10, 10);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}
	@Test (expected=IllegalArgumentException.class)
	public void drawFlippedSubImageNull() throws IOException {
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawImage(null, 640, 480, 0, 0, 10, 10, Flip.BOTH);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}

}
