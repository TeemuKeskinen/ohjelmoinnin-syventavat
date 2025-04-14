package tamk.ohsyte.datamodel;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Comparator;
import java.util.Objects;

/**
 * Represents an event that has occurred once in history.
 */
public class SingularEvent extends Event implements Comparable<Event> {
    private int year;

    /**
     * Constructs a singular event with date, description, and category.
     *
     * @param date the date of the event
     * @param description the description of the event
     * @param category the category of the event
     */
    public SingularEvent(LocalDate date, String description, Category category) {
        // Call the superclass constructor to initialize
        super(MonthDay.of(date.getMonth(), date.getDayOfMonth()), description, category);

        this.year = date.getYear();
    }

    /**
     * Gets the date of the event.
     *
     * @return the date
     */
    public LocalDate getDate() {
        return LocalDate.of(
            this.year, 
            this.getMonthDay().getMonth(),
            this.getMonthDay().getDayOfMonth());
    }

    /**
     * Gets the year of this event.
     *
     * @return the year
     */
    public int getYear() {
        return this.year;
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
        if (!(o instanceof SingularEvent)) return false;

        // Cast to our type:
        SingularEvent that = (SingularEvent) o;

        if (Objects.equals(this.year, that.year) && 
            Objects.equals(this.getMonthDay(), that.getMonthDay()) &&
            Objects.equals(this.getDescription(), that.getDescription()) &&
            Objects.equals(this.getCategory(), that.getCategory())) {
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
        return Objects.hash(this.year, this.getMonthDay(), this.getDescription(), this.getCategory());
    }

    /**
     * Compares this event to another.
     *
     * @return negative, zero, or positive
     * @see Comparable
     */
    @Override
    public int compareTo(Event other) {
        SingularEvent otherEvent = (SingularEvent) other;

        int result = Objects.compare(
                this.year,
                otherEvent.year,
                Comparator.naturalOrder());
        if (result != 0) {
            return result;
        }

        result = Objects.compare(
            this.getMonthDay(),
            otherEvent.getMonthDay(),
            Comparator.naturalOrder());
        if (result != 0) {
            return result;
        }
        
        result = Objects.compare(
                this.getDescription(),
                otherEvent.getDescription(),
                Comparator.naturalOrder());
        if (result != 0) {
            return result;
        }

        return Objects.compare(
                this.getCategory(),
                otherEvent.getCategory(),
                Comparator.naturalOrder());
    }
}
