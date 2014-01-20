package SPA_NG_01;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class VehicleTrain {

	// enumeration for request of swept path of body and/or wheels
	public static final int FRONT_RIGHT = 1;
	public static final int FRONT_LEFT = 2;
	public static final int REAR_RIGHT = 3;
	public static final int REAR_LEFT = 4;
	
	private ArrayList<Vehicle> theVehicles = new ArrayList<Vehicle>();
	private String name = "";
	
	public VehicleTrain(BufferedReader input) throws IOException {
		// go through input and create a vehicle any time the word "Vehicle is encountered"
		String currentLine = "";
		String [] temp;
		while ((currentLine = input.readLine()) != null) {
			temp = currentLine.split(": ",2);
			//System.out.println("VehicleTrain >>" + temp[0] + "<< >>" + temp [1] + "<< ");

			if (temp[0].equalsIgnoreCase("VehicleTrainName")) {
				name = temp[1];				
			}
			else if (temp[0].equalsIgnoreCase("Vehicle")) { // create a new vehicle
				if (theVehicles.size()==0)
					theVehicles.add(new LeadVehicle(input));
				else
					theVehicles.add(new Vehicle(input, theVehicles.get(theVehicles.size()-1).getBackPivot()));
				input.reset();
			}
		}
	}
	
	public void write(PrintWriter output){
		// writes all the data for all the vehicles
	}
	
	public void draw(Graphics2D g, int stepNumber){
		for(int i=0; i < theVehicles.size(); i++){
			switch (i) {  // different color for each vehicle
			case 0: g.setColor(Color.red);
					break;
			case 1: g.setColor(Color.blue);
					break;
			case 2: g.setColor(Color.green);
					break;
			case 3: g.setColor(Color.pink);
					break;
			default: g.setColor(Color.black);
					break;
			}
			theVehicles.get(i).draw(g, stepNumber);
		}
		
	}
	
	public void reset(Point2D startLocation, double startAngle){
		if (theVehicles.size()>0){
			// reset first vehicle (lead vehicle)
			((LeadVehicle)theVehicles.get(0)).reset(startLocation, startAngle);
			// reset other vehicles
			for(int i=1; i < theVehicles.size(); i++){
				theVehicles.get(i).reset();
			}
		}
	}
	
	public void simulate(Point2D newLocation){
		if (theVehicles.size()>0){
			// move the first vehicle (lead vehicle) to newLocation
			theVehicles.get(0).moveTo(newLocation);
			// move the other vehicles using vehicle train configuration
			for(int i=1; i < theVehicles.size(); i++){
				theVehicles.get(i).move();
			}

		}	
	}
	
	public Path2D generateWheelPath(int wheel){
		return null;
	}
	public Path2D generateBodyPath(int wheel){
		return null;
	}
	
}
