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
package ch.blackspirit.graphics.util;

import java.util.List;

import javax.vecmath.Vector2f;

import ch.blackspirit.graphics.Line;
import ch.blackspirit.graphics.Triangle;
import ch.blackspirit.graphics.shape.Shape;

/**
 * Utility class to create shapes out of an outline and cut-outs.
 * @author Markus Koller
 */
public class ShapeCreator {
	
	private ShapeCreator() {}
	
	/**
	 * @param outline The outline to create the shape from.
	 * @return A shape based on the passed outline.
	 */
	public static Shape create(List<Vector2f> outline) {
		return create(outline, null);
	}

	/**
	 * @param outline The outline to create the shape from.
	 * @param cutouts A list of cut-outs to cut out of the shapes area.
	 * @return A shape based on the passed outline.
	 */
	public static Shape create(List<Vector2f> outline, List<List<Vector2f>> cutouts) {
		Triangulator triangulator = new Triangulator();
		List<Triangle> ts = triangulator.triangulate(outline, cutouts);

		Triangle[] triangles = new Triangle[ts.size()];
		triangles = ts.toArray(triangles);
		
		// Calculate number of lines
		int totalLines = outline.size();
		if(cutouts != null) {
			for(int l = 0; l < cutouts.size(); l++) {
				totalLines += cutouts.get(l).size();
			}
		}

		Line[] lines = new Line[totalLines];

		// Populate with outline lines
		int currentLine = 0;
		for(int i = 0; i < outline.size() - 1; i++) {
			lines[currentLine] = new ch.blackspirit.graphics.shape.Line(new Vector2f(outline.get(i)), new Vector2f(outline.get(i+1)));
			currentLine++;
		}
		lines[currentLine] = 
			new ch.blackspirit.graphics.shape.Line(new Vector2f(outline.get(outline.size() - 1)), new Vector2f(outline.get(0)));
		currentLine++;
		
		// Populate with cut-out lines
		if(cutouts != null) {
			for(int l = 0; l < cutouts.size(); l++) {
				List<Vector2f> cutout = cutouts.get(l);
	
				for(int i = 0; i < cutout.size() - 1; i++) {
					lines[currentLine] = new ch.blackspirit.graphics.shape.Line(new Vector2f(cutout.get(i)), new Vector2f(cutout.get(i+1)));
					currentLine++;
				}
				lines[currentLine] = 
					new ch.blackspirit.graphics.shape.Line(new Vector2f(cutout.get(cutout.size() - 1)), new Vector2f(cutout.get(0)));
				currentLine++;
			}
		}
		
		return new ch.blackspirit.graphics.shape.SimpleShape(triangles, lines);
	}
}
