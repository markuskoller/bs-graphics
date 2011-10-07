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

import ch.blackspirit.graphics.GraphicsContext;

// TODO get rid off this class
/**
 * @author Markus Koller
 */
abstract class AbstractGraphicsContext implements GraphicsContext {
	private static boolean drawing = false;
	private static AbstractGraphicsContext drawingGraphicsContext = null;
	
	protected void startDrawing() {
		if(drawing) throw new RuntimeException("Drawing on more than one graphics contexts at once is not allowed.");
		drawingGraphicsContext = this;
		drawing = true;
	}
	
	public static AbstractGraphicsContext getDrawingGraphicsContext() {
		return drawingGraphicsContext;
	}
	protected void endDrawing() {
		drawingGraphicsContext = null;
		drawing = false;
	}
	public static boolean isDrawing() {
		return drawing;
	}
}
