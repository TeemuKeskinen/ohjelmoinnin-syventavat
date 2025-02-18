package tamk.ohsyte;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Today {
    public static void main(String[] args) {
        EventManager manager = EventManager.getInstance();

        manager.addEventProvider(new FirstEventProvider("First"));
        manager.addEventProvider(new SecondEventProvider("Second"));

        String userHome = System.getProperty("user.home");
        Path csvPath = Paths.get(userHome, ".today", "events.csv");

        manager.addEventProvider(new CSVEventProvider(csvPath.toString()));

        // final MonthDay monthDay = MonthDay.of(2, 10);

        // Get events for given day, any year, any category, newest first
        List<Event> events = manager.getAllEvents();
        Collections.sort(events);
        Collections.reverse(events);

        List<String> identifiers = manager.getEventProviderIdentifiers();
        System.out.println("Event providers: "
        + Arrays.toString(identifiers.toArray()));

        List<Event> allEvents = manager.getAllEvents();

        int providerCount = manager.getEventProviderCount();
        int eventCount = allEvents.size();
        System.out.printf("Manager has %d event providers,%n", providerCount);
        System.out.printf("with a total of %d events.%n", eventCount);


        manager.removeEventProvider("CSV");

        providerCount = manager.getEventProviderCount();
        events = manager.getAllEvents();  // refresh event list
        eventCount = events.size();
        System.out.printf("Manager has %d event providers,%n", providerCount);
        System.out.printf("with a total of %d events.%n", eventCount);

        /*for (Event event : events) {
            System.out.println(event);
        }*/
    }
}