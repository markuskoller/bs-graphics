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

/**
 * Notifications to the window listener are not necessarily made on the users thread.
 * @author Markus Koller
 */
public interface WindowListener {
	public static WindowListener EXIT_ON_CLOSE = new WindowListener() {
		public void windowActivated() {}
		public void windowClosing() {System.exit(0);}
	    public void windowClosed() {}
		public void windowDeactivated() {}
		public void windowDeiconified() {}
		public void windowIconified() {}
	};
	
    /**
     * Invoked when the user attempts to close the window.
     */
    public void windowClosing();
    public void windowClosed();
    public void windowIconified();
    public void windowDeiconified();
    public void windowActivated();
    public void windowDeactivated();
}
