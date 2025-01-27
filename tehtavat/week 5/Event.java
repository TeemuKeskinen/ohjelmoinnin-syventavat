import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an event in history.
 */
class Event {
    private LocalDate date;
    private String description;
    private Category category;

    /**
     * Constructs a new event.
     *
     * @param date the date of the event
     * @param description the description of the event
     * @param category the category of the event
     *
     * @see Category
     */
    public Event(LocalDate date, String description, Category category) {
        this.date = date;
        this.description = description;
        this.category = category;
    }

    /**
     * Gets the date of the event.
     *
     * @return the date
     */
    public LocalDate getDate() {
        return this.date;
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
            this.date, this.description, this.category);
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

        if (Objects.equals(this.date, that.date) &&
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
        return Objects.hash(this.date, this.description, this.category);
    }
}
