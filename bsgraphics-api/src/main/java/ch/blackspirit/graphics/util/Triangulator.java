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

import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Vector2f;

import ch.blackspirit.graphics.Triangle;
import ch.blackspirit.graphics.geometry.Geometry;
import ch.blackspirit.graphics.geometry.Intersection;
import ch.blackspirit.graphics.shape.Line;

/**
 * Triangulates concave polygons with cut-outs.
 * The <code>Triangulator</code> is not threadsafe. 
 * @author Markus Koller
 */
public class Triangulator {
	private static final float TWO_PI = (float)Math.PI * 2;
	
//	public Renderer renderer;
	
	private boolean removeRedundantPoints = false;
	
	private ArrayList<Line> segments = new ArrayList<Line>(1000);
	private HashMap<Vector2f, List<Line>> segmentMap = new HashMap<Vector2f, List<Line>>();
	private ArrayList<Vector2f> nonConvexPoints = new ArrayList<Vector2f>(100);
	private ArrayList<Vector2f> nonConvexPointsParallel = new ArrayList<Vector2f>(100);
	private HashMap<Vector2f, Line> nonConvexPointOutsideSegment = new HashMap<Vector2f, Line>();
	
	private static final class PathTreeNode {
		public GeneralPath path;
		public List<Vector2f> points;
		public List<PathTreeNode> children = new ArrayList<PathTreeNode>();
		
		public PathTreeNode(GeneralPath path, List<Vector2f> points) {
			this.path = path;
			this.points = points;
		}
	}

	public Triangulator() {
		super();
	}
	
	/**
	 * Should redundant points be removed.
	 * Point B between A and C is unnecessary if B is on the segment A,C.
	 * If two consecutive points are the same, one is unnecessary.
	 * @param remove If <code>true</code>, points unnecessary for the look of the TShape get removed.
	 */
	public void setRemoveRedundantPoints(boolean remove) {
		this.removeRedundantPoints = remove;
	}
	
	/**
	 * @return Should redundant points be removed.
	 */
	public boolean getRemoveRedundantPoints() {
		return this.removeRedundantPoints;
	}
	
	/**
	 * Which paths are contained in which will be handled automatically.
	 * The only guarantee for the triangulation is that a minimum number of triangles is used.
	 * @param paths A list of paths. Last point of each path will be connected with its first.
	 * @return A list of triangles representing the paths.
	 */
	public List<Triangle> triangulate(List<List<Vector2f>> paths) {
		List<TShape> TShapes = createTShapes(paths);
		List<Triangle> triangles = new ArrayList<Triangle>();
		for(TShape shape: TShapes) {
			triangles.addAll(triangulate(shape));
		}
		return triangles;
	}

	/**
	 * The only guarantee for the triangulation is that a minimum number of triangles is used.
	 * @param outline A line describing the outline. Last point will be connected with its first.
	 * @param cutOuts A list of all cut-outs. Last point of each path will be connected with its first.
	 * @return A list of all triangles.
	 */
	public List<Triangle> triangulate(List<Vector2f> outline, List<List<Vector2f>> cutOuts) {
		TShape shape = new TShape(outline, cutOuts);
		List<Triangle> triangles = new ArrayList<Triangle>();
		triangles.addAll(triangulate(shape));
		return triangles;
	}

	private List<TShape> createTShapes(List<List<Vector2f>> paths) {
		// Create GeneralPaths
		HashMap<GeneralPath, List<Vector2f>> pathMap = new HashMap<GeneralPath, List<Vector2f>>();
		for(List<Vector2f> points: paths) {
			if(points.size() == 0) continue;
			GeneralPath path = new GeneralPath(GeneralPath.WIND_NON_ZERO);
			Vector2f last = points.get(points.size() -1);
			path.moveTo(last.x, last.y);
			for(Vector2f point: points) {
				path.lineTo(point.x, point.y);
			}
			pathMap.put(path, points);
		}

		// Create PathTree
		ArrayList<PathTreeNode> nodes = new ArrayList<PathTreeNode>();
		for(GeneralPath path: pathMap.keySet()) {
			PathTreeNode newNode = new PathTreeNode(path, pathMap.get(path));

			boolean contained = false;
			for(PathTreeNode node: nodes) {
				if(addChild(node, newNode)) {
					contained = true;
					break;
				}
			}
			if(!contained) {
				ArrayList<PathTreeNode> contains = new ArrayList<PathTreeNode>();
				for(PathTreeNode node: nodes) {
					if(contains(newNode.path, node.path)) {
						contains.add(node);
					}
				}
				for(PathTreeNode containedIn: contains) {
					newNode.children.add(containedIn);
					nodes.remove(containedIn);
				}
				nodes.add(newNode);
			}
		}
		
		// Create TShapes
		ArrayList<TShape> shapes = new ArrayList<TShape>(paths.size());
		for(PathTreeNode node: nodes) {
			createTShapes(node, shapes);
		}
		return shapes;
	}
	
