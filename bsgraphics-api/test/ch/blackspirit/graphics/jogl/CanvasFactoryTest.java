package ch.blackspirit.graphics.jogl;

import javax.swing.JFrame;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import ch.blackspirit.graphics.AWTCanvas;
import ch.blackspirit.graphics.CanvasFactory;
import ch.blackspirit.graphics.DisplayMode;

public class CanvasFactoryTest {
	@Test
	public void createDisposeRealtimeWindow() {
		CanvasFactory factory = new ch.blackspirit.graphics.jogl.CanvasFactory();
		factory.createRealtimeCanvasWindow(320, 240).dispose();
	}
	@Test
	public void createDisposeRealtimeFullscreenCurrentDisplayMode() {
		CanvasFactory factory = new ch.blackspirit.graphics.jogl.CanvasFactory();
		factory.createRealtimeCanvasFullscreen().dispose();
	}
	@Ignore
	@Test
	public void createDisposeRealtimeFullscreenAllAvailableDisplayModes() {
		CanvasFactory factory = new ch.blackspirit.graphics.jogl.CanvasFactory();
		for (DisplayMode mode: factory.getDisplayModes()) {
			System.out.println(mode);
			factory.createRealtimeCanvasFullscreen(mode).dispose();
		}
	}
	
	// Test display mode methods
	@Test
	public void getDisplayMode() {
		CanvasFactory factory = new ch.blackspirit.graphics.jogl.CanvasFactory();
		DisplayMode mode = factory.getDisplayMode();
		assertMode(mode);
	}

	private void assertMode(DisplayMode mode) {
		Assert.assertNotNull(mode);
		Assert.assertTrue(mode.getColorDepth()> 0);
		Assert.assertTrue(mode.getHeight()> 0);
		Assert.assertTrue(mode.getWidth()> 0);
		Assert.assertTrue(mode.getRefreshRate()> 0);
	}
	
	@Test
	public void getDisplayModeWithSameRefreshRateAndColorDepth() {
		CanvasFactory factory = new ch.blackspirit.graphics.jogl.CanvasFactory();
		DisplayMode currentMode = factory.getDisplayMode();
		DisplayMode expectedMode = null;
		for (DisplayMode mode: factory.getDisplayModes()) {
			if (mode.getColorDepth() == currentMode.getColorDepth() &&
					mode.getRefreshRate() == currentMode.getRefreshRate()) {
				expectedMode = mode;
				break;
			}
		}
		if (expectedMode == null)
			return;
		DisplayMode actualMode = factory.getDisplayMode(expectedMode.getWidth(), expectedMode.getHeight());
		assertMode(actualMode);
		Assert.assertEquals(expectedMode.getWidth(), actualMode.getWidth());
		Assert.assertEquals(expectedMode.getHeight(), actualMode.getHeight());
		Assert.assertEquals(expectedMode.getColorDepth(), actualMode.getColorDepth());
		Assert.assertEquals(expectedMode.getRefreshRate(), actualMode.getRefreshRate());
	}

	@Test
	public void getDisplayModeWithSameRefreshRate() {
		CanvasFactory factory = new ch.blackspirit.graphics.jogl.CanvasFactory();
		DisplayMode currentMode = factory.getDisplayMode();
		DisplayMode expectedMode = null;
		for (DisplayMode mode: factory.getDisplayModes()) {
			if (mode.getRefreshRate() == currentMode.getRefreshRate()) {
				expectedMode = mode;
				break;
			}
		}
		if (expectedMode == null)
			return;
		DisplayMode actualMode = factory.getDisplayMode(expectedMode.getWidth(), expectedMode.getHeight(), expectedMode.getColorDepth());
		assertMode(actualMode);
		Assert.assertEquals(expectedMode.getWidth(), actualMode.getWidth());
		Assert.assertEquals(expectedMode.getHeight(), actualMode.getHeight());
		Assert.assertEquals(expectedMode.getColorDepth(), actualMode.getColorDepth());
		Assert.assertEquals(expectedMode.getRefreshRate(), actualMode.getRefreshRate());
	}

	// TODO Test AWT creation
	@Test
	public void createAWTCanvasLightweight() throws InterruptedException {
		CanvasFactory factory = new ch.blackspirit.graphics.jogl.CanvasFactory();
		AWTCanvas canvas = factory.createAWTCanvas(true);
		
		JFrame frame = new JFrame();
		frame.getContentPane().add(canvas.getComponent());
		frame.setSize(800, 600);
		
		frame.setVisible(true);
	}

	@Test
	public void createAWTCanvasHeavyweight() throws InterruptedException {
		CanvasFactory factory = new ch.blackspirit.graphics.jogl.CanvasFactory();
		AWTCanvas canvas = factory.createAWTCanvas(false);
		
		JFrame frame = new JFrame();
		frame.getContentPane().add(canvas.getComponent());
		frame.setSize(800, 600);
		
		frame.setVisible(true);
	}
}
