package client;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public interface SocketClient {

    Emitter emit(String query);

    Emitter listen(Emitter.Listener fn);

    Socket disconnect();

    Socket getSocket();
}
