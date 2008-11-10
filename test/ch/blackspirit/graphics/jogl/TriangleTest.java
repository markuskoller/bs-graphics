package ch.blackspirit.graphics.jogl;

import java.io.IOException;

import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.GraphicsContext;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.View;
import ch.blackspirit.graphics.WindowListener;
import ch.blackspirit.graphics.shape.Triangle;

public class TriangleTest {
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

	private Triangle getTriangle(boolean includeColorNull, boolean includeTexCoordNull) {
		Triangle t = new Triangle();
		t.getPoint(0).set(0,0);
		t.getPoint(1).set(50,50);
		t.getPoint(2).set(0,50);
		if(!includeColorNull) {
			setColor(t, new Color4f(0, 1, 0, 1));
		}
		if(!includeTexCoordNull) {
			setTexCoords(t);
		}
		return t;
	}

	private Triangle[] getTriangles(boolean includeNull, boolean includeColorNull, boolean includeTexCoordNull) {
		Triangle[] triangles = new Triangle[3];
		Triangle t1 = new Triangle();
		t1.getPoint(0).set(0,0);
		t1.getPoint(1).set(50,50);
		t1.getPoint(2).set(0,50);
		setColor(t1, new Color4f(1, 0, 0, 1));
		setTexCoords(t1);
		triangles[0] = t1;
		
		if(!includeNull) {
			Triangle t2 = new Triangle();
			t2.getPoint(0).set(100,100);
			t2.getPoint(1).set(150,150);
			t2.getPoint(2).set(100,150);
			setColor(t2, new Color4f(0, 1, 0, 1));
			setTexCoords(t2);
			triangles[1] = t2;
		}

		Triangle t3 = new Triangle();
		t3.getPoint(0).set(200,200);
		t3.getPoint(1).set(250,250);
		t3.getPoint(2).set(200,250);
		if(!includeColorNull) {
			setColor(t3, new Color4f(0, 0, 1, 1));
		}
		if(!includeTexCoordNull) {
			setTexCoords(t3);
		}
		
		triangles[2] = t3;
		return triangles;
	}

	private void setColor(Triangle triangle, Color4f color) {
		for(int i = 0; i < 3; i++) {
			triangle.setColor(i, color);
		}
	}
	private void setTexCoords(Triangle triangle) {
		triangle.setTextureCoordinate(0, new Vector2f(0, 0));
		triangle.setTextureCoordinate(1, new Vector2f(79, 63));
		triangle.setTextureCoordinate(2, new Vector2f(0, 63));
	}
	
