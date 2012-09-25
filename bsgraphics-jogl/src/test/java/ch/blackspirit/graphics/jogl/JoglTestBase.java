package ch.blackspirit.graphics.jogl;

import org.junit.After;
import org.junit.Before;

import ch.blackspirit.graphics.GraphicsContext;
import ch.blackspirit.graphics.ImageFactory;
import ch.blackspirit.graphics.WindowListener;

public class JoglTestBase {
	private static final boolean VIEW_TESTS = false;
	
	protected GraphicsContext context;
	protected ImageFactory imageFactory;
	private ch.blackspirit.graphics.RealtimeCanvas canvas;
	private boolean finish;
	
	@Before
	public void setup() {
		finish = !(Boolean.valueOf(System.getProperty("VIEW_TESTS", String.valueOf(VIEW_TESTS))));
		CanvasProperties properties = new CanvasProperties();
		properties.setDebugGL(true);
		canvas = new CanvasFactory().createRealtimeCanvasWindow(800, 600);
//		canvas = new CanvasFactory().createRealtimeCanvasFullscreen();
		context = canvas;
		imageFactory = canvas.getImageFactory();
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
		try {
			while (!finish) context.draw();;
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
		if (context != null) context.dispose();
	}
}
