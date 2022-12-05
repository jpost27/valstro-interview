package processing;

import client.SocketClient;
import factory.LoggerFactory;
import io.socket.emitter.Emitter;
import model.StarWarsSearchResponse;

import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;
import java.util.logging.Logger;

public class SearchEventProcessor implements EventProcessor {

    private static final Logger log = LoggerFactory.getLogger(SearchEventProcessor.class);
    private static final Queue<StarWarsSearchResponse> starWarsResponseQueue = new SearchEventQueue().getQueue();
    private final SocketClient client;
    private static Emitter emitter;

    public SearchEventProcessor(SocketClient client) {
        this.client = client;
        EventQueue<StarWarsSearchResponse> eventQueue = new SearchEventQueue();
        if (!eventQueue.isSubscribedToClient(client)) {
            log.warning("Processor initialized without SearchEventQueue subscribing to events.");
        }
    }

    /**
     * Processes user input. prints all characters from SWAPI that contain the user input
     * @param input
     */
    public void process(String input) {
        if (!starWarsResponseQueue.isEmpty()) {
            starWarsResponseQueue.clear();
        }
        emitter = client.emit(input);
        boolean continueWaiting = true;
        while (continueWaiting) {
            if (awaitResponse()) {
                while (!starWarsResponseQueue.isEmpty()) {
                    StarWarsSearchResponse response = starWarsResponseQueue.poll();
                    if (response.getError() == null) {
                        System.out.println("(" + response.getPage() + '/' + response.getResultCount() + ") " + response.getName() + " - " + Arrays.toString(response.getFilms()));
                    } else {
                        System.out.println("Error response received: " + response.getError());
                    }
                    if (Objects.equals(response.getPage(), response.getResultCount())) {
                        continueWaiting = false;
                    }
                }
            } else {
                System.out.println("Error: socket is not connected.");
                continueWaiting = false;
            }
        }
    }

    /**
     * continues waiting for the queue to be filled as long as the socket is connected
     * @return boolean: true if processing should continue or false if the connection has disconnected
     */
    private boolean awaitResponse() {
        while (starWarsResponseQueue.isEmpty()) {
            if (!client.getSocket().connected()) {
                return false;
            }
        }
        return true;
    }

}
