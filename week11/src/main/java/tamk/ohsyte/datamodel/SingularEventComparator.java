package tamk.ohsyte.datamodel;

import java.util.Comparator;

/**
 * Compares two singular events based on their years.
 */
public class SingularEventComparator implements Comparator<SingularEvent> {
    /**
     * Compares the singular events.
     *
     * @param a first event
     * @param b second event
     * @return difference between the years
     */
    public int compare(SingularEvent a, SingularEvent b) {
        return a.getYear() - b.getYear();
    }
}
