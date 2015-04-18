package prog2;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Prog2 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void createAndShowGUI() {
        JFrame f = new JFrame("Traveling Salesman Problem");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(PlotPanel.frameWidth, PlotPanel.frameHeight);
        final PlotPanel pp = new PlotPanel();
        f.add(pp);
        f.setVisible(true);

        int delay = 100; //milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                
                pp.goRandom();
                
                
                
                
                
            }
        };
        new Timer(delay, taskPerformer).start();
    }

}
