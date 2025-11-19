package server.api;

import commons.Debt;
import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.database.EventRepository;
import server.database.ParticipantRepository;
import server.utils.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DebtControllerTest {
    EventRepositoryImpl eventRepository = new EventRepositoryImpl();
    ExpenseRepositoryImpl expenseRepository = new ExpenseRepositoryImpl();
    ParticipantRepositoryImpl participantRepository = new ParticipantRepositoryImpl();
    TagRepositoryImpl tagRepository = new TagRepositoryImpl();
    DatabaseUtils du = new DatabaseUtils(eventRepository, expenseRepository, participantRepository, tagRepository);

    Participant participant1;
    Participant participant2;
    Participant participant3;
    @BeforeEach
    void setup() {
        participant1 = new Participant("John", "j@gmail.com","IBAN000","BIC000");
        participant2 = new Participant("Emma", "e@gmail.com","IBAN000","BIC000");
        participant3 = new Participant("Test", "e@gmail.com","IBAN000","BIC000");
        participant1.setNetDebt(-3);
        participant2.setNetDebt(-4);
        participant3.setNetDebt(7);
        participant1.setId((long)1);
        participant2.setId((long)2);
        participant3.setId((long)3);
        eventRepository = new EventRepositoryImpl();
        expenseRepository = new ExpenseRepositoryImpl();
        participantRepository = new ParticipantRepositoryImpl();
        tagRepository = new TagRepositoryImpl();
        du = new DatabaseUtils(eventRepository, expenseRepository, participantRepository, tagRepository);
    }
    @Test
    void debts() {
        DebtController dc = new DebtController(du);
        Event ev = new Event();
        List<Participant> participants = new ArrayList<>();
        participants.add(participant1);
        participants.add(participant2);
        participants.add(participant3);
        du.participantSave(participant1);
        du.participantSave(participant2);
        du.participantSave(participant3);
        ev.setPrtList(participants);
        ev.setId((long)1);
        du.eventSave(ev);
        List<Debt> ld = dc.debts(1).getBody();
        List<Debt> ldtest = new ArrayList<>();
        ldtest.add(new Debt(participant3,participant2,4,-4,null));
        ldtest.add(new Debt(participant3,participant1,3,-3,null));
        assertEquals(ld,ldtest);
    }
    @Test
    void debts1() {
        DebtController dc = new DebtController(du);
        Event ev = new Event();
        List<Participant> participants = new ArrayList<>();
        participant1.setNetDebt(3);
        participant2.setNetDebt(4);
        participant3.setNetDebt(-7);
        participants.add(participant1);
        participants.add(participant2);
        participants.add(participant3);
        du.participantSave(participant1);
        du.participantSave(participant2);
        du.participantSave(participant3);
        ev.setPrtList(participants);
        ev.setId((long)1);
        du.eventSave(ev);
        List<Debt> ld = dc.debts(1).getBody();
        List<Debt> ldtest = new ArrayList<>();
        ldtest.add(new Debt(participant2,participant3,7,-7,null));
        ldtest.add(new Debt(participant1,participant2,3,-3,null));
        assertEquals(ld,ldtest);
        assertEquals(dc.debts(-1), ResponseEntity.notFound().build());
    }

    @Test
    void removeDebt() {
        DebtController dc = new DebtController(du);
        Event ev = new Event();
        List<Participant> participants = new ArrayList<>();
        participant1.setNetDebt(-3);
        participant2.setNetDebt(-4);
        participant3.setNetDebt(7);
        participants.add(participant1);
        participants.add(participant2);
        participants.add(participant3);
        du.participantSave(participant1);
        du.participantSave(participant2);
        du.participantSave(participant3);
        ev.setPrtList(participants);
        ev.setId((long)1);
        du.eventSave(ev);
        Debt d = new Debt(participant3,participant2,4,-4,null);
        Debt dd = dc.removeDebt(1,d).getBody();
        d.setAmt1Owes(0);
        d.setAmt2Owes(0);
        assertEquals(d,dd);
        List<Debt> debts = new ArrayList<>();
        participant3.setNetDebt(3);
        participant1.setNetDebt(-3);
        debts.add(new Debt(participant3,participant1,3,-3,null));
        List<Debt> expdebts = dc.debts(1).getBody();

        assertEquals(expdebts,debts);

        d = new Debt(participant3,participant1,3,-3,null);
        dc.removeDebt(1,d);
        debts.clear();

        assertEquals(dc.debts(1).getBody(),debts);
        assertEquals(dc.removeDebt(-1, null), ResponseEntity.notFound().build());
        Participant first = new Participant();
        Participant second = new Participant();
        assertEquals(dc.removeDebt(-1, new Debt(first, second,1,-1, null)), ResponseEntity.notFound().build());

    }
}