	// Create TShapes from a PathTree
	private void createTShapes(PathTreeNode node, List<TShape> shapes) {
		TShape shape = new TShape(node.points);
		for(PathTreeNode child: node.children) {
			shape.addCutOut(child.points);
			for(PathTreeNode childChild: child.children) {
				createTShapes(childChild, shapes);
			}
		}
		shapes.add(shape);
	}
	
	/**
	 * Add a PathTreeNode to another PathTreeNode if it contains it.
	 * It gets added as deep in the tree as possible.
	 * @return <code>true</code> if it has been added.
	 */
	private boolean addChild(PathTreeNode node, PathTreeNode add) {
		for(PathTreeNode child: node.children) {
			if(addChild(child, add)) return true;
		}
		if(contains(node.path, add.path)) {
			return node.children.add(add);
		} else {
			return false;
		}
	}
	
	/**
	 * @return <code>true</code> only if the path contains all the points of the other path. (What about line intersections?)
	 */
	private boolean contains(GeneralPath path1, GeneralPath path2) {
		PathIterator iter = path2.getPathIterator(null);
		double[] ar = new double[6];
		boolean contains = false;
		int tests = 0;
		while(!iter.isDone()) {
			iter.currentSegment(ar);
			if(path1.contains(ar[0], ar[1])) {
				if(tests > 0 && contains == false) {
					return false;
				}
				contains = true;
			} else {
				if(tests > 0 && contains == true) return false;
			}
			iter.next();
			tests++;
		}
		return contains;
	}
	
