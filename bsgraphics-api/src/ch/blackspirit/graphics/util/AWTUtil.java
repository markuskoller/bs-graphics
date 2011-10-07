/*
 * Copyright 2009 Markus Koller
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
package ch.blackspirit.graphics.util;

import java.awt.image.BufferedImage;

import javax.vecmath.Color4f;

import ch.blackspirit.graphics.BufferType;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.jogl.BufferTypes;

/**
 * The AWTUtil helps converting buffered Blackspirit Graphics images to AWT images, 
 * thus allowing the user to easily save images using the <code>ImageIO</code> class.
 * @author Markus Koller
 */
public class AWTUtil {
	public static BufferedImage readImageBuffer(Image image) {
		if(!image.isBuffered()) throw new IllegalArgumentException("Only buffered images can be converted.");
		if(image.getBufferType() == BufferTypes.RGB_3Byte) {
			BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
			byte[] buffer = (byte[])image.getBuffer();
			int index = 0;
			for(int y = 0; y < image.getHeight(); y++) {
				for(int x = 0; x < image.getWidth(); x++) {
					int r = buffer[index++]; 
					int g = buffer[index++]; 
					int b = buffer[index++]; 
					bi.setRGB(x, y, (r << 16 & 0x00FF0000) + (g << 8 & 0x0000FF00) + (b  & 0x000000FF));
				}
			}
			return bi;
		} else if(image.getBufferType() == BufferTypes.RGBA_4Byte) {
			BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			byte[] buffer = (byte[])image.getBuffer();
			int index = 0;
			for(int y = 0; y < image.getHeight(); y++) {
				for(int x = 0; x < image.getWidth(); x++) {
					int r = buffer[index++]; 
					int g = buffer[index++]; 
					int b = buffer[index++]; 
					int a = buffer[index++]; 
					bi.setRGB(x, y, (a << 24 & 0xFF000000) + (r << 16 & 0x00FF0000) + (g << 8 & 0x0000FF00) + (b  & 0x000000FF));
				}
			}
			return bi;
		} else {
			BufferType type = image.getBufferType();

			BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			Color4f color = new Color4f();
			for(int y = 0; y < image.getHeight(); y++) {
				for(int x = 0; x < image.getWidth(); x++) {
					type.getColor(image, x, y, color);
					int r = (int)(color.x * 255); 
					int g = (int)(color.y * 255);
					int b = (int)(color.z * 255);
					int a = (int)(color.w * 255);
					bi.setRGB(x, y, (a << 24 & 0xFF000000) + (r << 16 & 0x00FF0000) + (g << 8 & 0x0000FF00) + (b  & 0x000000FF));
				}
			}
			return bi;
		}
	}
}
