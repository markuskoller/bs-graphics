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
package ch.blackspirit.graphics.jogl2;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.glu.GLU;

import ch.blackspirit.graphics.Canvas;

import com.jogamp.opengl.util.texture.TextureCoords;

/**
 * @author Markus Koller
 */
final class BufferRegionUpdate implements GLExecutable {
	public Image image;
	public int x;
	public int y;
	public int width;
	public int height;
	
	
    private int[] rowLength = new int[1];
    private int[] skipRows = new int[1];
    private int[] skipPixels = new int[1];

    private final Canvas canvas;
    
    public BufferRegionUpdate(Canvas canvas) {
    	this.canvas = canvas;
    }
    
    // FIXME Images loaded with TextureIO having SGI or TGA texture will be upside down in buffer.. remove them from TextureIO and add TGA support using TGAImage
    // TODO Do full buffer update using glGetTexImage for speed (glGetTexSubImage does not exist!)
	public void execute(GLDrawable drawable, GL2 gl) {
	    if(image.texture == null) throw new RuntimeException("Buffer update only possible on cached images");
	    if(!image.isBuffered()) throw new RuntimeException("Buffer update only possible on buffered images");
	    
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

	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glLoadIdentity();
	    GLU glu = new GLU();
	    glu.gluOrtho2D(0, drawable.getWidth(), drawable.getHeight(), 0);
	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    gl.glLoadIdentity();

	    // reset base color
	    float[] baseColorArray = new float[]{1,1,1,1};
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, baseColorArray, 0);

		// reset drawing mode
		boolean isGlExtBlendSubtractSupported = canvas.getPropertyBoolean(Properties.IS_DRAWING_MODE_SUBTRACT_SUPPORTED);
		if(isGlExtBlendSubtractSupported) {
			gl.glBlendEquation(GL.GL_FUNC_ADD);
		}
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

		// reset color mask
		gl.glColorMask(true, true, true, true);
		
		image.texture.enable(gl);
		image.texture.bind(gl);

		// Slow but needed!
		gl.glClearColor(0, 0, 0, 0);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		gl.glBegin(GL.GL_TRIANGLE_STRIP);
		gl.glColor4f(1, 1, 1, 1);
		
		TextureCoords coords = image.texture.getImageTexCoords();
        
		gl.glTexCoord2f(coords.left(), coords.top());
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(coords.right(), coords.top());
        gl.glVertex2f(image.getWidth(), 0);
        gl.glTexCoord2f(coords.left(), coords.bottom());
        gl.glVertex2f(0, image.getHeight());
        gl.glTexCoord2f(coords.right(), coords.bottom());
        gl.glVertex2f(image.getWidth(), image.getHeight());

        gl.glEnd();
        
        gl.glGetIntegerv(GL2.GL_PACK_ROW_LENGTH,  rowLength,  0); // save row length
        gl.glGetIntegerv(GL2.GL_PACK_SKIP_ROWS,   skipRows,   0); // save skipped rows
        gl.glGetIntegerv(GL2.GL_PACK_SKIP_PIXELS, skipPixels, 0); // save skipped pixels

        // ! be careful with pixelstorei.. using wrong values causes null pointers in underlying dll ! 
        gl.glPixelStorei(GL2.GL_PACK_ROW_LENGTH, image.getWidth());
        gl.glPixelStorei(GL2.GL_PACK_SKIP_PIXELS, x);
        gl.glPixelStorei(GL2.GL_PACK_SKIP_ROWS, y);
        
		image.texture.disable(gl);

		if(image.getBufferType() == BufferTypes.RGBA_4Byte) {
    		gl.glReadPixels(x, drawable.getHeight() - (image.getHeight() - y), width, height, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, image.byteBuffer);
        } else {
    		gl.glReadPixels(x, drawable.getHeight() - (image.getHeight() - y), width, height, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, image.byteBuffer);
        }

        gl.glPixelStorei(GL2.GL_PACK_ROW_LENGTH,  rowLength[0]);  // restore row length
        gl.glPixelStorei(GL2.GL_PACK_SKIP_ROWS,   skipRows[0]);   // restore skipped rows
        gl.glPixelStorei(GL2.GL_PACK_SKIP_PIXELS, skipPixels[0]); // restore skipped pixels
	}
}
