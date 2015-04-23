package prog2;

import javax.swing.*;
import java.awt.event.*;
import static prog2.PlotPanel.*;

public class Prog2 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static boolean randomMode = false;
    public static boolean nnMode = false;
    public static boolean simAnn = true;
    public static int speed = 100; // ms per loop
    public static int numPoints = 10;
    public static int radius = 200;

    public static void createAndShowGUI() {
        final JFrame f = new JFrame("Travelling Salesman Problem");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(PlotPanel.frameWidth, PlotPanel.frameHeight);

        final PlotPanel pp = new PlotPanel(); // random
//        final PlotPanel pp = new PlotPanel(numPoints); // rectangle
//        final PlotPanel pp = new PlotPanel(numPoints * 1.0); // circle
        
        f.add(pp);
        f.setVisible(true);

        int delay = speed;
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                pp.go();

            }
        };
        new Timer(delay, taskPerformer).start();
    }
}
