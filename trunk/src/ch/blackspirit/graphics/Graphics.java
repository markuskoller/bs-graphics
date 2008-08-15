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
package ch.blackspirit.graphics;

import java.awt.Font;
import java.awt.geom.Rectangle2D;

import javax.vecmath.Color4f;

/**
 * Gives access to all drawing operations and parameters.
 * @author Markus Koller
 */
public interface Graphics {

	/**
	 * Clears all drawings.
	 */
	public void clear();
	public void setClearColor(Color4f color);
	public void getClearColor(Color4f color);

	/**
	 * Set the color used for rendering.
	 * @param color The color to be set.
	 */
	public void setColor(Color4f color);
	public void getColor(Color4f color);

	
	/**
	 * Draws the shapes outline and cut-outs in the specified color.<br/>
	 * Every x,y pair of coordinates corresponds to a point of the triangle.
	 */
	public void drawLine(Line line, boolean useColors);
	public void drawLines(Line[] lines, boolean useColors);
	/**
	 * Draws a line in the specified width and color.<br/>
	 * The width is in pixels, not in the renderers logical resolution.
	 * Every x,y pair of coordinates corresponds to a point of the line.
	 */
	public abstract void drawLine(float x1, float y1, float x2, float y2);
	
	/**
	 * Draws the filled shape in the specified color.<br/>
	 * Every x,y pair of coordinates corresponds to a point of the triangle.
	 */
	public abstract void drawTriangle(Triangle triangle, boolean useColors);
	public abstract void drawTriangles(Triangle[] triangles, boolean useColors);
	public abstract void fillTriangle(Triangle triangle, boolean useColors);
	public abstract void fillTriangle(Triangle triangle, boolean useColors, Image image);
	public abstract void fillTriangles(Triangle[] triangles, boolean useColors);
	public abstract void fillTriangles(Triangle[] triangles, boolean useColors, Image image);
	/**
	 * Draws a filled triangle in the specified color.<br/>
	 * Every x,y pair of coordinates corresponds to a point of the triangle.
	 */
	public abstract void fillTriangle(float x1, float y1, float x2, float y2,
			float x3, float y3);


	/**
	 * Draws a point with the specified diameter and color.<br/>
	 * The diameter is in pixels, not in the renderers logical resolution.
	 */
	public abstract void drawPoint(float x, float y);

	/**
	 * Draws the image at the given position (x,y) in the given size (width,height).<br/>
	 * If the image is not cached already, it will be cached first.
	 */
	public abstract void drawImage(Image image, float width, float height);

	/**
	 * Draws the image at the given position (x,y) in the given size (width,height).<br/>
	 * The image will be flipped according to the <code>flip</code> parameter.<br/>
	 * If the image is not cached already, it will be cached first.
	 */
	public abstract void drawImage(Image image, float width, float height, Flip flip);

	
	/**
	 * Draws a subimage of the image at the given position (x,y) in the given size (width,height).<br/>
	 * The subimage is specified with the position (subImageX,subImageY) from the top-left and
	 * the subimage size (subImageWidth,subImageHeight). 
	 * The subimage is specified in the images coordinate system with (0, 0) being top-left and
	 * (1, 1) being bottom-right.<br/>
	 * If the image is not cached already, it will be cached first.<br/>
	 * The subImage parameters are not necessarly checked. You might get an Exception
	 * or unexpected behaviour setting them to illegal values.
	 */
	public abstract void drawImage(Image image, float width, float height, 
			int subImageX, int subImageY, int subImageWidth, int subImageHeight);

	/**
	 * Draws a subimage of the image at the given position (x,y) in the given size (width,height).<br/>
	 * The subimage is specified with the position (subImageX,subImageY) from the top-left and
	 * the subimage size (subImageWidth,subImageHeight). 
	 * The subimage is specified in the images coordinate system with (0, 0) being top-left and
	 * (1, 1) being bottom-right.<br/>
	 * The image will be flipped according to the <code>flip</code> parameter.<br/>
	 * If the image is not cached already, it will be cached first.<br/>
	 * The subImage parameters are not necessarly checked. You might get an Exception
	 * or unexpected behaviour setting them to illegal values.
	 */
	public abstract void drawImage(Image image, float width, float height, 
			int subImageX, int subImageY, int subImageWidth, int subImageHeight,
			Flip flip);
	
	/**
	 * Sets the base color. The default base color is white.<br/>
	 * Each drawing color gets multiplied by the base color.<br/>
	 * Example: If the base color is RGBA(0.5, 0.5, 0.5, 1) and the drawing color is
	 * RGBA(1, 0.5, 0, 1) the resulting color is RGBA(0.5, 0.25, 0, 1).  
	 * This should be called before any drawing during a frame, otherwise unpredictive results might occur.
	 * @param color The base color to be set.
	 */
	public abstract void setBaseColor(Color4f color);
	public abstract void getBaseColor(Color4f color);

	/**
	 * Rotates about the origin (0,0).
	 * To rotate about a different point, translate first.
	 * @param angle Angle in degrees to rotate in clockwise direction.
	 */
	public void rotate(float angle);
	public void translate(float x, float y);
	public void scale(float x, float y);
	/**
	 * Undos all transformations (rotation, translation, scale).
	 */
	public void clearTransformation();
	// public void undoTransformation();
	// public Transformation getTransformation();
	// public void setTransformation(Transformation transformation);
	
	/**
	 * Sets the font used for text rendering and bound measurement.<br/>
	 * Setting a font does not automaticely cache it.
	 * @param font The font to be used for rendering text.
	 */
	public void setFont(Font font);
	/**
	 * @return The font currently used for rendering text
	 */
	public Font getFont();
	/**
	 * @param text The string to be rendered.
	 */
	public void drawText(String text);
	/**
	 * Calculates the strings boundaries assuming it was rendered at the origin with scale 1.
	 * @param text The string whose bounds should be measured.
	 * @param bounds The bounds of the string.
	 */
	public void getTextBounds(String text, Rectangle2D bounds);
	
	public void setDrawingMode(DrawingMode drawingMode);
	public DrawingMode getDrawingMode();
	
	// ColorMask: r, g, b, a (glColorMask)
	public void setColorMask(boolean red, boolean green, boolean blue, boolean alpha);
	public void setRedMask(boolean red);
	public void setGreenMask(boolean green);
	public void setBlueMask(boolean blue);
	public void setAlphaMask(boolean alpha);
	public boolean getRedMask();
	public boolean getGreenMask();
	public boolean getBlueMask();
	public boolean getAlphaMask();
	
	public void setLineAntialiasing(boolean enabled);
	public boolean getLineAntialiasing();
	
	public void copyToImage(Image image);
	public void copyToImage(Image image, int x, int y, int width, int height);
}