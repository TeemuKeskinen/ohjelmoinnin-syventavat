package tamk.ohsyte.datamodel;

import java.time.LocalDate;
import java.time.MonthDay;

public class RuleBasedEvent extends AnnualEvent {
    private Rule rule;

    public RuleBasedEvent(Rule rule, String description, Category category) {
        super(rule.getMonthDay(LocalDate.now().getYear()), description, category);
        this.rule = rule;
    }

    @Override
    public MonthDay getMonthDay() {
        int year = LocalDate.now().getYear();
        return this.rule.getMonthDay(year);
    }
}
