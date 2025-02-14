package tamk.ohsyte;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.MonthDay;
import java.util.Collections;
import java.util.List;

public class Today {
    public static void main(String[] args) {
        String userHome = System.getProperty("user.home");
        Path csvPath = Paths.get(userHome, ".today", "events.csv");

        if (!Files.exists(csvPath)) {
            System.err.println("CSV file not found in " + csvPath);
            return;
        }

        EventProvider provider = new CSVEventProvider(csvPath.toString());

        final MonthDay monthDay = MonthDay.of(2, 10);

        // Get events for given day, any year, any category, newest first
        List<Event> events = provider.getEventsOfDate(monthDay);
        Collections.sort(events);
        Collections.reverse(events);

        for (Event event : events) {
            System.out.println(event);
        }
    }
}