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

import javax.media.opengl.DebugGL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.TraceGL;

/**
 * GLEventListener base class handling insertion of DebugGL and TraceGL.
 * @author Markus Koller
 */
abstract class AbstractGLEventListener implements GLEventListener {
	private boolean traceGL = false;
	private boolean debugGL = false;
	
	protected void debug(GLAutoDrawable drawable) { 
		// Install debugging and tracing in the GL pipeline
		if(traceGL) {
			if(debugGL) {
				if(!(drawable.getContext().getGL() instanceof TraceGL)) {
					drawable.getContext().setGL(new TraceGL(new DebugGL(drawable.getContext().getGL()), System.out));
				}
				if(!(drawable.getGL() instanceof TraceGL)) {
					drawable.setGL(new TraceGL(new DebugGL(drawable.getGL()), System.out));
				}
				
			} else {
				if(!(drawable.getContext().getGL() instanceof TraceGL)) {
					drawable.getContext().setGL(new TraceGL(drawable.getContext().getGL(), System.out));
				}
				if(!(drawable.getGL() instanceof TraceGL)) {
					drawable.setGL(new TraceGL(drawable.getGL(), System.out));
				}
			}
		} else if(debugGL) {
			if(!(drawable.getContext().getGL() instanceof DebugGL)) {
				drawable.getContext().setGL(new DebugGL(drawable.getContext().getGL()));
			}
			if(!(drawable.getGL() instanceof DebugGL)) {
				drawable.setGL(new DebugGL(drawable.getGL()));
			}
		}
	}

	public boolean isTraceGL() {
		return traceGL;
	}
	public void setTraceGL(boolean traceGL) {
		this.traceGL = traceGL;
	}
	public boolean isDebugGL() {
		return debugGL;
	}
	public void setDebugGL(boolean debugGL) {
		this.debugGL = debugGL;
	}
}