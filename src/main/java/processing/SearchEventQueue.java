package processing;

import client.SocketClient;
import io.socket.emitter.Emitter;
import model.StarWarsSearchResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Search event queue funnels all StarWarsSearchResponses from one or more clients into one queue for simpler event processing
 */
public class SearchEventQueue implements EventQueue<StarWarsSearchResponse> {

    private static final Queue<StarWarsSearchResponse> starWarsResponseQueue = new ConcurrentLinkedQueue<>();
    private static final Map<SocketClient, Emitter> subscriptions = new HashMap<>();

    public SearchEventQueue() {
    }

    /**
     * create subscription to a client so the queue will start pulling messages from it
     * @param client
     */
    public void subscribeToClient(SocketClient client) {
        if (!isSubscribedToClient(client)) {
            subscriptions.put(client, (client.listen(response -> Arrays.stream(response).forEach(res -> {
                try {
                    getQueue().add(new StarWarsSearchResponse((JSONObject) res));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }))));
        }
    }

    /**
     *
     * @param client
     * @return boolean indicating whether the eventQueue is subscribed to the client
     */
    public boolean isSubscribedToClient(SocketClient client) {
        return subscriptions.containsKey(client);
    }

    /**
     *
     * @return the queue of which events are added to
     */
    public Queue<StarWarsSearchResponse> getQueue() {
        return starWarsResponseQueue;
    }

}