	private List<Triangle> triangulate(TShape shape) {
		segments.clear();
		segmentMap.clear();
		
		if(removeRedundantPoints) {
			removeRedundantPoints(shape.getOutline());
			for(List<Vector2f> points: shape.getCutOuts()) {
				removeRedundantPoints(points);
			}
		}
		
		// testing: draw outline
//		for(int i = 0; i < TShape.getOutline().size(); i++) {
//			int j = rolloverRange(i + 1, TShape.getOutline().size() -1);
//			Vector2f point = TShape.getOutline().get(i);
//			Vector2f point2 = TShape.getOutline().get(j);
//			renderer.drawLine(point.x, point.y, point2.x, point2.y, 1f, new Color4f(1,1,1,1));
//		}
//		for(List<Vector2f> points: TShape.getCutOuts()) {
//			for(int i = 0; i < points.size(); i++) {
//				int j = rolloverRange(i + 1, points.size() -1);
//				Vector2f point = points.get(i);
//				Vector2f point2 = points.get(j);
//				renderer.drawLine(point.x, point.y, point2.x, point2.y, 1f, new Color4f(1,1,1,1));
//			}
//		}
		
		// Add cut-outs first as they will more likely intersect
		for(List<Vector2f> points: shape.getCutOuts()) {
			createSegments(points);
		}
		createSegments(shape.getOutline());
		
		// Create segments from cut-out points
		for(List<Vector2f> cutOut: shape.getCutOuts()) {
			List<Vector2f> points = new ArrayList<Vector2f>(cutOut);
			for(Vector2f point: points) {
				for(List<Vector2f> cutOut2: shape.getCutOuts()) {
					if(cutOut2 != cutOut) {
						for(Vector2f point2: cutOut2) {
							addSegment(point, point2);
						}
					}
				}

				for(Vector2f point2: shape.getOutline()) {
					addSegment(point, point2);
				}
			}
			
			// Create segments from the cut-outs convex points.
			// A segment is only valid if its ray doesn't intersect with segment 
			// from the point before to the point after. (it's not pointing outside!)
			populateNonConvexPoints(cutOut, false);
			for(Vector2f point: nonConvexPoints) {
				Line outside = nonConvexPointOutsideSegment.get(point);
				for(Vector2f point2: cutOut) {
					if(	point2 != point && 
						point2 != outside.getPoint(0) &&
						point2 != outside.getPoint(1)) {
						if(Geometry.raySegmentIntersect(point, point2, outside.getPoint(0), outside.getPoint(1)) == Intersection.FALSE) {
							addSegment(point, point2);
						}
					}
				}
			}

			// testing: draw concave and parallel cut-out points
//			for(Vector2f point: nonConvexPoints) {
//				renderer.drawPoint(point.x, point.y, 6f, new Color4f(1,0,0,1));
//			}
//			for(Vector2f point: nonConvexPointsParallel) {
//				renderer.drawPoint(point.x, point.y, 6f, new Color4f(0,1,0,1));
//			}
		}
		
		// Create segments from outline points making the TShape non-convex.
		// A segment is only valid if its ray doesn't intersect with segment 
		// from the point before to the point after. (it's not pointing outside!)
		populateNonConvexPoints(shape.getOutline(), true);
		for(Vector2f point: nonConvexPoints) {
			Line outside = nonConvexPointOutsideSegment.get(point);
			for(Vector2f point2: shape.getOutline()) {
				if(	point2 != point && 
					point2 != outside.getPoint(0) &&
					point2 != outside.getPoint(1)) {
					if(Geometry.raySegmentIntersect(point, point2, outside.getPoint(0), outside.getPoint(1)) == Intersection.FALSE) {
						addSegment(point, point2);
					}
				}
			}
		}

		// Handle redundant points
		if(!removeRedundantPoints) {
			// parallel points have not necessary been connected yet
			// connect them to all common connected points of their pre- and successor
			// if the connection does not already exist!
			for(Vector2f vec: nonConvexPointsParallel) {
				Line prepost = nonConvexPointOutsideSegment.get(vec);
				List<Line> lines1 = segmentMap.get(prepost.getPoint(0));
				List<Line> lines2 = segmentMap.get(prepost.getPoint(1));
				for(Line line1: lines1) {
					Vector2f point1 = line1.getPoint(0);
					if(point1 == prepost.getPoint(0)) point1 = line1.getPoint(1);
					
					for(Line line2: lines2) {
						Vector2f point2 = line2.getPoint(0);
						if(point2 == prepost.getPoint(1)) point2 = line2.getPoint(1);
						
						if(point2 == point1) {
							if(getLine(vec, point1) == null) {
								addSegment(vec, point1);
							}
						}
					}
				}
			}
		}

		// If there are no cut-outs and no concave points the first point get connected to all but the one before and after
		if(nonConvexPoints.size() == 0 && shape.getCutOuts().size() == 0) {
			Vector2f point = shape.getOutline().get(1);
			for(int i = 3; i < shape.getOutline().size(); i++) {
				Vector2f point2 = shape.getOutline().get(i);
				addSegment(point, point2);
			}
		}

		
		// Build a list of all points for construction of triangles
		ArrayList<Vector2f> points = new ArrayList<Vector2f>();
		points.addAll(shape.getOutline());
		for(List<Vector2f> cutOut: shape.getCutOuts()) {
			points.addAll(cutOut);
		}
		ArrayList<Vector2f> insidePoints = new ArrayList<Vector2f>(points);
		
		
		// Testing: drawing
//		for(Line segment: segments) {
//			renderer.drawLine(segment.getPoint1().x, segment.getPoint1().y, segment.getPoint2().x, segment.getPoint2().y, 0.001f, new Color4f(1,1,1,1));
//		}
//		for(Vector2f point: points) {
//			renderer.drawPoint(point.x, point.y, 3f, new Color4f(1,1,1,1));
//		}
//		for(Vector2f point: nonConvexPoints) {
//			renderer.drawPoint(point.x, point.y, 6f, new Color4f(1,0,0,1));
//		}
//		for(Vector2f point: nonConvexPointsParallel) {
//			renderer.drawPoint(point.x, point.y, 6f, new Color4f(0,1,0,1));
//		}

		
		ArrayList<Triangle> triangles = new ArrayList<Triangle>();
		
		// create triangles with the calculated segments by processing each point
		while(points.size() > 0) {
			Vector2f point1 = points.get(points.size() - 1);
			Vector2f point2;
			Vector2f point3;
			
			// process each combination of segments starting at the current point
			List<Line> pointSegments = segmentMap.get(point1);
			for(Line line: pointSegments) {
				
				// if the opposite point has been processed already this segment has already been processed
				point2 = line.getPoint(0);
				if(point2 == point1) point2 = line.getPoint(1);
				if(points.contains(point2)) {
					for(Line line2: pointSegments) {
						if(line == line2) continue;
						
						// if the opposite point has been processed already this segment has already been processed
						point3 = line2.getPoint(0);
						if(point3 == point1) point3 = line2.getPoint(1);
						if(points.contains(point3)) {
							if(getLine(point2, point3) != null) {
								
								if(validTriangle(point1, point2, point3, shape) && !containsPoint(point1, point2, point3, insidePoints)) {
									triangles.add(new ch.blackspirit.graphics.shape.Triangle(point1, point2, point3));
								}
							}
						}
					}
				}
			}
			points.remove(points.size() - 1);
		}
		return triangles;
	}

