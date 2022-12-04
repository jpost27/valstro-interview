package processing;

import client.SocketClient;

import java.util.Queue;

public interface EventQueue<T> {

    void subscribeToClient(SocketClient client);

    boolean isSubscribedToClient(SocketClient client);

    Queue<T> getQueue();

}
