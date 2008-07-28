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

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Markus Koller
 */
class BSGraphicsProperties {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	private java.util.Properties properties = new java.util.Properties();

	public BSGraphicsProperties() {
		URL url = this.getClass().getResource("/bsgraphics.properties");
		if(url != null) {
			try {
				properties.load(url.openStream());
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "Error reading existing bsgraphics.properties", e);
			}
		}
	}
	
	private int getProperty(String property, int defaultValue) {
		String value = properties.getProperty(property, String.valueOf(defaultValue));
		try {
			return Integer.valueOf(value).intValue();
		} catch(NumberFormatException e) {
			LOGGER.log(Level.WARNING, "Property is not a valid integer: " + property, e);
			return defaultValue;
		}
	}
	private boolean getProperty(String property, boolean defaultValue) {
		String value = properties.getProperty(property, String.valueOf(defaultValue));
		if("true".equalsIgnoreCase(value)) return true;
		else if("false".equalsIgnoreCase(value)) return false;
		else return defaultValue;
	}

	public boolean isPBuffer() { 
		return getProperty("ch.blackspirit.graphics.jogl.debug.pbuffer", true);
	}
	public boolean isDebugGL() { 
		return getProperty("ch.blackspirit.graphics.jogl.debug.debuggl", false);
	}
	public boolean isTraceGL() { 
		return getProperty("ch.blackspirit.graphics.jogl.debug.tracegl", false);
	}
	public int getImageDrawingWidth() {
		return getProperty(Properties.MAX_IMAGE_DRAWING_WIDTH, 1024);
	}
	public int getImageDrawingHeight() {
		return getProperty(Properties.MAX_IMAGE_DRAWING_HEIGHT, 1024);
	}
}
