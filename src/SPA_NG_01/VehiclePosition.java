package SPA_NG_01;


import java.awt.geom.Point2D;

public class VehiclePosition {
	private Point2D location;
	private double rotation;
	
	public VehiclePosition(Point2D loc, double rot){
		location = loc;
		rotation = rot;
	}
	public VehiclePosition(double x, double y, double rot){
		location = new Point2D.Double(x,y);
		rotation = rot;
	}
	public Point2D getLocation(){
		return location;
	}
	public double getRotation(){
		return rotation;
	}
}
