package client;

import factory.LoggerFactory;
import factory.SWApiSocketFactory;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;

import java.util.Collections;
import java.util.logging.Logger;

public class SWApiSearchClient implements SocketClient {

    private static final Logger log = LoggerFactory.getLogger(SWApiSearchClient.class);
    private final Socket socket;
    private static final String SEARCH_EVENT_NAME = "search";

    public SWApiSearchClient() {
        log.info("Creating SWApi Socket...");
        Socket socket = SWApiSocketFactory.createSocket();
        log.info("SWApi Socket created. Starting connection.");
        socket.connect();
        this.socket = socket;
    }

    public SWApiSearchClient(Socket socket) {
        if (!socket.connected()) {
            log.info("SWApi Socket provided without connection. Starting connection.");
            socket.connect();
            log.info("SWApi Socket connection successful.");
        }
        this.socket = socket;
    }

    /**
     * send a message to the server
     *
     * @param query
     * @return
     */
    public Emitter emit(String query) {
        return socket.emit(SEARCH_EVENT_NAME, new JSONObject(Collections.singletonMap("query", query)));
    }

    /**
     * receive a message from the server
     *
     * @param fn
     */
    public Emitter listen(Emitter.Listener fn) {
        return socket.on(SEARCH_EVENT_NAME, fn);
    }

    /**
     * disconnect from the service
     *
     * @return
     */
    public Socket disconnect() {
        return socket.disconnect();
    }

    public Socket getSocket() {
        return socket;
    }
}
