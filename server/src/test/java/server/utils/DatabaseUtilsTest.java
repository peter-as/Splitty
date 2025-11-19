package server.utils;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import server.api.EventController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DatabaseUtilsTest {
    EventRepositoryImpl eventRepository = new EventRepositoryImpl();
    ExpenseRepositoryImpl expenseRepository = new ExpenseRepositoryImpl();
    ParticipantRepositoryImpl participantRepository = new ParticipantRepositoryImpl();
    TagRepositoryImpl tagRepository = new TagRepositoryImpl();
    DatabaseUtils du = new DatabaseUtils(eventRepository, expenseRepository, participantRepository, tagRepository);
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        eventRepository = new EventRepositoryImpl();
        expenseRepository = new ExpenseRepositoryImpl();
        participantRepository = new ParticipantRepositoryImpl();
        tagRepository = new TagRepositoryImpl();
        du = new DatabaseUtils(eventRepository, expenseRepository, participantRepository, tagRepository);
    }
    @Test
    void eventGetAll() {
        Event ev = new Event();
        ev.setName("test");
        du.eventCreate(ev);
        assertSame(du.eventGetAll().size(), 1);
        assertEquals(du.eventGetAll().get(0), ev);
    }

    @Test
    void eventExistsById() {
        Event ev = new Event();
        ev.setName("test");
        ev.setId((long)1);
        du.eventSave(ev);
        assertEquals(du.eventExistsById(null),false);
        assertEquals(du.eventExistsById((long)69), false);
        assertEquals(du.eventExistsById((long)1), true);
    }

    @Test
    void eventFindById() {
        Event ev = new Event();
        ev.setName("test");
        ev.setId((long)1);
        du.eventSave(ev);
        assertEquals(du.eventFindById(null), Optional.empty());
        assertEquals(du.eventFindById((long)69), Optional.empty());
        assertEquals(du.eventFindById((long)1), Optional.of(ev));
    }

    @Test
    void eventSave() {
        Participant p = new Participant();
        p.setName("asdf");
        p.setId((long)1);
        Expense ex = new Expense();
        ex.setName("asdf");
        ex.setId((long)1);
        Event ev = new Event();
        ev.setName("name");
        ev.setId((long)1);
        Tag t = new Tag();
        t.setName("asdf");
        ex.setParticipants(new ArrayList<>());
        ex.getParticipants().add(p);
        List<Participant> participants = new ArrayList<>();
        List<Expense> expenses = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();
        participants.add(p);
        expenses.add(ex);
        tags.add(t);
        ev.setExpList(expenses);
        ev.setPrtList(participants);
        ev.setTagList(tags);
        assertEquals(du.eventSave(null), null);
        du.eventSave(ev);
        assertEquals(eventRepository.events.get(0), ev);
        assertEquals(participantRepository.participants.get(0),p);
        assertEquals(expenseRepository.expenses.get(0),ex);
        assertEquals(tagRepository.tags.get(0),t);
    }

    @Test
    void eventDeleteById() {
        Event ev = new Event();
        ev.setName("test");
        ev.setId((long)3);
        du.eventSave(ev);
        du.eventDeleteById(null);
        assertSame(eventRepository.events.size(), 1);
        du.eventDeleteById((long)3);
        assertSame(eventRepository.events.size(), 0);
    }

    @Test
    void eventCreate() {
        assertEquals(du.eventCreate(null), null);
    }

    @Test
    void tagExistsById() {
        assertSame(false, du.tagExistsById(null));
        assertSame(false, du.tagExistsById((long)69));
    }

    @Test
    void tagFindById() {
        Tag t = new Tag();
        t.setId((long)3);
        t.setName("asdf");
        du.tagSave(t);
        assertEquals(Optional.empty(), du.tagFindById(null));
        assertEquals(Optional.empty(), du.tagFindById((long)234));
        assertEquals(Optional.of(t), du.tagFindById((long)3));
    }


    @Test
    void tagDeleteById() {
        Tag t = new Tag();
        t.setId((long)3);
        t.setName("asdf");
        du.tagSave(t);
        assertSame(1, tagRepository.tags.size());
        du.tagDeleteById(null);
        assertSame(1, tagRepository.tags.size());
        du.tagDeleteById((long)3);
        assertSame(0, tagRepository.tags.size());
    }

    @Test
    void tagCreate() {
        du.tagCreate(null);
        assertSame(0, tagRepository.tags.size());
        Tag t = new Tag();
        t.setName("asdf");
        t = du.tagCreate(t);
        assertSame(1, tagRepository.tags.size());
        assertEquals(t, tagRepository.tags.get(0));
    }

    @Test
    void expenseExistsById() {
        Expense ex = new Expense();
        ex.setId((long)324);
        du.expenseSave(ex);
        assertSame(du.expenseExistsById((long)3), false);
        assertSame(du.expenseExistsById(null), false);
    }

    @Test
    void expenseFindById() {
        Expense ex = new Expense();
        ex.setId((long)324);
        du.expenseSave(ex);
        assertEquals(du.expenseFindById(null), Optional.empty());
        assertEquals(du.expenseFindById((long)324), Optional.of(ex));
    }

    @Test
    void expenseSave() {
        du.expenseSave(null);
        assertSame(expenseRepository.expenses.size(), 0);
    }

    @Test
    void expenseDeleteById() {
        Expense ex = new Expense();
        ex.setId((long)324);
        du.expenseSave(ex);
        du.expenseDeleteById(null);
        assertSame(expenseRepository.expenses.size(), 1);
        du.expenseDeleteById((long)324);
        assertSame(expenseRepository.expenses.size(), 0);
    }

    @Test
    void expenseCreate() {
        Expense exp = new Expense();
        exp.setName("asdf");
        exp.setParticipants(new ArrayList<>());
        assertEquals(du.expenseCreate(null), null);
        assertEquals(du.expenseCreate(exp), exp);
    }

    @Test
    void participantExistsById() {
        assertSame(du.participantExistsById(null), false);
        assertSame(du.participantExistsById((long)3), false);
    }

    @Test
    void participantFindById() {
        assertEquals(Optional.empty(), du.participantFindById(null));
        Participant p = new Participant();
        p.setId((long)3);
        du.participantSave(p);
        assertEquals(Optional.of(p), du.participantFindById((long)3));
    }

    @Test
    void participantDeleteById() {
        Participant p = new Participant();
        p.setId((long)3);
        du.participantSave(p);
        du.participantDeleteById(null);
        assertSame(participantRepository.participants.size(), 1);
        du.participantDeleteById((long)3);
        assertSame(participantRepository.participants.size(), 0);
    }

    @Test
    void participantCreate() {
        assertEquals(null, du.participantCreate(null));
        Participant p = new Participant();
        p.setId((long)3);
        Participant t = new Participant();
        assertEquals(t, du.participantCreate(p));
    }
    @Mock
    EventController ecc;
    @Test
    void copyTest() {
        Event ev = new Event();
        Participant p = new Participant();p.setId((long)3);
        Tag t = new Tag();t.setId((long)3);
        Expense e = new Expense();e.setId((long)3);
        e.setWhoPaid(p);
        e.setTag(t);
        e.setParticipants(new ArrayList<>());
        ev.setPrtList(List.of(p));
        ev.setExpList(List.of(e));
        ev.setTagList(List.of(t));
        du.setEc(ecc);
        du.eventClone(ev);
        verify(ecc, times(4)).handleUpdate(Mockito.any());
    }
}