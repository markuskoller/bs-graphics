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
package ch.blackspirit.graphics.jogl;

import javax.vecmath.Color4f;

import ch.blackspirit.graphics.BufferType;
import ch.blackspirit.graphics.Image;

/**
 * Buffer types supported by the Blackspirit Graphics JOGL implementation.
 * @author Markus Koller
 */
public final class BufferTypes {
	private BufferTypes() {}
	/**
	 * 4 Byte RGBA buffer.<br/>
	 * To get a pixels first byte in the buffer use the following formula: <code>(y * width + x) * 4</code><br/>
	 */
	public static BufferType RGBA_4Byte = new BufferType() {
		private static final int MASK = (0x00000080 + 127);
		private static final int RED_INDEX = 0;
		private static final int GREEN_INDEX = 1;
		private static final int BLUE_INDEX = 2;
		private static final int ALPHA_INDEX = 3;
		private static final int BYTES = 4;

		public float getAlpha(Image image, int x, int y) {
			return (float)(((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + ALPHA_INDEX] & MASK) / 255f;
		}
		public float getBlue(Image image, int x, int y) {
			return (float)(((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + BLUE_INDEX] & MASK) / 255f;
		}
		public float getGreen(Image image, int x, int y) {
			return (float)(((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + GREEN_INDEX] & MASK) / 255f;
		}
		public float getRed(Image image, int x, int y) {
			return (float)(((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + RED_INDEX] & MASK) / 255f;
		}
		public void setRed(Image image, int x, int y, float value) {
			((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + RED_INDEX] = (byte)(value * 255);
		}
		public void setGreen(Image image, int x, int y, float value) {
			((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + GREEN_INDEX] = (byte)(value * 255);
		}
		public void setBlue(Image image, int x, int y, float value) {
			((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + BLUE_INDEX] = (byte)(value * 255);
		}
		public void setAlpha(Image image, int x, int y, float value) {
			((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + ALPHA_INDEX] = (byte)(value * 255);
		}
		public void getColor(Image image, int x, int y, Color4f color) {
			color.x = (float)(((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + RED_INDEX] & MASK) / 255f;
			color.y = (float)(((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + GREEN_INDEX] & MASK) / 255f;
			color.z = (float)(((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + BLUE_INDEX] & MASK) / 255f;
			color.w = (float)(((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + ALPHA_INDEX] & MASK) / 255f;
		}
		public void setColor(Image image, int x, int y, Color4f color) {
			((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + RED_INDEX] = (byte)(color.x * 255);
			((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + GREEN_INDEX] = (byte)(color.y * 255);
			((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + BLUE_INDEX] = (byte)(color.z * 255);
			((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + ALPHA_INDEX] = (byte)(color.w * 255);
		}
		
		public String toString() {
			return BufferTypes.class.getName() + ".RGBA_4Byte";
		}
	};
	/**
	 * 3 Byte RGB buffer.<br/>
	 * To get a pixels first byte in the buffer use the following formula: <code>(y * width + x) * 3</code><br/>
	 */
	public static BufferType RGB_3Byte = new BufferType() {
		private static final int MASK = (0x00000080 + 127);
		private static final int RED_INDEX = 0;
		private static final int GREEN_INDEX = 1;
		private static final int BLUE_INDEX = 2;
		private static final int BYTES = 3;

		public void setAlpha(Image image, int x, int y, float value) {}
		public float getAlpha(Image image, int x, int y) {
			return 1;
		}
		public float getBlue(Image image, int x, int y) {
			return (float)(((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + BLUE_INDEX] & MASK) / 255f;
		}
		public float getGreen(Image image, int x, int y) {
			return (float)(((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + GREEN_INDEX] & MASK) / 255f;
		}
		public float getRed(Image image, int x, int y) {
			return (float)(((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + RED_INDEX] & MASK) / 255f;
		}
		public void setRed(Image image, int x, int y, float value) {
			((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + RED_INDEX] = (byte)(value * 255);
		}
		public void setGreen(Image image, int x, int y, float value) {
			((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + GREEN_INDEX] = (byte)(value * 255);
		}
		public void setBlue(Image image, int x, int y, float value) {
			((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + BLUE_INDEX] = (byte)(value * 255);
		}
		public void getColor(Image image, int x, int y, Color4f color) {
			color.x = (float)(((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + RED_INDEX] & MASK) / 255f;
			color.y = (float)(((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + GREEN_INDEX] & MASK) / 255f;
			color.z = (float)(((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + BLUE_INDEX] & MASK) / 255f;
			color.w = 1;
		}
		public void setColor(Image image, int x, int y, Color4f color) {
			((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + RED_INDEX] = (byte)(color.x * 255);
			((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + GREEN_INDEX] = (byte)(color.y * 255);
			((byte[])image.getBuffer())[(y * image.getWidth() + x) * BYTES + BLUE_INDEX] = (byte)(color.z * 255);
		}

		public String toString() {
			return BufferTypes.class.getName() + ".RGB_3Byte";
		}
	};
}
