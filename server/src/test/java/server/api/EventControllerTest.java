package server.api;

import commons.Event;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.utils.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class EventControllerTest {
    EventRepositoryImpl eventRepository = new EventRepositoryImpl();
    ExpenseRepositoryImpl expenseRepository = new ExpenseRepositoryImpl();
    ParticipantRepositoryImpl participantRepository = new ParticipantRepositoryImpl();
    TagRepositoryImpl tagRepository = new TagRepositoryImpl();
    DatabaseUtils du = new DatabaseUtils(eventRepository, expenseRepository, participantRepository, tagRepository);
    EventController ec;
    Participant participant1;
    Participant participant2;
    Event event1;
    Event event2;
    Event event2SameID;
    List<Participant> participants1;
    List<Participant> participants2;

    @BeforeEach
    void setup() {
        eventRepository = new EventRepositoryImpl();
        expenseRepository = new ExpenseRepositoryImpl();
        participantRepository = new ParticipantRepositoryImpl();
        tagRepository = new TagRepositoryImpl();
        du = new DatabaseUtils(eventRepository, expenseRepository, participantRepository, tagRepository);
        ec = new EventController(du, null);
        Date date1 = new Date(2012, Calendar.JANUARY,12);
        Date date2 = new Date(2024,Calendar.MARCH,3);
        participant1 = new Participant("John", "j@gmail.com","IBAN000","BIC000");
        participant2 = new Participant("Emma", "e@gmail.com","IBAN000","BIC000");
        participants1 = new ArrayList<>();
        participants2 = new ArrayList<>();
        participants1.add(participant1);
        participants2.add(participant2);
        event1 = new Event(date1, date2, "TestEvent", participants1, new ArrayList<Expense>(),null);
        event1.setId(1L);
        event2 = new Event(date1, date2, "TestEvent2", participants2, new ArrayList<Expense>(),null);
        event2.setId(2L);
        event2SameID = new Event(date1, date2, "TestEvent2", participants2, new ArrayList<Expense>(),null);
        event2SameID.setId(1L);
    }

    @Test
    public void noEvent() {
        List<Event> le = new ArrayList<>();
        assertEquals(le,ec.events());
    }

    @Test
    public void oneEvent() {
        List<Event> le = new ArrayList<>();
        le.add(event1);
        ec.createEvent(event1);
        assertEquals(le,ec.events());
    }

    @Test
    public void moreEvents() {
        List<Event> le = new ArrayList<>();
        le.add(event1);
        ec.createEvent(event1);
        assertSame(le.size(),ec.events().size());
        assertEquals(le.get(0).getName(), ec.events().get(0).getName());
    }

    @Test
    public void getIdOneTest() {
        du.eventSave(event1);
        assertEquals(event1,ec.getEvent(1L).getBody());
    }

    @Test
    public void getIdTwoTest() {
        du.eventSave(event1);
        du.eventSave(event2);
        assertEquals(event2,ec.getEvent(2L).getBody());
    }

    @Test
    public void postTest() {
        ec.createEvent(event1);
        assertEquals(event1,ec.events().get(0));
    }
    @Test
    public void postMultipleTest() {
        ec.createEvent(event1);
        ec.createEvent(event2);
        List<Event> events = new ArrayList<>();
        events.add(event1);
        assertEquals(events,ec.events());
    }

    @Test
    public void putTest() {
        du.eventSave(event1);
        du.eventSave(event2);
        ec.updateEvent(event1.getId(),event2);
        assertEquals(ec.getEvent(event2.getId()).getBody().getName(),ec.getEvent(event1.getId()).getBody().getName());
        assertEquals(ec.getEvent(event2.getId()).getBody().getDateOfCreation(),ec.getEvent(event1.getId()).getBody().getDateOfCreation());
        assertEquals(ec.getEvent(event2.getId()).getBody().getPrtList(),ec.getEvent(event1.getId()).getBody().getPrtList());
        assertEquals(ec.getEvent(event2.getId()).getBody().getExpList(),ec.getEvent(event1.getId()).getBody().getExpList());
    }

    @Test
    public void deleteTest() {
        du.eventSave(event1);
        du.eventSave(event2);
        ec.deleteEvent(event1.getId());

        List<Event> expected = new ArrayList<>();
        expected.add(event2);

        assertEquals(expected, ec.events());
    }
    @Test
    public void cornerCases() {
        assertEquals(ResponseEntity.notFound().build(), ec.getEvent((long)-1));
        assertEquals(ResponseEntity.badRequest().build(),ec.deleteEvent((long)69));
        assertEquals(ResponseEntity.badRequest().build(), ec.updateEvent(null, null));
    }
}
