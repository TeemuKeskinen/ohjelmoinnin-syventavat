package tamk.ohsyte;

import tamk.ohsyte.datamodel.*;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

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

        try {
            if (dateString.startsWith("--")) {  // yearless date
                MonthDay monthDay = MonthDay.parse(dateString);
                //System.out.printf("Making AnnualEvent: %s, %s, %s%n", dateString, description, category);
                return new AnnualEvent(monthDay, description, category);
            }

            if (Character.isDigit(dateString.charAt(0))) {
                LocalDate date = LocalDate.parse(dateString);
                //System.out.printf("Making SingularEvent: %s, %s, %s%n", dateString, description, category);
                return new SingularEvent(date, description, category);
            }
        } catch (DateTimeParseException dtpe) {
            System.err.println("Error parsing date for event: " + dtpe.getLocalizedMessage());
        }

        String[] parameters = {dateString, description, categoryString};
        throw new IllegalArgumentException("Unable to make an event with parameters: "
                + Arrays.toString(parameters));
    }

    public static void main(String[] args) {
        Event event = EventFactory.makeEvent("1-2-3", "bogus", "test");
    }
}
