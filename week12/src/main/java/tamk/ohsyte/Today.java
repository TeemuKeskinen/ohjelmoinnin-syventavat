package tamk.ohsyte;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import tamk.ohsyte.commands.ListEvents;
import tamk.ohsyte.commands.ListProviders;
import tamk.ohsyte.commands.WebEventCommand;
import tamk.ohsyte.providers.CSVEventProvider;

@Command(name = "today", subcommands = { ListProviders.class, ListEvents.class, WebEventCommand.class }, description = "Shows events from history and annual observations")
public class Today {
    public Today() {
        // Gets the singleton manager. Later calls to getInstance
        // will return the same reference.
        EventManager manager = EventManager.getInstance();

        // Construct a path to a file in the user's home directory,
        // in a subdirectory called ".today".
        String homeDirectory = System.getProperty("user.home");
        String configDirectory = ".today";
        Path path = Paths.get(homeDirectory, configDirectory, "events.csv");
        //System.out.println("Path = " + path.toString());

        // Create the events file if it doesn't exist
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                System.err.println("Unable to create events file");
                System.exit(1);
            }
        }

        String eventProviderId = "standard";

        // Add a CSV event provider that reads from the given file.
        manager.addEventProvider(
                new CSVEventProvider(path.toString(), eventProviderId));

        // Try to add an event provider with the same ID again:
        if (!manager.addEventProvider(
                new CSVEventProvider(path.toString(), eventProviderId))) {
            System.err.printf("Event provider '%s' is already registered%n", eventProviderId);
        }

        /* Add WebEventProvider
        try {
            URI serverUri = new URI("https://todayserver-89bb2a1b2e80.herokuapp.com/api/v1/events?date=03-17");
            manager.addEventProvider(new WebEventProvider(serverUri));
        } catch (URISyntaxException e) {
            System.err.println("Invalid URI: " + e.getLocalizedMessage());
        }

        // Exercise the Java HTTP client API.
        // This will later be folded into WebEventProvider.
        MonthDay monthDay = MonthDay.now();  // get today's month-day
        var serverAddress = "https://todayserver-89bb2a1b2e80.herokuapp.com/";
        var serverEventsPath = "api/v1/events";
        var eventsParameters = String.format("?date=%s", monthDay.toString().substring(2));
        String bodyString = null;
        try {
            HttpClient client = HttpClient.newHttpClient();
            var serverUriString = String.format("%s%s%s", serverAddress, serverEventsPath, eventsParameters);
            URI serverUri = new URI(serverUriString);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(serverUri)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            bodyString = response.body();
            int status = response.statusCode();
            if (status != 200) {
                System.err.printf("HTTP response: %d%n", status);
                System.err.println("Response body = " + bodyString);
            } else {
                System.out.println("Response headers: " + response.headers());
                System.out.println("Response body = " + bodyString);
            }
        } catch (URISyntaxException use) {
            System.err.println("Error making URI: " + use.getLocalizedMessage());
        } catch (IOException | InterruptedException ex) {
            System.err.println("Error sending HTTP request: " + ex.getLocalizedMessage());
        }

        try {
            // Create a custom deserializer for Event objects
            // and register it with Jackson's object mapper.
            SimpleModule module = new SimpleModule("EventDeserializer");
            module.addDeserializer(Event.class, new EventDeserializer());
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(module);

            // Make a custom collection type for Jackson
            // representing a List<Event>.
            JavaType customClassCollection = mapper.getTypeFactory().constructCollectionType(List.class, Event.class);

            // Use Jackson to parse the response body string as a List<Event>.
            List<Event> webEvents = mapper.readValue(bodyString, customClassCollection);

            // Separate the events to lists by their types
            List<AnnualEvent> annualEvents = new ArrayList<>();
            List<SingularEvent> singularEvents = new ArrayList<>();
            for (Event event : webEvents) {
                if (event instanceof AnnualEvent) {
                    annualEvents.add((AnnualEvent) event);
                } else if (event instanceof SingularEvent) {
                    singularEvents.add((SingularEvent) event);
                }
            }

            // Just print the events (just like commands.ListEvents)
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
                System.out.println("---web".repeat(10));
                System.out.println("Today in history (events from web):");
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

                System.out.println("---web".repeat(10));
            }
        } catch (JsonProcessingException ex) {
            System.err.println("Error processing JSON: " + ex.toString());
            ex.printStackTrace();
        }
        */
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Today()).execute(args);
        System.exit(exitCode);
    }
}
