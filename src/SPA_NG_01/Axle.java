package SPA_NG_01;


import java.awt.Graphics2D;
// import java.awt.geom.Point2D;
// import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


public class Axle{
	private boolean steerable;
	private double referenceLocation, width, wheelWidth, wheelDiameter;
	private int numberOfWheels;
	
	public Axle(BufferedReader input) throws IOException {
		String currentLine = "";
		String [] temp;
		boolean doneWithAxle = false;
		
		input.mark(1000);
		while ((currentLine = input.readLine()) != null && !doneWithAxle) {
			temp = currentLine.split(": ",2);
			//System.out.println("Axle >>" + temp[0] + "<< >>" + temp [1] + "<< ");

			if (temp[0].equalsIgnoreCase("Axle") || temp[0].equalsIgnoreCase("Vehicle")) {
				doneWithAxle = true;
			} else {
				if (temp[0].equalsIgnoreCase("Location")) referenceLocation = java.lang.Double.parseDouble(temp[1]);
				else if (temp[0].equalsIgnoreCase("Width")) width = java.lang.Double.parseDouble(temp[1]);
				else if (temp[0].equalsIgnoreCase("NumberOfTires")) numberOfWheels = java.lang.Integer.parseInt(temp[1]);	
				else if (temp[0].equalsIgnoreCase("TireDiameter")) wheelDiameter = java.lang.Double.parseDouble(temp[1]);
				else if (temp[0].equalsIgnoreCase("TireWidth")) wheelWidth = java.lang.Double.parseDouble(temp[1]);	
				else if (temp[0].equalsIgnoreCase("Steerable")) steerable = temp[1].equalsIgnoreCase("True");
				
				input.mark(1000);
			}
		}
	}

	protected void draw(Graphics2D g){

		// draw outer wheels
		double refX = (referenceLocation - wheelDiameter/2.);
		double refY = - width/2.;
		double sizeArc = wheelWidth*0.3; // round corner to 40% of wheel width
		
		g.fillRoundRect((int)Math.round(refX * SPA_Viewer.preScale), (int)Math.round(refY * SPA_Viewer.preScale), 
				(int)Math.round(wheelDiameter * SPA_Viewer.preScale), (int)Math.round(wheelWidth * SPA_Viewer.preScale), 
				(int)Math.round(sizeArc * SPA_Viewer.preScale), (int)Math.round(sizeArc * SPA_Viewer.preScale));
		
		refY = width/2. - wheelWidth;
		g.fillRoundRect((int)Math.round(refX * SPA_Viewer.preScale), (int)Math.round(refY * SPA_Viewer.preScale), 
				(int)Math.round(wheelDiameter * SPA_Viewer.preScale), (int)Math.round(wheelWidth * SPA_Viewer.preScale), 
				(int)Math.round(sizeArc * SPA_Viewer.preScale), (int)Math.round(sizeArc * SPA_Viewer.preScale));

		// draw inner wheels
		double gapSize = 1.1; // implies a 10% of wheel width gap between the wheels
		for (int i=1; i < numberOfWheels/2; i++){
			refY = -width/2. + wheelWidth * gapSize * i;
			g.fillRoundRect((int)Math.round(refX * SPA_Viewer.preScale), (int)Math.round(refY * SPA_Viewer.preScale), 
					(int)Math.round(wheelDiameter * SPA_Viewer.preScale), (int)Math.round(wheelWidth * SPA_Viewer.preScale), 
					(int)Math.round(sizeArc * SPA_Viewer.preScale), (int)Math.round(sizeArc * SPA_Viewer.preScale));
			refY = width/2.  - wheelWidth - wheelWidth * gapSize * i;
			g.fillRoundRect((int)Math.round(refX * SPA_Viewer.preScale), (int)Math.round(refY * SPA_Viewer.preScale), 
					(int)Math.round(wheelDiameter * SPA_Viewer.preScale), (int)Math.round(wheelWidth * SPA_Viewer.preScale), 
					(int)Math.round(sizeArc * SPA_Viewer.preScale), (int)Math.round(sizeArc * SPA_Viewer.preScale));
		}
	}
		
	/* a bunch of getters that never get called */
	public double getRefLocation(){
		return referenceLocation;
	}
	public double getWidth(){
		return width;
	}
	public double getWheelWidth(){
		return wheelWidth;
	}
	public boolean isSteerable(){
		return steerable;
	}

	/* the log function, may implement later*/
	public void write(PrintWriter output){
		output.println("Location: " + referenceLocation);
		output.println("Width: " + width);
		output.println("NumberOfTires: " + numberOfWheels);
		output.println("TireDiameter: " + wheelDiameter);
		output.println("TireWidth: " + wheelWidth);
		output.print("Steerable: ");
		if (steerable) output.println("True");
		else output.println("False");
	}
}
