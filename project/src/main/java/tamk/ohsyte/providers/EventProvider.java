package tamk.ohsyte.providers;

import java.time.MonthDay;
import java.util.List;

import tamk.ohsyte.datamodel.Category;
import tamk.ohsyte.datamodel.Event;

public interface EventProvider {
    List<Event> getEvents();
    List<Event> getEventsOfCategory(Category category);
    List<Event> getEventsOfDate(MonthDay monthDay);
    String getIdentifier();
    String getFilename();
    void addEvent(Event event, String filename);
    default boolean isAddSupported() { return false; }
}