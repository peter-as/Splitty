package server.api;

import commons.*;
import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.database.EventRepository;
import server.utils.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TagControllerTest {

    EventRepositoryImpl eventRepository = new EventRepositoryImpl();
    ExpenseRepositoryImpl expenseRepository = new ExpenseRepositoryImpl();
    ParticipantRepositoryImpl participantRepository = new ParticipantRepositoryImpl();
    TagRepositoryImpl tagRepository = new TagRepositoryImpl();
    DatabaseUtils du = new DatabaseUtils(eventRepository, expenseRepository, participantRepository, tagRepository);

    private TagController sut;
    private Tag tag1;
    private Tag tag2;
    private Event event;

    @BeforeEach
    public void setup() {
        eventRepository = new EventRepositoryImpl();
        expenseRepository = new ExpenseRepositoryImpl();
        participantRepository = new ParticipantRepositoryImpl();
        tagRepository = new TagRepositoryImpl();
        du = new DatabaseUtils(eventRepository, expenseRepository, participantRepository, tagRepository);
        sut = new TagController(du);
        Color color = new Color(1,1,1);
        tag1= new Tag(color, "Food");
        tag1.setId((long)0);
        tag2= new Tag(color, "Present");
        tag2.setId((long) 1);

        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);

        event = new Event(null, null, "name", null, null,tags);
        event.setId((long) 1);
        du.tagSave(tag1);
        du.tagSave(tag2);
        du.eventSave(event);
    }

    /**
     * Test to test the get method from the tag controller.
     */
    @Test
    public void getByIdTest() {
        du.tagSave(tag1);
        Tag expected = tag1;
        assertEquals(expected, sut.getTag(tag1.getId()));
    }

    /**
     * Test to test the post method of the TagController
     */
    @Test
    public void postTest() {
        sut.postTag(event.getId(), new Tag(new Color(0,0,0,0), "asdf"));
        assertEquals(new Tag(new Color(0,0,0,0), "asdf"), tagRepository.tags.get(2));
    }

    /**
     * Test to test the put method of the tag controller
     */
    @Test
    public void putTest() {
        sut.postTag(event.getId(), tag1);
        sut.postTag(event.getId(), tag2);
        sut.putTag(tag1.getId(), event.getId(), tag2);
        assertEquals(sut.getAllTags(event.getId()).getBody().get(1).getName(), sut.getAllTags(event.getId()).getBody().get(2).getName());
        assertEquals(sut.getAllTags(event.getId()).getBody().get(1).getColor(), sut.getAllTags(event.getId()).getBody().get(2).getColor());
    }

    /**
     * Test to test the delete tag method of the tag controller
     */
    @Test
    public void deleteTest() {
        event.getTagList().remove(tag1);
        sut.deleteTag(tag1.getId(), event.getId());
        assertTrue(tagRepository.tags.size() == 1);
    }
    @Test
    public void getAllTags() {
        List<Tag> tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
        assertEquals(tags, sut.getAllTags((long)1).getBody());
    }

    @Test
    public void cornerCases() {
        assertEquals(sut.getAllTags(null), ResponseEntity.notFound().build());
        assertEquals(sut.postTag(null, null), ResponseEntity.notFound().build());
        assertEquals(sut.putTag(null, null, null), ResponseEntity.notFound().build());
        assertEquals(sut.deleteTag( null, null), ResponseEntity.notFound().build());
        event.setTagList(null);
        assertEquals(sut.postTag((long)1, new Tag(new Color(0,0,0,0), "asdf")).getBody(), new Tag(new Color(0,0,0,0), "asdf"));
    }
}
