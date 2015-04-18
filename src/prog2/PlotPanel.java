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
    Point points[]; // 0,1
    Random r;

    public PlotPanel() {
        this.points = new Point[numPoints];
        this.r = new Random();
        for (int i = 0; i < numPoints; i++) {
            this.points[i] = new Point(r.nextDouble() * width, r.nextDouble() * height);
//            System.out.println("Point " + i + " = (" + this.points[i].x + ", " + this.points[i].y + ")");
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    public double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    double travelDist = 0.0;
    int counter = 0;

    void goRandom() {
        this.revalidate();
        this.repaint();
        // Run algorithm

//        System.out.println("Distance travelled: " + travelDist);
//        travelDist += distance(points[counter], points[counter + 1]);
//        counter++;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int ovalSize = 10;
        int startx = (int) points[0].x;
        int starty = (int) points[0].x;

        g.setColor(Color.RED);
        g.drawOval(startx - ovalSize / 2, starty - ovalSize / 2, ovalSize, ovalSize);
        for (int i = 0; i < numPoints; i++) {
            int x = (int) points[i].x;
            int y = height - (int) points[i].y;
            g.setColor(Color.RED);
            g.drawOval(x, y, ovalSize, ovalSize);
            g.setColor(Color.BLACK);
            int newx = x + ovalSize / 2;
            int newy = y + ovalSize / 2;
            g.drawLine(startx, starty, newx, newy);
            startx = newx;
            starty = newy;
        }
    }

//        int sx = (int) Math.round(s);
//
//        g.setColor(Color.PINK);
//        g.fillRect((int) Math.round(sx - warp_width), 0, (int) Math.round(2.0 * warp_width), height);
//
//        g.setColor(Color.BLUE);
//        g.drawLine(sx, 0, sx, height);
//
//        int osx = (int) Math.round(os);
//        g.setColor(Color.GREEN);
//        g.drawLine(osx, 0, osx, height);
    //        
}
