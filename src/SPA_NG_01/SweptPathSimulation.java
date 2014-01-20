/*
*  Author: Enze Li
*
*/

package SPA_NG_01;

// import java.lang.Math;
import java.awt.geom.Point2D;
// import java.awt.geom.Point2D.Double;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class SweptPathSimulation{
	private VehicleTrain theTrain;
	private WheelPath thePath;
	private Road theRoad;
	private double stepSize = 5.;
	private int countOfSteps = 0;

	public void setTrain(VehicleTrain thisTrain){
		theTrain = thisTrain;
	}

	public void setPath(WheelPath thisPath){
		thePath = thisPath;
	}

	public void setRoad(WheelPath innBound, WheelPath outBound){
		theRoad = new Road(innBound, outBound);
	}

	public void resetSimulation(){
		Point2D.Double startLocation = thePath.getPointAlongLength(0);
		double startAngle = Line2DUtility.getAngleStandard(thePath.getTangent(0));
		theTrain.reset(startLocation,startAngle);
		countOfSteps = 1;
	}

	public  void simulate(){
		resetSimulation();
		for (double currLength = 0.; thePath.getPointAlongLength(currLength) != null; currLength += stepSize){
			Point2D.Double nextPoint = thePath.getPointAlongLength(currLength);
			theTrain.simulate(nextPoint);
			countOfSteps++;
			}
		}
	
	public int getCountOfSteps(){
		return countOfSteps;
	}
	
	private int stepsToSkip;	
	private int stepSizeToDraw;  // step I want to draw
	private int sizeOfShadow;

	public void setDrawParams(int a,int b, int c){
//		stepSize = step;
		stepsToSkip = a;
		stepSizeToDraw = b;
		sizeOfShadow = c;
	}
	
	public void setStepSize(double size){
		stepSize = size;
	}
	
	public void draw(Graphics2D g2d){
		if (viewRoad){theRoad.draw(g2d,Color.magenta);}
		if (viewPath){thePath.draw(g2d,Color.black);}
		for (int i=stepSizeToDraw-(sizeOfShadow+1)*(stepsToSkip+1); i<stepSizeToDraw; i+=(stepsToSkip+1)){
			theTrain.draw(g2d,i);
			}
	}
	
	public void drawAllSteps(Graphics2D g2d){
		if (viewAllSteps){
			if (viewRoad){theRoad.draw(g2d,Color.magenta);}
			if (viewPath){thePath.draw(g2d,Color.black);}
			for (int i=0; i<countOfSteps; i+=(stepsToSkip+1)){theTrain.draw(g2d,i);}
		}
	}
	
	public void drawHighlight(Graphics2D g2d){
		if(highlight){theRoad.draw(g2d,Color.yellow);}
	}


	public Rectangle2D getBoundary(){
		return theRoad.getBoundary();
	}
		
	private boolean viewPath = true;
	private boolean viewRoad = true;
	private boolean viewAllSteps = false;
	private boolean highlight = false;
	
	public boolean togglePathView(){return viewPath = !viewPath;}
	
	public boolean toggleRoadView(){return viewRoad = !viewRoad;}
	
	public boolean toggleAllStepsView(){return viewAllSteps = !viewAllSteps;}
	
	public boolean toggleHighlight(){return highlight = !highlight;}
	
//	public void generateWheelSweeps(){}
//
//	public void generateBodySweeps(){}
//
//	public boolean checkWheelsOnRoad(){return false;}
//
//	public boolean checkBodyOnRoad(){return false;}
	


}