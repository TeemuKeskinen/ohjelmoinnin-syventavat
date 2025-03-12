package tamk.ohsyte.datamodel;

import java.util.Comparator;

/**
 * Compares two annual events based on their descriptions.
 */
public class AnnualEventComparator implements Comparator<AnnualEvent> {
    /**
     * Compares the event descriptions.
     *
     * @param a first event
     * @param b second event
     * @return the difference of the descriptions
     */
    public int compare(AnnualEvent a, AnnualEvent b) {
        return a.getDescription().compareTo(b.getDescription());
    }
}
