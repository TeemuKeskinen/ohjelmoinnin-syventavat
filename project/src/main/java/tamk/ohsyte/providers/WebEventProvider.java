package tamk.ohsyte.providers;



import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import tamk.ohsyte.filters.EventFilter;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.time.Month;


import tamk.ohsyte.datamodel.Category;
import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.datamodel.AnnualEvent;
import tamk.ohsyte.datamodel.SingularEvent;
import tamk.ohsyte.providers.EventDeserializer;



public class WebEventProvider implements EventProvider {
    private final List<Event> events;
    private  URI serverUri;
    private final String identifier;

    public WebEventProvider(URI serverUri, String identifier) {
        this.identifier = identifier;
        this.serverUri = serverUri;
        this.events = new ArrayList<>();
    }

    private void fetchEvents() {
        String bodyString = null;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(serverUri)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            bodyString = response.body();
            int status = response.statusCode();
            System.out.println("Fetching events from: " + serverUri);
            if (status != 200) {
                System.err.printf("HTTP response: %d%n", status);
                //System.err.println("Response body = " + bodyString);
            }
        } catch (IOException | InterruptedException ex) {
            System.err.println("Error sending HTTP request: " + ex.getLocalizedMessage());
        }

        try {
            SimpleModule module = new SimpleModule("EventDeserializer");
            module.addDeserializer(Event.class, new EventDeserializer());
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(module);

            JavaType customClassCollection = mapper.getTypeFactory().constructCollectionType(List.class, Event.class);
            List<Event> webEvents = mapper.readValue(bodyString, customClassCollection);

            List<AnnualEvent> annualEvents = new ArrayList<>();
            List<SingularEvent> singularEvents = new ArrayList<>();
            for (Event event : webEvents) {
                switch (event) {
                    case AnnualEvent annualEvent -> annualEvents.add(annualEvent);
                    case SingularEvent singularEvent -> singularEvents.add(singularEvent);
                    default -> {
                    }
                }
            }

            events.addAll(annualEvents);
            events.addAll(singularEvents);
        } catch (JsonProcessingException ex) {
            System.err.println("Error processing JSON: " + ex.toString());
            ex.printStackTrace();
        }
    }

    @Override
    public List<Event> getEvents() {
        if (this.events.isEmpty()) {
            fetchEvents();
        }
        return this.events;
    }

    @Override
    public List<Event> getEventsOfCategory(Category category) {
        List<Event> eventsOfCategory = new ArrayList<Event>();
        for (Event event : this.events) {
            if (event.getCategory().equals(category)) {
                eventsOfCategory.add(event);
            }
        }
        return eventsOfCategory;
    }

    @Override
    public List<Event> getEventsOfDate(MonthDay monthDay) {
        if (this.events.isEmpty()) {
            fetchEvents();
        }

        List<Event> eventsOfDate = new ArrayList<>();
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
                        "Operation not supported for " + event.getClass().getName());
            }

            if (monthDay.getMonth() == eventMonth && monthDay.getDayOfMonth() == eventDay) {
                eventsOfDate.add(event);
            }
        }
        return eventsOfDate;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    public URI getServerUri() {
        return serverUri;
    }

    public void setServerUri(URI serverUri) {
        this.serverUri = serverUri;
    }

    public List<Event> getFilteredEvents(EventFilter filter) {
    if (this.events.isEmpty()) {
        fetchEvents();
    }
    return this.events.stream()
        .filter(filter::accepts)
        .sorted((e1, e2) -> {
            if (e1 instanceof SingularEvent && e2 instanceof SingularEvent) {
                return ((SingularEvent) e1).getDate().compareTo(((SingularEvent) e2).getDate());
            } else if (e1 instanceof AnnualEvent && e2 instanceof AnnualEvent) {
                return e1.getMonthDay().compareTo(e2.getMonthDay());
            }
            return 0;
        })
        .toList();
    }

    @Override
    public String getFilename() {
        return this.serverUri.toString();
    }

    @Override
    public void addEvent(Event event, String fileName) {
        // This provider does not support adding events
        // to the text file. The event is just added to the
        // in-memory list.
    throw new UnsupportedOperationException("Adding events is not supported by WebEventProvider.");
}

    @Override
    public boolean isAddSupported() {
        return false;
    }
}

