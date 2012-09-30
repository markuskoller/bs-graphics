/*
 * Copyright 2008-2011 Markus Koller
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
package ch.blackspirit.graphics.geometry;

import javax.vecmath.Vector2f;

/**
 * @author Markus Koller
 */
public class Geometry {
	private static float ZERO_POS = 0.000001f; 
	private static float ZERO_NEG= -0.000001f; 
	
	public static Intersection segmentIntersect(Vector2f a, Vector2f b,
			Vector2f c, Vector2f d, Vector2f intersection) {

		float denominator = ((b.x-a.x)*(d.y-c.y)-(b.y-a.y)*(d.x-c.x)); 
		if(denominator > ZERO_NEG && denominator < ZERO_POS) {
			// TODO Single point parallel intersection
			if(pointSegmentDistanceSquare(a, c, d) == 0f) return Intersection.INFINITE;
			if(pointSegmentDistanceSquare(b, c, d) == 0f) return Intersection.INFINITE;
			if(pointSegmentDistanceSquare(c, a, b) == 0f) return Intersection.INFINITE;
			if(pointSegmentDistanceSquare(d, a, b) == 0f) return Intersection.INFINITE;
			return Intersection.FALSE;
		}
		
		float r = 	((a.y-c.y)*(d.x-c.x)-(a.x-c.x)*(d.y-c.y)) / denominator;
		if(r > 1 || r < 0) return Intersection.FALSE;
		
		float s = 	((a.y-c.y)*(b.x-a.x)-(a.x-c.x)*(b.y-a.y)) / denominator; 
		if(s > 1 || s < 0) return Intersection.FALSE;
	
		intersection.x = a.x + r * (b.x - a.x);
		intersection.y = a.y + r * (b.y - a.y);
		
		return Intersection.TRUE;
	}

	public static Intersection segmentIntersect(Vector2f a, Vector2f b,
			Vector2f c, Vector2f d) {
	
		float denominator = ((b.x-a.x)*(d.y-c.y)-(b.y-a.y)*(d.x-c.x)); 
		if(denominator > ZERO_NEG && denominator < ZERO_POS) {
			// TODO Single point parallel intersection
			if(pointSegmentDistanceSquare(a, c, d) == 0f) return Intersection.INFINITE;
			if(pointSegmentDistanceSquare(b, c, d) == 0f) return Intersection.INFINITE;
			if(pointSegmentDistanceSquare(c, a, b) == 0f) return Intersection.INFINITE;
			if(pointSegmentDistanceSquare(d, a, b) == 0f) return Intersection.INFINITE;
			return Intersection.FALSE;
		}
		
		float r = 	((a.y-c.y)*(d.x-c.x)-(a.x-c.x)*(d.y-c.y)) / denominator;
		if(r > 1 || r < 0.00001) return Intersection.FALSE;
		
		float s = 	((a.y-c.y)*(b.x-a.x)-(a.x-c.x)*(b.y-a.y)) / denominator; 
		if(s > 1 || s < 0.00001) return Intersection.FALSE;
	
		return Intersection.TRUE;
	}
	
	public static Intersection raySegmentIntersect(Vector2f a, Vector2f b,
			Vector2f c, Vector2f d, Vector2f intersection) {
		// ray starting at a with direction b
		float denominator = ((b.x-a.x)*(d.y-c.y)-(b.y-a.y)*(d.x-c.x)); 
		if(denominator > ZERO_NEG && denominator < ZERO_POS) {
			// TODO Single point parallel intersection
			if(pointSegmentDistanceSquare(a, c, d) == 0f) return Intersection.INFINITE;
			if(pointSegmentDistanceSquare(b, c, d) == 0f) return Intersection.INFINITE;
			if(pointSegmentDistanceSquare(c, a, b) == 0f) return Intersection.INFINITE;
			if(pointSegmentDistanceSquare(d, a, b) == 0f) return Intersection.INFINITE;
			return Intersection.FALSE;
		}
		
		float r = 	((a.y-c.y)*(d.x-c.x)-(a.x-c.x)*(d.y-c.y)) / denominator;
		if(r < 0) return Intersection.FALSE;
		
		float s = 	((a.y-c.y)*(b.x-a.x)-(a.x-c.x)*(b.y-a.y)) / denominator; 
		if(s > 1 || s < 0) return Intersection.FALSE;
	
		intersection.x = a.x + r * (b.x - a.x);
		intersection.y = a.y + r * (b.y - a.y);
		
		return Intersection.TRUE;
	}

	public static Intersection raySegmentIntersect(Vector2f a, Vector2f b,
			Vector2f c, Vector2f d) {
		// ray starting at a with direction b
		float denominator = ((b.x-a.x)*(d.y-c.y)-(b.y-a.y)*(d.x-c.x)); 
		if(denominator > ZERO_NEG && denominator < ZERO_POS) {
			// TODO Single point parallel intersection
			if(pointSegmentDistanceSquare(a, c, d) == 0f) return Intersection.INFINITE;
			if(pointSegmentDistanceSquare(b, c, d) == 0f) return Intersection.INFINITE;
			if(pointSegmentDistanceSquare(c, a, b) == 0f) return Intersection.INFINITE;
			if(pointSegmentDistanceSquare(d, a, b) == 0f) return Intersection.INFINITE;
			return Intersection.FALSE;
		}
		
		float r = 	((a.y-c.y)*(d.x-c.x)-(a.x-c.x)*(d.y-c.y)) / denominator;
		if(r < 0) return Intersection.FALSE;
		
		float s = 	((a.y-c.y)*(b.x-a.x)-(a.x-c.x)*(b.y-a.y)) / denominator; 
		if(s > 1 || s < 0) return Intersection.FALSE;
	
		return Intersection.TRUE;
	}

