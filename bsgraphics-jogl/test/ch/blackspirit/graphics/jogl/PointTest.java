package ch.blackspirit.graphics.jogl;

import javax.vecmath.Color4f;

import org.junit.Test;

import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.GraphicsContext;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.View;

public class PointTest extends JoglTestBase {
	@Test
	public void drawPoints() {
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawPoint(0, 0);
				graphics.drawPoint(100, 100);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}
	@Test
	public void drawPointsWithColor() {
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.setColor(new Color4f(1, 0, 0, 1));
				graphics.drawPoint(0, 0);
				graphics.setColor(new Color4f(0, 1, 0, 1));
				graphics.drawPoint(100, 100);
				graphics.setColor(new Color4f(0, 0, 1, 1));
				graphics.drawPoint(200, 200);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();	
	}
}
