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

import javax.media.opengl.GLAutoDrawable;

/**
 * @author Markus Koller
 */
final class GLExecutableGLEventListener extends AbstractGLEventListener {
	GLExecutable executable;

	public void display(GLAutoDrawable drawable) {
		debug(drawable);
		if(executable == null) throw new RuntimeException("Internal error, GLExecutable unexpectedly null");
		executable.execute(drawable, drawable.getGL());
	}
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}
	public void init(GLAutoDrawable drawable) {}
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
	
}