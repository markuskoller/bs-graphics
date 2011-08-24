/*
 * Copyright 2008-2009 Markus Koller
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
package ch.blackspirit.graphics.debug;

import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.vecmath.Color4f;

import ch.blackspirit.graphics.DrawingMode;
import ch.blackspirit.graphics.Flip;
import ch.blackspirit.graphics.Graphics;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.Line;
import ch.blackspirit.graphics.Triangle;

/**
 * Logs each method call on the graphics object using the specified log level.
 * @author Markus Koller
 */
public class TraceGraphics implements Graphics {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private Level level = Level.INFO;
	
	private Graphics delegate;

	public TraceGraphics() {}
	/**
	 * @param delegate Graphics to delegate calls to.
	 */
	public TraceGraphics(Graphics delegate) {
		this.delegate = delegate;
	}
	public Graphics getDelegate() {
		return delegate;
	}
	/**
	 * @param delegate Graphics to delegate calls to.
	 */
	public void setDelegate(Graphics delegate) {
		this.delegate = delegate;
	}
	
	public Level getLevel() {
		return level;
	}
	/**
	 * @param level Log level to use for logging.
	 */
	public void setLevel(Level level) {
		this.level = level;
	}
	public void clear() {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "clear()");
		delegate.clear();
	}
	@SuppressWarnings("deprecation")
	public void clearTransformation() {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "clearTransformation()");
		delegate.clearTransformation();
	}
	public void clearTransform() {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "clearTransform()");
		delegate.clearTransform();
	}
	public void popTransform() {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "popTransform()");
		delegate.popTransform();
	}
	public void pushTransform() {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "pushTransform()");
		delegate.pushTransform();
	}
	public void copyToImage(Image image, int x, int y, int width, int height) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "copyToImage(" + image.toString() + ", " + x + ", " + y + ", " + width + ", " + height + ")");
		delegate.copyToImage(image, x, y, width, height);
	}
	public void copyToImage(Image image) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "copyToImage()");
		delegate.copyToImage(image);
	}
	public void drawImage(Image image, float width, float height, Flip flip) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "drawImage(" + image.toString() + ", " + width + ", " + height + ", " + flip.name() + ")");
		delegate.drawImage(image, width, height, flip);
	}
	public void drawImage(Image image, float width, float height,
			int subImageX, int subImageY, int subImageWidth,
			int subImageHeight, Flip flip) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "drawImage(" + image.toString() + ", " + width + ", " + height + ", " + subImageX + ", " + subImageY + ", " + subImageWidth + ", " + subImageHeight + ", " + flip.name() + ")");
		delegate.drawImage(image, width, height, subImageX, subImageY,
				subImageWidth, subImageHeight, flip);
	}
	public void drawImage(Image image, float width, float height,
			int subImageX, int subImageY, int subImageWidth, int subImageHeight) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "drawImage(" + image.toString() + ", " + width + ", " + height + ", " + subImageX + ", " + subImageY + ", " + subImageWidth + ", " + subImageHeight + ")");
		delegate.drawImage(image, width, height, subImageX, subImageY,
				subImageWidth, subImageHeight);
	}
	public void drawImage(Image image, float width, float height) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "drawImage(" + image.toString() + ", " + width + ", " + height + ")");
		delegate.drawImage(image, width, height);
	}
	public void drawLine(float x1, float y1, float x2, float y2) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "drawLine(" + x1 + ", " + y1 + ", " + x2 + ", " + y2 + ")");
		delegate.drawLine(x1, y1, x2, y2);
	}
	public void drawLine(Line line, boolean useColors) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "drawLine(Line(" + line.getPoint(0).x + ", " + line.getPoint(0).y + ", " + line.getPoint(1).x + ", " + line.getPoint(1).y + "), " + useColors + ")");
		delegate.drawLine(line, useColors);
	}
	public void drawLines(Line[] lines, boolean useColors) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "drawLines(Line[" + lines.length + "], " + useColors + ")");
		delegate.drawLines(lines, useColors);
	}
	public void drawPoint(float x, float y) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "drawPoint(" + x + ", " + y + ")");
		delegate.drawPoint(x, y);
	}
	public void drawText(String text) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "drawText(" + text + ")");
		delegate.drawText(text);
	}
	public void drawTriangle(Triangle triangle, boolean useColors) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "drawTriangle(Triangle(" + 
				triangle.getPoint(0).x + ", " + triangle.getPoint(0).y + ", " +
				triangle.getPoint(1).x + ", " + triangle.getPoint(1).y + ", " +
				triangle.getPoint(2).x + ", " + triangle.getPoint(2).y + "), " + useColors + ")");
		delegate.drawTriangle(triangle, useColors);
	}
	public void drawTriangles(Triangle[] triangles, boolean useColors) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "drawTriangles(Triangle[" + triangles.length + "], " + useColors + ")");
		delegate.drawTriangles(triangles, useColors);
	}
	public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "fillTriangle(" + x1 + ", " + y1 + ", " + x2 + ", " + y2 + ", " + x3 + ", " + y3 + ")");
		delegate.fillTriangle(x1, y1, x2, y2, x3, y3);
	}
	public void fillTriangle(Triangle triangle, boolean useColors, Image image) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "fillTriangle(Triangle(" + 
				triangle.getPoint(0).x + ", " + triangle.getPoint(0).y + ", " +
				triangle.getPoint(1).x + ", " + triangle.getPoint(1).y + ", " +
				triangle.getPoint(2).x + ", " + triangle.getPoint(2).y + "), " + useColors + ", " + image.toString() + ")");
		delegate.fillTriangle(triangle, useColors, image);
	}
	public void fillTriangle(Triangle triangle, boolean useColors) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "fillTriangle(Triangle(" + 
				triangle.getPoint(0).x + ", " + triangle.getPoint(0).y + ", " +
				triangle.getPoint(1).x + ", " + triangle.getPoint(1).y + ", " +
				triangle.getPoint(2).x + ", " + triangle.getPoint(2).y + "), " + useColors + ")");
		delegate.fillTriangle(triangle, useColors);
	}
	public void fillTriangles(Triangle[] triangles, boolean useColors, Image image) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "fillTriangles(Triangle[" + triangles.length + "], " + useColors + ", " + image.toString() + ")");
		delegate.fillTriangles(triangles, useColors, image);
	}
	public void fillTriangles(Triangle[] triangles, boolean useColors) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "fillTriangles(Triangle[" + triangles.length + "], " + useColors + ")");
		delegate.fillTriangles(triangles, useColors);
	}
	public boolean getAlphaMask() {
		boolean value = delegate.getAlphaMask();
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "getAlphaMask() = " + value);
		return value;
	}
	public boolean getBlueMask() {
		boolean value = delegate.getBlueMask();
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "getBlueMask() = " + value);
		return value;
	}
	public boolean getGreenMask() {
		boolean value = delegate.getGreenMask();
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "getGreenMask() = " + value);
		return value;
	}
	public boolean getRedMask() {
		boolean value = delegate.getRedMask();
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "getRedMask() = " + value);
		return value;
	}
	public void getBaseColor(Color4f color) {
		delegate.getBaseColor(color);
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "getBaseColor() = " + color);
	}
	public void getClearColor(Color4f color) {
		delegate.getClearColor(color);
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "getClearColor() = " + color);
	}
	public void getColor(Color4f color) {
		delegate.getColor(color);
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "getColor() = " + color);
	}
	public DrawingMode getDrawingMode() {
		DrawingMode value = delegate.getDrawingMode();
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "getDrawingMode() = " + value.name());
		return value;
	}
	public Font getFont() {
		Font value = delegate.getFont();
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "getFont() = " + value);
		return value;
	}
	public boolean getLineAntialiasing() {
		boolean value = delegate.getLineAntialiasing();
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "getLineAntialiasing() = " + value);
		return value;
	}
	public void getTextBounds(String text, Rectangle2D bounds) {
		delegate.getTextBounds(text, bounds);
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "getTextBounds(" + text + ") = " + bounds);
	}
	public void rotate(float angle) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "rotate(" + angle + ")");
		delegate.rotate(angle);
	}
	public void scale(float x, float y) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "scale(" + x + ", " + y + ")");
		delegate.scale(x, y);
	}
	public void setAlphaMask(boolean alpha) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "setAlphaMask(" + alpha + ")");
		delegate.setAlphaMask(alpha);
	}
	public void setBaseColor(Color4f color) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "setBaseColor(" + color + ")");
		delegate.setBaseColor(color);
	}
	public void setBlueMask(boolean blue) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "setBlueMask(" + blue + ")");
		delegate.setBlueMask(blue);
	}
	public void setClearColor(Color4f color) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "setClearColor(" + color + ")");
		delegate.setClearColor(color);
	}
	public void setColor(Color4f color) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "setColor(" + color + ")");
		delegate.setColor(color);
	}
	public void setColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "setColorMask(" + red + ", " + green + ", " + blue + ", " + alpha + ")");
		delegate.setColorMask(red, green, blue, alpha);
	}
	public void setDrawingMode(DrawingMode drawingMode) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "setDrawingMode(" + drawingMode.name() + ")");
		delegate.setDrawingMode(drawingMode);
	}
	public void setFont(Font font) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "setFont(" + font + ")");
		delegate.setFont(font);
	}
	public void setGreenMask(boolean green) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "setGreenMask(" + green + ")");
		delegate.setGreenMask(green);
	}
	public void setLineAntialiasing(boolean enabled) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "setLineAntialiasing(" + enabled + ")");
		delegate.setLineAntialiasing(enabled);
	}
	public void setRedMask(boolean red) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "setRedMask(" + red + ")");
		delegate.setRedMask(red);
	}
	public void translate(float x, float y) {
		if(LOGGER.isLoggable(level)) LOGGER.log(level, "translate(" + x + ", " + y + ")");
		delegate.translate(x, y);
	}	
}
