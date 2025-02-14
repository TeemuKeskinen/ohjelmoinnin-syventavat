package tamk.ohsyte;

import java.util.Comparator;
import java.util.Objects;

/**
 * The category of an event, with primary and secondary categories.
 */
public class Category implements Comparable<Category> {
    private String primary;
    private String secondary;

    /**
     * Constructs a category with primary and secondary values.
     * 
     * @param primary the primary category
     * @param secondary the secondary category
     */
    public Category(String primary, String secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    /**
     * Gets the primary category.
     * 
     * @return primary
     */
    public String getPrimary() {
        return this.primary;
    }

    /**
     * Gets the secondary category.
     * 
     * @return secondary
     */
    public String getSecondary() {
        return this.secondary;
    }

    /**
     * Returns a string representation of this category.
     * 
     * @return category as string 
     */
    @Override
    public String toString() {
        return String.format("%s/%s", this.primary, this.secondary);
    }

    /**
     * Tests for equality with another category.
     * 
     * @return true if categories are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        // Identical references?
        if (o == this) return true;

        // Correct type and non-null?
        if (!(o instanceof Category)) return false;

        // Cast to our type:
        Category that = (Category) o;

        if (this.primary.equals(that.primary) && 
            this.secondary.equals(that.secondary)) {
            return true;
        }

        return false;
    }

    /**
     * Returns a hash code for this category.
     * 
     * @return hash code computed based on primary and secondary categories
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.primary, this.secondary);
    }

    @Override
    public int compareTo(Category other) {
        int result = Objects.compare(
            this.primary, 
            other.getPrimary(),
            Comparator.naturalOrder());
        if (result != 0) {
            return result;
        }

        return Objects.compare(
            this.secondary,
            other.getSecondary(),
            Comparator.naturalOrder());
    }
}
