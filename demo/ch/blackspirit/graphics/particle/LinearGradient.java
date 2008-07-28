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
package ch.blackspirit.graphics.particle;

/**
 * @author Markus Koller
 */
public class LinearGradient implements Gradient {
	private Point first;
	
	public float getValue(float position) {
		if(position <= first.position) {
			return first.value;
		} else {
			Point next = first;
			Point previous = null;
			while(next.position < position) {
				previous = next;
				next = next.next;
				if(next == null) {
					return previous.value;
				}
			}
			float region = next.position - previous.position;
			float previousPercent = (next.position - position) / region; 
			float nextPercent = (position - previous.position) / region; 
			return nextPercent * next.value + previousPercent * previous.value;
		}
	}

	public void addPoint(float position, float value) {
		if(first == null) {
			first = new Point(position, value);
		} else {
			if(position < first.position) {
				Point p = new Point(position, value);
				p.next = first;
				first = p;
			} else {
				Point next = first;
				Point previous = null;
				while(next.position < position) {
					previous = next;
					next = next.next;
					if(next == null) break;
				}
				Point p = new Point(position, value);
				if(previous != null && previous.position == position) throw new IllegalArgumentException("Duplicate position in linear gradient: " + position);
				if(next != null && next.position == position) throw new IllegalArgumentException("Duplicate position in linear gradient: " + position);
				if(previous != null) {
					previous.next = p;
					p.previous = previous;
				}
				if(next != null) {
					next.previous = p;
					p.next = next;
				}
			}
		}
	}
	
	private static final class Point {
		public float position;
		public float value;
		public Point previous;
		public Point next;

		public Point(float position, float value) {
			super();
			this.position = position;
			this.value = value;
		}
	}
}