	private boolean containsPoint(Vector2f point, Vector2f point1, Vector2f point2, List<Vector2f> points) {
		boolean containsAPoint = false;
		for(Vector2f inside: points) {
			if(inside != point && inside != point1 && inside != point2) {
				if(Geometry.pointInTriangle(inside, point, point1, point2)) {
					containsAPoint = true;
				}
			}
		}
		return containsAPoint;
	}
	
	private boolean validTriangle(Vector2f point, Vector2f point1, Vector2f point2, TShape shape) {
		List<Vector2f> p1Container = null;
		List<Vector2f> p2Container = null;
		List<Vector2f> pContainer = null;
		
		if(shape.getOutline().contains(point)) {
			pContainer = shape.getOutline();
		} else {
			for(List<Vector2f> container: shape.getCutOuts()) {
				if(container.contains(point)) {
					pContainer = container;
				}
			}
		}
		if(shape.getOutline().contains(point1)) {
			p1Container = shape.getOutline();
		} else {
			for(List<Vector2f> container: shape.getCutOuts()) {
				if(container.contains(point1)) {
					p1Container = container;
				}
			}
		}
		if(shape.getOutline().contains(point2)) {
			p2Container = shape.getOutline();
		} else {
			for(List<Vector2f> container: shape.getCutOuts()) {
				if(container.contains(point2)) {
					p2Container = container;
				}
			}
		}
		if(pContainer == p1Container && p1Container == p2Container && pContainer != shape.getOutline()) {
			return pContainer.size() > 3;
		} else {
			return true;
		}
	}
	
	
	private Line getLine(Vector2f point1, Vector2f point2) {
		List<Line> lines = segmentMap.get(point1);
		if(lines == null) return null;
		for(Line line:lines) {
			if(line.getPoint(0) == point2 || line.getPoint(1) == point2) {
				return line;
			}
		}
		return null;
	}
	
	private void removeRedundantPoints(List<Vector2f> points) {
		for(int i = 0; i < points.size();) {
			int before = rolloverRange(i-1, points.size() -1);
			int after = rolloverRange(i+1, points.size() -1);
			float dist = Geometry.pointSegmentDistance(points.get(i), points.get(before), points.get(after));
			if(dist == 0) {
				points.remove(i);
			} else {
				i++;
			}
		}
	}
	
	private void populateNonConvexPoints(List<Vector2f> points2, boolean nonConvex) {
		nonConvexPointOutsideSegment.clear();
		nonConvexPointsParallel.clear();
		nonConvexPoints.clear();

		// separate list where parallel points will be removed
		LinkedList<Vector2f> points = new LinkedList<Vector2f>(points2);
		HashMap<Vector2f, Vector2f> nonConvexPointsParallelPredecessor = new HashMap<Vector2f, Vector2f>(100);

		// Handle parallel points
		if(!removeRedundantPoints) {
			// check for unnecessary points due to parallel segments
			for(int i = 0; i < points.size();) {
				int before = rolloverRange(i-1, points.size() -1);
				int after = rolloverRange(i+1, points.size() -1);
				float dist = Geometry.pointSegmentDistance(points.get(i), points.get(before), points.get(after));
				if(dist == 0) {
					nonConvexPointsParallel.add(points.get(i));
					nonConvexPointsParallelPredecessor.put(points.get(i), points.get(before));
					points.remove(i);
				} else {
					i++;
				}
			}
			for(Vector2f vec: nonConvexPointsParallel) {
				Vector2f pre = nonConvexPointsParallelPredecessor.get(vec);
				int preindex = rolloverRange(points.indexOf(pre), points.size() -1);
				int postindex = rolloverRange(preindex + 1, points.size() -1);
				
				Vector2f post = points.get(postindex);
				nonConvexPointOutsideSegment.put(vec, new Line(pre, post));
			}
		}
		
		int max = points.size() - 1;
		List<Vector2f> aSide = new ArrayList<Vector2f>(); 
		List<Vector2f> bSide = new ArrayList<Vector2f>();
		float aSideAngle = 0f;
		float bSideAngle = 0f;
		boolean useASide = true;
		Vector2f angleVec1 = new Vector2f();
		Vector2f angleVec2 = new Vector2f();
		for(int i = 0; i < points.size(); i++) {
			int before = rolloverRange(i-1,max);
			int after = rolloverRange(i+1,max);
			int after2 = rolloverRange(i+2,max);

			Vector2f currentVec = points.get(i);
			angleVec1.set(points.get(before));
			angleVec2.set(points.get(after));
			angleVec1.sub(currentVec);
			angleVec2.sub(currentVec);
			float angle = angleVec2.angle(angleVec1); 
				
			nonConvexPointOutsideSegment.put(currentVec, new Line(points.get(before), points.get(after)));
			
			if(useASide) {
				aSide.add(currentVec);
				aSideAngle += angle;
				bSideAngle += TWO_PI - angle;
			} else {
				bSide.add(currentVec);
				bSideAngle += angle;
				aSideAngle += TWO_PI - angle;
			}
			
			// check for side change
			if(Geometry.lineSegmentIntersect(currentVec, points.get(after), points.get(before), points.get(after2)) == Intersection.TRUE) {
				useASide = !useASide;
			}
		}
		
		if(nonConvex == false) {
			float temp = aSideAngle;
			aSideAngle = bSideAngle;
			bSideAngle = temp;
		}
		if(aSideAngle < bSideAngle) {
			nonConvexPoints.addAll(bSide);
			for(Vector2f p: aSide) {
				nonConvexPointOutsideSegment.remove(p);
			}
		} else {
			nonConvexPoints.addAll(aSide);
			for(Vector2f p: bSide) {
				nonConvexPointOutsideSegment.remove(p);
			}
		}
	}
	
