package tamk.ohsyte.filters;

import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.datamodel.Category;

/**
 * Filters events by their category.
 */
public class CategoryFilter extends EventFilter {
    private Category category;

    /**
     * Constructs a filter with the specified category.
     *
     * @param category the category to use
     */
    public CategoryFilter(Category category) {
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
        return event.getCategory().equals(this.category);
    }

    /**
     * Gets the category used by this filter.
     *
     * @return the category
     */
    public Category getCategory() { return this.category; }
}
