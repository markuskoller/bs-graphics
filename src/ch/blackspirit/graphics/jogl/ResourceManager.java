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

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.opengl.GL;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLException;

import ch.blackspirit.graphics.Canvas;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.pool.ObjectPool;

import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

/**
 * @author Markus Koller
 */
final class ResourceManager implements
		ch.blackspirit.graphics.ResourceManager {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	private HashSet<ch.blackspirit.graphics.jogl.Image> cachedImages = 
		new HashSet<ch.blackspirit.graphics.jogl.Image>();
	
	private HashSet<Font> cachedFonts = 
		new HashSet<Font>();
	private HashMap<Font, TextRenderer> textRenderers = 
		new HashMap<Font, TextRenderer>();

	private GLExecutor glExecutor;

	private BufferRegionUpdate bufferRegionUpdate;

	private ObjectPool<FreeImage> freeImagePool = new ObjectPool<FreeImage>(new FreeImage(), 10);
	private ObjectPool<FreeImages> freeImagesPool = new ObjectPool<FreeImages>(new FreeImages(), 10);
	private ObjectPool<CacheImage> cacheImagePool = new ObjectPool<CacheImage>(new CacheImage(), 10);

	private ObjectPool<FreeFont> freeFontPool = new ObjectPool<FreeFont>(new FreeFont(), 10);
	private ObjectPool<FreeFonts> freeFontsPool = new ObjectPool<FreeFonts>(new FreeFonts(), 10);
	private ObjectPool<CacheFont> cacheFontPool = new ObjectPool<CacheFont>(new CacheFont(), 10);
	
	private ObjectPool<UpdateCache> updateCachePool = new ObjectPool<UpdateCache>(new UpdateCache(), 10);
	private ObjectPool<UpdateCacheRegion> updateCacheRegionPool = new ObjectPool<UpdateCacheRegion>(new UpdateCacheRegion(), 10);
	
	private ArrayList<GLExecutable> failedExecutables = new ArrayList<GLExecutable>(100);
	
	public ResourceManager(GLExecutor glExecutor, Canvas canvas) {
		this.glExecutor = glExecutor;
		this.bufferRegionUpdate = new BufferRegionUpdate(canvas);
	}
	
	void cleanup() {
		int failed = failedExecutables.size();
		for(int i = 0; i < failed; i++) {
			GLExecutable executable = failedExecutables.get(i);
			if(!glExecutor.execute(executable)) {
				throw new RuntimeException("Error executing GLExecutables");
			}
			// TODO do that better! (Pool aware objects having a free method!)
			if(executable instanceof UpdateCache) updateCachePool.free((UpdateCache)executable);
			else if(executable instanceof UpdateCacheRegion) updateCacheRegionPool.free((UpdateCacheRegion)executable);
			else if(executable instanceof FreeImage) freeImagePool.free((FreeImage)executable);
			else if(executable instanceof FreeImages) freeImagesPool.free((FreeImages)executable);
			else if(executable instanceof CacheImage) cacheImagePool.free((CacheImage)executable);
			else if(executable instanceof FreeFont) freeFontPool.free((FreeFont)executable);
			else if(executable instanceof FreeFonts) freeFontsPool.free((FreeFonts)executable);
			else if(executable instanceof CacheFont) cacheFontPool.free((CacheFont)executable);
		}
		failedExecutables.clear();
	}
	
	void updateCache(ch.blackspirit.graphics.jogl.Image image) {
		UpdateCache executable = updateCachePool.get();
		executable.resourceManager = this;
		executable.image = image;
		if(!glExecutor.execute(executable)) {
			failedExecutables.add(executable);
		} else {
			updateCachePool.free(executable);
		}
	}
	void updateCacheRegion(ch.blackspirit.graphics.jogl.Image image, int offsetX, int offsetY, int width, int height) {
		UpdateCacheRegion executable = updateCacheRegionPool.get();
		executable.resourceManager = this;
		executable.image = image;
		executable.offsetX = offsetX;
		executable.offsetY = offsetY;
		executable.width = width;
		executable.height = height;
		if(!glExecutor.execute(executable)) {
			failedExecutables.add(executable);
		} else {
			updateCacheRegionPool.free(executable);
		}
	}

	public void freeFont(Font font) {
		FreeFont executable = freeFontPool.get();
		executable.resourceManager = this;
		executable.font = font;
		if(!glExecutor.execute(executable)) {
			failedExecutables.add(executable);
		} else {
			freeFontPool.free(executable);
		}
	}
	public void freeFonts() {
		FreeFonts executable = freeFontsPool.get();
		executable.resourceManager = this;
		if(!glExecutor.execute(executable)) {
			failedExecutables.add(executable);
		} else {
			freeFontsPool.free(executable);
		}
	}
	public boolean cacheFont(Font font) {
		CacheFont executable = cacheFontPool.get();
		executable.resourceManager = this;
		executable.font = font;
		if(!glExecutor.execute(executable)) {
			failedExecutables.add(executable);
			if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Caching font failed: " + font);
			return false;
		} else {
			cacheFontPool.free(executable);
			if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Successfully cached font: " + font);
			return true;
		}
	}

	public void freeImage(ch.blackspirit.graphics.Image image) {
		FreeImage executable = freeImagePool.get();
		executable.resourceManager = this;
		executable.image = image;
		if(!glExecutor.execute(executable)) {
			failedExecutables.add(executable);
		} else {
			freeImagePool.free(executable);
		}
	}
	public void freeImages() {
		FreeImages executable = freeImagesPool.get();
		executable.resourceManager = this;
		if(!glExecutor.execute(executable)) {
			failedExecutables.add(executable);
		} else {
			freeImagesPool.free(executable);
		}
	}
	public boolean cacheImage(Image image) throws IOException {
		CacheImage executable = cacheImagePool.get();
		executable.resourceManager = this;
		executable.image = image;
		boolean success = glExecutor.execute(executable);
		if(executable.exception != null) throw executable.exception;
		if(!success) {
			failedExecutables.add(executable);
			if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Caching image failed: " + image.toString());
		} else {
			cacheImagePool.free(executable);
			if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Successfully cached image: " + image.toString());
		}
		return success;
	}

	
	void refreshCache() {
		refreshImageCache();
		refreshFontCache();
	}

	/* -------------------- Font Management -------------------- */
	private void refreshFontCache() {
		Set<Font> fonts = new HashSet<Font>(cachedFonts);
		freeFontCache();
		for(Font font: fonts) {
			cache(font);
		}
	}

	public Collection<Font> getCachedFonts() {
		return new HashSet<Font>(cachedFonts);
	}
	private void cache(Font font) {
		TextRenderer textRenderer = textRenderers.get(font);
		if(textRenderer == null) {
			textRenderer = new TextRenderer(font, true, false);
			this.textRenderers.put(font, textRenderer);
			this.cachedFonts.add(font);
		}
	}
	private void freeFontCache(Font font) {
		if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Free cached font: " + font);
		TextRenderer renderer = textRenderers.get(font);
		if(renderer != null) {
			renderer.dispose();
			textRenderers.remove(font);
			cachedFonts.remove(font);
		}
	}
	private void freeFontCache() {
		if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Free cached fonts");
		for(Font font: cachedFonts) {
			TextRenderer renderer = textRenderers.get(font);
			renderer.dispose();
		}
		textRenderers.clear();
		cachedFonts.clear();
	}

	TextRenderer getTextRenderer(Font font) {
		TextRenderer textRenderer = textRenderers.get(font);
		if(textRenderer == null) {
			cacheFont(font);
			textRenderer = textRenderers.get(font);
			if(textRenderer == null) throw new RuntimeException("Unable to cache font: " + font);
		}
		return textRenderer;
	}

	/* -------------------- Image Management -------------------- */
	
	public Collection<ch.blackspirit.graphics.Image> getCachedImages() {
		return new HashSet<Image>(cachedImages);
	}
	
	void updateBuffer(ch.blackspirit.graphics.jogl.Image image) {
		updateBuffer(image, 0, 0, image.getWidth(), image.getHeight());
	}

	void updateBuffer(ch.blackspirit.graphics.jogl.Image image, int xOffset, int yOffset, int width, int height) {
		if(AbstractGraphicsContext.isDrawing()) throw new RuntimeException("Image buffer update not allowed while drawing");

		if(image.texture == null) {
			try {
				if(!cacheImage(image)) {
//					System.out.println("failed");
					return;
				}
			} catch (IOException e) {
				throw new RuntimeException("Error caching image. Do manual caching to prevent such errors at runtime.", e);
			}
		}
		
		if(xOffset >= image.getWidth()) throw new IllegalArgumentException("X offset outside image");
		if(yOffset >= image.getHeight()) throw new IllegalArgumentException("Y offset outside image");
		if(xOffset < 0) throw new IllegalArgumentException("X offset must not be less than 0");
		if(yOffset < 0) throw new IllegalArgumentException("Y offset must not be less than 0");
		if(xOffset + width > image.getWidth()) throw new IllegalArgumentException("Defined region is bigger than image: xOffset=" + xOffset + " ,width=" + width);
		if(yOffset + height > image.getHeight()) throw new IllegalArgumentException("Defined region is bigger than image: yOffset=" + yOffset + " ,height=" + height);

		bufferRegionUpdate.image = image;
		bufferRegionUpdate.x = xOffset;
		bufferRegionUpdate.y = yOffset;
		bufferRegionUpdate.width = width;
		bufferRegionUpdate.height = height;
		
		glExecutor.execute(bufferRegionUpdate);
	}

	void cache(Image image) throws IOException {
		if(!(image instanceof ch.blackspirit.graphics.jogl.Image)) throw new RuntimeException("Image has not been created by the JOGL Blackspirit Graphics implementation!");
		ch.blackspirit.graphics.jogl.Image joglImage = (ch.blackspirit.graphics.jogl.Image)image;
		if(joglImage.resourceManager != this) throw new RuntimeException("Image has not been created in the same canvas!");

		if(joglImage.texture != null) {
			// Already cached, so just do a buffer update.
			if(image.isBuffered()) {
				updateBufferedCache(joglImage);
			}
			return;
		}
		
		if(image.isBuffered()) cacheBuffered(joglImage);
		else cacheUnbuffered(joglImage);
	}
	
	private void cacheBuffered(ch.blackspirit.graphics.jogl.Image image) {
		if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Caching buffered image: " + image.toString());
		image.texture = TextureIO.newTexture(image.getTextureData());

		// Default?
		image.texture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		image.texture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
        
		image.texture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        image.texture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        cachedImages.add(image);
	}
	private void cacheUnbuffered(ch.blackspirit.graphics.jogl.Image image) throws IOException {
		if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Caching unbuffered image: " + image.toString());
		TextureData textureData = image.createTextureData();
		image.texture = TextureIO.newTexture(textureData);
		textureData.flush();
		// Default?
		image.texture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		image.texture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
        
		image.texture.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        image.texture.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        cachedImages.add(image);
	}
	
	private void freeImageCache() {
		if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Free image cache");
		for(ch.blackspirit.graphics.jogl.Image image: cachedImages) {
			if(image.texture != null) image.texture.dispose();
			image.texture = null;
		}
		cachedImages.clear();
	}

	private void freeImageCache(Image image) {
		if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Free cached image: " + image.toString());
		if(image instanceof ch.blackspirit.graphics.jogl.Image) {
			ch.blackspirit.graphics.jogl.Image joglImage = (ch.blackspirit.graphics.jogl.Image)image;
			if(joglImage.resourceManager != this) throw new RuntimeException("Image has not been created in the same canvas!");
			
			if(joglImage.texture != null) joglImage.texture.dispose();
			joglImage.texture = null;
			cachedImages.remove(image);
		} else {
			throw new RuntimeException("Image has not been created by the JOGL Blackspirit Graphics implementation!");
		}
	}
	
	private void refreshImageCache() {
		Set<Image> images = new HashSet<Image>(cachedImages);
		freeImageCache();
		for(Image image: images) {
			try {
				cache(image);
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Failed reloading an already cached image: " + image.toString(), e);
			}
		}
	}
	
	private void updateBufferedCache(ch.blackspirit.graphics.jogl.Image image) {
		if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Update buffered image cache: " + image.toString());
		long time = System.nanoTime();
		
		if(image.resourceManager != this) throw new RuntimeException("Image has not been created in the same canvas!");
		if(image.texture == null) cacheBuffered(image);
		else image.texture.updateImage(image.getTextureData());

		if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Update buffered image cache took " + (System.nanoTime() - time) + "ns");
	}
	
	private void updateBufferedCache(ch.blackspirit.graphics.jogl.Image image, int offsetX, int offsetY, int width, int height) {
		if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Update buffered image cache region " + offsetX + "," + offsetY + " " + width + "x" + height + ": " + image.toString());
		long time = System.nanoTime();
		
		if(image.resourceManager != this) throw new RuntimeException("Image has not been created in the same canvas!");
		if(image.texture == null) cacheBuffered(image);
		else image.texture.updateSubImage(image.getTextureData(), 0, offsetX, offsetY, offsetX, offsetY, width, height);
//		else image.texture.updateSubImage(image.getTextureData(), 0, 10, 2, 0, 63, width, height);
		
		if(LOGGER.isLoggable(Level.FINER)) LOGGER.finer("Update buffered image cache region took " + (System.nanoTime() - time) + "ns");
	}
	
	/* ----------- GLExecutables -------------- */
	public static class UpdateCache implements GLExecutable {
		ResourceManager resourceManager;
		ch.blackspirit.graphics.jogl.Image image;
		public void execute(GLDrawable drawable, GL gl) {
//			System.out.println("cache");
			resourceManager.updateBufferedCache(image);
		}
	}
	public static class UpdateCacheRegion implements GLExecutable {
		ResourceManager resourceManager;
		ch.blackspirit.graphics.jogl.Image image;
		int offsetX;
		int offsetY;
		int width;
		int height;
		public void execute(GLDrawable drawable, GL gl) {
//			System.out.println("cache region");
			resourceManager.updateBufferedCache(image, offsetX, offsetY, width, height);
		}
	}
	public static class FreeImages implements GLExecutable {
		ResourceManager resourceManager;
		public void execute(GLDrawable drawable, GL gl) {
			resourceManager.freeImageCache();
		}
	}
	public static class FreeImage implements GLExecutable {
		ResourceManager resourceManager;
		Image image;
		public void execute(GLDrawable drawable, GL gl) {
			resourceManager.freeImageCache(image);
		}
	}
	public static class CacheImage implements GLExecutable {
		ResourceManager resourceManager;
		Image image;
		IOException exception;
		
		public void execute(GLDrawable drawable, GL gl) {
			exception = null;
			try {
				resourceManager.cache(image);
			} catch(IOException e) {
				exception = e;
			}
		}
	}

	public static class FreeFonts implements GLExecutable {
		ResourceManager resourceManager;
		public void execute(GLDrawable drawable, GL gl) {
			resourceManager.freeFontCache();
		}
	}
	public static class FreeFont implements GLExecutable {
		ResourceManager resourceManager;
		Font font;
		public void execute(GLDrawable drawable, GL gl) {
			resourceManager.freeFontCache(font);
		}
	}
	public static class CacheFont implements GLExecutable {
		ResourceManager resourceManager;
		Font font;
		Exception exception;
		
		public void execute(GLDrawable drawable, GL gl) {
			exception = null;
			try {
				resourceManager.cache(font);
			} catch(GLException e) {
				exception = e;
			}
		}
	}
}
