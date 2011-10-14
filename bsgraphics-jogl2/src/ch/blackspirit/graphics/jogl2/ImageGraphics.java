/*
 * Copyright 2009 Markus Koller
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
package ch.blackspirit.graphics.jogl2;

import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Color4f;

import ch.blackspirit.graphics.DrawingMode;
import ch.blackspirit.graphics.Flip;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.Line;
import ch.blackspirit.graphics.Triangle;

/**
 * @author Markus Koller
 */
final class ImageGraphics implements ch.blackspirit.graphics.Graphics, ViewListener {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	private final GraphicsDelegate delegate;
	private final View view;
	private final Image image;

	public ImageGraphics(Image image, GraphicsDelegate delegate, ResourceManager resourceManager, View view) {
		this.delegate = delegate;
		this.view = view;
		view.addViewListener(this);
		this.image = image;
	}
	
	public void dispose() {
		this.view.removeViewListener(this);
	}
	
	void init() {
		if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Initializing image graphics");

		//		delegate.prepareExternalCode();
		GL2 gl = delegate.getGL();
		
		// Depth Testing
		gl.glDisable(GL.GL_DEPTH_TEST);
		
	    gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	    
	    // Lighting, Coloring
	    gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL.GL_BLEND);
	    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	    gl.glEnable(GL2.GL_COLOR_MATERIAL);
	    gl.glColorMaterial(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
	    gl.glShadeModel(GL2.GL_SMOOTH);

		// Texturing
	    gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		
	    // View
	    initGLViewPort();
	    cameraChanged();
		
		delegate.init();
	}
	
	private void initGLViewPort() {
		GL2 gl = delegate.getGL();
	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glLoadIdentity();
	    
	    GLU glu = new GLU();
	    float factorX = (float)delegate.getContext().getGLDrawable().getWidth() / (float)image.getWidth();
	    float factorY = (float)delegate.getContext().getGLDrawable().getHeight() / (float)image.getHeight();
	    glu.gluOrtho2D(0, view.getWidth() * factorX, 0, view.getHeight() * factorY);
	    gl.glMatrixMode(GL2.GL_MODELVIEW);
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

//	public float getLineWidth() {
//		return delegate.getLineWidth();
//	}
//	public float getPointRadius() {
//		return delegate.getPointRadius();
//	}
//	public void setLineWidth(float width) {
//		delegate.setLineWidth(width);
//	}
//	public void setPointRadius(float radius) {
//		delegate.setPointRadius(radius);
//	}		

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
	@SuppressWarnings("deprecation")
	public void clearTransformation() {
		delegate.clearTransformation();
	}
	public void clearTransform() {
		delegate.clearTransform();
	}
	public void pushTransform() {
		delegate.pushTransform();
	}
	public void popTransform() {
		delegate.popTransform();
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
		delegate.setCamera(-view.getCameraX(), -view.getCameraY()-view.getHeight(), view.getCameraAngle());
	}
	public void viewSizeChanged() {
		initGLViewPort();
		cameraChanged();
	}

	public void copyToImage(Image image) {
		delegate.copyToImage(image);
	}

	public void copyToImage(Image image, int x, int y, int width, int height) {
		delegate.copyToImage(image, x, y, width, height);
	}
}
