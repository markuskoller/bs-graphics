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
package ch.blackspirit.graphics;

import java.io.IOException;
import java.net.URL;

// FEATURE create image from bufferedimage

/**
 * Responsible for creation of images.
 * Supported images include: PNG (recommended), JPG, GIF, BMP, TGA
 * @author Markus Koller
 */
public interface ImageFactory {
	/**
	 * @return Maximum image width.
	 */
	public int getMaxImageWidth();
	/**
	 * @return Maximum image width.
	 */
	public int getMaxImageHeight();
	/**
	 * @return Maximum size an image can have height*width.
	 */
	public int getMaxImagePixels();

	/**
	 * Load a new unbuffered image from the given URL.
	 * Supported images include: PNG (recommended), JPG, GIF, BMP, TGA.
	 * @param url Image to be loaded.
	 * @param forceAlpha Should an alpha channel be forced if not needed by the image.
	 * @return The loaded image.
	 * @throws IOException If loading the image is not possible.
	 */
	public Image createImage(URL url, boolean forceAlpha) throws IOException;
	// using new TextureData for empty buffer.. copy image content! 
//	public Image createImage(BufferedImage, boolean forceAlpha) throws IOException;
	
	/**
	 * Creates a new unbuffered image with the given size optionally having an alpha channel.
	 * @param width Width of the image to create.
	 * @param height Height of the image to create.
	 * @param alpha Should the image contain an alpha channel.
	 * @return The loaded image.
	 * @throws IOException If loading the image is not possible.
	 */
	public Image createImage(int width, int height, boolean alpha) throws IOException;
	
	/**
	 * Load a new buffered image from the given URL.<br/>
	 * The appropriate BufferType for the image gets chosen.
	 * Appropriate means, that the BufferType will be used, which holds the least 
	 * data, but can store all information the image contains.
	 * Supported images include: PNG (recommended), JPG, GIF, BMP, TGA.
	 * @param url Image to be loaded.
	 * @param forceAlpha When set to true the chosen BufferType will always contain an alpha channel.
	 * @return The loaded image.
	 * @throws IOException If loading the image is not possible.
	 */
	public Image createBufferedImage(URL url, boolean forceAlpha) throws IOException;
	// using new TextureData for empty buffer.. copy image content! 
//	public Image createBufferedImage(BufferedImage, boolean forceAlpha) throws IOException;
	
	/**
	 * Load a new buffered image from the given URL.
	 * Supported images include: PNG (recommended), JPG, GIF, BMP, TGA.
	 * @param url Image to be loaded.
	 * @param bufferType Type of buffer to be created.
	 * @return The loaded image.
	 * @throws IOException If loading the image is not possible.
	 */
	public Image createBufferedImage(URL url, BufferType bufferType) throws IOException;
	// using new TextureData for empty buffer.. copy image content! 
//	public Image createBufferedImage(BufferedImage, BufferType bufferType) throws IOException;

	/**
	 * Creates a new buffered image with the given size.
	 * @param width Width of the image to create.
	 * @param height Height of the image to create.
	 * @param alpha Must the buffer have an alpha channel.
	 * @return The created image.
	 */
	public Image createBufferedImage(int width, int height, boolean alpha);
	/**
	 * Creates a new buffered image with the given size.
	 * @param width Width of the image to create.
	 * @param height Height of the image to create.
	 * @param bufferType Type of buffer to be created.
	 * @return The created image.
	 */
	public Image createBufferedImage(int width, int height, BufferType bufferType);
}
