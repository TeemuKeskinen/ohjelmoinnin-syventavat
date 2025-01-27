import java.time.LocalDate;
import java.util.Arrays;

class MacOSRelease {
    public static void main(String[] args) {
        Event[] events = {
            new Event(LocalDate.parse("2024-09-16"), "macOS 15 Sequoia released", new Category("apple", "macos")),
            new Event(LocalDate.parse("2023-09-26"), "macOS 14 Sonoma released", new Category("apple", "macos")),
            new Event(LocalDate.parse("2022-10-24"), "macOS 13 Ventura released", new Category("apple", "macos")),
            new Event(LocalDate.parse("2021-10-25"), "macOS 12 Monterey released", new Category("apple", "macos")),
            new Event(LocalDate.parse("2020-11-12"), "macOS 11 Big Sur released", new Category("apple", "macos"))
        };

        for (Event event : events) {
            String[] descriptionParts = event.getDescription().split(" ");
            String version = descriptionParts[1];
            String name = descriptionParts[2];

            String dayOfWeek = event.getDate().getDayOfWeek().toString().toLowerCase();
            dayOfWeek = dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1);

            System.out.printf("macOS %s %s was released on a %s\n", version, name, dayOfWeek);
        }

        String[] macNames = new String[events.length];
        for (int i = 0; i < events.length; i++) {
            macNames[i] = events[i].getDescription().split(" ")[2];
        }
        Arrays.sort(macNames);

        System.out.println("\nIn alphabetical order: " + Arrays.toString(macNames));
    }
}
