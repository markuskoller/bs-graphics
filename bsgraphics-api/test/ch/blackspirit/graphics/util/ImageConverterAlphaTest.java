package ch.blackspirit.graphics.util;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.vecmath.Color4f;

import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.GraphicsContext;
import ch.blackspirit.graphics.GraphicsListener;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.RealtimeCanvas;
import ch.blackspirit.graphics.View;
import ch.blackspirit.graphics.jogl.BufferTypes;
import ch.blackspirit.graphics.jogl.CanvasFactory;

public class ImageConverterAlphaTest {
	public static void main(String[] args) {
		RealtimeCanvas canvas = new CanvasFactory().createRealtimeCanvasWindow(512, 512);
		canvas.setWindowTitle("bla");
		final Image image = canvas.getImageFactory().createBufferedImage(521, 512, BufferTypes.RGBA_4Byte);
		
		canvas.setGraphicsListener(new GraphicsListener() {
			public void draw(View view, Graphics renderer) {
				renderer.clear();
				renderer.drawImage(image, 512, 512);
			}
			public void init(View view, Graphics renderer) {
				view.setCamera(256, 256, 0);
				view.setSize(512, 512);
				image.updateCache();
			}
			public void sizeChanged(GraphicsContext graphicsContext, View view) {}
		});
		
		ColorGradientFactory gradient = new ColorGradientFactory();
		gradient.setBaseColor(new Color4f(0,0,0,0));
		gradient.addSourcePoint(256, 256, 256, new Color4f(0,0,1,1));
		
		gradient.drawGradient(image);
		
		BufferedImage bimage = null;
//		long time = System.nanoTime();
//		for(int i = 0; i < 100; i++) {
			bimage = AWTUtil.readImageBuffer(image);
//		}
//		long time2 = System.nanoTime();
//		System.out.println("convert: " + (time2 - time));
		
		AffineTransform af = new AffineTransform();
		af.scale(.5, .5);
		AffineTransformOp op = new AffineTransformOp(af, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage orig = bimage;
//		long time3 = System.nanoTime();
//		for(int i = 0; i < 100; i++) {
			bimage = op.filter(orig, null);
//		}
//		long time4 = System.nanoTime();
//		System.out.println("transform: " + (time4 - time3));
		
		
		JFrame frame = new JFrame();
		JLabel label = new JLabel();
		frame.getContentPane().setBackground(Color.GREEN);
		label.setIcon(new ImageIcon(bimage));
		frame.getContentPane().add(label);
		frame.pack();
		frame.setVisible(true);
		
		while(true) {
			canvas.draw();
		}
	}
}
