package SPA_NG_01;
/*
 *        Name: Nestor Gomez
 *      Course: 12-746 -- Rapid Prototyping for AIS though O-O Programming 
 *  Assignment: Problem Set 2
 *    Due Date: Sept. 14, 2012
 *   File Name: Line2DUtility.java
 * Description: The swept path analysis prototype we will build throughout the course 
 * 				essentially helps to visualize a vehicle train as the guiding wheels 
 * 				are led along a specific path.  This problem set will develop the class 
 * 				WheelPath (and others deemed necessary) in order to implement a path that 
 * 				can be used to solve the swept path analysis problem.
 */

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Line2DUtility {
	//private ArrayList theVertices = new ArrayList();
	
	public static boolean areParallel(Line2D.Double line1, Line2D.Double line2){
		// - > given two line segments, determine if they are parallel to each other.
		if (line1.getX1() == line1.getX2()) {   //  line 1 is vertical
		    if (line2.getX1() == line2.getX2()) //   both lines are vertical
		    	return true;
		    else
		    	return false;
		}
		else if (line2.getX1() == line2.getX2()) // line 2 is vertical, but line 1 is not
			return false;
		else {
		    double slope1 = (line1.getY2() - line1.getY1()) / (line1.getX2() - line1.getX1());
		    double slope2 = (line2.getY2() - line2.getY1()) / (line2.getX2() - line2.getX1());
		    return Math.abs(slope1-slope2) < 10e-5; // tolerance for (slope1 == slope2)
		}
	}
	public static boolean arePerpendicular(Line2D.Double line1, Line2D.Double line2){
		// - > given two line segments, determine in they are perpendicular to each other.
		if (line1.getX1() == line1.getX2()) {   //  line 1 is vertical
		    if (line2.getY1() == line2.getY2()) //   line 2 is horizontal
		    	return true;
		    else
		    	return false;
		}
		else if (line2.getX1() == line2.getX2()) { // line 2 is vertical
		    if (line1.getY1() == line1.getY2()) //   line 1 is horizontal
		    	return true;
		    else
		    	return false;
		}
		else if (line2.getY1() == line2.getY2())  // line 2 is horizontal, but line 1 is not vertical
			return false;
		else {
		    double slope1 = (line1.getY2() - line1.getY1()) / (line1.getX2() - line1.getX1());
		    double slope2 = (line2.getY2() - line2.getY1()) / (line2.getX2() - line2.getX1());
		    return Math.abs(1 + slope1*slope2) < 10e-5; // tolerance for (slope1 == -1/slope2)
		}
	}
	
	public static Line2D getPerp(Line2D other){
		// -> returns a line segment perpendicular to other and with same starting point as other.
		double newX = other.getX1() + (other.getY2() - other.getY1());
		double newY = other.getY1() - (other.getX2() - other.getX1());
		return new Line2D.Double(other.getX1(), other.getY1(), newX, newY);
	}
	
	public static Point2D.Double findIntersection(Line2D line1, Line2D line2){
		// - > given two line segments, determine the coordinates of the intersection 
		// point of the infinite extensions of the two segments. Returns null if segments
		// are parallel.
		double wantedX, wantedY;
		if (line1.getX1() == line1.getX2()) {   //  line 1 is vertical
		    if (line2.getX1() == line2.getX2()) //   both lines are vertical, no intersection  << possible exit from method
		        return null;     							
		    else {                   // find point at which line 2 intersects vertical line 1
		        	wantedX = line1.getX1();
		          //wantedY = (x1b - x2a) / (x2b - x2a) * (y2b - y2a) + y2a
				    wantedY = (line1.getX2() - line2.getX1()) / (line2.getX2() - line2.getX1()) 
				    				* (line2.getY2() - line2.getY1()) + line2.getY1();
		    }
		}
		else if (line2.getX1() == line2.getX2()) {   // line 2 is vertical, line 1 is not
			wantedX = line2.getX1(); // line.get()
	        //wantedY = (x2b - x1a) / (x1b - x1a) * (y1b - y1a) + y1a
			wantedY = (line2.getX2() - line1.getX1()) / (line1.getX2() - line1.getX1()) 
							* (line1.getY2() - line1.getY1()) + line1.getY1();
		}
		else {  // neither line is vertical
		    double slope1 = (line1.getY2() - line1.getY1()) / (line1.getX2() - line1.getX1());
		    double intercept1 = line1.getY1() - slope1 * line1.getX1();
		    double slope2 = (line2.getY2() - line2.getY1()) / (line2.getX2() - line2.getX1());
		    double intercept2 = line2.getY1() - slope2 * line2.getX1();
		    
		    if (Math.abs(slope1-slope2)<1e-8) // lines are parallel, no intersection  << possible exit from method
		    	return null;
		    else {
		    	wantedX = (intercept1 - intercept2) / (slope2 - slope1);
		    	wantedY = wantedX * slope1 + intercept1;
		    }
		}
		
		return new Point2D.Double(wantedX, wantedY);
	}

	public static double getAngleBetween(Line2D.Double line1, Line2D.Double line2){
		// returns the angle between the two line segments
		double vector1X = line1.getX2() - line1.getX1();
		double vector1Y = line1.getY2() - line1.getY1();
		double vector2X = line2.getX2() - line2.getX1();
		double vector2Y = line2.getY2() - line2.getY1();
		double length1 = Math.sqrt(vector1X*vector1X + vector1Y*vector1Y) ;
		double length2 = Math.sqrt(vector2X*vector2X + vector2Y*vector2Y) ;
		double cosTheta = ( vector1X*vector2X + vector1Y*vector2Y ) / length1 / length2; // based on vector dot product
		double sinTheta = ( vector1X*vector2Y - vector2X*vector1Y ) / length1 / length2; // based on vector cross product
		cosTheta = Math.max(cosTheta, -1.);
		cosTheta = Math.min(cosTheta, 1.);
		if (sinTheta >= 0)
			return Math.acos(cosTheta)*180./Math.PI;
		else
			return 360. - Math.acos(cosTheta)*180./Math.PI;
	}
	
	public static double getAngleBetween(double x0, double y0, double x1, double y1, double x2, double y2){
		// return the angle (in degrees) between the two vectors (x0,y0),(x1,y1) and (x0,y0),(x2,y2)
		return getAngleBetween(new Line2D.Double(x0,y0,x1,y1), new Line2D.Double(x0,y0,x2,y2));
	}

	public static double getAngleStandard(Line2D theLine){
		// return the angle (in degrees) between line and the positive X-axis
		return getAngleStandard(theLine.getP1(), theLine.getP2());
	}
	
	public static double getAngleStandard(Point2D point1, Point2D point2){
		// return the angle (in degrees) between line made from point1 to point2 and the positive X-axis
		double theAngle;
		if (point2.getX() == point1.getX()){
			if (point2.getY() >= point1.getY())
				theAngle = 90.;
			else
				theAngle = 180.;
		}else 
			theAngle = Math.atan((point2.getY()-point1.getY())/(point2.getX()-point1.getX())) / Math.PI * 180.;

		if (point2.getX() < point1.getX())  // need this correction since arctangent only returns 90 to -90.
			theAngle = 180. + theAngle;
		if (theAngle < 0)                      // clearer to return an angle between 0 and 360
			theAngle = 360 + theAngle;
		
		return theAngle;

	}

}
