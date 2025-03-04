package tamk.ohsyte;

import java.time.MonthDay;
import java.util.Objects;

public class DateCategoryFilter extends EventFilter {
    private MonthDay monthDay;
    private Category category;

    public DateCategoryFilter(MonthDay monthDay, Category category) {
        this.monthDay = monthDay;
        this.category = category;
    }

    @Override
    public boolean accepts(Event event) {
        if (event instanceof SingularEvent singularEvent) {
            Category eventCategory = Objects.requireNonNullElse(event.getCategory(), new Category("default"));
            return singularEvent.getMonthDay().equals(this.monthDay) &&
                   eventCategory.equals(this.category);
        }
        return false;
    }
}
