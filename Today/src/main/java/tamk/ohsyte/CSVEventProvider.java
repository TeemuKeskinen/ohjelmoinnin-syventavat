package tamk.ohsyte;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVEventProvider implements EventProvider {
    private List<Event> events;

    public CSVEventProvider(String fileName) {
        this.events = new ArrayList<>();
        Path path = Paths.get(fileName);
        try {
            CSVParser parser = CSVParser.parse(path, java.nio.charset.StandardCharsets.UTF_8, CSVFormat.DEFAULT);
            for (CSVRecord record : parser) {
                this.events.add(makeEvent(record));
            }
            System.out.printf("Read %d events from CSV file%n", this.events.size());
        } catch (IOException ioe) {
            System.err.println("File '" + fileName + "' not found");
        }
    }

    @Override
    public List<Event> getEvents() {
        return this.events;
    }

    @Override
    public List<Event> getEventsOfCategory(Category category) {
        List<Event> result = new ArrayList<>();
        for (Event event : this.events) {
            if (event.getCategory().equals(category)) {
                result.add(event);
            }
        }
        return result;
    }

    @Override
    public List<Event> getEventsOfDate(MonthDay monthDay) {
        List<Event> result = new ArrayList<>();
        for (Event event : this.events) {
            final Month eventMonth = event.getDate().getMonth();
            final int eventDay = event.getDate().getDayOfMonth();
            if (monthDay.getMonth() == eventMonth && monthDay.getDayOfMonth() == eventDay) {
                result.add(event);
            }
        }
        return result;
    }

    private Event makeEvent(CSVRecord record) {
        LocalDate date = LocalDate.parse(record.get(0));
        String description = record.get(1);
        String categoryString = record.get(2);
        String[] categoryParts = categoryString.split("/");
        String primary = categoryParts[0];
        String secondary = categoryParts.length == 2 ? categoryParts[1] : null;
        Category category = new Category(primary, secondary);
        return new Event(date, description, category);
    }
}
