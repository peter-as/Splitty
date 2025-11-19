package server.api;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.utils.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseControllerTest {

    EventRepositoryImpl eventRepository = new EventRepositoryImpl();
    ExpenseRepositoryImpl expenseRepository = new ExpenseRepositoryImpl();
    ParticipantRepositoryImpl participantRepository = new ParticipantRepositoryImpl();
    TagRepositoryImpl tagRepository = new TagRepositoryImpl();
    DatabaseUtils du = new DatabaseUtils(eventRepository, expenseRepository, participantRepository, tagRepository);

    private static ExpenseController sut;
    private static ParticipantController psut;
    private static Expense expense1;
    private static Expense expense2;
    private static Expense expense3;
    private static Expense expense4;
    private static Event event1;
    private static Event event2;
    private static Participant participant1;
    private static Participant participant2;

    @BeforeEach
    public void setup() {
        eventRepository = new EventRepositoryImpl();
        expenseRepository = new ExpenseRepositoryImpl();
        participantRepository = new ParticipantRepositoryImpl();
        tagRepository = new TagRepositoryImpl();
        du = new DatabaseUtils(eventRepository, expenseRepository, participantRepository, tagRepository);
        sut = new ExpenseController(du);
        psut = new ParticipantController(du);

        Date date1 = new Date(2024, Calendar.JANUARY,13);
        Date date2 = new Date(2024, Calendar.JANUARY,21);

        participant1 = new Participant("John", "j@gmail.com","IBAN000","BIC000");
        participant2 = new Participant("Emma", "e@gmail.com","IBAN000","BIC000");
        participant1.setId((long)1);
        participant2.setId((long)2);

        List<Participant> participants = new ArrayList<>();
        participants.add(participant1);
        participants.add(participant2);

        Color color = new Color(1,1,1);
        Tag tag = new Tag(color,"food");

        expense1 = new Expense("e1",date1, tag, participant1,400,"EUR",participants);
        expense2 = new Expense("e2",date1, tag, participant2, 500, "EUR", participants);
        expense1.setId((long)1);
        expense2.setId((long)2);
        expense3 = new Expense("e3",date1, tag, participant1, 400, "EUR", null);
        expense3.setId((long)3);
        expense4 = new Expense("e4",date1, tag, participant1, 400, "EUR", participants);
        expense4.setId((long)4);

        List<Expense> expenses = new ArrayList<>();
        expenses.add(expense1);
        expenses.add(expense4);

        event1 = new Event(date1,date2,"Event1",participants,expenses,null);
        event2 = new Event(date1,date2,"Event1",participants,expenses,null);

        List<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        event1.setId((long)1);
        event2.setId((long)2);

        du.participantSave(participant1);
        du.participantSave(participant2);
        du.expenseSave(expense1);
        du.expenseSave(expense2);
        du.expenseSave(expense3);
        du.expenseSave(expense4);
        du.eventSave(event1);
        du.eventSave(event2);
    }

    @Test
    void getAllExpenses() {

        List<Expense> expected = new ArrayList<>();
        expected.add(expense1);
        expected.add(expense4);
        assertEquals(ResponseEntity.status(HttpStatus.OK).body(expected),sut.getAllExpenses(event1.getId()));
    }

    @Test
    void getExpense() {
        assertEquals(ResponseEntity.status(HttpStatus.OK).body(expense1),sut.getExpense(expense1.getId()));
    }

    @Test
    void createExpense() {
        Expense exp = new Expense();
        assertEquals(null, sut.createExpense(event1.getId(),exp).getBody());
    }

    @Test
    void updateExpense() {

        sut.createExpense(event1.getId(),expense4);
        expense4.setId((long)4);
        assertEquals(-200, psut.getParticipantById(participant1.getId()).getBody().getNetDebt());

        sut.updateExpense(expense4.getId(),event1.getId(), expense2);
        expense2.setId((long)2);
        assertEquals(expense2,sut.getExpense(expense2.getId()).getBody());

        assertEquals(0, psut.getParticipantById(participant1.getId()).getBody().getNetDebt());
        assertEquals(0, psut.getParticipantById(participant2.getId()).getBody().getNetDebt());
    }

    @Test
    void deleteExpense() {
        event1.getExpList().clear();
        event1.getExpList().add(expense4);
        participant1.setNetDebt(-200);
        participant2.setNetDebt(200);
        sut.deleteExpense(event1.getId(), (long)4);
        assertEquals(0, psut.getParticipantById(participant1.getId()).getBody().getNetDebt());
        assertEquals(0, psut.getParticipantById(participant2.getId()).getBody().getNetDebt());

        sut.createExpense(event1.getId(), expense3);
        sut.createExpense(event1.getId(), expense2);
        sut.deleteExpense(event1.getId(), expense3.getId());
        assertEquals(0, psut.getParticipantById(participant1.getId()).getBody().getNetDebt());
        assertEquals(0, psut.getParticipantById(participant2.getId()).getBody().getNetDebt());

       assertEquals(ResponseEntity.notFound().build(), sut.getExpense(expense3.getId()));
    }
}