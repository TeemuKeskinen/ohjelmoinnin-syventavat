package tamk.ohsyte;

import java.time.MonthDay;
import java.util.Objects;

public class DateFilter extends EventFilter {
    private MonthDay monthDay;
    private Integer year;

    public DateFilter(MonthDay monthDay) {
        this.monthDay = monthDay;
        this.year = null;
    }

    public DateFilter(MonthDay monthDay, int year) {
        this.monthDay = monthDay;
        this.year = year;
    }

    @Override
    public boolean accepts(Event event) {
        if (event instanceof SingularEvent singularEvent) {
            MonthDay eventMonthDay = Objects.requireNonNullElse(singularEvent.getMonthDay(), MonthDay.of(1, 1));
            Integer eventYear = Objects.requireNonNullElse(singularEvent.getYear(), 0);
            if (this.year != null) {
                return eventMonthDay.equals(this.monthDay) && eventYear.equals(this.year);
            }
            return eventMonthDay.equals(this.monthDay);
        }
        return false;
    }
}