	public static Intersection lineSegmentIntersect(Vector2f a, Vector2f b,
			Vector2f c, Vector2f d, Vector2f intersection) {

		float denominator = ((b.x-a.x)*(d.y-c.y)-(b.y-a.y)*(d.x-c.x)); 
		if(denominator > ZERO_NEG && denominator < ZERO_POS) {
			// TODO Single point parallel intersection
			// TODO Point line distance
			if(pointLineDistanceSquare(c, a, b) == 0f) return Intersection.INFINITE;
			if(pointLineDistanceSquare(d, a, b) == 0f) return Intersection.INFINITE;
			return Intersection.FALSE;
		}
		
		float s = 	((a.y-c.y)*(b.x-a.x)-(a.x-c.x)*(b.y-a.y)) / denominator; 
		if(s > 1 || s < 0) return Intersection.FALSE;
	
		intersection.x = c.x + s * (d.x - c.x);
		intersection.y = c.y + s * (d.y - c.y);
		
		return Intersection.TRUE;
	}
	
	public static Intersection lineSegmentIntersect(Vector2f a, Vector2f b,
			Vector2f c, Vector2f d) {

		float denominator = ((b.x-a.x)*(d.y-c.y)-(b.y-a.y)*(d.x-c.x)); 
		if(denominator > ZERO_NEG && denominator < ZERO_POS) {
			// TODO Single point parallel intersection
			// TODO Point line distance
			if(pointLineDistanceSquare(c, a, b) == 0f) return Intersection.INFINITE;
			if(pointLineDistanceSquare(d, a, b) == 0f) return Intersection.INFINITE;
			return Intersection.FALSE;
		}
		
		float s = 	((a.y-c.y)*(b.x-a.x)-(a.x-c.x)*(b.y-a.y)) / denominator; 
		if(s > 1 || s < 0) return Intersection.FALSE;
	
		return Intersection.TRUE;
	}
	

	public static float pointSegmentDistance(Vector2f P, Vector2f A, Vector2f B) {
		return (float)Math.sqrt(pointSegmentDistanceSquare(P, A, B));
	}
	
	public static float pointSegmentDistanceSquare(Vector2f P, Vector2f A, Vector2f B) {
	    float px;
	    float py;
	    
		// calculation of frequently used values
	    // instead of making new variables, 2 floats could be used for all temporary values for performance
	    float bax = B.x - A.x;
	    float bay = B.y - A.y;

	    // numer = dotProduct(P-A,B-A)
	    float numer = (P.x - A.x)*bax + (P.y - A.y)*bay;  
	    if (numer <= 0.0f) {
	        px = A.x;
	        py = A.y;
	    } else {
		    // denom = dotProduct(B-A,B-A)
		    float denom = bax*bax + bay*bay;
		    if (numer >= denom) {
		        px = B.x;
		        py = B.y;
		    } else {
		    	float temp = (numer/denom);
			    px = A.x + temp * bax;
			    py = A.y + temp * bay;
		    }
	    }
	    
	    // instead of making new variables, 2 floats could be used for all temporary values for performance
	    float distanceX = px-P.x;
	    float distanceY = py-P.y;
	    return distanceX*distanceX + distanceY*distanceY;
	}
	
	public static float pointLineDistance(Vector2f P, Vector2f A, Vector2f B) {
		return (float)Math.sqrt(pointLineDistanceSquare(P, A, B));
	}

	public static float pointLineDistanceSquare(Vector2f P, Vector2f A, Vector2f B) {
		// calculation of frequently used values
	    // instead of making new variables, 2 floats could be used for all temporary values for performance
	    float bax = B.x - A.x;
	    float bay = B.y - A.y;

	    // numer = dotProduct(P-A,B-A)
	    //float numer = (P.x - A.x)*bax + (P.y - A.y)*bay;  
	    // denom = dotProduct(B-A,B-A)
	    //float denom = bax*bax + bay*bay;
	    //float temp = (numer/denom);

	    float temp = ((P.x - A.x)*bax + (P.y - A.y)*bay)/(bax*bax + bay*bay);

	    //float px = A.x + temp * bax;
	    //float py = A.y + temp * bay;
	    //float distanceX = px-P.x;
	    //float distanceY = py-P.y;

	    // instead of making new variables, 2 floats could be used for all temporary values for performance
	    float distanceX = A.x + temp * bax-P.x;
	    float distanceY = A.y + temp * bay-P.y;
	    return distanceX*distanceX + distanceY*distanceY;
	}

