package tamk.ohsyte;

import java.time.LocalDate;
import java.time.MonthDay;

/**
 * Makes different kinds of events based on their date format.
 */
public class EventFactory {
    /**
     * Makes a singular event or an annual event based on the date format.
     * If the date starts with "--", it is parsed as a yearless date,
     * otherwise as a full date, in ISO 8601 format.
     *
     * @param dateString yearless or full date in standard format
     * @param description description of the event
     * @param categoryString category of the event
     * @return an instance of a subclass of Event
     */
    public static Event makeEvent(String dateString, String description, String categoryString) {
        // Category handling is the same for all currently known subclasses.
        Category category = Category.parse(categoryString);

        if (dateString.startsWith("--")) {  // yearless date
            MonthDay monthDay = MonthDay.parse(dateString);
            //System.out.printf("Making AnnualEvent: %s, %s, %s%n", dateString, description, category);
            return new AnnualEvent(monthDay, description, category);
        } else {
            LocalDate date = LocalDate.parse(dateString);
            //System.out.printf("Making SingularEvent: %s, %s, %s%n", dateString, description, category);
            return new SingularEvent(date, description, category);
        }
    }
}
