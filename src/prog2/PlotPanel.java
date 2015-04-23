package prog2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.*;
import java.util.Random;
import java.util.List;
import javax.swing.JPanel;
import static prog2.Prog2.*;

class PlotPanel extends JPanel {

    static int width = 640;
    static int height = 640;
    public static int radius = 200;
    static int frameWidth = width + 10;
    static int frameHeight = height + 35;

    Point points[] = new Point[numPoints]; // [0,1)
    Random r = new Random();

    // random
    public PlotPanel() {
        for (int i = 0; i < numPoints; i++) {
            this.points[i] = new Point(r.nextDouble() * width, r.nextDouble() * height);
//            System.out.println("Point " + i + " = (" + this.points[i].x + ", " + this.points[i].y + ")");
        }
    }

    // rectangle
    public PlotPanel(int max) {
        for (int i = 0; i < max / 5; i++) {
            for (int j = 0; j < 5; j++) {
                this.points[i * 5 + j] = new Point(100 + i * 50, 250 + j * 50);
//                System.out.println("Point " + (i * 5 + j) + " = (" + this.points[i].x + ", " + this.points[i].y + ")");
            }
        }
        System.out.println("Shortest possible path: " + 50 * (numPoints -1));
    }

    public PlotPanel(double theta) {
        int midx = width / 2;
        int midy = height / 2;
        for (int i = 0; i < numPoints; i++) {
            double t = 2 * Math.PI * r.nextDouble();
            int x = (int) Math.round(radius * Math.cos(t));
            int y = (int) Math.round(radius * Math.sin(t));
            this.points[i] = new Point(x + midx, y + midy);
//            System.out.println("Point " + i + " = (" + this.points[i].x + ", " + this.points[i].y + ")");
        }
         System.out.println("Circumference path (2πr): " + 2 * Math.PI * radius);
    }

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
        } else if (saMode) {
            shuffleSA(points);
        }

        // reset path length
        travelDist = 0.0;

        // draw points and lines
        g.setColor(Color.white);
        if (randomMode || nnMode) {
            for (int i = 0; i < numPoints - 1; i++) {
                int cx = (int) Math.round(points[i].x);
                int cy = (int) Math.round(points[i].y);
                g.drawOval(cx - ovalSize / 2, cy - ovalSize / 2, ovalSize, ovalSize);
                int newx = (int) Math.round(points[(i + 1)].x);
                int newy = (int) Math.round(points[(i + 1)].y);
                g.drawOval(newx - ovalSize / 2, newy - ovalSize / 2, ovalSize, ovalSize);
                g.drawLine(cx, cy, newx, newy);
                travelDist += distance(cx, newx, cy, newy);
            }
        } else if (saMode) {
            for (int i = 0; i < numPoints - 1; i++) {
                int cx = (int) Math.round(points[i].x);
                int cy = (int) Math.round(points[i].y);
                g.drawOval(cx - ovalSize / 2, cy - ovalSize / 2, ovalSize, ovalSize);
                int newx = (int) Math.round(points[(i + 1)].x);
                int newy = (int) Math.round(points[(i + 1)].y);
                g.drawOval(newx - ovalSize / 2, newy - ovalSize / 2, ovalSize, ovalSize);
                g.drawLine(cx, cy, newx, newy);
                travelDist += distance(cx, newx, cy, newy);
            }
        }

        // assess that path length - if it was the shortest ever, save those points
        if (travelDist < bestEver) {
            System.arraycopy(points, 0, pointsCopy, 0, numPoints);
            bestEver = travelDist;
            System.out.println("Shortest path found: " + bestEver);
        }

        // and then display the shortest past ever found (unless you're doing SA)
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
        List<Point> avail = new ArrayList<Point>();
        List<Point> used = new ArrayList<Point>();
        List<DPoint> dpl = new ArrayList<DPoint>();

        // add first point to used points
        used.add(ar[0]);

        // add rest to available points
        for (int i = 1; i < ar.length; i++) {
            avail.add(ar[i]);
        }

        // while there are points left to connect to
        while (used.size() < numPoints) {
            // find distance between each available point and last used point
            // and save that distance in the dpoint list
            for (int i = 0; i < avail.size(); i++) {
                double dist = distance(used.get(used.size() - 1), avail.get(i));
                DPoint dpt = new DPoint(avail.get(i), dist);
                dpl.add(dpt);
            }

            // sort the dpoint list by distance
            Collections.sort(dpl, new DPointComp());

            // remove closest point from available and add it to used
            avail.remove(dpl.get(0).pt);
            used.add(dpl.get(0).pt);
            dpl.clear();
        }

        // reset ar to be used
        for (int i = 0; i < used.size(); i++) {
            ar[i] = used.get(i);
        }

    }

    void shuffleSA(Point[] ar) {
        List<Point> arl = new ArrayList<Point>();

        for (int i = 0; i < ar.length; i++) {
            arl.add(ar[i]);
        }

        int rp1 = r.nextInt(ar.length);
        int rp2 = r.nextInt(ar.length);

        Collections.swap(arl, rp1, rp2);

        for (int i = 0; i < arl.size(); i++) {
            ar[i] = arl.get(i);
        }

    }
}
