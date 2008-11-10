package ch.blackspirit.graphics.jogl;

import javax.vecmath.Color4f;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.GraphicsContext;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.View;
import ch.blackspirit.graphics.WindowListener;

public class TextTest {
	private static final boolean VIEW_TESTS = true;
	
	private GraphicsContext context;
	private ch.blackspirit.graphics.RealtimeCanvas canvas;
	private boolean finish;
	
	@Before
	public void setup() {
		finish = !VIEW_TESTS;
		canvas = new CanvasFactory().createRealtimeCanvasWindow(800, 600);
		context = canvas;
		canvas.addWindowListener(new WindowListener() {
			public void windowActivated() {}
			public void windowClosed() {}
			public void windowClosing() {
				finish = true;
			}
			public void windowDeactivated() {}
			public void windowDeiconified() {}
			public void windowIconified() {}
		});
	}
	
	@After
	public void teardown() {
		context.dispose();
	}

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
		do {context.draw();} while (!finish);
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
		do {context.draw();} while (!finish);
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
		do {context.draw();} while (!finish);
	}
}
