package processing;

import client.SocketClient;
import factory.LoggerFactory;
import model.StarWarsSearchResponse;

import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;
import java.util.logging.Logger;

public class SearchEventProcessor implements EventProcessor {

    private static final Logger log = LoggerFactory.getLogger(SearchEventProcessor.class);
    private static final Queue<StarWarsSearchResponse> starWarsResponseQueue = new SearchEventQueue().getQueue();
    private final SocketClient client;

    public SearchEventProcessor(SocketClient client) {
        this.client = client;
        EventQueue<StarWarsSearchResponse> eventQueue = new SearchEventQueue();
        if (!eventQueue.isSubscribedToClient(client)) {
            log.warning("Processor initialized without SearchEventQueue subscribing to events.");
        }
    }

    public void process(String input) {
        client.emit(input);
        boolean continueWaiting = true;
        while (continueWaiting) {
            awaitResponse();
            while (!starWarsResponseQueue.isEmpty()) {
                StarWarsSearchResponse response = starWarsResponseQueue.poll();
                if (response.getError() == null) {
                    System.out.println('(' + response.getPage() + '/' + response.getResultCount() + ") " + response.getName() + " - " + Arrays.toString(response.getFilms()));
                } else {
                    System.out.println("Error response received: " + response.getError());
                }
                if (Objects.equals(response.getPage(), response.getResultCount())) {
                    continueWaiting = false;
                }
            }
        }
    }

    private static void awaitResponse() {
        while (starWarsResponseQueue.isEmpty()) { }
    }

}
