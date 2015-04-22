package prog2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.*;
import java.util.Random;
import javax.swing.JPanel;
import static prog2.Prog2.*;

class PlotPanel extends JPanel {

    static int width = 640;
    static int height = 640;
    static int frameWidth = width + 10;
    static int frameHeight = height + 35;

    static int numPoints = 10;
    Point points[] = new Point[numPoints]; // [0,1)
    Random r = new Random();

    public PlotPanel() {
        for (int i = 0; i < numPoints; i++) {
            this.points[i] = new Point(r.nextDouble() * width, r.nextDouble() * height);
//            System.out.println("Point " + i + " = (" + this.points[i].x + ", " + this.points[i].y + ")");
        }
    }

//    public PlotPanel(int max) {
//        for (int i = 0; i < max / 5; i++) {
//            for (int j = 0; j < 5; j++) {
//                this.points[i * 5 + j] = new Point(150 + i * 50, 250 + j * 50);
////                System.out.println("Point " + (i * 5 + j) + " = (" + this.points[i].x + ", " + this.points[i].y + ")");
//            }
//        }
//    }
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    public double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    public double distance(int x1, int x2, int y1, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    void go() {
        this.revalidate();
        this.repaint();
    }

    int ovalSize = 10;

    double bestEver = distance(0, width, 0, height) * numPoints; // worst case scenario
    double travelDist = bestEver; // just to start

    Point[] pointsCopy = new Point[numPoints];

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // shuffle the points and select starting point
        if (randomMode) {
            shuffleRandom(points);
        } else if (nnMode) {
            shuffleNN(points);
        }

        // reset path length
        travelDist = 0.0;

        // draw points and lines
        g.setColor(Color.white);
        for (int i = 0; i < numPoints - 1; i++) {
            int cx = (int) Math.round(points[i].x);
            int cy = (int) Math.round(points[i].y);
            g.drawOval(cx - ovalSize / 2, cy - ovalSize / 2, ovalSize, ovalSize);
            int newx = (int) Math.round(points[(i + 1)].x);
            int newy = (int) Math.round(points[(i + 1)].y);
            g.drawOval(newx - ovalSize / 2, newy - ovalSize / 2, ovalSize, ovalSize);
            g.drawLine(cx, cy, newx, newy);
            travelDist += distance(cx, cy, newx, newy);
        }

        // assess that path length - if it was the shortest ever, save those points
        if (travelDist < bestEver) {
            System.arraycopy(points, 0, pointsCopy, 0, numPoints);
            bestEver = travelDist;
            System.out.println("Shortest path found: " + bestEver);
        }

        // and then display the shortest past ever found
        g.setColor(Color.black);
        for (int i = 0; i < numPoints - 1; i++) {
            int cx = (int) Math.round(pointsCopy[i].x);
            int cy = (int) Math.round(pointsCopy[i].y);
            g.drawOval(cx - ovalSize / 2, cy - ovalSize / 2, ovalSize, ovalSize);
            int newx = (int) Math.round(pointsCopy[(i + 1)].x);
            int newy = (int) Math.round(pointsCopy[(i + 1)].y);
            g.drawOval(newx - ovalSize / 2, newy - ovalSize / 2, ovalSize, ovalSize);
            g.drawLine(cx, cy, newx, newy);
        }

    }

    // put points in a random order
    // Fisher-Yates shuffle (a random shuffle)
    void shuffleRandom(Point[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Point a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    // put points in a NN order
    void shuffleNN(Point[] ar) {
        shuffleRandom(ar);
        DPoint[] dpa = new DPoint[numPoints];

        for (int i = 0; i < ar.length; i++) {
            double dist = distance(ar[0], ar[i]);
            DPoint dpt = new DPoint(ar[i], dist);
            dpa[i] = dpt;

        }
        
//        for (DPoint dp : dpa) {
//            
//        }
        
//        insertionSort(dpa);

//        for (int i = 0; i < dpa.length; i++) {
//            System.out.println(dpa[i].pt.x + " " + dpa[i].pt.y);
//        }
    }

    public static void insertionSort(DPoint array[]) {
        int n = array.length;
        for (int j = 1; j < n; j++) {
            double key = array[j].y;
            int i = j - 1;
            while ((i > -1) && (array[i].y > key)) {
                array[i + 1] = array[i];
                i--;
            }
            array[i + 1] = array[j];
        }
    }
}
