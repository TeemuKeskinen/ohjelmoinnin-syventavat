package tamk.ohsyte;

import java.util.Objects;

public class CategoryFilter extends EventFilter {
    private Category category;

    public CategoryFilter(Category category) {
        this.category = category;
    }

    @Override
    public boolean accepts(Event event) {
        Category eventCategory = Objects.requireNonNullElse(event.getCategory(), new Category("default"));
        return eventCategory.equals(this.category);
    }
}
