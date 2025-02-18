package tamk.ohsyte;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.Collections;
import java.util.List;

public class FirstEventProvider implements EventProvider {
    protected Event event;
    protected Category category;
    protected String identifier;

    public FirstEventProvider(String identifier) {
        this.identifier = identifier;
        this.category = new Category("test", "first");
        this.event = new Event(LocalDate.now(),
                "Event returned by FirstEventProvider",
                this.category);
    }

    @Override
    public List<Event> getEvents() {
        return Collections.singletonList(this.event);
    }

    @Override
    public List<Event> getEventsOfCategory(Category category) {
        if (category.equals(this.category)) {
            return Collections.singletonList(this.event);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Event> getEventsOfDate(MonthDay monthDay) {
        final Month eventMonth = event.getDate().getMonth();
        final int eventDay = event.getDate().getDayOfMonth();
        if (monthDay.getMonth() == eventMonth && monthDay.getDayOfMonth() == eventDay) {
            return Collections.singletonList(event);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }
}