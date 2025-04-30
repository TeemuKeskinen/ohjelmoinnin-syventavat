package tamk.ohsyte.commands;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.MonthDay;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import tamk.ohsyte.EventManager;
import tamk.ohsyte.datamodel.AnnualEvent;
import tamk.ohsyte.datamodel.AnnualEventComparator;
import tamk.ohsyte.datamodel.Category;
import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.datamodel.SingularEvent;
import tamk.ohsyte.datamodel.SingularEventComparator;
import tamk.ohsyte.filters.DateCategoryFilter;
import tamk.ohsyte.filters.DateFilter;
import tamk.ohsyte.filters.EventFilter;
import tamk.ohsyte.providers.WebEventProvider;

@Command(name = "listevents")
public class ListEvents implements Runnable {
    @Option(names = {"-c", "-cat"}, description = "Category of events to list")
    String categoryOptionString;

    @Option(names = {"-d", "-date"}, description = "Date of events to list in the format MM-dd (default is today)")
    String dateOptionString;

    @Option(names = {"-p", "-provider"}, description = "Event provider to use")
    String providerOptionString;

    @Override
    public void run() {
        Category category = null;

        if (this.categoryOptionString != null) {
            try {
                category = Category.parse(this.categoryOptionString);
            } catch (IllegalArgumentException iae) {
                System.err.println("Invalid category string: '" + this.categoryOptionString + "'");
                return;
            }
        }

        MonthDay monthDay = null;
        if (this.dateOptionString != null) {
            try {
                monthDay = MonthDay.parse("--" + this.dateOptionString);
            } catch (DateTimeParseException dtpe) {
                System.err.println("Invalid date string: '" + this.dateOptionString + "'");
                return;
            }
            System.out.printf("Events for %s:%n%n", monthDay);
        } else {
            monthDay = MonthDay.now();
        }

        EventManager manager = EventManager.getInstance();

        String dateToAppend = (this.dateOptionString != null) ? this.dateOptionString : MonthDay.now().toString().substring(2);

        if ("web".equalsIgnoreCase(this.providerOptionString)) {
            WebEventProvider webEventProvider = (WebEventProvider) manager.getEventProviders().stream()
                .filter(provider -> provider instanceof WebEventProvider)
                .findFirst()
                .orElse(null);

            if (webEventProvider == null) {
                System.err.println("No WebEventProvider is registered. Please add one first.");
                return;
            }

            try {
                URI updatedUri = new URI(webEventProvider.getServerUri() + dateToAppend);
                webEventProvider.setServerUri(updatedUri);
            } catch (URISyntaxException e) {
                System.err.println("Invalid URI: " + e.getLocalizedMessage());
                return;
            }

            // Fetch events from the updated WebEventProvider
            List<Event> webEvents = webEventProvider.getEvents();

            if (webEvents.isEmpty()) {
                System.out.println("No events found for the specified date.");
                return;
            }

            System.out.println("Web events:");
            for (Event event : webEvents) {
                System.out.println(event);
            }
            return;
        }

        EventFilter filter = (category != null) ? new DateCategoryFilter(monthDay, category) : new DateFilter(monthDay);

        List<Event> filteredEvents;

        if (this.providerOptionString != null) {
            String normalizedProvider = this.providerOptionString.toLowerCase();
            List<Event> providerEvents = manager.getProviderEvents(normalizedProvider);

            if (providerEvents.isEmpty()) {
                System.out.printf("No events found for provider: %s%n", this.providerOptionString);
                return;
            }

            filteredEvents = providerEvents.stream()
                .filter(filter::accepts)
                .toList();

            if (filteredEvents.isEmpty()) {
                System.out.printf("No events found for provider: %s with the specified filters%n", this.providerOptionString);
                return;
            }

            System.out.printf("Printing events for provider: %s%n", this.providerOptionString);
        } else {
            filteredEvents = manager.getFilteredEvents(filter);
        }

        List<AnnualEvent> annualEvents = new ArrayList<>();
        List<SingularEvent> singularEvents = new ArrayList<>();
        for (Event event : filteredEvents) {
            if (event instanceof AnnualEvent) {
                annualEvents.add((AnnualEvent) event);
            } else if (event instanceof SingularEvent) {
                singularEvents.add((SingularEvent) event);
            }
        }

        if (!annualEvents.isEmpty()) {
            System.out.println("Observed today:");
            Collections.sort(annualEvents, new AnnualEventComparator());
            for (AnnualEvent a : annualEvents) {
                System.out.printf("- %s (%s)%n", a.getDescription(), a.getCategory());
            }
        }

        if (!singularEvents.isEmpty()) {
            System.out.println("\nToday in history:");
            Collections.sort(singularEvents, new SingularEventComparator());
            Collections.reverse(singularEvents);
            for (SingularEvent s : singularEvents) {
                int year = s.getDate().getYear();
                System.out.printf("%d: %s (%s)%n", year, s.getDescription(), s.getCategory());
            }
        }
    }
}