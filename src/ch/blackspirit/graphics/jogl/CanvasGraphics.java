/*
 * Copyright 2008 Markus Koller
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.blackspirit.graphics.jogl;

import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Color4f;

import ch.blackspirit.graphics.DrawingMode;
import ch.blackspirit.graphics.Flip;
import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.Line;
import ch.blackspirit.graphics.Triangle;

/**
 * @author Markus Koller
 */
class CanvasGraphics implements Graphics, ViewListener {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	private final GraphicsDelegate delegate;
	private final View view;
	
	public CanvasGraphics(GraphicsDelegate delegate, View view) {
		this.delegate = delegate;
		this.view = view;
		view.addViewListener(this);
	}
	
	public void endFrame() {
		delegate.endFrame();
	}
	
	void init() {
		if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Initializing canvas graphics");
//		delegate.prepareExternalCode();
		GL gl = delegate.getGL();
		

		// Depth Testing
		gl.glDisable(GL.GL_DEPTH_TEST);
		
	    gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	    
	    // Lighting, Coloring
	    gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_BLEND);
	    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	    gl.glEnable(GL.GL_COLOR_MATERIAL);
	    gl.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE);
	    gl.glShadeModel(GL.GL_SMOOTH);

	    // Anti-Aliasing (Polygon antialiasing causes problems on most graphics cards, don't use it!)
//		FloatBuffer buf = FloatBuffer.allocate(2);
//		gl.glGetFloatv(gl.GL_POINT_SIZE_RANGE, buf);
//		gl.glPointSize(buf.get());
//		System.out.println(buf.get());
//		System.out.println(buf.get());
	    
		// Texturing
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);
		
	    // View
	    initGLViewPort();
	    cameraChanged();

		delegate.init();
	}

	private void initGLViewPort() {
		GL gl = delegate.getGL();
	    gl.glMatrixMode(GL.GL_PROJECTION);
	    gl.glLoadIdentity();
	    
	    GLU glu = new GLU();
	    glu.gluOrtho2D(-view.getWidth() / 2, view.getWidth() / 2, -view.getHeight() / 2, view.getHeight() / 2);
	    gl.glMatrixMode(GL.GL_MODELVIEW);
	}
	
	public void viewSizeChanged() {
		initGLViewPort();
	}

	public void clear() {
		delegate.clear();
	}
	public void getClearColor(Color4f color) {
		delegate.getClearColor(color);
	}
	public void setClearColor(Color4f color) {
		delegate.setClearColor(color);
	}

	public void drawImage(Image image, float width, float height, Flip flip) {
		delegate.drawImage(image, width, height, flip);
	}
	public void drawImage(Image image, float width, float height, int subImageX, int subImageY, int subImageWidth, int subImageHeight, Flip flip) {
		delegate.drawImage(image, width, height, subImageX, subImageY, subImageWidth, subImageHeight, flip);
	}
	public void drawImage(Image image, float width, float height, int subImageX, int subImageY, int subImageWidth, int subImageHeight) {
		delegate.drawImage(image, width, height, subImageX, subImageY, subImageWidth, subImageHeight);
	}
	public void drawImage(Image image, float width, float height) {
		delegate.drawImage(image, width, height);
	}

	public void drawLine(float x1, float y1, float x2, float y2) {
		delegate.drawLine(x1, y1, x2, y2);
	}
	public void drawPoint(float x, float y) {
		delegate.drawPoint(x, y);
	}

	public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
		delegate.fillTriangle(x1, y1, x2, y2, x3, y3);
	}

	public void getBaseColor(Color4f color) {
		delegate.getBaseColor(color);
	}
	public void getColor(Color4f color) {
		delegate.getColor(color);
	}
	public void setBaseColor(Color4f color) {
		delegate.setBaseColor(color);
	}
	public void setColor(Color4f color) {
		delegate.setColor(color);
	}

	public void rotate(float angle) {
		delegate.rotate(angle);
	}
	public void scale(float x, float y) {
		delegate.scale(x, y);
	}
	public void translate(float x, float y) {
		delegate.translate(x, y);
	}
	public void clearTransformation() {
		delegate.clearTransformation();
	}

	public void setDrawingMode(DrawingMode drawingMode) {
		delegate.setDrawingMode(drawingMode);
	}
	public DrawingMode getDrawingMode() {
		return delegate.getDrawingMode();
	}

	public void drawLines(Line[] lines, boolean useColors) {
		delegate.drawLines(lines, useColors);
	}
	public void drawTriangle(Triangle triangle, boolean useColors) {
		delegate.drawTriangle(triangle, useColors);
	}
	public void drawTriangles(Triangle[] triangles, boolean useColors) {
		delegate.drawTriangles(triangles, useColors);
	}
	public void fillTriangles(Triangle[] area, boolean useColors, Image image) {
		delegate.fillTriangles(area, useColors, image);
	}
	public void fillTriangles(Triangle[] area, boolean useColors) {
		delegate.fillTriangles(area, useColors);
	}
	public void drawLine(Line line, boolean useColors) {
		delegate.drawLine(line, useColors);
	}
	public void fillTriangle(Triangle triangle, boolean useColors, Image image) {
		delegate.fillTriangle(triangle, useColors, image);
	}
	public void fillTriangle(Triangle triangle, boolean useColors) {
		delegate.fillTriangle(triangle, useColors);
	}

	public Font getFont() {
		return delegate.getFont();
	}
	public void setFont(Font font) {
		delegate.setFont(font);
	}
	public void drawText(String text) {
		delegate.drawText(text);
	}
	public void getTextBounds(String text, Rectangle2D bounds) {
		delegate.getTextBounds(text, bounds);
	}
	public boolean getAlphaMask() {
		return delegate.getAlphaMask();
	}
	public boolean getGreenMask() {
		return delegate.getGreenMask();
	}
	public boolean getRedMask() {
		return delegate.getRedMask();
	}
	public boolean getBlueMask() {
		return delegate.getBlueMask();
	}
	public void setAlphaMask(boolean alpha) {
		delegate.setAlphaMask(alpha);
	}
	public void setBlueMask(boolean blue) {
		delegate.setBlueMask(blue);
	}
	public void setGreenMask(boolean green) {
		delegate.setGreenMask(green);
	}
	public void setRedMask(boolean red) {
		delegate.setRedMask(red);
	}
	public void setColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
		delegate.setColorMask(red, green, blue, alpha);
	}
	public boolean getLineAntialiasing() {
		return delegate.getLineAntialiasing();
	}
	public void setLineAntialiasing(boolean enabled) {
		delegate.setLineAntialiasing(enabled);
	}

	public void cameraChanged() {
		delegate.setCamera(-view.getCameraX(), -view.getCameraY(), view.getCameraAngle());
	}

	public void copyToImage(Image image) {
		delegate.copyToImage(image);
	}

	public void copyToImage(Image image, int x, int y, int width, int height) {
		delegate.copyToImage(image, x, y, width, height);
	}
}
