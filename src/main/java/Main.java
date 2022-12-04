import client.SWApiSearchClient;
import client.SocketClient;
import factory.LoggerFactory;
import model.StarWarsSearchResponse;
import processing.EventProcessor;
import processing.EventQueue;
import processing.SearchEventProcessor;
import processing.SearchEventQueue;

import java.util.Scanner;
import java.util.logging.Logger;

public class Main {

    private static final EventQueue<StarWarsSearchResponse> searchEventQueue = new SearchEventQueue();
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SocketClient client = new SWApiSearchClient();
        searchEventQueue.subscribeToClient(client);
        EventProcessor searchEventProcessor = new SearchEventProcessor(client);

        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            System.out.print("What character would you like to search for? ");
            input = scanner.nextLine();
            log.info("User input: '" + input + "'.");
            if (input.equals("exit")) {
                System.out.println("Exiting application...");
                break;
            }
            searchEventProcessor.process(input);
        }
        scanner.close();
        client.disconnect();
    }
}
