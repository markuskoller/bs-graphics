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
package ch.blackspirit.graphics;

/**
 * Enumeration of the available drawing modes.
 * @author Markus Koller
 */
public enum DrawingMode {
	/**
	 * This is the default drawing mode.<br/>
	 * The drawn pixel color gets combined with the buffers pixel color 
	 * based on the drawn pixels alpha value.<br/>
	 * The lower the drawn pixels alpha value, 
	 * the more of the buffers color and less of the drawn pixel color gets use, 
	 * making it look transparent.
	 */
	ALPHA_BLEND, 
	
	/**
	 * Each drawn and buffer pixel color get added on a per component basis.<br/>
	 * The value range for a color component is from 0 to 1. 
	 * If the new value is outside this range it gets truncated. 
	 */
	ADD, 
	
	/**
	 * Each drawn and buffer pixel color get added on a per component basis.
	 * The drawn pixels color components get multiplied with the drawn pixels alpha before addition.<br/>
	 * The value range for a color component is from 0 to 1. 
	 * If the new value is outside this range it gets truncated. 
	 */
	ALPHA_ADD,
	
	/**
	 * Each drawn pixel color gets subtracted from the buffer pixel color on a per component basis.<br/>
	 * The value range for a color component is from 0 to 1. 
	 * If the new value is outside this range it gets truncated.
	 */
	SUBTRACT, 
	
	/**
	 * Each drawn and buffer pixel color get multiplied on a per component basis.<br/>
	 * The value range for a color component is from 0 to 1.
	 * If the new value is outside this range it gets truncated. 
	 */
	MULTIPLY, 
	
	/**
	 * The buffer pixel simply gets overridden by the drawn pixel. 
	 */
	OVERWRITE
}