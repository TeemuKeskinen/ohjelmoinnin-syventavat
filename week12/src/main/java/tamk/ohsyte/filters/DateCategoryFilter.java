package tamk.ohsyte.filters;

import java.time.MonthDay;

import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.datamodel.Category;

/**
 * Filters events by both their month-day and category.
 */
public class DateCategoryFilter extends DateFilter {
    private final Category category;

    /**
     * Constructs a new filter with the given month-day and category.
     *
     * @param monthDay the month-day
     * @param category the category
     */
    public DateCategoryFilter(MonthDay monthDay, Category category) {
        super(monthDay);
        this.category = category;
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
        boolean monthDayMatches = this.getMonthDay().equals(event.getMonthDay());
        boolean categoryMatches = this.category.equals(event.getCategory());
        boolean result = monthDayMatches && categoryMatches;
        //System.out.printf("DateCategoryFilter.accepts: %s && %s = %s - event: %s, filter: %s%n",
        //        monthDayMatches, categoryMatches, result, event, this);
        return result;
    }

    /**
     * Gets a string representation of this filter.
     *
     * @return descriptive string
     */
    @Override
    public String toString() {
        return String.format("monthDay = %s  category = '%s'", this.getMonthDay(), this.getCategory());
    }

    /**
     * Gets the category used by this filter.
     *
     * @return a category
     */
    public Category getCategory() {
        return this.category;
    }
}
