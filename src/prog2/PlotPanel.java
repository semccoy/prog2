package prog2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.JPanel;

class PlotPanel extends JPanel {

    static int width = 640;
    static int height = 640;
    static int frameWidth = width + 10;
    static int frameHeight = height + 35;

    static int numPoints = 40;
    Point points[] = new Point[numPoints]; // [0,1)
    Random r = new Random();

    public PlotPanel() {
        for (int i = 0; i < numPoints; i++) {
            this.points[i] = new Point(r.nextDouble() * width, r.nextDouble() * height);
//            System.out.println("Point " + i + " = (" + this.points[i].x + ", " + this.points[i].y + ")");
        }
    }

    public PlotPanel(int max) {
        for (int i = 0; i < max / 5; i++) {
            for (int j = 0; j < 5; j++) {
                this.points[i * 5 + j] = new Point(150 + i * 50, 250 + j * 50);
//                System.out.println("Point " + (i * 5 + j) + " = (" + this.points[i].x + ", " + this.points[i].y + ")");
            }
        }
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

    int startx = r.nextInt();
    int starty = r.nextInt();

    int ovalSize = 10;

    double bestEver = distance(0, width, 0, height) * numPoints; // worst case scenario
    double travelDist = bestEver; // just to start

    Point[] pointsCopy = new Point[numPoints];

    int iterations = 0;

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (travelDist < bestEver) {
            bestEver = travelDist;
            System.out.println("Shortest path found: " + bestEver);
            g.setColor(Color.red);
            pointsCopy = points; // if we've found a new best path, save those points for later use
        } else { // if not, don't save them
            g.setColor(Color.black);
        }

        travelDist = 0.0;
        shuffle(points);

        g.drawOval(startx - ovalSize / 2, starty - ovalSize / 2, ovalSize, ovalSize);

        for (int i = 0; i < numPoints; i++) {
            int x = (int) points[i].x;
            int y = height - (int) points[i].y;
            g.drawOval(x, y, ovalSize, ovalSize);
            int newx = x + ovalSize / 2;
            int newy = y + ovalSize / 2;
            g.drawLine(startx, starty, newx, newy);
            travelDist += distance(startx, starty, newx, newy);

            startx = newx;
            starty = newy;
        }

//        if (iterations > 0) {
//            g.setColor(Color.green);
//
//            for (int i = 0; i < numPoints; i++) {
//                int x = (int) pointsCopy[i].x;
//                int y = height - (int) pointsCopy[i].y;
//                int newx = x + ovalSize / 2;
//                int newy = y + ovalSize / 2;
//                g.drawLine(startx, starty, newx, newy);
//                travelDist += distance(startx, starty, newx, newy);
//
//                startx = newx;
//                starty = newy;
//            }
//        }
//        iterations++;
    }

    // also maybe create a preset set of points to test on
    // Fisher-Yates shuffle (a random shuffle)
    static void shuffle(Point[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Point a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

}
