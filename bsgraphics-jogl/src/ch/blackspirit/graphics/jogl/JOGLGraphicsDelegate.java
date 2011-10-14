/*
 * Copyright 2008-2011 Markus Koller
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

import ch.blackspirit.graphics.DrawingMode;
import ch.blackspirit.graphics.Flip;
import ch.blackspirit.graphics.Line;
import ch.blackspirit.graphics.Triangle;
import ch.blackspirit.graphics.pool.ObjectPool;

import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.TextureCoords;

/**
 * @author Markus Koller
 */
final class JOGLGraphicsDelegate implements GraphicsDelegate {
	private ResourceManager resourceManager;

	private DrawingMode drawingMode = DrawingMode.ALPHA_BLEND;

	private boolean redMask = true;
	private boolean greenMask = true;
	private boolean blueMask = true;
	private boolean alphaMask = true;

	private Color4f clearColor = new Color4f(0f, 0f, 0f, 0f);
	private Color4f baseColor = new Color4f(1f, 1f, 1f, 1f);
	private float[] baseColorArray = new float[4];
	private Color4f color = new Color4f(1f, 1f, 1f, 1f);
	
	private float lineWidth = 1f;
	private float pointDiameter = 1f;
	
	private float translationX = 0;
	private float translationY = 0;
	private float angle = 0;
	
	private boolean lineAntialiasing = true;
//	private boolean polygonAntialiasing = true;
	
	private Font font = new Font("SansSerif", Font.PLAIN, 24);
	
	private RuntimeProperties properties;
	
	// Transformation caches
	private ObjectPool<Rotation> rotations = new ObjectPool<Rotation>(new Rotation(), 10);
	private ObjectPool<Translation> translations = new ObjectPool<Translation>(new Translation(), 10);
	private ObjectPool<Scale> scales = new ObjectPool<Scale>(new Scale(), 10);
	
	private HashMap<Font, TextRenderer> textRenderers = new HashMap<Font, TextRenderer>();
	
	private ArrayList<Transformation> transforms = new ArrayList<Transformation>(1000);
	// not optimal but the first 127 Integers are cached.. more is unlikely and not performant anyway..
	// TODO reimplement transformation logic with Matrices
	private ArrayList<Integer> transformStack = new ArrayList<Integer>(200);
	private RenderContext drawable;
	
	public JOGLGraphicsDelegate(RenderContext context, ResourceManager resourceManager, RuntimeProperties properties) {
		this.resourceManager = resourceManager;
		this.drawable = context;
		this.properties = properties;
	}
		
	public void init() {
		for(TextRenderer textRenderer: textRenderers.values()) {
			textRenderer.dispose();
		}
		textRenderers.clear();
	    applyColor();
	    applyBaseColor();
	    applyClearColor();
	    applyDrawingMode();
	    applyColorMask();
	    applyPointSize();
	    applyLineWidth();
	    applyPointAntialiasing();
	    applyLineAntialiasing();
	    applyPolygonAntialiasing();
		setTransform();
	}
	
	// ==================== Accessors ====================
	public GL getGL() {
		return drawable.getGL();
	}
	public GLContext getContext() {
		return drawable.getDrawable().getContext();
	}

	// ==================== Clearing ====================
	public void clear() {
		endPrimitivesKeepImage();
		drawable.getGL().glClear(GL.GL_COLOR_BUFFER_BIT);
	}
	public void getClearColor(Color4f color) {
		color.set(clearColor);
	}
	public void setClearColor(Color4f color) {
		endPrimitivesKeepImage();
		if(color.equals(clearColor)) return;
		clearColor.set(color);
		applyClearColor();
	}
	private void applyClearColor() {
		drawable.getGL().glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
	}
	
	// ==================== Color ====================
	public void setColor(Color4f color) {
		// glEnd should not be necessary here, but my FireGL V3200 disagrees!
		endPrimitivesKeepImage();
	    this.color.set(color);
	    applyColor();
	}
	public void getColor(Color4f color) {
		color.set(this.color);
	}
	public void applyColor() {
		GL gl = drawable.getGL();
		gl.glColor4f(color.x, color.y, color.z, color.w);
	}
	
