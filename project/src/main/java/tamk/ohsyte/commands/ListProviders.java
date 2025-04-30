package tamk.ohsyte.commands;

import java.util.List;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import tamk.ohsyte.EventManager;

@Command(name = "listproviders")
public class ListProviders implements Runnable {
    @Option(names = {"-a", "-add"}, description = "List only providers that support adding events")
    boolean supported;

    @Override
    public void run() {
        //System.out.println("Listing event provider IDs");
        EventManager manager = EventManager.getInstance();
        if (supported) {
            System.out.println("Listing event provider IDs that support adding events");
            List<String> providerIds = manager.getSupportedEventProviderIdentifiers();
            for (String id : providerIds) {
                System.out.println(id);
            }
            return;
        }

        List<String> providerIds = manager.getEventProviderIdentifiers();
        for (String id : providerIds) {
            System.out.println(id);
        }
    }
}