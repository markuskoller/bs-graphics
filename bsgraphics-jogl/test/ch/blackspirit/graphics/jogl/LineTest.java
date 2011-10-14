package ch.blackspirit.graphics.jogl;

import javax.vecmath.Color4f;

import org.junit.Test;

import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.GraphicsContext;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.View;
import ch.blackspirit.graphics.shape.Line;

public class LineTest extends JoglTestBase {
	@Test (expected=IllegalArgumentException.class)
	public void drawLineNull() {
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawLine(null, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();
	}

	@Test (expected=IllegalArgumentException.class)
	public void drawLinesNull() {
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawLines(null, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();
	}

	
	@Test
	public void drawLinesWithColor() {
		final Line[] lines = getLines(false, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawLines(lines, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();
	}
	@Test
	public void drawLines() {
		final Line[] lines = getLines(false, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawLines(lines, false);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();
	}
	@Test
	public void drawLinesWithColorAndNull() {
		final Line[] lines = getLines(true, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawLines(lines, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();
	}
	@Test
	public void drawLinesWithNull() {
		final Line[] lines = getLines(true, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawLines(lines, false);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();
	}
	@Test
	public void drawLinesWithColorNull() {
		final Line[] lines = getLines(false, true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawLines(lines, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();
	}

	@Test 
	public void drawLineWithColor() {
		final Line lines = getLine(false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawLine(lines, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();
	}
	@Test 
	public void drawLine() {
		final Line lines = getLine(false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawLine(lines, false);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();
	}
	@Test 
	public void drawLineWithColorNull() {
		final Line lines = getLine(true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawLine(lines, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		context.draw();
	}

	private Line getLine(boolean includeColorNull) {
		Line t = new Line();
		t.getPoint(0).set(0,0);
		t.getPoint(1).set(50,50);
		if(!includeColorNull) {
			setColor(t, new Color4f(0, 0, 1, 1));
		}
		return t;
	}

	private Line[] getLines(boolean includeNull, boolean includeColorNull) {
		Line[] lines = new Line[3];
		Line t1 = new Line();
		t1.getPoint(0).set(0,0);
		t1.getPoint(1).set(50,50);
		setColor(t1, new Color4f(1, 0, 0, 1));
		lines[0] = t1;
		
		if(!includeNull) {
			Line t2 = new Line();
			t2.getPoint(0).set(100,100);
			t2.getPoint(1).set(150,150);
			setColor(t2, new Color4f(0, 1, 0, 1));
			lines[1] = t2;
		}

		Line t3 = new Line();
		t3.getPoint(0).set(200,200);
		t3.getPoint(1).set(250,250);
		if(!includeColorNull) {
			setColor(t3, new Color4f(0, 0, 1, 1));
		}
		lines[2] = t3;
		return lines;
	}

	private void setColor(Line line, Color4f color) {
		for(int i = 0; i < 2; i++) {
			line.setColor(i, color);
		}
	}
}
