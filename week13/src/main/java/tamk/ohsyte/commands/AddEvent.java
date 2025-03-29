package tamk.ohsyte.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import tamk.ohsyte.providers.SQLiteEventProvider;

@Command(name = "addevent", description = "Adds a new event to the database")
public class AddEvent implements Runnable {
    @Option(names = "-db", required = false, description = "Path to the database file")
    String databaseFile;

    @Option(names = "-date", required = true, description = "Date of the event (format: YYYY-MM-DD)")
    String eventDate;

    @Option(names = "-desc", required = false, description = "Description of the event")
    String eventDescription;

    @Option(names = "-cat", required = true, description = "Category ID of the event")
    int categoryId;

    @Override
    public void run() {
        if (databaseFile == null) {
            databaseFile = System.getProperty("user.home") + "/.today/events.sqlite3";
        }
        if (eventDate == null || categoryId <= 0) {
            System.err.println("Event date and category ID are required.");
            return;
        }

        SQLiteEventProvider provider = new SQLiteEventProvider(databaseFile);

        try {
            provider.addEvent(eventDate, eventDescription, categoryId);
            System.out.println("Event added successfully.");
        } catch (Exception e) {
            System.err.println("Error adding event: " + e.getMessage());
        }
    }
}