	public void setBaseColor(Color4f color) {
		endPrimitivesKeepImage();
		this.baseColor = color;
		applyBaseColor();
	}
	public void getBaseColor(Color4f color) {
		color.set(baseColor);
	}
	private void applyBaseColor() {
		GL gl = drawable.getGL();
	    baseColor.get(baseColorArray);
		gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, baseColorArray, 0);
	}

	// ==================== Images ====================
	public void drawImage(ch.blackspirit.graphics.Image image, float width, float height) {
		this.drawImage(image, width, height, Flip.NONE);
	}
	public void drawImage(ch.blackspirit.graphics.Image image, float width, float height, int subImageX, int subImageY, int subImageWidth, int subImageHeight) {
		this.drawImage(image, width, height, subImageX, subImageY, subImageWidth, subImageHeight, Flip.NONE);
	}

	public void drawImage(ch.blackspirit.graphics.Image image, float width, float height, Flip flip) {
		if (image == null) throw new IllegalArgumentException("image must not be null");
		GL gl = drawable.getGL();
		
		if (!(image instanceof Image)) throw new RuntimeException("Image has not been created by the JOGL Blackspirit Graphics implementation!");
		Image joglImage = (Image)image;
		
		startPrimitive(Primitive.IMAGE, joglImage);
        
		TextureCoords coords = joglImage.texture.getImageTexCoords();
		float texTop = coords.top();
		float texBottom = coords.bottom();
		float texLeft = coords.left();
		float texRight = coords.right();

		
		if(flip == Flip.BOTH || flip == Flip.HORIZONTAL) {
			float temp = texTop;
			texTop = texBottom;
			texBottom = temp;
		}
		if(flip == Flip.BOTH || flip == Flip.VERTICAL) {
			float temp = texLeft;
			texLeft = texRight;
			texRight = temp;
		}
        
		gl.glTexCoord2f(texLeft, texTop);
        gl.glVertex2f(0, 0);

        gl.glTexCoord2f(texRight, texTop);
        gl.glVertex2f(width, 0);

        gl.glTexCoord2f(texRight, texBottom);
        gl.glVertex2f(width, height);
    
        gl.glTexCoord2f(texLeft, texBottom);
        gl.glVertex2f(0, height);
 
	}

	public void drawImage(ch.blackspirit.graphics.Image image, float width, float height, int subImageX, int subImageY, int subImageWidth, int subImageHeight, Flip flip) {
		if (image == null) throw new IllegalArgumentException("image must not be null");
		GL gl = drawable.getGL();
		
		if (!(image instanceof Image)) throw new RuntimeException("Image has not been created by the JOGL Blackspirit Graphics implementation!");
		Image joglImage = (Image)image;

		startPrimitive(Primitive.IMAGE, joglImage);

		TextureCoords coords = joglImage.texture.getImageTexCoords();
		float texTop = coords.top() + (coords.bottom() - coords.top()) / image.getHeight() * subImageY; 
		float texBottom = coords.top() + (coords.bottom() - coords.top()) / image.getHeight() * (subImageY + subImageHeight);
		float texLeft = coords.left() + (coords.right() - coords.left()) / image.getWidth() * subImageX;
		float texRight = coords.left() + (coords.right() - coords.left()) / image.getWidth() * (subImageX + subImageWidth);

		if(flip == Flip.BOTH || flip == Flip.HORIZONTAL) {
			float temp = texTop;
			texTop = texBottom;
			texBottom = temp;
		}
		if(flip == Flip.BOTH || flip == Flip.VERTICAL) {
			float temp = texLeft;
			texLeft = texRight;
			texRight = temp;
		}

		gl.glTexCoord2f(texLeft, texTop);
        gl.glVertex2f(0, 0);

        gl.glTexCoord2f(texRight, texTop);
        gl.glVertex2f(width, 0);

        gl.glTexCoord2f(texRight, texBottom);
        gl.glVertex2f(width, height);

        gl.glTexCoord2f(texLeft, texBottom);
        gl.glVertex2f(0, height);

	}
	
	// ==================== Points ====================
	public void drawPoint(float x, float y) {
		startPrimitive(Primitive.POINT, null);
		GL gl = drawable.getGL();
        gl.glVertex2f(x, y);
	}
	private void applyPointSize() {
		GL gl = drawable.getGL();
		gl.glPointSize(pointDiameter);
	}
	public float getPointRadius() {
		return pointDiameter / 2f;
	}
	public void setPointRadius(float radius) {
		endPrimitivesKeepImage();
		this.pointDiameter = radius * 2f;
		applyPointSize();
	}

	// ==================== Lines ====================
	public void drawLine(float x1, float y1, float x2, float y2) {
        startPrimitive(Primitive.LINE, null);
		GL gl = drawable.getGL();
        gl.glVertex2f(x1, y1);
        gl.glVertex2f(x2, y2);
        
	}
	private void applyLineWidth() {
		GL gl = drawable.getGL();
		gl.glLineWidth(lineWidth);
	}
	private Line[] lineArray = new Line[1];
	public void drawLine(Line line, boolean useColors) {
		if (line == null) throw new IllegalArgumentException("line must not be null");
		lineArray[0] = line;
		drawLines(lineArray, useColors);
	}
	public void drawLines(Line[] lines, boolean useColor) {
		if (lines == null) throw new IllegalArgumentException("lines must not be null");
	    startPrimitive(Primitive.LINE, null);
		GL gl = drawable.getGL();
		Vector2f p;
		if(useColor) {
			Color4f c;
			for(int i = 0; i < lines.length; i++) {
				Line l = lines[i];
				if(l == null) continue;

				p = l.getPoint(0);
				c = l.getColor(0);
				if(c == null) c = color;
				gl.glColor4f(c.x, c.y, c.z, c.w);
			    gl.glVertex2f(p.x, p.y);
	
			    p = l.getPoint(1);
				c = l.getColor(1);
				if(c == null) c = color;
				gl.glColor4f(c.x, c.y, c.z, c.w);
			    gl.glVertex2f(p.x, p.y);
			}
			// reset color;
			applyColor();
		} else {
			for(int i = 0; i < lines.length; i++) {
				Line l = lines[i];
				if(l == null) continue;

				p = l.getPoint(0);
			    gl.glVertex2f(p.x, p.y);
			    
			    p = l.getPoint(1);
			    gl.glVertex2f(p.x, p.y);
			}
		}
	}
	public float getLineWidth() {
		return lineWidth;
	}
	public void setLineWidth(float width) {
		endPrimitivesKeepImage();
		lineWidth = width;
		applyLineWidth();
	}
	
	// ==================== Triangles ====================
	public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        startPrimitive(Primitive.TRIANGLE, null);
		GL gl = drawable.getGL();
        gl.glVertex2f(x1, y1);
        gl.glVertex2f(x3, y3);
        gl.glVertex2f(x2, y2);
	}
	
	private Triangle[] triangleArray = new Triangle[1];
	public void drawTriangle(Triangle triangle, boolean useColors) {
		if (triangle == null) throw new IllegalArgumentException("triangle must not be null");
		triangleArray[0] = triangle;
		drawTriangles(triangleArray, useColors);
	}
	public void drawTriangles(Triangle[] triangles, boolean useColors) {
		if (triangles == null) throw new IllegalArgumentException("triangles must not be null");
	    startPrimitive(Primitive.LINE, null);
		GL gl = drawable.getGL();
		Vector2f p;
		if(useColors) {
			Color4f c;
			for(int i = 0; i < triangles.length; i++) {
				Triangle t = triangles[i];
				if(t == null) continue;
			
				p = t.getPoint(0);
				c = t.getColor(0);
				if(c == null) c = color;
				gl.glColor4f(c.x, c.y, c.z, c.w);
			    gl.glVertex2f(p.x, p.y);
	
			    p = t.getPoint(1);
				c = t.getColor(1);
				if(c == null) c = color;
				gl.glColor4f(c.x, c.y, c.z, c.w);
			    gl.glVertex2f(p.x, p.y);

				p = t.getPoint(1);
				c = t.getColor(1);
				if(c == null) c = color;
				gl.glColor4f(c.x, c.y, c.z, c.w);
			    gl.glVertex2f(p.x, p.y);
	
			    p = t.getPoint(2);
				c = t.getColor(2);
				if(c == null) c = color;
				gl.glColor4f(c.x, c.y, c.z, c.w);
			    gl.glVertex2f(p.x, p.y);

				p = t.getPoint(2);
				c = t.getColor(2);
				if(c == null) c = color;
				gl.glColor4f(c.x, c.y, c.z, c.w);
			    gl.glVertex2f(p.x, p.y);
	
			    p = t.getPoint(0);
				c = t.getColor(0);
				if(c == null) c = color;
				gl.glColor4f(c.x, c.y, c.z, c.w);
			    gl.glVertex2f(p.x, p.y);
			}
			// reset color;
			applyColor();
		} else {
			for(int i = 0; i < triangles.length; i++) {
				Triangle t = triangles[i];
				if(t == null) continue;
				
				p = t.getPoint(0);
			    gl.glVertex2f(p.x, p.y);
	
			    p = t.getPoint(1);
			    gl.glVertex2f(p.x, p.y);

				p = t.getPoint(1);
			    gl.glVertex2f(p.x, p.y);
	
			    p = t.getPoint(2);
			    gl.glVertex2f(p.x, p.y);

				p = t.getPoint(2);
			    gl.glVertex2f(p.x, p.y);
	
			    p = t.getPoint(0);
			    gl.glVertex2f(p.x, p.y);
			}
		}
	}

	public void fillTriangles(Triangle[] triangles, boolean useColors) {
		if (triangles == null) throw new IllegalArgumentException("triangles must not be null");
		GL gl = drawable.getGL();

		Vector2f p;
		
		startPrimitive(Primitive.TRIANGLE, null);

		if(useColors) {
			Color4f c = null;
			for(int i = 0; i < triangles.length; i++) {
				Triangle t = triangles[i];
				if(t == null) continue;

				p = t.getPoint(0);
				c = t.getColor(0);
				if(c == null) c = color;
				gl.glColor4f(c.x, c.y, c.z, c.w);
		        gl.glVertex2f(p.x, p.y);
				
		        p = t.getPoint(1);
				c = t.getColor(1);
				if(c == null) c = color;
				gl.glColor4f(c.x, c.y, c.z, c.w);
		        gl.glVertex2f(p.x, p.y);
				
		        p = t.getPoint(2);
				c = t.getColor(2);
				if(c == null) c = color;
				gl.glColor4f(c.x, c.y, c.z, c.w);
		        gl.glVertex2f(p.x, p.y);
			}
			// reset color;
			applyColor();
		} else {
			for(int i = 0; i < triangles.length; i++) {
				Triangle t = triangles[i];
				if(t == null) continue;
				p = t.getPoint(0);
		        gl.glVertex2f(p.x, p.y);
				p = t.getPoint(1);
		        gl.glVertex2f(p.x, p.y);
				p = t.getPoint(2);
		        gl.glVertex2f(p.x, p.y);
			}
		}
	}
	public void fillTriangle(Triangle triangle, boolean useColors) {
		if (triangle == null) throw new IllegalArgumentException("triangle must not be null");
		triangleArray[0] = triangle;
		fillTriangles(triangleArray, useColors);
	}
	public void fillTriangle(Triangle triangle, boolean useColors, ch.blackspirit.graphics.Image image) {
		if (triangle == null) throw new IllegalArgumentException("triangle must not be null");
		triangleArray[0] = triangle;
		fillTriangles(triangleArray, useColors, image);
	}
	
	// TODO make faster using glDrawArrays (arrays of 100 triangles?) => much less native calls (http://www.opengl.org/sdk/docs/man/)
	// TODO texture width, height calls only once
	public void fillTriangles(Triangle[] triangles, boolean useColors, ch.blackspirit.graphics.Image image) {
		if (triangles == null) throw new IllegalArgumentException("triangles must not be null");
		if (image == null) throw new IllegalArgumentException("image must not be null");
		GL gl = drawable.getGL();

		Vector2f p;
		Image joglImage = null;

		joglImage = (Image)image;
		startPrimitive(Primitive.TEXTURED_TRIANGLE, joglImage);

		TextureCoords coords = joglImage.texture.getImageTexCoords();
		
		if(useColors) {
			Color4f c;
	    	for(int i = 0; i < triangles.length; i++) {
				Triangle t = triangles[i];
				if(t == null) continue;

				Vector2f tc1 = t.getTextureCoordinate(0);
				if (tc1 == null) throw new IllegalArgumentException("Texture coordinate for triangle must not be null");
				Vector2f tc2 = t.getTextureCoordinate(1);
				if (tc2 == null) throw new IllegalArgumentException("Texture coordinate for triangle must not be null");
				Vector2f tc3 = t.getTextureCoordinate(2);
				if (tc3 == null) throw new IllegalArgumentException("Texture coordinate for triangle must not be null");
		
				p = t.getPoint(0);
				c = t.getColor(0);
				if(c == null) c = color;
				gl.glColor4f(c.x, c.y, c.z, c.w);
				float texX = coords.left() + (coords.right() - coords.left()) / joglImage.texture.getImageWidth() * tc1.x;
				float texY = coords.top() + (coords.bottom() - coords.top()) / joglImage.texture.getImageHeight() * tc1.y;
				gl.glTexCoord2f(texX, texY);
		        gl.glVertex2f(p.x, p.y);
				
		        p = t.getPoint(1);
				c = t.getColor(1);
				if(c == null) c = color;
				gl.glColor4f(c.x, c.y, c.z, c.w);
				texX = coords.left() + (coords.right() - coords.left()) / joglImage.texture.getImageWidth() * tc2.x;
				texY = coords.top() + (coords.bottom() - coords.top()) / joglImage.texture.getImageHeight() * tc2.y; 
				gl.glTexCoord2f(texX, texY);
		        gl.glVertex2f(p.x, p.y);
				
		        p = t.getPoint(2);
				c = t.getColor(2);
				if(c == null) c = color;
				gl.glColor4f(c.x, c.y, c.z, c.w);
				texX = coords.left() + (coords.right() - coords.left()) / joglImage.texture.getImageWidth() * tc3.x;
				texY = coords.top() + (coords.bottom() - coords.top()) / joglImage.texture.getImageHeight() * tc3.y; 
				gl.glTexCoord2f(texX, texY);
		        gl.glVertex2f(p.x, p.y);
		
	    	}
		} else {
	    	for(int i = 0; i < triangles.length; i++) {
				Triangle t = triangles[i];
				if(t == null) continue;
	
				Vector2f tc1 = t.getTextureCoordinate(0);
				if (tc1 == null) throw new IllegalArgumentException("Texture coordinate for triangle must not be null");
				Vector2f tc2 = t.getTextureCoordinate(1);
				if (tc2 == null) throw new IllegalArgumentException("Texture coordinate for triangle must not be null");
				Vector2f tc3 = t.getTextureCoordinate(2);
				if (tc3 == null) throw new IllegalArgumentException("Texture coordinate for triangle must not be null");

				p = t.getPoint(0);
				float texX = coords.left() + (coords.right() - coords.left()) / joglImage.texture.getImageWidth() * tc1.x;
				float texY = coords.top() + (coords.bottom() - coords.top()) / joglImage.texture.getImageHeight() * tc1.y;
				gl.glTexCoord2f(texX, texY);
		        gl.glVertex2f(p.x, p.y);
				
		        p = t.getPoint(1);
				texX = coords.left() + (coords.right() - coords.left()) / joglImage.texture.getImageWidth() * tc2.x;
				texY = coords.top() + (coords.bottom() - coords.top()) / joglImage.texture.getImageHeight() * tc2.y; 
				gl.glTexCoord2f(texX, texY);
		        gl.glVertex2f(p.x, p.y);
				
		        p = t.getPoint(2);
				texX = coords.left() + (coords.right() - coords.left()) / joglImage.texture.getImageWidth() * tc3.x;
				texY = coords.top() + (coords.bottom() - coords.top()) / joglImage.texture.getImageHeight() * tc3.y; 
				gl.glTexCoord2f(texX, texY);
		        gl.glVertex2f(p.x, p.y);
		
	    	}
		}
	}

	// ==================== Text ====================
	public void drawText(String text) {
		if (text == null) throw new IllegalArgumentException("text must not be null");
		endPrimitives();
		TextRenderer textRenderer = resourceManager.getTextRenderer(font);
		textRenderer.begin3DRendering();
		applyColor();
		setTextTransformation();
		textRenderer.draw(text, 0, 0);
		textRenderer.end3DRendering();
		setTransform();
	}
	public void getTextBounds(String text, Rectangle2D bounds) {
		if (text == null) throw new IllegalArgumentException("text must not be null");
		if (bounds == null) throw new IllegalArgumentException("bounds must not be null");
		resourceManager.getTextRenderer(font).getBounds(text);
	}
	
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	
	// ==================== Buffer content copying ====================
	public void copyToImage(ch.blackspirit.graphics.Image image) {
		Image joglImage = (Image)image;
		startPrimitive(null, joglImage);
		
		int cwidth = drawable.getDrawable().getWidth();
		int cheight = drawable.getDrawable().getHeight();

		int width = image.getWidth();
		int height = image.getHeight();
		
		if(width > cwidth) width = cwidth;
		if(height > cheight) height = cheight;
		
		// copy buffer to image
		drawable.getGL().glCopyTexSubImage2D(
				joglImage.texture.getTarget(), 0, 
				0, 0, 0, cheight - height, width, height);
	}

	public void copyToImage(ch.blackspirit.graphics.Image image, int x, int y, int width, int height) {
		Image joglImage = (Image)image;
		startPrimitive(null, joglImage);
		
		int cwidth = drawable.getDrawable().getWidth();
		int cheight = drawable.getDrawable().getHeight();

		if(x >= cwidth) return;
		if(y >= cheight) return;
		
		if(width + x > cwidth) width = cwidth - x;
		if(height + y > cheight) height = cheight - y;
		
		// copy buffer to image
		drawable.getGL().glCopyTexSubImage2D(
				joglImage.texture.getTarget(), 0, 
				0, image.getHeight() - y, x, cheight - height - y, width, height);
	}
	
	// ==================== Transformation ====================
	public void setCamera(float translationX, float translationY, float angle) {
		this.translationX = translationX;
		this.translationY = translationY;
		this.angle = angle;
		setTransform();
	}

	public void rotate(float angle) {
		endPrimitivesKeepImage();
		Rotation rotation = rotations.get();
		rotation.setAngle(angle);
		transforms.add(rotation);
		rotation.apply(drawable.getGL());
	}
	public void translate(float x, float y) {
		endPrimitivesKeepImage();
		Translation translation = translations.get();
		translation.setTranslateX(-x);
		translation.setTranslateY(-y);
		transforms.add(translation);
		translation.apply(drawable.getGL());
	}
	public void scale(float x, float y) {
		endPrimitivesKeepImage();
		Scale scale = scales.get();
		scale.setScaleX(x);
		scale.setScaleY(y);
		transforms.add(scale);
		scale.apply(drawable.getGL());
	}
	public void clearTransformation() {
		this.clearTransform();
	}
	
	public void clearTransform() {
		for(int i = 0; i < transforms.size(); i++) {
			Transformation t = transforms.get(i);
			if(t instanceof Rotation) rotations.free((Rotation)t);
			else if(t instanceof Translation) translations.free((Translation)t);
			else if(t instanceof Scale) scales.free((Scale)t);
		}
		transforms.clear();
		transformStack.clear();
		setTransform();
	}

	public void popTransform() {
		if (transformStack.isEmpty()) {
			throw new RuntimeException("No transformation left to pop from transform stack!");
		}
		int last = transformStack.remove(transformStack.size() - 1);
		for (int i = transforms.size() - 1; i >= last; i--) {
			Transformation t = transforms.remove(i);
			if(t instanceof Rotation) rotations.free((Rotation)t);
			else if(t instanceof Translation) translations.free((Translation)t);
			else if(t instanceof Scale) scales.free((Scale)t);
		}
		setTransform();
	}

	public void pushTransform() {
		transformStack.add(transforms.size());
	}
	
	private void setTransform() {
		endPrimitivesKeepImage();
		GL gl = drawable.getGL();
		
		// Reset
		gl.glLoadIdentity();
		
		// Rotate to correct view
		gl.glRotatef(180f, 1, 0, 0);
		gl.glRotatef(angle, 0, 0, 1);

		// Translate to camera
		gl.glTranslatef(translationX, translationY, 0);

		// Recreate correct transformation
		for(int i = 0, l = transforms.size(); i < l; i++) {
			transforms.get(i).apply(gl);
		}
	}
	private void setTextTransformation() {
		endPrimitives();
		GL gl = drawable.getGL();
		
		// Reset
		gl.glLoadIdentity();
		// Rotate to correct view
		gl.glRotatef(180f, 1, 0, 0);
		// Translate to camera
		gl.glTranslatef(translationX, translationY, 0);
		
		// Recreate correct transformation
		for(int i = 0; i < transforms.size(); i++) {
			transforms.get(i).apply(gl);
		}
		gl.glScalef(1, -1, 1);
	}

	// ==================== Drawing Settings ====================
	public boolean getRedMask() {
		return redMask;
	}
	public void setRedMask(boolean red) {
		endPrimitivesKeepImage();
		this.redMask = red;
		applyColorMask();
	}
	public boolean getGreenMask() {
		return greenMask;
	}
	public void setGreenMask(boolean green) {
		endPrimitivesKeepImage();
		this.greenMask = green;
		applyColorMask();
	}
	public boolean getBlueMask() {
		return blueMask;
	}
	public void setBlueMask(boolean blue) {
		endPrimitivesKeepImage();
		this.blueMask = blue;
		applyColorMask();
	}
	public boolean getAlphaMask() {
		return alphaMask;
	}
	public void setAlphaMask(boolean alpha) {
		endPrimitivesKeepImage();
		this.alphaMask = alpha;
		applyColorMask();
	}
	public void setColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
		endPrimitivesKeepImage();
		this.redMask = red;
		this.greenMask = green;
		this.blueMask = blue;
		this.alphaMask = alpha;
		applyColorMask();
	}
	public void applyColorMask() {
		drawable.getGL().glColorMask(redMask, greenMask, blueMask, alphaMask);
	}

	public DrawingMode getDrawingMode() {
		return this.drawingMode;
	}
	public void setDrawingMode(DrawingMode drawingMode) {
		endPrimitivesKeepImage();
		this.drawingMode = drawingMode; 
		applyDrawingMode();
	}
	public void applyDrawingMode() {
		boolean isGlExtBlendSubtractSupported = properties.getPropertyBoolean(Properties.IS_DRAWING_MODE_SUBTRACT_SUPPORTED);
		GL gl = drawable.getGL();
		if(drawingMode == DrawingMode.ALPHA_BLEND) {
			if(isGlExtBlendSubtractSupported) {
				gl.glBlendEquation(GL.GL_FUNC_ADD);
			}
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		} else if(drawingMode == DrawingMode.ADD) {
			if(isGlExtBlendSubtractSupported) {
				gl.glBlendEquation(GL.GL_FUNC_ADD);
			}
			gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
		} else if(drawingMode == DrawingMode.ALPHA_ADD) {
			if(isGlExtBlendSubtractSupported) {
				gl.glBlendEquation(GL.GL_FUNC_ADD);
			}
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
		} else if(drawingMode == DrawingMode.MULTIPLY) {
			if(isGlExtBlendSubtractSupported) {
				gl.glBlendEquation(GL.GL_FUNC_ADD);
			}
			gl.glBlendFunc(GL.GL_DST_COLOR, GL.GL_ZERO);
		} else if(drawingMode == DrawingMode.OVERWRITE) {
			if(isGlExtBlendSubtractSupported) {
				gl.glBlendEquation(GL.GL_FUNC_ADD);
			}
			gl.glBlendFunc(GL.GL_ONE, GL.GL_ZERO);
		} else if(drawingMode == DrawingMode.SUBTRACT) {
			// This uses an extension!!
			if(isGlExtBlendSubtractSupported) {
				gl.glBlendEquation(GL.GL_FUNC_REVERSE_SUBTRACT);
			} else {
				throw new UnsupportedOperationException("Subtract drawing mode is not supported.");
			}
			gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
		}
	} 
	
	//	public void setPolygonAntialiasing(boolean enabled) {
	//	polygonAntialiasing = enabled;
	//	applyPolygonAntialiasing();
	//}
	public boolean getPolygonAntialiasing() {
		return false;//polygonAntialiasing;
	}
	private void applyPolygonAntialiasing() {
		GL gl = drawable.getGL();
		// !! Enabling this causes problems on most GFX Cards (though not on my FireGL)
		gl.glDisable(GL.GL_POLYGON_SMOOTH);
		gl.glHint(GL.GL_POLYGON_SMOOTH_HINT, GL.GL_NICEST);
	}
	private void applyPointAntialiasing() {
		GL gl = drawable.getGL();
		gl.glEnable(GL.GL_POINT_SMOOTH);
		gl.glHint(GL.GL_POINT_SMOOTH_HINT, GL.GL_NICEST);
	}
	
	public void setLineAntialiasing(boolean enabled) {
		endPrimitivesKeepImage();
		lineAntialiasing = enabled;
		applyLineAntialiasing();
	}
	public boolean getLineAntialiasing() {
		return lineAntialiasing;
	}
	private void applyLineAntialiasing() {
		GL gl = drawable.getGL();
		if(lineAntialiasing) {
			gl.glEnable(GL.GL_LINE_SMOOTH);
		} else {
			gl.glDisable(GL.GL_LINE_SMOOTH);
		}
		gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
}
	
	// ==================== Drawing State ====================
	public void endFrame() {
		endPrimitives();	
		clearTransformation();
	}
	public void endPrimitivesKeepImage() {
		startPrimitive(null, drawable.getLastImage());
	}
	public void endPrimitives() {
		startPrimitive(null, null);
	}
	private boolean ended = true;
	private void startPrimitive(Primitive primitive, Image image) {
		// end last primitive if necessary
		if(drawable.getLastPrimitive() != null) {
			// LINES AND POINTS NOT CURRENTLY OPTIMIZED
			if(primitive != drawable.getLastPrimitive() || 
					primitive == Primitive.LINE || primitive == Primitive.POINT || primitive == Primitive.TEXTURED_TRIANGLE) {
				drawable.getGL().glEnd();
				ended = true;
			} 
		}
		// handle image binding and target enabling/disabling 
		if(image != drawable.getLastImage()) {
			if(image != null && image.texture == null) {
				try {
					resourceManager.cache(image);
				} catch (IOException e) {
					throw new RuntimeException("Error caching image. Do manual caching to prevent such errors during rendering.", e);
				}
			}
			if(drawable.getLastImage() != null && image != null) {
				if(drawable.getLastImage().texture.getTarget() != image.texture.getTarget()) {
					drawable.getLastImage().texture.disable();
					image.texture.enable();
				}
			} else {
				if(drawable.getLastImage() != null) { 
					drawable.getLastImage().texture.disable();
				}
				if(image != null) {
					image.texture.enable();
				}
			}
			if(image != null) {
				image.texture.bind();
			}
		}
		// start new primitive if necessary
		if(primitive != null && ended) {
			GL gl = drawable.getGL();
			if(primitive == Primitive.POINT) {
				gl.glBegin(GL.GL_POINTS);
			}
			if(primitive == Primitive.LINE) {
				gl.glBegin(GL.GL_LINES);
			}
			if(primitive == Primitive.TRIANGLE || primitive == Primitive.TEXTURED_TRIANGLE) {
				gl.glBegin(GL.GL_TRIANGLES);
			}
			if(primitive == Primitive.IMAGE) {
				gl.glBegin(GL.GL_QUADS);
			}
			
			ended = false;
		}
		drawable.setLastImage(image);
		drawable.setLastPrimitive(primitive);
	}
}
