package SPA_NG_01;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFrame;

public class SPA_Control {

	/**
	 * @param args
	 * @throws IOException
	 */
	
	
	public static void main(String[] args) throws IOException {

		BufferedReader inputStream;
		String dataPath = "./SPA_inputData/";
		Scanner in = new Scanner(System.in);
		String inputFileName, outputFileName, innerBoundary, outerBoundary, inputPathName;
		System.out.
				println("Choose the type of vehicle: [B]us or [T]ruck(default)?");
		if (in.nextLine().equalsIgnoreCase("B")) {
			inputFileName = dataPath + "CityBus-01.txt";
			outputFileName = dataPath + "CityBus-01_out.txt";
		} else {
			inputFileName = dataPath + "TractorTrailer.txt";
			outputFileName = dataPath + "TractorTrailer_out.txt";
		}

		System.out
				.println("Choose the type of vehicle: [T]ee or [R]oundabout(default)?");
		if (in.nextLine().equalsIgnoreCase("T")) {
			innerBoundary = dataPath + "TeeInnerBoundary01.txt";
			outerBoundary = dataPath + "TeeOuterBoundary01.txt";
			inputPathName = dataPath + "TeeWheelPath01.txt";
		} else {
			innerBoundary = dataPath + "RoundaboutInnerBoundary01.txt";
			outerBoundary = dataPath + "RoundaboutOuterBoundary01.txt";
			inputPathName = dataPath + "RoundaboutWheelPath01.txt";
		}
		// System.out.println(innerBoundary);

		try {
			inputStream = new BufferedReader(new FileReader(inputFileName));
			VehicleTrain optimusPrime = new VehicleTrain(inputStream);
			WheelPath path = new WheelPath(inputPathName);
			WheelPath innBound = new WheelPath(innerBoundary);
			WheelPath outBound = new WheelPath(outerBoundary);

			SweptPathSimulation aSimulation = new SweptPathSimulation();
			aSimulation.setPath(path);
			aSimulation.setTrain(optimusPrime);
			aSimulation.setRoad(innBound, outBound);
			aSimulation.resetSimulation();
			aSimulation.simulate();
			
			try {
				PrintWriter outputStream = new PrintWriter(outputFileName);
				optimusPrime.write(outputStream);
			} catch (FileNotFoundException e) {
				System.out.println("SPA_Control: Output file not created: "
						+ inputFileName);
			}

			System.out.println("SPA_Control: All done with loading");

			JFrame frame = new JFrame("SPA Viewer");
			SPA_ViewerController viewerController = new SPA_ViewerController(
					aSimulation, frame);
			SPA_Viewer viewer = viewerController.getViewer();
			
			//double currAngle = 180.;
			//Point2D.Double startPoint = path.getPointAlongLength(0);
			frame.setBackground(Color.cyan);
			frame.setContentPane(viewer);
			viewer.setBackground(Color.cyan);
			frame.setSize(1000, 800);
			frame.setContentPane(viewer);
			frame.setVisible(true);
			frame.setLocation(new Point(50, 50));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JFrame frame2 = new JFrame("SPA controller");			
			frame2.setBackground(Color.yellow);
			frame2.setSize(500, 50);
			frame2.setContentPane(viewerController);
			viewerController.init();
			viewerController.start();
			frame2.setVisible(true);
			frame2.setLocation(new Point(1050, 50));
			frame2.pack();
			frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		} catch (FileNotFoundException e) {
			System.out.println("SPA_Control: Input file not found: " 
					+ inputFileName);
		}
	}

}
