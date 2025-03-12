package tamk.ohsyte.filters;

import java.time.MonthDay;

import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.datamodel.SingularEvent;
import tamk.ohsyte.datamodel.AnnualEvent;

/**
 * Filters events by their month-day.
 */
public class DateFilter extends EventFilter {
    private final MonthDay monthDay;

    /**
     * Constructs a new filter with the specified month-day.
     *
     * @param monthDay the month-day to use
     */
    public DateFilter(MonthDay monthDay) {
        this.monthDay = monthDay;
    }

    /**
     * Predicate for accepting or rejecting the specified event.
     *
     * @param event the event to accept or reject
     * @return <code>true</code> if the event is accepted,
     * <code>false</code> if not
     */
    @Override
    public boolean accepts(Event event) {
        MonthDay monthDayToMatch = event.getMonthDay();
        if (event instanceof SingularEvent) {
            return (this.monthDay.equals(monthDayToMatch));
        } else if (event instanceof AnnualEvent) {
            return this.monthDay.equals(monthDayToMatch);
        } else {
            return false;  // some other type of event, must be false
        }
    }

    /**
     * Gets the month-day used by this filter.
     *
     * @return the month-day
     */
    public MonthDay getMonthDay() { return this.monthDay; }
}
