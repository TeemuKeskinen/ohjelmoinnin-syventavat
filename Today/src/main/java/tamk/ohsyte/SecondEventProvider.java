package tamk.ohsyte;

import java.time.LocalDate;

public class SecondEventProvider extends FirstEventProvider {
    public SecondEventProvider(String identifier) {
        // Call the superclass constructor to set the identifier:
        super(identifier);

        this.category = new Category("test", "second");
        this.event = new Event(LocalDate.now(),
                "Event returned by SecondEventProvider",
                this.category);
    }

    // No need to implement the getIdentifier method in this subclass,
    // since it would be identical.
}