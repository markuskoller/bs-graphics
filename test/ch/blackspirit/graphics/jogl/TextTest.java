package ch.blackspirit.graphics.jogl;

import javax.vecmath.Color4f;

import org.junit.Test;

import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.GraphicsContext;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.View;

public class TextTest extends JoglTestBase {
	@Test
	public void drawText() {
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawText("This is a test string");
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}
	@Test
	public void drawTextWithColor() {
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.setColor(new Color4f(1, 0, 0, 1));
				graphics.drawText("This is a test string");
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}
	
	@Test
	public void drawTextTranslated() {
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clearTransformation();
				graphics.translate(100, 50);
				graphics.clear();
				graphics.drawText("This is a test string");
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}

	@Test (expected=IllegalArgumentException.class)
	public void drawTextNull() {
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawText(null);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}

}
