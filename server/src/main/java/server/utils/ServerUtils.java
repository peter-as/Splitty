package server.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.databind.JsonNode;
import commons.Event;
import commons.EventDTO;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    /**
     * Get events method
     * @return the events
     */
    public List<Event> getEvents() {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("events")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    /**
     * Post event method
     * Posts event to server utils
     * @param ev Event to post
     */
    public void postEvent(Event ev) {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("events")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(ev, APPLICATION_JSON), Event.class);
    }

    /**
     * Delete event method
     * @param e the event to be deleted
     */
    public void deleteEvent(Event e) {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("events/{eid}")
                .resolveTemplate("eid", e.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    /**
     * Call upon the server to get exchange rates for a day
     * @param date the date of the day
     * @return JsonNode made of the exchange rates
     */
    public JsonNode getRates(String date) {
        try {
            Response response = ClientBuilder.newClient(new ClientConfig())
                    .target(SERVER)
                    .path("exchange")
                    .path(date)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(new GenericType<>() {
                });
            } else {
                System.err.println("Failed getting events");
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private StompSession session;

    /**
     * Connect
     * @param url url for websocket
     * @return the connection
     */
    public StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        throw new IllegalStateException();
    }

    /**
     * register for messages
     * @param dest Destination
     * @param consumer consumer
     */
    public void registerForMessages(String dest, Consumer<EventDTO> consumer) {
        session = connect("ws://localhost:8080/websocket");
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return EventDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((EventDTO) payload);
            }
        });
    }

    /**
     * Send websocket message
     * @param destination destination
     * @param o message
     */
    public void send(String destination, Object o) {
        session.send(destination, o);
    }
}