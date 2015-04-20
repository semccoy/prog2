package prog2;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import static prog2.PlotPanel.*;

public class Prog2 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void createAndShowGUI() {
        final JFrame f = new JFrame("Travelling Salesman Problem");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(PlotPanel.frameWidth, PlotPanel.frameHeight);
        final PlotPanel pp = new PlotPanel(); // random Points
//        final PlotPanel pp = new PlotPanel(numPoints); // fixed Points (mostly for testing)
        // make this one circular
        f.add(pp);
        f.setVisible(true);

        int delay = 10; //milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                pp.go();

            }
        };
        new Timer(delay, taskPerformer).start();
    }
}
