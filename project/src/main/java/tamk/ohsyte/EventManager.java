package tamk.ohsyte;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.filters.EventFilter;
import tamk.ohsyte.providers.EventProvider;

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

     * @param provider the event provider to add
     * @return <code>true</code> if the provider was added, <code>false</code> otherwise
     */
    public boolean addEventProvider(EventProvider provider) {
        if (this.eventProviders.stream().noneMatch(
                (p) -> p.getIdentifier().equals(provider.getIdentifier()))) {
            this.eventProviders.add(provider);
            return true;
        }
        return false;
    }

    /**
     * Removes the specified event provider from the manager's list.
     *
     * @param providerId the identifier of the event provider to remove
     * @return <code>true</code> if the provider was removed, <code>false</code> if not
     */
    public boolean removeEventProvider(String providerId) {
        return this.eventProviders.removeIf(
                p -> p.getIdentifier().equals(providerId));
    }

    /**
     * Get all the events available from all registered event providers.
     *
     * @return the list of all events
     */
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();

        Consumer<EventProvider> adder =
                provider -> events.addAll(provider.getEvents());
        this.eventProviders.forEach(adder);

        return events;
    }

    /*
    public List<Event> getEventsOfDate(MonthDay monthDay) {
        List<Event> events = new ArrayList<>();

        for (EventProvider provider : this.eventProviders) {
            events.addAll(provider.getEventsOfDate(monthDay));
        }

        return events;
    }
    */

    /**
     * Adds an event to the specified event provider.
     *
     * @param category the category to match
     * @return list of events in the specified category
     */
    public void addEventToProvider(Event event, String providerId) {
        EventProvider provider = eventProviders.stream()
                .filter(p -> p.getIdentifier().equals(providerId))
                .findFirst()
                .orElse(null);

        if (provider != null && provider.isAddSupported()) {
            provider.addEvent(event);
        } else {
            System.err.println("Provider not found or does not support adding events.");
        }
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
     * Gets the event providers of the manager.
     *
     * @return list of event providers
     */
    public List<EventProvider> getEventProviders() {
        return this.eventProviders;
    }
    /**
     * Gets the identifiers of all event providers of the manager.
     *
     * @return list of provider identifiers
     */
    public List<String> getEventProviderIdentifiers() {
        return this.eventProviders.stream()
                .map(EventProvider::getIdentifier)
                .toList();
    }

    public List<EventProvider> getEventProviderFile() {
        return this.eventProviders.stream()
                .filter(provider -> provider.getIdentifier().equals("file"))
                .toList();
    }

    /**
     * Gets the events that are accepted by the specified filter.
     *
     * @param filter the filter
     * @return list of events
     */
    public List<Event> getFilteredEvents(EventFilter filter) {
        return this.getAllEvents().stream()
                .filter(event -> filter.accepts(event))
                .toList();
    }
}