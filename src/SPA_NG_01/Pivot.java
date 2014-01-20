package SPA_NG_01;


import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class Pivot {

	private Vehicle leadVehicle, followVehicle;
	private double relativeLocLead, relativeLocFollow;
	
	public Pivot(Vehicle lead, double relLocLead){
		leadVehicle = lead;
		relativeLocLead = relLocLead;
	}
	
	public void setFollow(Vehicle follow, double relLocFollow){
		followVehicle = follow;
		relativeLocFollow = relLocFollow;
	}
	
	public Point2D getLocation(int stepNumber){
		Point2D vehLocation = leadVehicle.getMidFrontLocation(stepNumber);
		double vehRotation = leadVehicle.getStepData(stepNumber).getRotation();
		double newX = vehLocation.getX() - relativeLocLead * Math.cos(vehRotation / 180. * Math.PI);
		double newY = vehLocation.getY() - relativeLocLead * Math.sin(vehRotation / 180. * Math.PI);
		
		return new Point2D.Double(newX, newY);
	}
	
	public void draw(Graphics2D g){
		double diam = 1.5;
		g.drawOval((int)((relativeLocLead-diam/2.)*SPA_Viewer.preScale), (int)(-diam/2*SPA_Viewer.preScale),
				(int)(diam*SPA_Viewer.preScale), (int)(diam*SPA_Viewer.preScale));
	}
	
	public Vehicle getLeadVehicle(){
		return leadVehicle;
	}
	public Vehicle getFollowVehicle(){
		return followVehicle;
	}
	public double getRelativeLocLead(){
		return relativeLocLead;
	}
	public double getRelativeLocFollow(){
		return relativeLocFollow;
	}
}
