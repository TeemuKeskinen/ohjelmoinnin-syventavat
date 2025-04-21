package tamk.ohsyte.providers;

import java.nio.file.Path;
import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import tamk.ohsyte.datamodel.AnnualEvent;
import tamk.ohsyte.datamodel.Category;
import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.datamodel.SingularEvent;
import tamk.ohsyte.EventFactory;

/**
 * Provides events stored in a text file.
 * The events consist of three lines: date, description, and category.
 * They must be separated by exactly one empty line. There should be
 * no blank line after the last event.
 */
public class TextFileEventProvider implements EventProvider {
    private final List<Event> events;
    private final String identifier;

    public TextFileEventProvider(Path path, String identifier) {
        this.identifier = identifier;
        this.events = new ArrayList<>();

        // We are loading from a local file,
        // so just cache the events for now.
        this.loadEvents(path);
    }

    private void loadEvents(Path path) {
        this.events.clear();

        String dateString = "";
        String descriptionString = "";
        String categoryString = "";

        var state = ReadingState.DATE;
        try (var in = new BufferedReader(new FileReader(path.toString()))) {
            do {
                String line = in.readLine();

                if (line == null) {  // end of file
                    state = ReadingState.DONE;
                }

                System.err.println("state = " + state);

                switch (state) {
                    case DATE:
                        dateString = line;
                        state = state.nextState();
                        break;

                    case DESCRIPTION:
                        descriptionString = line;
                        state = state.nextState();
                        break;

                    case CATEGORY:
                        categoryString = line;
                        state = state.nextState();
                        break;

                    case BLANK:
                        this.events.add(EventFactory.makeEvent(dateString, descriptionString, categoryString));
                        state = state.nextState();
                        break;

                    case DONE:
                        break;
                } // switch
            } while (state != ReadingState.DONE);
        } catch (IOException ioe) {
            System.err.println("Error reading events from text file: "
                + ioe.getMessage());
        }
    }

    @Override
    public List<Event> getEvents() {
        return this.events;
    }

    @Override
    public List<Event> getEventsOfCategory(Category category) {
        List<Event> result = new ArrayList<>();
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
        List<Event> result = new ArrayList<>();

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

    @Override
    public boolean isAddSupported() { return true; }
}