package SPA_NG_01;


import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;


public class LeadVehicle extends Vehicle{
	
	// inherited from Vehicle
	//protected ArrayList<VehiclePosition> stepData = new ArrayList<VehiclePosition>();
	//protected String description;
	//protected Pivot frontPivot = null, backPivot = null;
	//protected double length, width;
	//protected ArrayList<Axle> theAxles = new ArrayList<Axle>();

	public static final int FRONT_LEFT_WHEEL_LEAD = 1;
	public static final int MID_FRONT_BUMPER_LEAD = 2;
	public static final int MID_FRONT_AXLE_LEAD = 3;
	
	// select a leading reference point:
	private int refLocationType = FRONT_LEFT_WHEEL_LEAD;
	//private int refLocationType = MID_FRONT_BUMPER_LEAD;
	//private int refLocationType = MID_FRONT_AXLE_LEAD;
	
	public LeadVehicle(BufferedReader input) throws IOException{
		super(input, null);
	}
	public void write(OutputStream output){
		
	}
	
	protected double getRearAxleRefLocation(int stepNumber){
		return 0.;
	}
		
	public void reset(Point2D refPoint, double initialAngle){
		stepData.clear();
		stepData.add(new VehiclePosition(refPoint, initialAngle));
	}
	
	
	public double getWheelBaseDist() {
		// return 0.;
		return theAxles.get(theAxles.size()-1).getRefLocation()-theAxles.get(0).getRefLocation();
	}
	
	public Point2D getMidFrontLocation(int stepNumber){
		if (stepNumber >= stepData.size())
			return null;
		else {
			Point2D.Double refPoint = (Point2D.Double)stepData.get(stepNumber).getLocation();
			if (refLocationType == MID_FRONT_BUMPER_LEAD)
				return refPoint;
			else {
				double angle = stepData.get(stepNumber).getRotation();
				double firstAxleRefLocation = theAxles.get(0).getRefLocation();
				double firstAxleHalfWidth;
				//System.out.println("NG: in LeadVehicle.getMidFront for: " + description + " refPoint is " + refPoint.toString() + " firstAxleRefLocation:" + firstAxleRefLocation);
				
				if (refLocationType == FRONT_LEFT_WHEEL_LEAD)
					firstAxleHalfWidth= theAxles.get(0).getWidth()/2. - theAxles.get(0).getWheelWidth()/2.;
				else // refLocationType == MID_FRONT_AXLE_LEAD
					firstAxleHalfWidth = 0.;
				
				double newX = refPoint.getX() + firstAxleRefLocation * Math.cos(angle*Math.PI/180.)
						+ firstAxleHalfWidth * Math.sin(angle*Math.PI/180.);
				double newY = refPoint.getY() + firstAxleRefLocation * Math.sin(angle*Math.PI/180.)
						- firstAxleHalfWidth * Math.cos(angle*Math.PI/180.);
				
				//System.out.println("NG: in LeadVehicle.getMidFront for: " + description + " midFront is " + newX + ", " + newY);
				return new Point2D.Double(newX,newY);
			}
		}
	}
	
}
