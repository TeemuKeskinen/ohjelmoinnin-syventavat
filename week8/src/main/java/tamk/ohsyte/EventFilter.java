package tamk.ohsyte;

import java.util.ArrayList;

/**
 * Filters events based on a given criterion.
 */

public abstract class EventFilter {
    /**
     * Filters the given list of events.
     *
     * @param events the list of events to filter
     * @return the filtered list of events
     */
    public ArrayList<Event> filter(ArrayList<Event> events) {
        ArrayList<Event> filtered = new ArrayList<>();
        for (Event event : events) {
            if (accepts(event)) {
                filtered.add(event);
            }
        }
        return filtered;
    }

    /**
     * Checks if the given event is accepted by the filter.
     *
     * @param event the event to check
     * @return true if the event is accepted, false otherwise
     */
    public abstract boolean accepts(Event event);
}