package tamk.ohsyte.datamodel;

import java.util.Objects;
import java.time.MonthDay;

/**
 * Abstract superclass for representing an event in history.
 */
public abstract class Event implements Comparable<Event> {
    // All events have a description and a category.
    private Category category;
    private String description;

    // All events also have at least a MonthDay.
    private MonthDay monthDay;

    /**
     * Constructor that can be used by the subclasses.
     * 
     * @param monthDay the month-day of the event
     * @param description the description of the event
     * @param category the category of the event
     * 
     * @see Category
     */
    public Event(MonthDay monthDay, String description, Category category) {
        this.monthDay = monthDay;
        this.description = description;
        this.category = category;
    }

    /**
     * Gets the month-day of the event.
     * 
     * @return month-day
     */
    public MonthDay getMonthDay() {
        return this.monthDay;
    }

    /**
     * Gets the event description.
     * 
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets the category of the event.
     * 
     * @return the category
     * @see Category
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * Returns a string representation of this event.
     * 
     * @return the event as a string
     */
    @Override
    public String toString() {
        return String.format(
            "%s: %s (%s)",
            this.monthDay, this.description, this.category);
    }

    /**
     * Tests for equality with another event.
     * 
     * @return true if events are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        // Identical references?
        if (o == this) return true;

        // Correct type and non-null?
        if (!(o instanceof Event)) return false;

        // Cast to our type:
        Event that = (Event) o;

        if (Objects.equals(this.monthDay, that.monthDay) && 
            Objects.equals(this.description, that.description) &&
            Objects.equals(this.category, that.category)) {
            return true;
        }

        return false;
    }

    /**
     * Returns a hash code for this event.
     * 
     * @return hash code computed based on the fields
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.monthDay, this.description, this.category);
    }
}
