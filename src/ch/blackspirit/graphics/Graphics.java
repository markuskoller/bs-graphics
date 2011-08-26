/*
 * Copyright 2011 Markus Koller
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
	 * Clears all drawings with the clear color.
	 */
	public void clear();
	/**
	 * Set the color used for clearing.
	 * @param color The color to use for clearing.
	 */
	public void setClearColor(Color4f color);
	/**
	 * Get the color used for clearing.
	 * @param color Will be set to the color used for clearing.
	 */
	public void getClearColor(Color4f color);

	/**
	 * Set the overall drawing color.
	 * @param color The color to be set as overall drawing color.
	 */
	public void setColor(Color4f color);
	/**
	 * Get the overall drawing color.
	 * @param color Will be set to the overall drawing color.
	 */
	public void getColor(Color4f color);

	
	/**
	 * Draws a line with a width of 1 pixel. The line can either be drawn using the overall drawing color 
	 * or the colors specified for each line point. If a color per point is used 
	 * points with no color specified will be drawn using the overall drawing color.
	 * @param line The line to draw.
	 * @param useColors Should the colors specified for each line point be used or the overall drawing color.
 	 * @throws IllegalArgumentException If line is {@code null}.
	 */
	public void drawLine(Line line, boolean useColors);
	/**
	 * Draws multiple lines with a width of 1 pixel. The lines can either be drawn using the overall drawing color 
	 * or the colors specified for each line point. If a color per point is used 
	 * points with no color specified will be drawn using the overall drawing color.<br/>
	 * Entries in the line array set to {@code null} will be ignored.
	 * @param lines The lines to draw. {@code null} entries will be ignored
	 * @param useColors Should the colors specified for each line point be used or the overall drawing color.
	 * @throws IllegalArgumentException If lines is {@code null}.
	 */
	public void drawLines(Line[] lines, boolean useColors);
	/**
	 * Draws a line in the current drawing color with a width of 1 pixel.<br/>
	 * @param x1 X coordinate of first point.
	 * @param y1 Y coordinate of first point.
	 * @param x2 X coordinate of second point.
	 * @param y2 Y coordinate of second point.
	 */
	public abstract void drawLine(float x1, float y1, float x2, float y2);
	
	/**
	 * Draws a triangle with a line width of 1 pixel. The triangle can either be drawn using the overall drawing color 
	 * or the colors specified for each point. If a color per point is used 
	 * points with no color specified will be drawn using the overall drawing color.
	 * @param triangle The triangle to draw.
	 * @param useColors Should the colors specified for each point be used or the overall drawing color.
	 * @throws IllegalArgumentException If triangle is {@code null}.
	 */
	public abstract void drawTriangle(Triangle triangle, boolean useColors);
	/**
	 * Draws triangles with a line width of 1 pixel. The triangles can either be drawn using the overall drawing color 
	 * or the colors specified for each point. If a color per point is used 
	 * points with no color specified will be drawn using the overall drawing color.<br/>
	 * Entries in the triangle array set to {@code null} will be ignored.
	 * @param triangles The triangles to draw.
	 * @param useColors Should the colors specified for each point be used or the overall drawing color.
	 * @throws IllegalArgumentException If triangles is {@code null}.
	 */
	public abstract void drawTriangles(Triangle[] triangles, boolean useColors);
	/**
	 * Fills a triangle. The triangle can either be filled using the overall drawing color 
	 * or the colors specified for each point. If a color per point is used 
	 * for points with no color specified the overall drawing color will be used.
	 * @param triangle The triangle to fill.
	 * @param useColors Should the colors specified for each point be used or the overall drawing color.
	 * @throws IllegalArgumentException If triangle is {@code null}.
	 */
	public abstract void fillTriangle(Triangle triangle, boolean useColors);
	/**
	 * Fills a triangle with a texture. Texture coordinates must be specified for every point.
	 * The triangle will be filled with the texture blended with
	 * either the overall drawing color or the colors specified for each point. 
	 * If a color per point is used for points with no color specified the overall drawing color will be used.
	 * @param triangle The triangle to fill.
	 * @param useColors Should the colors specified for each point be used or the overall drawing color.
	 * @param image The image to use as texture.
	 * @throws IllegalArgumentException If triangle or image are {@code null} or texture coordinates for a point
	 * 	are not set.
	 */
	public abstract void fillTriangle(Triangle triangle, boolean useColors, Image image);
	/**
	 * Fills triangles. The triangles can either be filled using the overall drawing color 
	 * or the colors specified for each point. If a color per point is used 
	 * for points with no color specified the overall drawing color will be used.
	 * Entries in the triangle array set to {@code null} will be ignored.
	 * @param triangles The triangles to fill.
	 * @param useColors Should the colors specified for each point be used or the overall drawing color.
	 * @throws IllegalArgumentException If triangles is {@code null}.
	 */
	public abstract void fillTriangles(Triangle[] triangles, boolean useColors);
	/**
	 * Fills triangles with a texture. Texture coordinates must be specified for every point.
	 * The triangles will be filled with the texture blended with
	 * either the overall drawing color or the colors specified for each point. 
	 * If a color per point is used for points with no color specified the overall drawing color will be used.
	 * Entries in the triangle array set to {@code null} will be ignored.
	 * @param triangles The triangles to fill.
	 * @param useColors Should the colors specified for each point be used or the overall drawing color.
	 * @param image The image to use as texture.
	 * @throws IllegalArgumentException If triangles or image are {@code null} or texture coordinates for a point
	 * 	are not set.
	 */
	public abstract void fillTriangles(Triangle[] triangles, boolean useColors, Image image);

	/**
	 * Draws a filled triangle in the current drawing color.<br/>
	 * @param x1 X coordinate of first point.
	 * @param y1 Y coordinate of first point.
	 * @param x2 X coordinate of second point.
	 * @param y2 Y coordinate of second point.
	 * @param x3 X coordinate of third point.
	 * @param y3 Y coordinate of third point.
	 */
	public abstract void fillTriangle(float x1, float y1, float x2, float y2,
			float x3, float y3);


	/**
	 * Draws a point in the current drawing color with a size of 1 pixel.<br/>
	 * @param x X coordinate of first point.
	 * @param y Y coordinate of first point.
	 */
	public abstract void drawPoint(float x, float y);

	/**
	 * Draws the image in the given size (width, height).<br/>
	 * If the image is not cached already, it will be cached first.
	 * @param image The image to draw.
	 * @param width Width to draw the image with.
	 * @param height Height to draw the image with.
	 */
	public abstract void drawImage(Image image, float width, float height);

	/**
	 * Draws the image in the given size (width, height) possibly flipped horizontally and/or vertically.<br/>
	 * If the image is not cached already, it will be cached first.
	 * @param image The image to draw.
	 * @param width Width to draw the image with.
	 * @param height Height to draw the image with.
	 * @param flip Should the image be horizontally and/or vertically flipped.
	 */
	public abstract void drawImage(Image image, float width, float height, Flip flip);

	
	/**
	 * Draws part of an image in the given size (width, height).<br/>
	 * If the image is not cached already, it will be cached first.
	 * @param image The image to draw.
	 * @param width Width to draw the image with.
	 * @param height Height to draw the image with.
	 * @param subImageX X coordinate of the top left corner of the sub image.
	 * @param subImageY Y coordinate of the top left corner of the sub image.
	 * @param subImageWidth Width of the sub image.
	 * @param subImageHeight Height of the sub image.
	 * @throws IllegalArgumentException If image is {@code null} or illegal sub image parameters are passed.
	 */
	public abstract void drawImage(Image image, float width, float height, 
			int subImageX, int subImageY, int subImageWidth, int subImageHeight);

	/**
	 * Draws part of an image in the given size (width, height) 
	 * possibly flipped horizontally and/or vertically.<br/>
	 * If the image is not cached already, it will be cached first.
	 * @param image The image to draw.
	 * @param width Width to draw the image with.
	 * @param height Height to draw the image with.
	 * @param subImageX X coordinate of the top left corner of the sub image.
	 * @param subImageY Y coordinate of the top left corner of the sub image.
	 * @param subImageWidth Width of the sub image.
	 * @param subImageHeight Height of the sub image.
	 * @param flip Should the image be horizontally and/or vertically flipped.
	 * @throws IllegalArgumentException If image is {@code null} or illegal sub image parameters are passed.
	 */
	public abstract void drawImage(Image image, float width, float height, 
			int subImageX, int subImageY, int subImageWidth, int subImageHeight,
			Flip flip);
	
	/**
	 * Sets the base color. The default base color is white.<br/>
	 * Each drawing color gets multiplied by the base color.<br/>
	 * Example: If the base color is RGBA(0.5, 0.5, 0.5, 1) and the drawing color is
	 * RGBA(1, 0.5, 0, 1) the resulting color is RGBA(0.5, 0.25, 0, 1).  
	 * This should be called before any drawing during a frame, otherwise unpredictable results might occur.
	 * @param color The base color to be set.
	 */
	public abstract void setBaseColor(Color4f color);
	/**
	 * Gets the base color.
	 * Each drawing color gets multiplied by the base color.<br/>
	 * Example: If the base color is RGBA(0.5, 0.5, 0.5, 1) and the drawing color is
	 * RGBA(1, 0.5, 0, 1) the resulting color is RGBA(0.5, 0.25, 0, 1).  
	 * @param color Will be set to the base color.
	 */
	public abstract void getBaseColor(Color4f color);

	/**
	 * <p>Rotates the coordinate system about the origin (0,0).</p>
	 * <p>To rotate about a different point use it together with translation.</p>
	 * @param angle Angle in degrees to rotate in clockwise direction.
	 */
	public void rotate(float angle);
	/**
	 * Translates (moves) the coordinate system.
	 * @param x Translation on x axis.
	 * @param y Translation on y axis.
	 */
	public void translate(float x, float y);
	/**
	 * Scales the coordinate system.
	 * @param x Scaling on x axis.
	 * @param y Scaling on y axis.
	 */
	public void scale(float x, float y);
	/**
	 * Undoes all transformations (rotation, translation, scale).
	 * @deprecated
	 */
	public void clearTransformation();
	// public Transformation getTransformation();
	// public void setTransformation(Transformation transformation);
	// public void undoTransformation();
	
	/**
	 * Clears all transformations and also the transformation stack.
	 * There is currently no way to keep the transformation stack but that functionality may be added later.
	 */
	public void clearTransform();
	public void pushTransform();
	public void popTransform();
	
	/**
	 * Sets the font used for text rendering and bound measurement.<br/>
	 * Setting a font does not automatically cache it.
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
	
	/**
	 * @param drawingMode The {@link DrawingMode} to set.
	 */
	public void setDrawingMode(DrawingMode drawingMode);
	public DrawingMode getDrawingMode();
	
	/**
	 * Sets the color mask. Only color components set to {@code true} will be updated in the buffer when drawing.
	 * @param red Should the red component be updated in the buffer when drawing.
	 * @param green Should the green component be updated in the buffer when drawing.
	 * @param blue Should the blue component be updated in the buffer when drawing.
	 * @param alpha Should the alpha component be updated in the buffer when drawing.
	 */
	public void setColorMask(boolean red, boolean green, boolean blue, boolean alpha);
	/**
	 * Sets the red mask. Only if set to {@code true} the red component will be updated in the buffer when drawing.
	 * @param red Should the red component be updated in the buffer when drawing.
	 */
	public void setRedMask(boolean red);
	/**
	 * Sets the green mask. Only if set to {@code true} the green component will be updated in the buffer when drawing.
	 * @param green Should the green component be updated in the buffer when drawing.
	 */
	public void setGreenMask(boolean green);
	/**
	 * Sets the blue mask. Only if set to {@code true} the blue component will be updated in the buffer when drawing.
	 * @param blue Should the blue component be updated in the buffer when drawing.
	 */
	public void setBlueMask(boolean blue);
	/**
	 * Sets the alpha mask. Only if set to {@code true} the alpha component will be updated in the buffer when drawing.
	 * @param alpha Should the alpha component be updated in the buffer when drawing.
	 */
	public void setAlphaMask(boolean alpha);
	public boolean getRedMask();
	public boolean getGreenMask();
	public boolean getBlueMask();
	public boolean getAlphaMask();
	
	/**
	 * @param enabled Should lines drawn be antialiased.
	 */
	public void setLineAntialiasing(boolean enabled);
	public boolean getLineAntialiasing();
	
	/**
	 * Copies the current buffer content to an image. Width and height of the
	 * region written to in the image correspond to the GraphicsContext pixel resolution.
	 * The written data starts in the top left corner.
	 * @param image Image to copy current buffer content to. 
	 */
	public void copyToImage(Image image);
	/**
	/**
	 * Copies part of the current buffer content to an image. Width and height of the
	 * region written to in the image correspond to the GraphicsContext pixel resolution of
	 * the part copied.
	 * The written data starts in the top left corner.
	 * @param image Image to copy current buffer content to. 
	 * @param x X coordinate [pixel] from top left of the context to start copying.
	 * @param y Y coordinate [pixel] from top left of the context to start copying.
	 * @param width Width [pixel] of the region to copy.
	 * @param height Height [pixel] of the region to copy.
	 */
	public void copyToImage(Image image, int x, int y, int width, int height);
}