package SPA_NG_01;

/*
 *        Name: Nestor Gomez
 *      Course: 12-746 -- Rapid Prototyping for AIS though O-O Programming
 *  Assignment: Problem Set 2
 *    Due Date: Sept. 14, 2012
 *   File Name: WheelPath.java
 * Description: The swept path analysis prototype we will build throughout the course
 * 				essentially helps to visualize a vehicle train as the guiding wheels
 * 				are led along a specific path.  This problem set will develop the class
 * 				WheelPath (and others deemed necessary) in order to implement a path that
 * 				can be used to solve the swept path analysis problem.
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WheelPath {

	private ArrayList<Point2D.Double> vertices = new ArrayList<Point2D.Double>();

	public static final int HIDE_VERTICES = 0;
	public static final int CIRCLE_VERTICES = 1;
	public static final int DIAMOND_VERTICES = 2;
	public static final int SQUARE_VERTICES = 3;
	public static final int CROSS_VERTICES = 4;

	private int vertexStyle = HIDE_VERTICES;

	public WheelPath(String inputFileName) {
		readFromFile(inputFileName);
	}

	public void readFromFile(String inputFileName) {
		BufferedReader inputStream;
		try {
			inputStream = new BufferedReader(new FileReader(inputFileName));
			String line = null;

			try {
				vertices.clear();
				while ((line = inputStream.readLine()) != null) {
					vertices.add(new Point2D.Double(java.lang.Double
							.parseDouble(line.split(",", 2)[0]),
							java.lang.Double.parseDouble(line.split(",", 2)[1])));
				}
				inputStream.close();
				// System.out.println(vertices.toString());

			} catch (NumberFormatException e) {
				System.out.println("Could not convert " + line
						+ " to values of type double.");
			} catch (IOException e) {
				System.out.println("Could not read file " + inputFileName);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + inputFileName);
		}

	}


	public Path2D offset(double distance) {
		// -> returns a new Path2D which is equidistant from the original path
		// (similar to clone). Note that the offset path may have fewer segments
		// than the original path.
		ArrayList<Point2D.Double> newVertices = new ArrayList<Point2D.Double>();
		Path2D.Double newPath = new Path2D.Double(Path2D.WIND_EVEN_ODD);
		int i, j, k;
		double newX, newY, currLength = 0, adjustedDistance, segToPerpAngle;
		Line2D currPerpendicular;
		for (i = 0; i <= (vertices.size() - 1); i++) { // go through vertices
														// and get perpendicular
														// offset
			currPerpendicular = getPerpendicular(currLength);
			if (i == 0 || i == (vertices.size() - 1))
				adjustedDistance = distance;
			else {

				segToPerpAngle = Line2DUtility.getAngleBetween(
						currPerpendicular.getX1(), currPerpendicular.getY1(),
						vertices.get(i - 1).getX(), vertices.get(i - 1).getY(),
						currPerpendicular.getX2(), currPerpendicular.getY2());
				adjustedDistance = distance
						/ Math.sin(segToPerpAngle / 180. * Math.PI);
			}
			newX = vertices.get(i).getX() + adjustedDistance
					* (currPerpendicular.getX2() - currPerpendicular.getX1());
			newY = vertices.get(i).getY() + adjustedDistance
					* (currPerpendicular.getY2() - currPerpendicular.getY1());
			newVertices.add(new Point2D.Double(newX, newY));
			if (i < (vertices.size() - 1))
				currLength = currLength
						+ vertices.get(i).distance(vertices.get(i + 1));

		}
		// remove any "loops" that exists in newVertices (assumes WheelPath does
		// not self intersect)
		Line2D.Double thisSegment, otherSegment;
		i = 0;
		while (i < newVertices.size() - 3) {
			thisSegment = new Line2D.Double(newVertices.get(i),
					newVertices.get(i + 1));
			j = i + 2;
			while (j < (newVertices.size() - 1)) {
				// check if segment from i to i+1 intersects segment j to j+1
				// if intersects, remove all vertices from i to j and insert a
				// new vertex at the intersection point.
				otherSegment = new Line2D.Double(newVertices.get(j),
						newVertices.get(j + 1));
				if (thisSegment.intersectsLine(otherSegment)) {
					Point2D.Double interception = Line2DUtility
							.findIntersection(thisSegment, otherSegment);
					for (k = i + 1; k <= j; k++)
						newVertices.remove(i + 1);
					newVertices.add(i + 1, interception);
					j = 10 * newVertices.size(); // this will cause exit from j
													// while loop
				} else
					j++;
			}
			i++;
		}

		// load vertices into newPath
		newPath.moveTo(newVertices.get(0).getX(), newVertices.get(0).getY());
		// System.out.println(newVertices.get(0).toString());
		for (i = 1; i < newVertices.size(); i++) {
			// System.out.println(newVertices.get(i).toString());
			newPath.lineTo(newVertices.get(i).getX(), newVertices.get(i).getY());
		}

		return newPath;
	}

	public Point2D.Double getPointAlongLength(double lengthAlongPath) {
		// -> given a length along the path, return the coordinates at that
		// point.
		int currVertex = 0;
		int numberOfVertices = vertices.size();
		double leadLocation = 0., tailLocation = 0;

		if (lengthAlongPath < 0)
			return null;
		else if (lengthAlongPath == 0) // lengthAlongPath = 0, so return (first
										// point of path)
			return new Point2D.Double(vertices.get(0).getX(), vertices.get(0)
					.getY());
		else {
			while (leadLocation <= lengthAlongPath
					&& currVertex < numberOfVertices - 1) {
				tailLocation = leadLocation;
				leadLocation = leadLocation
						+ vertices.get(currVertex).distance(
								vertices.get(currVertex + 1));
				currVertex++;
			}
			if (leadLocation >= lengthAlongPath) { // point needed is between
													// currVertex and previous
													// Vertex
				double x1, y1, x2, y2, xnew, ynew;
				x1 = vertices.get(currVertex - 1).getX();
				y1 = vertices.get(currVertex - 1).getY();
				x2 = vertices.get(currVertex).getX();
				y2 = vertices.get(currVertex).getY();
				xnew = x1 + (lengthAlongPath - tailLocation)
						/ (leadLocation - tailLocation) * (x2 - x1);
				ynew = y1 + (lengthAlongPath - tailLocation)
						/ (leadLocation - tailLocation) * (y2 - y1);
				if (Math.abs(xnew - x1) < (xnew / 10e8)
						&& Math.abs(ynew - y1) < (ynew / 10e8))
					return new Point2D.Double(x1, y1);
				else if (Math.abs(xnew - x2) < (xnew / 10e8)
						&& Math.abs(ynew - y2) < (ynew / 10e8))
					return new Point2D.Double(x2, y2);
				else
					return new Point2D.Double(xnew, ynew);
			} else { // lengthAlongPath is larger than length of whole path so
						// return null
				return null;
			}
		}
	}

	public boolean insertVertexFractional(int segmentNumber, double fraction) {
		// -> inserts a new vertex in the given segment at a location given by
		// a fraction (0 < fraction < 1.0) of the segment length. Returns true
		// if a vertex was successfully added.
		if (segmentNumber < 0 || segmentNumber >= vertices.size()
				|| fraction <= 0 || fraction >= 1.0)
			return false;
		else {
			double x1, y1, x2, y2, xnew, ynew;
			x1 = vertices.get(segmentNumber - 1).getX();
			y1 = vertices.get(segmentNumber - 1).getY();
			x2 = vertices.get(segmentNumber).getX();
			y2 = vertices.get(segmentNumber).getY();
			xnew = x1 + fraction * (x2 - x1);
			ynew = y1 + fraction * (y2 - y1);
			vertices.add(segmentNumber - 1, new Point2D.Double(xnew, ynew));
			return true;
		}
	}

	public boolean insertVertexAbsolute(int segmentNumber, double length) {
		// -> inserts a new vertex in the given segment at a location given by
		// a length along the segment. If length is greater than segment, do not
		// insert vertex. Returns true if a vertex was successfully added.
		if (segmentNumber < 0 || segmentNumber >= vertices.size()
				|| length <= 0.)
			return false;
		else {
			double segmentLength = vertices.get(segmentNumber - 1).distance(
					vertices.get(segmentNumber));
			return insertVertexFractional(segmentNumber, length / segmentLength);
		}
	}

	public Line2D.Double getPerpendicular(double lengthAlongPath) {
		// -> return a line segment representing the perpendicular at given
		// point.
		// If the given point is a vertex, the returning line segment should
		// bisect the angle at the vertex.
		Line2D.Double theTangent = getTangent(lengthAlongPath);
		if (theTangent == null)
			return null;
		else { // use cross product of theTangent and a unit vector upwards from
				// point1 of theTangent
			double newX = theTangent.getX1()
					+ (theTangent.getY2() - theTangent.getY1());
			double newY = theTangent.getY1()
					- (theTangent.getX2() - theTangent.getX1());
			return new Line2D.Double(theTangent.getX1(), theTangent.getY1(),
					newX, newY);
		}
	}

	public Line2D.Double getTangent(double lengthAlongPath) {
		// -> return a line segment representing the tangent at given point. If
		// the given point is a vertex, the returning line segment should have
		// equal angles with the two path segments that share the vertex.

		Point2D.Double point1 = getPointAlongLength(lengthAlongPath);
		int currSegment = getSegmentNumber(lengthAlongPath);
		double newX, newY, segLength;

		if (lengthAlongPath < 0 || point1 == null || currSegment == -1)
			return null;
		else if (point1.equals(vertices.get(vertices.size() - 1))) {
			// point1 is at very end of path, use next-to-last vertex as other
			// point
			// and change to negative unit vector
			segLength = point1.distance(vertices.get(currSegment - 1));
			newX = point1.getX()
					- (vertices.get(currSegment - 1).getX() - point1.getX())
					/ segLength;
			newY = point1.getY()
					- (vertices.get(currSegment - 1).getY() - point1.getY())
					/ segLength;
			return new Line2D.Double(point1.getX(), point1.getY(), newX, newY);
		} else if (currSegment > 0 && point1.equals(vertices.get(currSegment))) {
			// request is at a middle vertex
			// System.out.println("At getTangent on vertex " + currSegment +
			// " with " + lengthAlongPath);
			Point2D.Double prevPoint = vertices.get(currSegment - 1);
			Point2D.Double nextPoint = vertices.get(currSegment + 1);

			// get unit vectors for previous and next segments
			segLength = point1.distance(prevPoint);
			double prevVx = (prevPoint.getX() - point1.getX()) / segLength;
			double prevVy = (prevPoint.getY() - point1.getY()) / segLength;

			segLength = point1.distance(nextPoint);
			double nextVx = (nextPoint.getX() - point1.getX()) / segLength;
			double nextVy = (nextPoint.getY() - point1.getY()) / segLength;

			newX = -prevVx + nextVx; // adding the negative of prev vector and
										// next vector
			newY = -prevVy + nextVy;

			segLength = Math.sqrt(newX * newX + newY * newY);
			newX = point1.getX() + newX / segLength; // get unit vectors for
														// tangent
			newY = point1.getY() + newY / segLength;

			return new Line2D.Double(point1.getX(), point1.getY(), newX, newY);
		} else {
			// request is at vertex 0, use next vertex as tangent end point and
			// change to unit vector
			segLength = point1.distance(vertices.get(currSegment + 1));
			newX = point1.getX()
					+ (vertices.get(currSegment + 1).getX() - point1.getX())
					/ segLength;
			newY = point1.getY()
					+ (vertices.get(currSegment + 1).getY() - point1.getY())
					/ segLength;
			return new Line2D.Double(point1.getX(), point1.getY(), newX, newY);
		}
	}

	public int getSegmentNumber(double lengthAlongPath) { // will likely become
															// private later
		// returns segment number where lengthAlongPath is found.
		// If on a vertex, returns lower-valued segment
		// (e.g., if on vertex between seg3 and seg4, returns 3. If on last
		// vertex,
		// returns size of ArrayList. If on first vertex returns 0.)
		// If path is shorter than lengthAlongPath returns -1

		int currVertex = 0;
		int numberOfVertices = vertices.size();
		double leadLocation = 0.;
		while (leadLocation <= lengthAlongPath
				&& currVertex < numberOfVertices - 1) {
			leadLocation = leadLocation
					+ vertices.get(currVertex).distance(
							vertices.get(currVertex + 1));
			currVertex++;
		}
		if (leadLocation < lengthAlongPath)
			return -1;
		else if (Math.abs(leadLocation - lengthAlongPath) < leadLocation / 10e8)
			return currVertex;
		else
			return currVertex - 1;
	}

	@Override
	public String toString() {
		int i, sigFigs = 3;
		String theString = null;
		for (i = 0; i < vertices.size(); i++) {
			if (i == 0)
				theString = "[";
			else
				theString = theString + ",";
			theString = theString
					+ "("
					+ roundToSignificantFigures(vertices.get(i).getX(), sigFigs)
					+ ","
					+ roundToSignificantFigures(vertices.get(i).getY(), sigFigs)
					+ ")";
		}
		if (theString != null)
			theString = theString + "]";

		return theString;
	}

	private static double roundToSignificantFigures(double num, int n) {
		if (num == 0) {
			return 0;
		}

		final double d = Math.ceil(Math.log10(num < 0 ? -num : num));
		final int power = n - (int) d;

		final double magnitude = Math.pow(10, power);
		final long shifted = Math.round(num * magnitude);
		return shifted / magnitude;
	}

	public Rectangle2D getBoundary() {
		double minX, minY, maxX, maxY, currX, currY;
		minX = vertices.get(0).getX();
		minY = vertices.get(0).getY();
		maxX = vertices.get(0).getX();
		maxY = vertices.get(0).getY();
		for (int i = 1; i < vertices.size(); i++){
			currX = vertices.get(i).getX();
			currY = vertices.get(i).getY();
			if(currX < minX) 
				minX = currX;
			else if (currX > maxX) 
				maxX = currX;
			if(currY < minY) 
				minY = currY;
			else if (currY > maxY) 
				maxY = currY;
		}
		return new Rectangle2D.Double(minX, minY, maxX-minX, maxY-minY);

	}

	public void showVertices(int style) {
		// -> after this method call, subsequent plots of the WheelPath will
		// show the vertex locations using one of several styles (circle,
		// diamond, etc.)
		vertexStyle = style;
	}

	public void hideVertices() {
		// -> after this method call, subsequent plots of the WheelPath
		// will not show the vertex locations.
		vertexStyle = HIDE_VERTICES;
	}

	public void draw(Graphics2D g2d, Color color, int style) {
		vertexStyle = style;
		draw(g2d, color);
	}
	public void draw(Graphics2D g2d, Color color) {
		int i, X1, Y1, X2, Y2;
		int vertexSize = 1 * (int)SPA_Viewer.preScale;
		
		g2d.setColor(color);
		for (i = 0; i < vertices.size() ; i++){
			X1 = (int) (vertices.get(i).getX() * SPA_Viewer.preScale);
			Y1 = (int) (vertices.get(i).getY() * SPA_Viewer.preScale);
			if (i<vertices.size()-1){
				X2 = (int) (vertices.get(i+1).getX() * SPA_Viewer.preScale);
				Y2 = (int) (vertices.get(i+1).getY() * SPA_Viewer.preScale);
				g2d.drawLine(X1, Y1, X2, Y2);
			}
			switch (vertexStyle){
			case SQUARE_VERTICES:
				g2d.drawRect(X1 - vertexSize, Y1 -vertexSize, 2*vertexSize, 2*vertexSize);
				break;
			case CIRCLE_VERTICES:
				g2d.drawOval(X1 - vertexSize, Y1 -vertexSize, 2*vertexSize, 2*vertexSize);
				break;
			case DIAMOND_VERTICES:
				g2d.drawLine(X1 - vertexSize, Y1 , X1, Y1 + vertexSize);
				g2d.drawLine(X1 - vertexSize, Y1 , X1, Y1 - vertexSize);
				g2d.drawLine(X1 + vertexSize, Y1 , X1, Y1 + vertexSize);
				g2d.drawLine(X1 + vertexSize, Y1 , X1, Y1 - vertexSize);
				break;
			case CROSS_VERTICES:
				g2d.drawLine(X1 - vertexSize, Y1 - vertexSize , X1 + vertexSize, Y1 + vertexSize);
				g2d.drawLine(X1 - vertexSize, Y1 + vertexSize , X1 + vertexSize, Y1 - vertexSize);
				break;
			}
			
		}
	}

}