package tamk.ohsyte;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Today {
    public static void main(String[] args) {
        // Gets the singleton manager. Later calls to getInstance
        // will return the same reference.
        EventManager manager = EventManager.getInstance();

        // Add a CSV event provider that reads from the given file.
        // Replace with a valid path to the events.csv file on your own computer!
        String userHome = System.getProperty("user.home");
        Path csvPath1 = Paths.get(userHome, "ohjelmoinnin-syventavat", "week8", "events.csv");
        manager.addEventProvider(new CSVEventProvider(csvPath1.toString()));

        Path csvPath2 = Paths.get(userHome, "ohjelmoinnin-syventavat", "week8", "singular-events.csv");
        manager.addEventProvider(new CSVEventProvider(csvPath2.toString()));

        MonthDay today = MonthDay.now();
        List<Event> allEvents = manager.getEventsOfDate(today);
        List<AnnualEvent> annualEvents = new ArrayList<>();
        List<SingularEvent> singularEvents = new ArrayList<>();
        for (Event event : allEvents) {
            if (event instanceof AnnualEvent) {
                annualEvents.add((AnnualEvent) event);
            } else if (event instanceof SingularEvent) {
                singularEvents.add((SingularEvent) event);
            }
        }

        System.out.println("Today:");
        Collections.sort(annualEvents, new AnnualEventComparator());

        for (AnnualEvent a : annualEvents) {
            System.out.printf(
                    "- %s (%s) %n",
                    a.getDescription(),
                    a.getCategory());
        }
        //System.out.printf("%d events%n", annualEvents.size());

        System.out.println("\nToday in history:");
        Collections.sort(singularEvents, new SingularEventComparator());
        Collections.reverse(singularEvents);

        for (SingularEvent s : singularEvents) {
            int year = s.getDate().getYear();
            if (year < 2015) {
                continue;
            }

            System.out.printf(
                    "%d: %s (%s)%n",
                    year,
                    s.getDescription(),
                    s.getCategory());
        }
        //System.out.printf("%d events%n", singularEvents.size());

        // pvm filter testi
        DateFilter dateFilter = new DateFilter(today);
        List<Event> dateFilteredEvents = manager.getFilteredEvents(dateFilter);
        System.out.println("\nEvents filtered by date:");
        for (Event event : dateFilteredEvents) {
            System.out.println(event);
        }

        // kategoria filter testi
        Category category = new Category("test");
        CategoryFilter categoryFilter = new CategoryFilter(category);
        List<Event> categoryFilteredEvents = manager.getFilteredEvents(categoryFilter);
        System.out.println("\nEvents filtered by category:");
        for (Event event : categoryFilteredEvents) {
            System.out.println(event);
        }

        // molemmat testi
        DateCategoryFilter dateCategoryFilter = new DateCategoryFilter(today, category);
        List<Event> dateCategoryFilteredEvents = manager.getFilteredEvents(dateCategoryFilter);
        System.out.println("\nEvents filtered by date and category:");
        for (Event event : dateCategoryFilteredEvents) {
            System.out.println(event);
        }
    }
}
