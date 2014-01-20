package SPA_NG_01;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * @author Chang Liu
 *
 */
public class SPA_Viewer extends JPanel {

	private static final long serialVersionUID = 1L;
	// needed to eliminate rounding errors in graphing for small dimensions
	public static final double preScale = 1000.;

	private VehicleTrain theTrain;
	private WheelPath thePath, theInnBound, theOutBound;
	private SweptPathSimulation theSim;
	private boolean zoomIn = false;
	private double minX, minY, maxX, maxY;
	private int deviX = 0, deviY = 0;
	
	// private WheelPath thePath;

	public SPA_Viewer(VehicleTrain train, WheelPath path, WheelPath innBound,
			WheelPath outBound) {
		theTrain = train;
		thePath = path;
		theInnBound = innBound;
		theOutBound = outBound;
	}

	public SPA_Viewer(SweptPathSimulation sim) {
		theSim = sim;
	}

		@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke((float) 0.25 * (float) preScale,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));

		setView(g2d);

		theSim.draw(g2d);
		theSim.drawAllSteps(g2d);
		theSim.drawHighlight(g2d);

	}

	private void setView(Graphics2D g2d) {
		double margin = 40.;
		if(zoomIn){margin = -40.;}
			minX = theSim.getBoundary().getMinX() - margin + deviX * 10;
			minY = theSim.getBoundary().getMinY() - margin + deviY * 10;
			maxX = theSim.getBoundary().getMaxX() + margin + deviX * 10;			
			maxY = theSim.getBoundary().getMaxY() + margin + deviY * 10;

			double testScale = Math.min(1000. / (maxX - minX), 800. / (maxY - minY));
			
			g2d.scale(testScale / preScale, -testScale / preScale);
			g2d.translate(-minX * preScale, -maxY * preScale);
		
	}
	
	public boolean toggleZoomIn(){return zoomIn = !zoomIn;}
	
	public void moveUp(){deviY++;}
	public void moveDown(){deviY--;}
	public void moveRight(){deviX++;}
	public void moveLeft(){deviX--;}
	
}
