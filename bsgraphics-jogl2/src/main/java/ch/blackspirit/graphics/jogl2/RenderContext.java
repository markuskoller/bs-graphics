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
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 * @author Markus Koller
 */
final class RenderContext  {
	private GLAutoDrawable drawable;
	private GLEventListener glEventListener;
	private GLEventListener mainGlEventListener;
	private RenderContext delegateRenderContext;

	// State
	private Primitive lastPrimitive = null;
	private Image lastImage = null;
	
	public RenderContext() {
		super();
	}
	public void setDrawable(GLAutoDrawable drawable) {
		if(delegateRenderContext != null) delegateRenderContext.setDrawable(drawable);
		else {
			if(this.drawable == drawable) return;
			if(this.drawable != null && glEventListener != null) {
				this.drawable.removeGLEventListener(glEventListener);
			}
			this.drawable = drawable;
			if(drawable != null && glEventListener != null) {
				drawable.addGLEventListener(glEventListener);
			}
		}
	}
	public GLAutoDrawable getDrawable() {
		if(delegateRenderContext != null) return delegateRenderContext.getDrawable();
		else return drawable;
	}
	public GL getGL() {
		if(delegateRenderContext != null) return delegateRenderContext.getGL();
		else return drawable.getContext().getGL();
	}
	public void setGLEventListener(GLEventListener glEventListener) {
		if(delegateRenderContext != null) delegateRenderContext.setGLEventListener(glEventListener);
		else {
			if(glEventListener == this.glEventListener) return;
			if(drawable != null && this.glEventListener != null) {
				drawable.removeGLEventListener(this.glEventListener);
			}
			if(drawable != null) {
				if(glEventListener != null)	drawable.addGLEventListener(glEventListener);
			}
			this.glEventListener = glEventListener;
		}
	}
	public GLEventListener getGLEventListener() {
		if(delegateRenderContext != null) return delegateRenderContext.getGLEventListener();
		else return glEventListener;
	}

	public void setMainGLEventListener(GLEventListener glEventListener) {
		if(delegateRenderContext != null) delegateRenderContext.setMainGLEventListener(glEventListener);
		this.mainGlEventListener = glEventListener;
		setGLEventListener(mainGlEventListener);
	}
	public GLEventListener getMainGLEventListener() {
		if(delegateRenderContext != null) return delegateRenderContext.getMainGLEventListener();
		else return mainGlEventListener;
	}
	public void resetGLEventListener() {
		if(delegateRenderContext != null) delegateRenderContext.resetGLEventListener();
		if(mainGlEventListener != null)	setGLEventListener(mainGlEventListener);
	}
	

	public RenderContext getDelegateRenderContext() {
		return delegateRenderContext;
	}
	public void setDelegateRenderContext(RenderContext delegateRenderContext) {
		this.delegateRenderContext = delegateRenderContext;
	}
	
	public Primitive getLastPrimitive() {
		if(delegateRenderContext != null) return delegateRenderContext.getLastPrimitive();
		else return lastPrimitive;
	}
	public void setLastPrimitive(Primitive lastPrimitive) {
		if(delegateRenderContext != null) delegateRenderContext.setLastPrimitive(lastPrimitive); 
		else this.lastPrimitive = lastPrimitive;
	}
	public Image getLastImage() {
		if(delegateRenderContext != null) return delegateRenderContext.getLastImage();
		else return lastImage;
	}
	public void setLastImage(Image lastImage) {
		if(delegateRenderContext != null) delegateRenderContext.setLastImage(lastImage);
		else this.lastImage = lastImage;
	}
	
	public void destroy() {
		drawable.getContext().destroy();
		drawable.destroy();
	}
}