	private int rolloverRange(int index, int max) {
		if(index > max) {
			return index - max - 1;
		} else if(index < 0) {
			return max + index + 1;
		} else {
			return index;
		}
	}
	
//	private void createSegment(Vector2f point1, Vector2f point2) {
//		if(!segmentIntersection(point1, point2)) {
//			addSegment(point1, point2);
//		}
//	}
	
	private boolean segmentIntersection(Vector2f point1, Vector2f point2) {
		for(Line segment: segments) {
			boolean samepoint1 = segment.getPoint(0) == point1 ||segment.getPoint(0) == point2;
			boolean samepoint2 = segment.getPoint(1) == point1 ||segment.getPoint(1) == point2;
			if(samepoint1 && samepoint2) return true;
			if(!(samepoint1 || samepoint2)) {
				if(!(Geometry.segmentIntersect(point1, point2, segment.getPoint(0), segment.getPoint(1)) == Intersection.FALSE)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void createSegments(List<Vector2f> points) {
		if(points.size() == 0) return;
		Vector2f last = points.get(points.size() - 1);
		for(Vector2f current: points) {
			addSegment(last, current);
//			System.out.println(last + "," + current);
			last = current;
		}
	}
	
	private void addSegment(Vector2f point1, Vector2f point2) {
		if(getLine(point1, point2) != null) {
			// Duplicates (should actually not happen, but does.
			return;
		}
		if(segmentIntersection(point1, point2)) {
			return;
		}
		
		Line line = new Line(point1, point2);
		segments.add(line);
		
		// Maintain line lists for points
		List<Line> lineList1 = segmentMap.get(point1);
		if(lineList1 == null) {
			lineList1 = new ArrayList<Line>();
			segmentMap.put(point1, lineList1);
		}
		lineList1.add(line);

		List<Line> lineList2 = segmentMap.get(point2);
		if(lineList2 == null) {
			lineList2 = new ArrayList<Line>();
			segmentMap.put(point2, lineList2);
		}
		lineList2.add(line);
	}
	
	private static final class TShape {
		private List<Vector2f> outline;
		private List<List<Vector2f>> cutOuts;

		public TShape(List<Vector2f> outline) {
			this(outline, null);
		}
		public TShape(List<Vector2f> outline, List<List<Vector2f>> cutOuts) {
			if(cutOuts == null) cutOuts = new ArrayList<List<Vector2f>>();
			this.outline = outline;
			this.cutOuts = cutOuts;
		}
		
		public List<Vector2f> getOutline() {
			return outline;
		}
		
		public List<List<Vector2f>> getCutOuts() {
			return cutOuts;
		}
		
		public void addCutOut(List<Vector2f> cutOut) {
			cutOuts.add(cutOut);
		}
		
//		public boolean removeCutOut(List<Vector2f> cutOut) {
//			return cutOuts.remove(cutOut);
//		}
	}
}
