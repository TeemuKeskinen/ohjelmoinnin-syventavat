package tamk.ohsyte;

import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages and queries the events available from event providers.
 */
public class EventManager {
    private static final EventManager INSTANCE = new EventManager();

    private final List<EventProvider> eventProviders;

    private EventManager() {
        this.eventProviders = new ArrayList<>();
    }

    /**
     * Gets the only instance of the event manager.
     *
     * @return the instance
     */
    public static EventManager getInstance() {
        return INSTANCE;
    }

    /**
     * Adds an event provider to the manager's list if it isn't
     * already there.
     *
     * @param provider the event provider to add
     * @return <code>true</code> if the provider was added, <code>false</code> otherwise
     */
    public boolean addEventProvider(EventProvider provider) {
        // TODO: Check if this event provider is already on the list.
        this.eventProviders.add(provider);
        return true;
    }

    /**
     * Removes the specified event provider from the manager's list.
     *
     * @param providerId the identifier of the event provider to remove
     * @return <code>true</code> if the provider was removed, <code>false</code> if not
     */
    public boolean removeEventProvider(String providerId) {
        throw new UnsupportedOperationException(
            "Removal of event providers is not supported yet.");
    }

    /**
     * Get all the events available from all registered event providers.
     *
     * @return the list of all events
     */
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();

        for (EventProvider provider : this.eventProviders) {
            events.addAll(provider.getEvents());
        }

        return events;
    }

    public List<Event> getEventsOfDate(MonthDay monthDay) {
        List<Event> events = new ArrayList<>();

        for (EventProvider provider : this.eventProviders) {
            events.addAll(provider.getEventsOfDate(monthDay));
        }

        return events;
    }

    /**
     * Gets the number of event providers for the manager.
     *
     * @return the number of event providers
     */
    public int getEventProviderCount() {
        return this.eventProviders.size();
    }

    /**
     * Gets the identifiers of all event providers of the manager.
     *
     * @return list of provider identifiers
     */
    public List<String> getEventProviderIdentifiers() {
        throw new UnsupportedOperationException(
            "Getting event provider IDs is not supported yet.");
    }

    /**
     * Gets the filtered events based on the provided filter.
     *
     * @param filter the event filter
     * @return the list of filtered events
     */
    public List<Event> getFilteredEvents(EventFilter filter) {
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : getAllEvents()) {
            if (filter.accepts(event)) {
                filteredEvents.add(event);
            }
        }
        List<AnnualEvent> annualEvents = new ArrayList<>();
        List<SingularEvent> singularEvents = new ArrayList<>();
        for (Event event : filteredEvents) {
            if (event instanceof AnnualEvent) {
                annualEvents.add((AnnualEvent) event);
            } else if (event instanceof SingularEvent) {
                singularEvents.add((SingularEvent) event);
            }
        }
        Collections.sort(annualEvents);
        Collections.sort(singularEvents);
        List<Event> sortedEvents = new ArrayList<>();
        sortedEvents.addAll(annualEvents);
        sortedEvents.addAll(singularEvents);
        return sortedEvents;
    }
}
