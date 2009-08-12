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

import java.util.ArrayList;

import javax.vecmath.Color4f;
import javax.vecmath.Vector2f;

import ch.blackspirit.graphics.BufferType;
import ch.blackspirit.graphics.Image;
import ch.blackspirit.graphics.geometry.Geometry;

/**
 * Utility class to create color gradients.<br/>
 * Can create gradients on images or just return the appropriate color for a specific point.
 * @author Markus Koller
 */
public class ColorGradientFactory {
	// TODO change to array for speed
	private ArrayList<PointSource> pointSources = new ArrayList<PointSource>();
	private ArrayList<LineSource> lineSources = new ArrayList<LineSource>();
	private Color4f baseColor = null;
	
	/**
	 * Adds a source point for color.<br/>
	 * The weight for the color at the source point will be 1 
	 * and it will lower linear reaching 0 at the specified distance 
	 * from the source point.
	 * @param x X coordinate of the source point.
	 * @param y Y coordinate of the source point.
	 * @param distance Distance from the source point where the weight should reach 0.
	 * @param color The color for this source point.
	 */
	public void addSourcePoint(float x, float y, float distance, Color4f color) {
		PointSource colorSource = new PointSource();
		colorSource.x = x;
		colorSource.y = y;
		colorSource.dist = distance;
		colorSource.color = color;
		this.pointSources.add(colorSource);
	}
	/**
	 * Adds a source segment for color.<br/>
	 * The weight for the color on the source segment will be 1 
	 * and it will lower linear reaching 0 at the specified distance 
	 * from the source segment.
	 * @param x1 X coordinate of the first point of the source segment.
	 * @param y1 Y coordinate of the first point of the source segment.
	 * @param x2 X coordinate of the second point of the source segment.
	 * @param y2 Y coordinate of the second point of the source segment.
	 * @param distance Distance from the source point where the weight should reach 0.
	 * @param color The color for this source segment.
	 */
	public void addSourceSegment(float x1, float y1, float x2, float y2, float distance, Color4f color) {
		LineSource colorSource = new LineSource();
		colorSource.x1 = x1;
		colorSource.y1 = y1;
		colorSource.x2 = x2;
		colorSource.y2 = y2;
		colorSource.dist = distance;
		colorSource.color = color;
		this.lineSources.add(colorSource);
	}
	/**
	 * The base color is by default set to null.<br/>
	 * If it is set to a color, all points having a color weight of less than one 
	 * will have that color added with a weight of (1 - color weight).
	 * @param color The color to use as base color.
	 */
	public void setBaseColor(Color4f color) {
		baseColor = color;
	}
		
	/**
	 * Removes all point and segment sources.
	 */
	public void clearSources() {
		pointSources.clear();
		lineSources.clear();
	}
	
	/**
	 * Calculates the color for specific coordinates based on the currently set sources.
	 * @param x X coordinate of the point to calculate the color for.
	 * @param y Y coordinate of the point to calculate the color for.
	 * @param color Will be set to the color for the specified coordinates.
	 */
	public void getColor(float x, float y, Color4f color) {
		color.set(0,0,0,0);
	
		float totalWeight = 0;
		for(int i = 0; i < pointSources.size(); i++) {
			PointSource source = pointSources.get(i);
			float diffX = (x-source.x);
			float diffY = (y-source.y);
			float dist = (float)Math.sqrt(diffX*diffX + diffY*diffY);

			float weight = 1f - (1 / source.dist * dist);
			if(weight < 0) weight = 0;
			
			totalWeight+=weight;

			color.x += source.color.x * weight;
			color.y += source.color.y * weight;
			color.z += source.color.z * weight;
			color.w += source.color.w * weight;
		}
		for(int i = 0; i < lineSources.size(); i++) {
			LineSource source = lineSources.get(i);
			float dist = Geometry.pointSegmentDistance(new Vector2f(x,y), new Vector2f(source.x1, source.y1), new Vector2f(source.x2, source.y2));

			float weight = 1f - (1 / source.dist * dist);
			if(weight < 0) weight = 0;

			totalWeight+=weight;

			color.x += source.color.x * weight;
			color.y += source.color.y * weight;
			color.z += source.color.z * weight;
			color.w += source.color.w * weight;
		}
		
		if(baseColor != null && totalWeight < 1f) {
			color.x += baseColor.x * (1f - totalWeight);
			color.y += baseColor.y * (1f - totalWeight);
			color.z += baseColor.z * (1f - totalWeight);
			color.w += baseColor.w * (1f - totalWeight);
			totalWeight = 1;
		}
		
		color.x /= totalWeight;
		color.y /= totalWeight;
		color.z /= totalWeight;
		color.w /= totalWeight;
	}
	
	/**
	 * Draws a gradient based on the currently set sources into the images buffer.
	 * @param image The image to draw the gradient on.
	 */
	public void drawGradient(Image image) {
		if(image.isBuffered() == false) throw new IllegalArgumentException("Only buffered images can be processed!");
		if(pointSources.size() == 0 && lineSources.size() == 0) return;
		BufferType bufferType = image.getBufferType();
		Color4f color = new Color4f();
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				getColor(x, y, color);
				bufferType.setColor(image, x, y, color);
			}
		}
	}
	
	private static class PointSource {
		public float x;
		public float y;
		public Color4f color;
		public float dist;
	}
	private static class LineSource {
		public float x1;
		public float y1;
		public float x2;
		public float y2;
		public float dist;
		public Color4f color;
	}
}
