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


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.time.Month;


import tamk.ohsyte.datamodel.Category;
import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.datamodel.AnnualEvent;
import tamk.ohsyte.datamodel.SingularEvent;
import tamk.ohsyte.providers.web.EventDeserializer;



public class WebEventProvider implements EventProvider {
    private final List<Event> events;
    private URI serverUri;
    private String identifier;

    public WebEventProvider(URI serverUri) {
        this.serverUri = serverUri;
        this.events = new ArrayList<>();
        fetchEvents();
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
            if (status != 200) {
                System.err.printf("HTTP response: %d%n", status);
                System.err.println("Response body = " + bodyString);
            } else {
                System.out.println("Response headers: " + response.headers());
                System.out.println("Response body = " + bodyString);
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
                if (event instanceof AnnualEvent) {
                    annualEvents.add((AnnualEvent) event);
                } else if (event instanceof SingularEvent) {
                    singularEvents.add((SingularEvent) event);
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
        List<Event> eventsOfDate = new ArrayList<Event>();
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
                        "Operation not supported for " +
                        event.getClass().getName());
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
}
