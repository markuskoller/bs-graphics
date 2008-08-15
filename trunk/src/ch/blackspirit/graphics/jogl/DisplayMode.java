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

/**
 * @author Markus Koller
 */
class DisplayMode implements ch.blackspirit.graphics.DisplayMode {
	private final int colorDepth;
	private final int width;
	private final int height;
	private final int refreshRate;
	public DisplayMode(final int width, final int height, final int colorDepth, int refreshRate) {
		super();
		this.width = width;
		this.height = height;
		this.colorDepth = colorDepth;
		this.refreshRate = refreshRate;
	}
	public int getColorDepth() {
		return colorDepth;
	}
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + colorDepth;
		result = prime * result + height;
		result = prime * result + refreshRate;
		result = prime * result + width;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DisplayMode other = (DisplayMode) obj;
		if (colorDepth != other.colorDepth)
			return false;
		if (height != other.height)
			return false;
		if (refreshRate != other.refreshRate)
			return false;
		if (width != other.width)
			return false;
		return true;
	}
	
	public int getRefreshRate() {
		return refreshRate;
	}
}
