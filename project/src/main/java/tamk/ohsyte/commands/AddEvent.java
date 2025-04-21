package tamk.ohsyte.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import tamk.ohsyte.EventFactory;
import tamk.ohsyte.EventManager;
import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.providers.CSVEventProvider;

@Command(name = "addevent", description = "Adds a new event to the database")
public class AddEvent implements Runnable {
    @Option(names = "-provider", required = false, description = "Event provider (sqlite, csv, text)")
    String provider;

    @Option(names = "-date", required = true, description = "Date of the event (format: YYYY-MM-DD or --MM-DD)")
    String eventDate;

    @Option(names = "-desc", required = false, description = "Description of the event")
    String eventDescription;

    @Option(names = "-cat", required = false, description = "Category of the event")
    String category;

    @Override
    public void run() {
        // Use EventFactory to create the event
        Event event = EventFactory.makeEvent(eventDate, eventDescription, category);

        if (provider == null) {
            System.out.println("No storage option provided. Adding event to primary provider");
            // Add event to default CSV file
            CSVEventProvider csv = new CSVEventProvider("events.csv", "csv");
            csv.addEvent(event);
            System.out.println("Event added to default CSV file.");

        } else {
            // Get the EventManager instance
            EventManager manager = EventManager.getInstance();
            // Check if the provider is registered
            if (manager.getEventProviders().stream().noneMatch(p -> p.getIdentifier().equals(provider))) {
                System.out.println("Provider not found: " + provider);
                return;
            }
            // Check if the provider supports adding events
            if (!manager.getEventProviders().stream()
                    .filter(p -> p.getIdentifier().equals(provider))
                    .findFirst()
                    .get()
                    .isAddSupported()) {
                System.out.println("Provider does not support adding events: " + provider);
                return;
            }
                // Add event to the specified provider
                manager.addEventToProvider(event, provider);
                System.out.println("Event added to " + provider + " provider.");
            }
                System.out.println("Provider does not support adding events: " + provider);
        }
    }

