package factory;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;

import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SWApiSocketFactory {

    private static final Logger log = LoggerFactory.getLogger(SWApiSocketFactory.class);
    private static final URI uri = URI.create("ws://localhost:3000");

    private SWApiSocketFactory() {}

    public static Socket createSocket() {
        log.info("Creating socket for " + uri.toString());
        final IO.Options options = IO.Options.builder()
                // ...
                .build();

        options.callFactory = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.SECONDS) // important for HTTP long-polling
                .build();

        Socket socket = IO.socket(uri, options);
        socket.on("connect", connection -> log.info("SWApi Socket connection successful."));
        socket.on("disconnect", connection -> log.info("SWApi Socket disconnected."));
        socket.on("error", error -> log.warning("SWApi Socket ERROR: " + Arrays.toString(error)));
        return socket;
    }

}
