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
package ch.blackspirit.graphics.jogl;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Properties to be used on creation of a canvas throught the {@link CanvasFactory}.
 * @author Markus Koller
 */
public final class CanvasProperties {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	private java.util.Properties properties = new java.util.Properties();

	private boolean isPBuffer = true;
	private boolean isDebugGL = false;
	private boolean isTraceGL = false;
	private int imageDrawingWidth = 1024;
	private int imageDrawingHeight = 1024;
	private Level traceLogLevel = Level.INFO;
	
	public CanvasProperties() {}
	
	private String getProperty(String property, String defaultValue) {
		return properties.getProperty(property, defaultValue);
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
		return isPBuffer;
	}
	public void setPBuffer(boolean isPBuffer) {
		this.isPBuffer = isPBuffer;
	}
	public boolean isDebugGL() {
		return isDebugGL;
	}
	public void setDebugGL(boolean isDebugGL) {
		this.isDebugGL = isDebugGL;
	}
	public boolean isTraceEnabled() {
		return isTraceGL;
	}
	public void setTraceEnabled(boolean isTrace) {
		this.isTraceGL = isTrace;
	}
	public int getImageDrawingWidth() {
		return imageDrawingWidth;
	}
	public void setImageDrawingWidth(int imageDrawingWidth) {
		this.imageDrawingWidth = imageDrawingWidth;
	}
	public int getImageDrawingHeight() {
		return imageDrawingHeight;
	}
	public void setImageDrawingHeight(int imageDrawingHeight) {
		this.imageDrawingHeight = imageDrawingHeight;
	}
	public Level getTraceLogLevel() {
		return traceLogLevel;
	}
	public void setTraceLogLevel(Level traceLogLevel) {
		this.traceLogLevel = traceLogLevel;
	}

	public void load(URL url) {
		try {
			properties.load(url.openStream());
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error reading existing bsgraphics.properties", e);
		}
		isPBuffer = getProperty("ch.blackspirit.graphics.jogl.debug.pbuffer", isPBuffer);
		isDebugGL = getProperty("ch.blackspirit.graphics.jogl.debug.debuggl", isDebugGL);
		isTraceGL = getProperty("ch.blackspirit.graphics.jogl.debug.trace", isTraceGL);
		try {
			traceLogLevel = Level.parse(getProperty("ch.blackspirit.graphics.jogl.debug.trace.log.level", traceLogLevel.getName()));
		} catch(Throwable t) {}
		imageDrawingWidth = getProperty(Properties.MAX_IMAGE_DRAWING_WIDTH, imageDrawingWidth);
		imageDrawingHeight = getProperty(Properties.MAX_IMAGE_DRAWING_HEIGHT, imageDrawingHeight);
	}
}
