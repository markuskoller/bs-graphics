/*
 * Copyright 2011 Markus Koller
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
package ch.blackspirit.graphics.demo;

import ch.blackspirit.graphics.BufferType;
import ch.blackspirit.graphics.ImageFactory;

/**
 * @author Markus Koller
 *
 */
public final class BufferTypeUtil {
	public static BufferType getBest(ImageFactory imageFactory, boolean alphaSupport) {
		int currentBits = 0;
		BufferType current = null;
		
		for (BufferType type: imageFactory.getSupportedBufferTypes()) {
			if (type.isAlphaSupported() == alphaSupport && type.getBitsPerPixel() > currentBits) {
				currentBits = type.getBitsPerPixel();
				current = type;
			}
		}
		
		return current;
	}
}