	@Test
	public void fillTrianglesWithColor() {
		final Triangle[] triangles = getTriangles(false, false, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangles(triangles, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test
	public void fillTriangles() {
		final Triangle[] triangles = getTriangles(false, false, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangles(triangles, false);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test
	public void fillTrianglesWithColorAndNull() {
		final Triangle[] triangles = getTriangles(true, false, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangles(triangles, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test
	public void fillTrianglesWithNull() {
		final Triangle[] triangles = getTriangles(true, false, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangles(triangles, false);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test
	public void fillTrianglesWithColorNull() {
		final Triangle[] triangles = getTriangles(false, true, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangles(triangles, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	
	@Test
	public void drawTrianglesWithColor() {
		final Triangle[] triangles = getTriangles(false, false, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawTriangles(triangles, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test
	public void drawTriangles() {
		final Triangle[] triangles = getTriangles(false, false, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawTriangles(triangles, false);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test
	public void drawTrianglesWithColorAndNull() {
		final Triangle[] triangles = getTriangles(true, false, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawTriangles(triangles, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test
	public void drawTrianglesWithNull() {
		final Triangle[] triangles = getTriangles(true, false, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawTriangles(triangles, false);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test
	public void drawTrianglesWithColorNull() {
		final Triangle[] triangles = getTriangles(false, true, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawTriangles(triangles, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}

	@Test 
	public void fillTriangleWithColor() {
		final Triangle triangle = getTriangle(false, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangle(triangle, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test 
	public void fillTriangle() {
		final Triangle triangle = getTriangle(false, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangle(triangle, false);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test 
	public void fillTriangleWithColorNull() {
		final Triangle triangle = getTriangle(true, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangle(triangle, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	
	
	@Test 
	public void drawTriangleWithColor() {
		final Triangle triangle = getTriangle(false, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawTriangle(triangle, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test 
	public void drawTriangle() {
		final Triangle triangle = getTriangle(false, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawTriangle(triangle, false);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test 
	public void drawTriangleWithColorNull() {
		final Triangle triangle = getTriangle(true, false);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.drawTriangle(triangle, true);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	
	// TODO Test texturing
	
	
	@Test
	public void fillTexturedTrianglesWithColor() throws IOException {
		final Triangle[] triangles = getTriangles(false, false, false);
		final ch.blackspirit.graphics.Image image = canvas.getImageFactory().createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangles(triangles, true, image);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test
	public void fillTexturedTriangles() throws IOException {
		final Triangle[] triangles = getTriangles(false, false, false);
		final ch.blackspirit.graphics.Image image = canvas.getImageFactory().createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangles(triangles, false, image);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test
	public void fillTexturedTrianglesWithColorAndNull() throws IOException {
		final Triangle[] triangles = getTriangles(true, false, false);
		final ch.blackspirit.graphics.Image image = canvas.getImageFactory().createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangles(triangles, true, image);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test
	public void fillTexturedTrianglesWithNull() throws IOException {
		final Triangle[] triangles = getTriangles(true, false, false);
		final ch.blackspirit.graphics.Image image = canvas.getImageFactory().createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangles(triangles, false, image);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test
	public void fillTexturedTrianglesWithColorNull() throws IOException {
		final Triangle[] triangles = getTriangles(false, true, false);
		final ch.blackspirit.graphics.Image image = canvas.getImageFactory().createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangles(triangles, true, image);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test 
	public void fillTexturedTriangleWithColor() throws IOException, InterruptedException {
		final Triangle triangle = getTriangle(false, false);
		final ch.blackspirit.graphics.Image image = canvas.getImageFactory().createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangle(triangle, true, image);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
		Thread.sleep(1000);
	}
	@Test 
	public void fillTexturedTriangle() throws IOException {
		final Triangle triangle = getTriangle(false, false);
		final ch.blackspirit.graphics.Image image = canvas.getImageFactory().createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangle(triangle, false, image);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test 
	public void fillTexturedTriangleWithColorNull() throws IOException {
		final Triangle triangle = getTriangle(true, false);
		final ch.blackspirit.graphics.Image image = canvas.getImageFactory().createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangle(triangle, true, image);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void fillTexturedTriangleWithTexCoordNull() throws IOException {
		final Triangle triangle = getTriangle(false, true);
		final ch.blackspirit.graphics.Image image = canvas.getImageFactory().createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangle(triangle, false, image);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test (expected=IllegalArgumentException.class)
	public void fillTexturedTriangleWithColorAndTexCoordNull() throws IOException {
		final Triangle triangle = getTriangle(false, true);
		final ch.blackspirit.graphics.Image image = canvas.getImageFactory().createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangle(triangle, true, image);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test (expected=IllegalArgumentException.class)
	public void fillTexturedTrianglesWithTexCoordNull() throws IOException {
		final Triangle[] triangles = getTriangles(false, false, true);
		final ch.blackspirit.graphics.Image image = canvas.getImageFactory().createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangles(triangles, false, image);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
	@Test (expected=IllegalArgumentException.class)
	public void fillTexturedTrianglesWithColorAndTexCoordNull() throws IOException {
		final Triangle[] triangles = getTriangles(false, false, true);
		final ch.blackspirit.graphics.Image image = canvas.getImageFactory().createImage(this.getClass().getResource("/player1.png"), true);
		context.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics graphics) {
				graphics.clear();
				graphics.fillTriangles(triangles, true, image);
			}
			public void init(View view, Graphics renderer) {}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		do {context.draw();} while (!finish);
	}
}
