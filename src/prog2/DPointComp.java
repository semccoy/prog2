package prog2;

import java.util.Comparator;

public class DPointComp implements Comparator<DPoint> {

    @Override
    public int compare(DPoint d1, DPoint d2) {
        Double dist1 = d1.y;
        Double dist2 = d2.y;
        return dist1.compareTo(dist2);
    }

}
