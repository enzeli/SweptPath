package SPA_NG_01;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;


// import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
// import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
// import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class SPA_ViewerController extends JApplet implements ChangeListener, ActionListener{

	private static final long serialVersionUID = 1L;
	private VehicleTrain theTrain;
	private Point2D origin;
	private JFrame controlledFrame;
	private SweptPathSimulation theSim;
	private SPA_Viewer theViewer;
	

	JButton togglePathView = new JButton("Hide Wheel Path");
	JButton toggleRoadView = new JButton("Hide Road");
	JButton toggleAllStepsView = new JButton("View All Steps");
	JButton toggleHighlight = new JButton("Highlight Road");
	
	JButton toggleZoomIn = new JButton("Zoom In");
	JButton moveUp = new JButton("^ Move up ^");
	JButton moveDown = new JButton("v Move down v");
	JButton moveRight = new JButton("> Move right >");
	JButton moveLeft = new JButton("< Move left <");
	
	JSpinner shadowSizeSpinner = new JSpinner();
    JSlider theSlider = new JSlider(JSlider.HORIZONTAL, 0, 120, 1);
    JSpinner stepSizeSpinner = new JSpinner();

	public SPA_ViewerController(VehicleTrain train, Point2D loc, JFrame other){
		theTrain = train;
		origin = loc;
		controlledFrame = other;
	}
	public SPA_ViewerController(SweptPathSimulation sim, JFrame other){
		theSim = sim;
		controlledFrame = other;
		theSlider.setMaximum(theSim.getCountOfSteps());
		theViewer = new SPA_Viewer(sim);
	}
	
    public Dimension getPreferredSize(){
        return new Dimension(300, 280);
    }

    public void start() {
        initComponents();
    }
    
    public void init() {
        /* Turn off metal's use of bold fonts */
        //UIManager.put("swing.boldMetal", Boolean.FALSE);
    }

    public void initComponents() {
        
        setLayout(new BorderLayout());
        
        JPanel p = new JPanel();
        
        GridBagConstraints c = new GridBagConstraints();
        p.add(new JLabel("Steps:"));
        theSlider.setMinorTickSpacing(5);
        theSlider.setMajorTickSpacing(20);
        theSlider.setPaintTicks(true);
        theSlider.setPaintLabels(true);
        theSlider.addChangeListener(this);
        p.add(theSlider);
        add(p);
 		
		shadowSizeSpinner.setModel(new SpinnerNumberModel(0,0,200,1));
		shadowSizeSpinner.addChangeListener(this);
        p.add(new JLabel("Shadow Size:"),c);
        p.add(shadowSizeSpinner);
        
        stepSizeSpinner.setModel(new SpinnerNumberModel(5.,1.,30.,.1));
		stepSizeSpinner.addChangeListener(this);
        p.add(new JLabel("Step Size:"),c);
        p.add(stepSizeSpinner);
        

        togglePathView.addActionListener(this);
        toggleRoadView.addActionListener(this);
        toggleAllStepsView.addActionListener(this);
        toggleHighlight.addActionListener(this);
        toggleZoomIn.addActionListener(this);
        moveUp.addActionListener(this);
        moveDown.addActionListener(this);
        moveRight.addActionListener(this);
        moveLeft.addActionListener(this);
        
        
        togglePathView.setPreferredSize(new Dimension(140,25));
        toggleRoadView.setPreferredSize(new Dimension(140,25));
        toggleAllStepsView.setPreferredSize(new Dimension(140,25));
        toggleHighlight.setPreferredSize(new Dimension(140,25));
        toggleZoomIn.setPreferredSize(new Dimension(280,25));
        moveUp.setPreferredSize(new Dimension(160, 25));
        moveDown.setPreferredSize(new Dimension(140, 25));
        moveLeft.setPreferredSize(new Dimension(140, 25));
        moveRight.setPreferredSize(new Dimension(140, 25));
                
        p.add(togglePathView, c);
        p.add(toggleRoadView, c);
        p.add(toggleAllStepsView, c);
        p.add(toggleHighlight, c);
        p.add(toggleZoomIn, c);
        p.add(moveUp,c);
        p.add(moveLeft,c);
        p.add(moveRight,c);
        p.add(moveDown,c);
               
    }

    public void stateChanged(ChangeEvent e) {
    	theSim.setDrawParams(0, theSlider.getValue(), Integer.parseInt(shadowSizeSpinner.getValue().toString()));
        theSim.setStepSize(Double.parseDouble(stepSizeSpinner.getValue().toString()));
        theSim.simulate();
        theSlider.setMaximum(theSim.getCountOfSteps());
    	SwingUtilities.updateComponentTreeUI(controlledFrame);
   }

	
	public void actionPerformed(ActionEvent e){
		// String returnValue;
		
		if(e.getSource() == togglePathView){
			if(theSim.togglePathView())
				togglePathView.setText("Hide Wheel Path");
			else
				togglePathView.setText("Show Wheel Path");
		} else if(e.getSource() == toggleRoadView){
			if(theSim.toggleRoadView())
				toggleRoadView.setText("Hide Road");
			else
				toggleRoadView.setText("Show Road");
		} else if(e.getSource() == toggleAllStepsView){
			if(theSim.toggleAllStepsView())
				toggleAllStepsView.setText("Hide All Steps");
			else
				toggleAllStepsView.setText("View All Steps");
		} else if(e.getSource() == toggleHighlight){
			if(theSim.toggleHighlight())
				toggleHighlight.setText("Unhighlight Road");
			else
				toggleHighlight.setText("Highlight Road");
		} else if(e.getSource() == toggleZoomIn){
			if(theViewer.toggleZoomIn())
				toggleZoomIn.setText("Zoom Out");
			else
				toggleZoomIn.setText("Zoom In");
		} else if(e.getSource() == moveUp){
			theViewer.moveUp();
		} else if(e.getSource() == moveDown){
			theViewer.moveDown();
		} else if(e.getSource() == moveLeft){
			theViewer.moveLeft();
		} else if(e.getSource() == moveRight){
			theViewer.moveRight();
		}
		
		
	    SwingUtilities.updateComponentTreeUI(controlledFrame);
		}
	
	public SPA_Viewer getViewer(){return theViewer;}
	
}
