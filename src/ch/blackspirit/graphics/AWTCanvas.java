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
package ch.blackspirit.graphics;

import java.awt.Component;

/**
 * A <code>Canvas</code> that can be used in an AWT or Swing application.
 * The drawing is event based and gets done by an <code>GraphisListener</code>.
 * @author Markus Koller
 */
public interface AWTCanvas extends Canvas {

	/**
	 * Tells wether the rendering takes place on a lightweight Swing component or not.
	 * @return <code>true</code> if the rendering takes place on a lightweight Swing component
	 */
	public boolean isLightweight();
	
	/**
	 * Get the AWT Component on which the rendering takes place.
	 * Can be integrated in an AWT/Swing application like a normal Component.
	 * @return The Component to integrate into an AWT/Swing application.
	 */
	public Component getComponent();
}
