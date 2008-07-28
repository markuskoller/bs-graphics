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

import javax.media.opengl.GL;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.texture.TextureCoords;

/**
 * @author Markus Koller
 */
class BufferUpdate implements GLExecutable {
	public Image image;
	
	public void execute(GLDrawable drawable, GL gl) {
		if(AbstractGraphicsContext.isDrawing()) throw new RuntimeException("Image buffer must not be updated while drawing any graphics context");
	    if(image.texture == null) return;

	    gl.glDisable(GL.GL_DEPTH_TEST);
	    gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	    
	    // Lighting, Coloring
	    gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_BLEND);
	    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	    gl.glEnable(GL.GL_COLOR_MATERIAL);
	    gl.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE);
	    gl.glShadeModel(GL.GL_SMOOTH);

		// Texturing
	    gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE);

	    gl.glMatrixMode(GL.GL_PROJECTION);
	    gl.glLoadIdentity();
	    GLU glu = new GLU();
	    glu.gluOrtho2D(0, drawable.getWidth(), drawable.getHeight(), 0);
	    gl.glMatrixMode(GL.GL_MODELVIEW);
	    gl.glLoadIdentity();
	    

		image.texture.enable();
		image.texture.bind();
		gl.glColor4f(1, 1, 1, 1);
		
		gl.glBegin(GL.GL_QUADS);
		
		TextureCoords coords = image.texture.getImageTexCoords();
        
		gl.glTexCoord2f(coords.left(), coords.top());
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(coords.right(), coords.top());
        gl.glVertex2f(image.getWidth(), 0);
        gl.glTexCoord2f(coords.right(), coords.bottom());
        gl.glVertex2f(image.getWidth(), image.getHeight());
        gl.glTexCoord2f(coords.left(), coords.bottom());
        gl.glVertex2f(0, image.getHeight());

        gl.glEnd();
        
        if(image.getBufferType() == BufferTypes.RGBA_4Byte) {
    		gl.glReadPixels(0, 0, image.getWidth(), image.getHeight(), GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, image.byteBuffer);
        } else {
    		gl.glReadPixels(0, 0, image.getWidth(), image.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, image.byteBuffer);
        }
		
		image.texture.disable();
	}
}
