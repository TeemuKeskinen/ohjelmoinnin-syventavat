package tamk.ohsyte.commands;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.providers.WebEventProvider;

@Command(name = "web-events", description = "Fetches events from the web")
public class WebEventCommand implements Runnable {

    @Option(names = {"-d", "--date"}, description = "Date for which to fetch events (format: MM-dd)", required = true)
    private String date;

    @Option (names = {"-s", "--site"}, description = "Site to fetch events from", required = true)
    private String site;

    @Override
    public void run() {
        try {
            URI serverUri = new URI(site + date);
            WebEventProvider webEventProvider = new WebEventProvider(serverUri);
            List<Event> events = webEventProvider.getEvents();
            // Display events
            events.forEach(event -> System.out.println(event));
        } catch (URISyntaxException e) {
            System.err.println("Invalid URI: " + e.getLocalizedMessage());
        }
    }
}
