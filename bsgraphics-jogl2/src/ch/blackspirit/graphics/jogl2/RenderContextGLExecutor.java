package ch.blackspirit.graphics.jogl2;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;

public class RenderContextGLExecutor implements GLExecutor {
	private GLExecutableGLEventListener executableListener = new GLExecutableGLEventListener();
	private RenderContext renderContext;
	
	public RenderContextGLExecutor(RenderContext renderContext, CanvasProperties properties) {
		super();
		this.renderContext = renderContext;
        executableListener.setDebugGL(properties.isDebugGL());
        executableListener.setTrace(properties.isTraceEnabled());
        executableListener.setTraceLevel(properties.getTraceLogLevel());
	}

	public boolean execute(GLExecutable glExecutable) {
		try {
			GL2 gl = (GL2)GLU.getCurrentGL();
			glExecutable.execute(GLContext.getCurrent().getGLDrawable(), gl);
		} catch(GLException e) {
			// no context current
			try {
				GLAutoDrawable imageDrawable = renderContext.getDrawable();
				imageDrawable.setAutoSwapBufferMode(false);
				renderContext.setGLEventListener(executableListener);
				executableListener.executable = glExecutable;
				imageDrawable.display();
				imageDrawable.setAutoSwapBufferMode(true);
				renderContext.resetGLEventListener();
			} catch(GLException e2) {
				// TODO This maybe is a real exception due to a problem not just because no canvas is visible
				return false;
			}
		}
		return true;
	}
}
