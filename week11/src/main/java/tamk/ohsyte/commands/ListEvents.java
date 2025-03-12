package tamk.ohsyte.commands;

import java.time.MonthDay;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import tamk.ohsyte.*;
import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.datamodel.AnnualEvent;
import tamk.ohsyte.datamodel.SingularEvent;
import tamk.ohsyte.datamodel.Category;
import tamk.ohsyte.datamodel.AnnualEventComparator;
import tamk.ohsyte.datamodel.SingularEventComparator;

import tamk.ohsyte.filters.DateCategoryFilter;
import tamk.ohsyte.filters.DateFilter;
import tamk.ohsyte.filters.EventFilter;

@Command(name = "listevents")
public class ListEvents implements Runnable {
    @Option(names = "-c", description = "Category of events to list")
    String categoryOptionString;

    @Option(names = "-d", description = "Date of events to list in the format MM-dd (default is today)")
    String dateOptionString;

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

        // Now we either have a valid Category instance or it is null.

        MonthDay monthDay = null;
        if (this.dateOptionString != null) {
            try {
                monthDay = MonthDay.parse("--" + this.dateOptionString);
            } catch (DateTimeParseException dtpe) {
                System.err.println("Invalid date string: '" + this.dateOptionString + "'");
                return;
            }
        } else {
            //System.out.println("DEBUG: No date specified, default to now");
            monthDay = MonthDay.now();
        }

        EventFilter filter = null;
        // We always have a valid monthDay (defaults to 'now'),
        // so just check if we need category filtering.
        if (category != null) {
            filter = new DateCategoryFilter(monthDay, category);
        } else {
            filter = new DateFilter(monthDay);
        }
        // We actually seem to have no use for CategoryFilter.

        EventManager manager = EventManager.getInstance();
        List<Event> filteredEvents = manager.getFilteredEvents(filter);

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
                System.out.printf(
                        "- %s (%s) %n",
                        a.getDescription(),
                        a.getCategory());
            }
        }

        if (!singularEvents.isEmpty()) {
            System.out.println("\nToday in history:");
            Collections.sort(singularEvents, new SingularEventComparator());
            Collections.reverse(singularEvents);

            for (SingularEvent s : singularEvents) {
                int year = s.getDate().getYear();

                System.out.printf(
                        "%d: %s (%s)%n",
                        year,
                        s.getDescription(),
                        s.getCategory());
            }
        }
    }
}