	public static boolean pointInTriangle(Vector2f p, Vector2f a, Vector2f b, Vector2f c) {
		return 	!(lineSegmentIntersect(a, b, p, c) != Intersection.FALSE ||
				lineSegmentIntersect(a, c, p, b) != Intersection.FALSE ||
				lineSegmentIntersect(b, c, p, a) != Intersection.FALSE);
	}
	
	public static void centerOfTriangle(Vector2f t1, Vector2f t2, Vector2f t3, Vector2f point) {
		point.set(t1);
		point.add(t2);
		point.add(t3);
		point.scale(1f/3f);
	}
	/*
	public static final float MINIMUM_VALID_AREA = 1f;
	public static boolean isPointInTriangle(Vector2f t1, Vector2f t2, Vector2f t3, Vector2f point) {
		//calculate the three cross products in one direction
		
		float[] z = new float[3];
		
		//check if its a corner
		if (t1.equals(point)) return true;
		if (t2.equals(point)) return true;
		if (t3.equals(point)) return true;
		
		// corner 1
		Vector2f corner = t1;
		Vector2f nextCorner = t2;
		//calculate a 3d cross product z coordinate
		z[0] = (nextCorner.y - corner.y) * (point.x - nextCorner.x) - (nextCorner.x - corner.x) * (point.y - nextCorner.y);
		
		// corner 2
		corner = t2;
		nextCorner = t3;
		//calculate a 3d cross product z coordinate
		z[1] = (nextCorner.y - corner.y) * (point.x - nextCorner.x) - (nextCorner.x - corner.x) * (point.y - nextCorner.y);

		// corner 3
		corner = t3;
		nextCorner = t1;
		//calculate a 3d cross product z coordinate
		z[2] = (nextCorner.y - corner.y) * (point.x - nextCorner.x) - (nextCorner.x - corner.x) * (point.y - nextCorner.y);

		//all z must have same signum or one must be 0 (point on triangle border)
		
		for (int i = 0; i < z.length; i++) {
			if (z[i] < MINIMUM_VALID_AREA && z[i] > -MINIMUM_VALID_AREA) z[i] = 0;
		}
		
		boolean ret = true;
		ret = ret && (Math.signum(z[0]) == Math.signum(z[1]) || z[0] == 0.0f || z[1] == 0.0f);
		ret = ret && (Math.signum(z[1]) == Math.signum(z[2]) || z[1] == 0.0f || z[2] == 0.0f);
		ret = ret && (Math.signum(z[0]) == Math.signum(z[2]) || z[0] == 0.0f || z[2] == 0.0f);	
		
		return ret;
	}
	
	private static final float RADIUS_ERROR_TOLERANCE = 1f;
	public static boolean isPointInTriangleCircumCircle(Vector2f t1, Vector2f t2, Vector2f t3, Vector2f point) {
		
		//calculate the intersectionpoint between the two "mittelsenkrechte"
		// "mittelsenkrechte": y = m1*x + q1 and y = m1*x + q1, then intersect.
		// perpendicular: m = -(1 / ms) where ms is the gradient of the original straight
		float m1 = - (t2.x - t1.x)/(t2.y - t1.y);
		float m2 = - (t3.x - t2.x)/(t3.y - t2.y);

		//coordinates of the intersection point
		float mx = 0;
		float my = 0;
		
		float q1 = ((t2.y + t1.y) / 2) - m1 * ((t2.x + t1.x) / 2);
		float q2 = ((t3.y + t2.y) / 2) - m2 * ((t3.x + t2.x) / 2);
		
		// check if gradient of "mittelsenkrechte" is vertical (gradient is infinite)
		if(Float.isInfinite(m1)) {
			mx = (t2.x + t1.x)/2;
			my = m2 * mx + q2;
		} else if(Float.isInfinite(m2)) {
			mx = (t3.x + t2.x)/2;
			my = m1 * mx + q1;
		} else {
			//then solve the equation system for the intersect: y = m1x + q1 and y = m2x + q2
			mx = (q2 - q1) / (m1 - m2);
			my = m1 * mx + q1;
		}
		Vector2f circumCircleCenter = new Vector2f(mx,my);
		
		float radiusSquared = distancePointsSquared(circumCircleCenter, t1);
		
		float distanceSquared = distancePointsSquared(circumCircleCenter, point);
		
		// to avoid endless flipping in triangulation
		if (radiusSquared - RADIUS_ERROR_TOLERANCE > distanceSquared) {
			return true;
		} else {
			return false;
		}
	}
	public static float areaTriangle(Vector2f t1, Vector2f t2, Vector2f t3) {
		Vector2f side1 = new Vector2f();
		side1.set(t2);
		side1.sub(t1);
		Vector2f side2 = new Vector2f();
		side2.set(t3);
		side2.sub(t2);
		
		return Math.abs((side1.x * side2.y - side1.y * side2.x) / 2);
		
	}
	*/

	public static float distancePointsSquared(Vector2f p1, Vector2f p2) {
		return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
	}
	public static float distancePoints(Vector2f p1, Vector2f p2) {
		return (float)Math.sqrt(distancePointsSquared(p1, p2));
	}

}
