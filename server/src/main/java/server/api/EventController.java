package server.api;

import commons.Event;
import commons.EventDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.utils.DatabaseUtils;


@RestController
@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private final DatabaseUtils du;
    @Autowired
    private final SimpMessagingTemplate simp;

    /**
     * Constructor for EventController
     * @param du The repository to use
     */
    @Autowired
    public EventController(DatabaseUtils du, SimpMessagingTemplate simp) {
        this.du = du;
        this.simp = simp;
        du.setEc(this);
    }

    /**
     * Gets all the events
     * @return A list of all the events
     */
    @GetMapping("")
    public List<Event> events() {
        return du.eventGetAll();
    }

    /**
     * Gets an event with a specific id
     * @param id The id of the event we are looking for
     * @return The event with that id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable("id") Long id) {
        if (id < 0 || !du.eventExistsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(du.eventFindById(id).orElse(null));
    }

    /**
     * Websocket receiver
     * @param event Event to add/delete
     * @return The event to be propagated to all subscribers
     */
    @MessageMapping("/events") // /app/events
    @SendTo("/topic/events")
    public EventDTO addMessage(EventDTO event) {
        System.out.println("Wow");
        if (event.isDeleted()) {
            du.eventDeleteById(event.getEvent().getId());
        } else {
            du.eventClone(event.getEvent());
        }
        return event;
    }

    /**
     * Sends update to all subscribers
     * @param ev event
     */
    public void handleUpdate(Event ev) {
        if (simp != null) {
            simp.convertAndSend("/topic/events", new EventDTO(false, ev));
        }
    }

    public static Map<Long, Map<Object, Consumer<Event>>> listeners = new HashMap<>();

    /**
     * Gets updated event
     * @return The updated event
     */
    @GetMapping("/updates/{id}")
    public DeferredResult<ResponseEntity<Event>> getUpdates(@PathVariable("id") Long id) {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<Event>>(50000L, noContent);
        var key = new Object();
        if (!listeners.containsKey(id)) {
            listeners.put(id, new HashMap<>());
        }
        listeners.get(id).put(key, e -> res.setResult(ResponseEntity.ok(e)));
        res.onCompletion(() -> {
            listeners.get(id).remove(key);
            if (listeners.get(id).isEmpty()) {
                listeners.remove(id);
            }
        });
        return res;
    }

    /**
     * Creates a new event
     * @param event the event to be stored in the repository
     * @return the event created
     */
    @PostMapping("")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(du.eventCreate(event));
    }

    /**
     * Updates an event
     * @param id the id of the event to be updated
     * @param event the data to update it to
     * @return the updated event
     */
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        Optional<Event> e = du.eventFindById(id);
        if (e.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        event.setId(id);
        return ResponseEntity.ok(du.eventSave(event));
    }

    /**
     * Delete for a specific event in the database.
     * @param id the event id of the event to delete.
     * @return the event that has been deleted.
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteEvent(@PathVariable("id") Long id) {
        if (du.eventExistsById(id)) {
            du.eventDeleteById(id);
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
