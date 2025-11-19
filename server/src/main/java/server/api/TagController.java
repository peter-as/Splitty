package server.api;

import commons.Event;
import commons.Expense;
import commons.Tag;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.TagRepository;
import server.utils.DatabaseUtils;

@RestController
@RequestMapping("events/{eid}")
public class TagController {

    @Autowired
    private final DatabaseUtils du;

    /**
     * Constructor for the TagController.
     *
     * @param du the interface for the database.
     */
    public TagController(DatabaseUtils du) {
        this.du = du;
    }

    /**
     * Gets all the tags in one event
     *
     * @param eid event id the id of the event
     * @return a list of all expenses in the event
     */
    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> getAllTags(@PathVariable Long eid) {
        if (eid == null || !du.eventExistsById(eid)) {
            return ResponseEntity.notFound().build();
        }
        Event event = du.eventFindById(eid).orElse(null);
        List<Tag> tags = new ArrayList<>();
        if (event != null) {
            tags = event.getTagList();
        }
        return ResponseEntity.ok(tags);
    }

    /**
     * Get for a specific tag from the database.
     *
     * @param id the tag id of the tag to be retrieved.
     * @return the retrieved tag.
     */
    @GetMapping("/tags/{id}")
    @ResponseBody
    public Tag getTag(@PathVariable Long id) {
        return du.tagFindById(id).orElse(null);
    }

    /**
     * Post for a new tag in the database.
     *
     * @param tag the new tag to be added to the database.
     * @return the tag that was added to the database.
     */
    @PostMapping("/tags")
    @ResponseBody
    public ResponseEntity<Tag> postTag(
            @PathVariable Long eid,
            @RequestBody Tag tag) {
        if (eid == null || !du.eventExistsById(eid) || tag == null) {
            return ResponseEntity.notFound().build();
        }

        Event event = du.eventFindById(eid).orElse(null);
        tag.setTagEvent(event);
        if (event.getTagList() == null) {
            event.setTagList(new ArrayList<>());
        }

        tag = du.tagCreate(tag);
        event.getTagList().add(tag);
        du.eventSave(event);

        return ResponseEntity.ok(tag);
    }

    /**
     * Put for a specific tag in the database.
     *
     * @param id  the tag id of the tag to be updated.
     * @param tag the tag to update the old tag with.
     * @return the updated tag.
     */
    @PutMapping("/tags/{id}")
    @ResponseBody
    public ResponseEntity<Tag> putTag(@PathVariable Long id,
                                      @PathVariable Long eid,
                                      @RequestBody Tag tag) {
        if (id == null || eid == null || !du.eventExistsById(eid)
                || !du.tagExistsById(id) || tag == null) {
            return ResponseEntity.notFound().build();
        }
        Tag t = du.tagFindById(id).get();
        t.setColor(tag.getColor());
        t.setName(tag.getName());
        du.eventSave(du.eventFindById(eid).get());
        return ResponseEntity.ok(du.tagSave(t));

    }


    /**
     * Delete for a specific tag in the database.
     *
     * @param id the tag id of the tag to delete.
     * @return the tag that has been deleted.
     */
    @DeleteMapping("/tags/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteTag(@PathVariable Long id,
                                            @PathVariable Long eid) {

        if (id == null || eid == null || !du.eventExistsById(eid) || !du.tagExistsById(id)) {
            return ResponseEntity.notFound().build();
        }
        du.tagDeleteById(id);
        du.eventSave(du.eventFindById(eid).get());
        return ResponseEntity.ok("Tag with id " + id + " was deleted.");
    }
}