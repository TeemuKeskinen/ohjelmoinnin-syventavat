package tamk.ohsyte;

import java.util.List;
import java.time.MonthDay;

public interface EventProvider {
    List<Event> getEvents();
    List<Event> getEventsOfCategory(Category category);
    List<Event> getEventsOfDate(MonthDay monthDay);
}
