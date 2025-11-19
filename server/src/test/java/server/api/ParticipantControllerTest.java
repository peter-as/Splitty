package server.api;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import jakarta.transaction.Transactional;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParticipantControllerTest {

    EventRepositoryImpl eventRepository = new EventRepositoryImpl();
    ExpenseRepositoryImpl expenseRepository = new ExpenseRepositoryImpl();
    ParticipantRepositoryImpl participantRepository = new ParticipantRepositoryImpl();
    TagRepositoryImpl tagRepository = new TagRepositoryImpl();
    DatabaseUtils du = new DatabaseUtils(eventRepository, expenseRepository, participantRepository, tagRepository);
    private static ParticipantController sut;
        private static Participant participant1;
        private static Participant participant2;
        private static Participant participant3;

        private static Event event1;
        private static Event event2;

        @BeforeEach
        public void setup() {
            eventRepository = new EventRepositoryImpl();
            expenseRepository = new ExpenseRepositoryImpl();
            participantRepository = new ParticipantRepositoryImpl();
            tagRepository = new TagRepositoryImpl();
            du = new DatabaseUtils(eventRepository, expenseRepository, participantRepository, tagRepository);
            sut = new ParticipantController(du);
            Date date1 = new Date(2024, Calendar.JANUARY,13);
            Date date2 = new Date(2024, Calendar.JANUARY,21);

            participant1 = new Participant("John", "j@gmail.com","IBAN000","BIC000");
            participant2 = new Participant("Emma", "e@gmail.com","IBAN000","BIC000");
            participant3 = new Participant("Alice", "a@gmail.com","IBAN000","BIC000");
            participant1.setId((long)1);
            participant2.setId((long)2);
            participant3.setId((long)3);

            List<Participant> participants = new ArrayList<>();
            participants.add(participant1);
            participants.add(participant2);

            Color color = new Color(1,1,1);
            Tag tag = new Tag(color,"food");

            Expense expense = new Expense("e1",date1,tag,participant1,400,"EUR",participants);
            List<Expense> expenses = new ArrayList<>();
            expenses.add(expense);

            event1 = new Event(date1,date2,"Event1",participants,expenses,null);
            event2 = new Event(date1,date2,"Event2",null,null,null);

            List<Event> events = new ArrayList<>();
            events.add(event1);
            events.add(event2);
            event1.setId((long)1);
            event2.setId((long)2);
            du.participantSave(participant1);
            du.participantSave(participant2);
            du.participantSave(participant3);
            du.eventSave(event1);
            du.eventSave(event2);
        }

        @Test
        public void getAllTest(){

            List<Participant> expected = new ArrayList<>();
            expected.add(participant1);
            expected.add(participant2);

            assertEquals(ResponseEntity.status(HttpStatus.OK).body(expected),sut.getAllParticipantsByEventId(event1.getId()));
        }
        @Test
        public void getOneByIdTest(){
            assertEquals(ResponseEntity.status(HttpStatus.OK).body(participant1),sut.getParticipantById(participant1.getId()));
        }

        @Test
        public void createNameExistsTest(){
            assertEquals(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Warning, name already exists!"),sut.createParticipant(event1.getId(),participant1));

        }

        @Test
        public void createTest2(){

        assertEquals(ResponseEntity.status(HttpStatus.CREATED).body("Participant with name Alice was added!"), sut.createParticipant(event1.getId(),participant3));

        }

        @Test
        public void updateTest(){
            sut.createParticipant(event1.getId(),participant1);
            sut.createParticipant(event1.getId(),participant2);
            sut.updateParticipant(participant1.getId(),participant2);
            assertEquals(sut.getParticipantById(participant2.getId()).getBody().getName(),sut.getParticipantById(participant1.getId()).getBody().getName());
            assertEquals(sut.getParticipantById(participant2.getId()).getBody().getBic(),sut.getParticipantById(participant1.getId()).getBody().getBic());
            assertEquals(sut.getParticipantById(participant2.getId()).getBody().getIban(),sut.getParticipantById(participant1.getId()).getBody().getIban());
            assertEquals(sut.getParticipantById(participant2.getId()).getBody().getEmail(),sut.getParticipantById(participant1.getId()).getBody().getEmail());

        }

        @Test
        public void deleteTest(){
            sut.createParticipant(event1.getId(),participant1);
            sut.createParticipant(event1.getId(),participant2);
            assertEquals(ResponseEntity.status(HttpStatus.OK).body("Participant with id " + participant2.getId() + " was deleted"), sut.deleteParticipant(event1.getId(),participant2.getId()));
        }

        @Test
        public void delete2Test(){
            sut.deleteParticipant(event1.getId(),participant2.getId());

            List<Participant> expected = new ArrayList<>();
            expected.add(participant1);

            assertEquals(participant1, sut.getParticipantById((long)1).getBody());
            assertEquals(sut.getParticipantById((long)2), ResponseEntity.notFound().build());
        }
        @Test
        public void cornerCases() {
            assertEquals(sut.getAllParticipantsByEventId(null), ResponseEntity.notFound().build());
            assertEquals(sut.createParticipant(null, null), ResponseEntity.notFound().build());
            event1.setPrtList(null);
            assertNotNull(sut.createParticipant((long)1, new Participant()));
            assertEquals(sut.deleteParticipant(null, null), ResponseEntity.badRequest().build());
            assertEquals(sut.updateParticipant(null, null), ResponseEntity.notFound().build());
        }
    }

