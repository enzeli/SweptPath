package SPA_NG_01;


import java.util.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
// import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;

// import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Vehicle {

	protected ArrayList<VehiclePosition> stepData = new ArrayList<VehiclePosition>();
	protected String description;
	protected Pivot frontPivot = null, backPivot = null;
	protected double length, width;
	protected ArrayList<Axle> theAxles = new ArrayList<Axle>();

	// constructor
	public Vehicle(BufferedReader input, Pivot leadPivot) throws IOException {
		String currentLine = "";
		String [] temp;
		boolean doneWithVehicle = false;
		frontPivot = leadPivot;

		input.mark(1000);
		while ((currentLine = input.readLine()) != null && !doneWithVehicle) {
			temp = currentLine.split(": ",2);
			//System.out.println("Vehicle >>" + temp[0] + "<< >>" + temp [1] + "<< ");

			if (temp[0].equalsIgnoreCase("Vehicle")) {
				doneWithVehicle = true;
			} else {
				if (temp[0].equalsIgnoreCase("Description")) description = temp[1];				
				else if (temp[0].equalsIgnoreCase("Length")) length = java.lang.Double.parseDouble(temp[1]);				
				else if (temp[0].equalsIgnoreCase("Width")) width = java.lang.Double.parseDouble(temp[1]);				

				else if (temp[0].equalsIgnoreCase("FrontPivot")) { 
					if (!temp[1].equalsIgnoreCase("None"))
						frontPivot.setFollow(this, java.lang.Double.parseDouble(temp[1]));
				}
				else if (temp[0].equalsIgnoreCase("RearPivot")) { // create a new pivot as needed
					if (!temp[1].equalsIgnoreCase("None"))
						backPivot = new Pivot(this, java.lang.Double.parseDouble(temp[1]));
				}
				// axle reads from input? in a while?				
				else if (temp[0].equalsIgnoreCase("Axle")) { // create a new axle
					theAxles.add(new Axle(input));
					input.reset();
				}
				input.mark(1000);
			}
		}
	}
	
	public void write(PrintWriter output){
		output.println("Description: " + description);				
		output.println("Length: " + length);				
		output.println("Width: " + width);				

		for(int i=1; i<theAxles.size(); i++){
			output.println("Axle: " + i);
			theAxles.get(i).write(output);
		}
	}


	public Point2D getMidFrontLocation(int stepNumber){
		//System.out.println("NG: in Vehicle.getMidFront for: " + description );
		if (stepNumber >= stepData.size())
			return null;
		else {
			Point2D.Double refPoint = (Point2D.Double)stepData.get(stepNumber).getLocation();
			double angle = stepData.get(stepNumber).getRotation();
			
			double newX = refPoint.getX() + frontPivot.getRelativeLocFollow() * Math.cos(angle*Math.PI/180.);
			double newY = refPoint.getY() + frontPivot.getRelativeLocFollow() * Math.sin(angle*Math.PI/180.);
			return new Point2D.Double(newX,newY);
		}
	}

	public double getWheelBaseDist(){
		// return 0.;
		return theAxles.get(theAxles.size()-1).getRefLocation()-frontPivot.getRelativeLocFollow();
	}


	public void draw(Graphics2D g, int stepNumber){

		if (stepNumber >= 0 && stepNumber < stepData.size()){

			double rotation = stepData.get(stepNumber).getRotation();
			Point2D.Double refPoint = (Point2D.Double)getMidFrontLocation(stepNumber);
			
/*		    // draw X at refPoint (wheel for lead, pivot for follow)
		    int crossX, crossY;
		    crossX = (int) Math.round(refPoint.getX() * SPA_Viewer.preScale); 
		    crossY = (int) Math.round(refPoint.getY() * SPA_Viewer.preScale); 
		    g.drawLine(crossX-2 * (int)SPA_Viewer.preScale, crossY-2 * (int)SPA_Viewer.preScale, crossX+2 * (int)SPA_Viewer.preScale, crossY+2 * (int)SPA_Viewer.preScale);
		    g.drawLine(crossX-2 * (int)SPA_Viewer.preScale, crossY+2 * (int)SPA_Viewer.preScale, crossX+2 * (int)SPA_Viewer.preScale, crossY-2 * (int)SPA_Viewer.preScale);
		    
		    // draw X at midfront
		    crossX = (int) Math.round(stepData.get(stepNumber).getLocation().getX() * SPA_Viewer.preScale); 
		    crossY = (int) Math.round(stepData.get(stepNumber).getLocation().getY() * SPA_Viewer.preScale); 
		    g.drawLine(crossX-1 * (int)SPA_Viewer.preScale, crossY-1 * (int)SPA_Viewer.preScale, crossX+1 * (int)SPA_Viewer.preScale, crossY+1 * (int)SPA_Viewer.preScale);
		    g.drawLine(crossX-1 * (int)SPA_Viewer.preScale, crossY+1 * (int)SPA_Viewer.preScale, crossX+1 * (int)SPA_Viewer.preScale, crossY-1 * (int)SPA_Viewer.preScale);

*/
			g.translate(refPoint.getX() * SPA_Viewer.preScale, refPoint.getY() * SPA_Viewer.preScale);
			g.rotate((rotation+180)*Math.PI/180.);
			
			// draw vehicle body
			g.draw(new Rectangle2D.Double(0,-width/2 * SPA_Viewer.preScale,
					length * SPA_Viewer.preScale, width * SPA_Viewer.preScale));
			// draw axles and all other things here
			if (backPivot != null)
					backPivot.draw(g);
			for (int i=0; i < theAxles.size(); i++)
				theAxles.get(i).draw(g);

			g.rotate((-rotation-180)*Math.PI/180.);
			g.translate(-refPoint.getX() * SPA_Viewer.preScale, -refPoint.getY() * SPA_Viewer.preScale);
			
			//Line2D temp = getRearAxleLine(stepNumber);
			//g.drawLine((int)(temp.getoldX()* SPA_Viewer.preScale), (int)(temp.getY1()* SPA_Viewer.preScale),
			//		(int)(temp.getX2()* SPA_Viewer.preScale), (int)(temp.getY2()* SPA_Viewer.preScale));
			

		}
	}
	
	/********************************************/	
	public void reset(){
		stepData.clear();
		stepData.add(new VehiclePosition(frontPivot.getLocation(0), 
				frontPivot.getLeadVehicle().getStepData(0).getRotation()));
	}
	
	protected double getRearAxleRefLocation(int stepNumber){
		return 0.;
	}
		
	public Line2D getRearAxleLine(int stepNumber){
		return null;
	}
	
	public void move(){
		// get pivot point location of leading vehicle at latest iteration
		int lastStepOfLeadingVehicle = stepData.size();  // leading vehicle has one more step than current
		moveTo(getFrontPivot().getLocation(lastStepOfLeadingVehicle));
	}
	
	public void moveTo(Point2D nextPoint){
		Point2D prevPoint = stepData.get(stepData.size()-1).getLocation();
		double oldAngle =stepData.get(stepData.size()-1).getRotation();
		double oldX = prevPoint.getX();
		double oldY = prevPoint.getY();
		double newX = nextPoint.getX();
		double newY = nextPoint.getY();
		
		double midX = (oldX + newX)/2;
		double midY = (oldY + newY)/2;
		double perpBisectX = midX + (newY-oldY);
		double perpBisectY = midY - (newX-oldX);
		Line2D.Double perpBisect = new Line2D.Double(midX,midY,perpBisectX,perpBisectY);	
		double rearWheelX = oldX - getWheelBaseDist()*Math.cos(oldAngle*Math.PI/180);
		double rearWheelY = oldY - getWheelBaseDist()*Math.sin(oldAngle*Math.PI/180);
		double rearPerpX = rearWheelX + (oldY-rearWheelY);
		double rearPerpY = rearWheelY - (oldX-rearWheelX);
		Line2D.Double rearPerp = new Line2D.Double(rearWheelX,rearWheelY,rearPerpX,rearPerpY);
		Point2D.Double rotationCenter = Line2DUtility.findIntersection(perpBisect,rearPerp);
		double rotatedAngle;
		if (rotationCenter !=null)
			rotatedAngle = Line2DUtility.getAngleBetween(rotationCenter.getX(),
					rotationCenter.getY(),prevPoint.getX(),prevPoint.getY(),nextPoint.getX(),nextPoint.getY());
		else
			rotatedAngle = 0.;
			
		double newAngle = oldAngle + rotatedAngle;
		stepData.add(new VehiclePosition(nextPoint,newAngle));
		
	}
	
//	public void moveTo(Point2D nextPoint){
//
//		int currStep = stepData.size() - 1;
//		Point2D prevPoint = stepData.get(currStep).getLocation();
//		
//		// get perpendicular bisector of line segment for move operation
//
//		// find instantaneous center of rotation by finding intersection of leadPerp and rear axle line
//
//		// determine the angle of rotation of leading point about instantaneous center
//
//		// add a VehiclePosition to stepData by adding the rotation to previous angle
//		stepData.add(new VehiclePosition(nextPoint, 
//				stepData.get(currStep).getRotation() /*+ rotatedAngle*/));
//	}
	
	public VehiclePosition getStepData(int stepNumber){
		return stepData.get(stepNumber);
	}
	public Pivot getFrontPivot(){
		return frontPivot;
	}
	public Pivot getBackPivot(){
		return backPivot;
	}
	public String getDescription(){
		return description;
	}
	/********************************************/
	

}
