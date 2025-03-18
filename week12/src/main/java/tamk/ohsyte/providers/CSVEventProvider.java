package tamk.ohsyte.providers;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import tamk.ohsyte.*;
import tamk.ohsyte.datamodel.AnnualEvent;
import tamk.ohsyte.datamodel.Category;
import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.datamodel.SingularEvent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Month;
import java.time.MonthDay;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;

/**
 * Provides events stored in a CSV file. Uses the OpenCSV library.
 */
public class CSVEventProvider implements EventProvider {
    private final List<Event> events;
    private final String identifier;

    public CSVEventProvider(String fileName, String identifier) {
        this.identifier = identifier;
        this.events = new ArrayList<>();

        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(fileName)).build();
            String[] line;
            while ((line = reader.readNext()) != null) {
                Event event = EventFactory.makeEvent(line[0], line[1], line[2]);
                this.events.add(event);
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println("CSV file '" + fileName + "' not found! " + fnfe.getLocalizedMessage());
        } catch (CsvValidationException cve) {
            System.err.println("Error in CSV file contents: " + cve.getLocalizedMessage());
        } catch (DateTimeParseException dtpe) {
            System.err.println("Error in date format: " + dtpe.getLocalizedMessage());
        } catch (IOException ioe) {
            System.err.println("Error reading CSV file: " + ioe.getLocalizedMessage());
        }
    }

    //
    // EventProvider interface implementation
    //

    /**
     * Gets all events from this provider.
     *
     * @return list of all events
     */
    @Override
    public List<Event> getEvents() {
        return this.events;
    }

    /**
     * Gets all events matching the specified category.
     *
     * @param category the category to match
     * @return list of matching events
     */
    @Override
    public List<Event> getEventsOfCategory(Category category) {
        List<Event> result = new ArrayList<Event>();
        for (Event event : this.events) {
            if (event.getCategory().equals(category)) {
                result.add(event);
            }
        }
        return result;
    }

    /**
     * Gets the events matching the given month-day combination.
     *
     * @param monthDay month and day to match
     * @return list of matching events
     */
    @Override
    public List<Event> getEventsOfDate(MonthDay monthDay) {
        List<Event> result = new ArrayList<Event>();

        for (Event event : this.events) {
            Month eventMonth;
            int eventDay;
            if (event instanceof SingularEvent) {
                SingularEvent s = (SingularEvent) event;
                eventMonth = s.getDate().getMonth();
                eventDay = s.getDate().getDayOfMonth();
            } else if (event instanceof AnnualEvent) {
                AnnualEvent a = (AnnualEvent) event;
                eventMonth = a.getMonthDay().getMonth();
                eventDay = a.getMonthDay().getDayOfMonth();
            } else {
                throw new UnsupportedOperationException(
                        "Operation not supported for " +
                        event.getClass().getName());
            }
            if (monthDay.getMonth() == eventMonth && monthDay.getDayOfMonth() == eventDay) {
                result.add(event);
            }
        }

        return result;
    }

    /**
     * Gets the identifier of this event provider.
     *
     * @return the identifier
     */
    @Override
    public String getIdentifier() {
        return this.identifier;
    }
}
