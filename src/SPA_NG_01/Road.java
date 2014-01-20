package SPA_NG_01;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;


public class Road {

	private WheelPath outerBoundary,innerBoundary;

	public Road(String innerFileName, String outerFileName) {
		innerBoundary = new WheelPath(innerFileName);
		outerBoundary = new WheelPath(outerFileName);
	}
	
	public Road(WheelPath innBound, WheelPath outBound){
		innerBoundary = innBound;
		outerBoundary = outBound;
	}

	public Rectangle2D getBoundary(){
		return outerBoundary.getBoundary().createUnion(innerBoundary.getBoundary());
	}
	
	public void draw(Graphics2D g2d, Color color){
		outerBoundary.draw(g2d, color);
		innerBoundary.draw(g2d, color);
	}
}