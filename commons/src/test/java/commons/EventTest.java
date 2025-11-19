package commons;

import net.bytebuddy.matcher.FilterableList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EventTest {
    @Test
    public void constructorTest(){
        Date date1 = new Date(2024, Calendar.JANUARY,13);
        Date date2 = new Date(2024, Calendar.JANUARY,21);

        Participant participant1 = new Participant("John", "j@gmail.com","IBAN000","BIC000");
        Participant participant2 = new Participant("Emma", "e@gmail.com","IBAN000","BIC000");

        List<Participant> participants = new ArrayList<>();
        participants.add(participant1);
        participants.add(participant2);

        Color color = new Color(1,1,1);
        Tag tag = new Tag(color,"food");

        Expense expense = new Expense("e1",date1,tag,participant1,400,"EUR",participants);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(expense);

        Event event1 = new Event(date1,date2,"Event1",participants,expenses,null);
        Event event2 = new Event(date1,date2,"Event1",participants,expenses,null);

        assertEquals(event1,event2);
    }

    @Test
    public void hashTest(){
        Date date1 = new Date(2024, Calendar.JANUARY,13);
        Date date2 = new Date(2024, Calendar.JANUARY,21);

        Participant participant1 = new Participant("John", "j@gmail.com","IBAN000","BIC000");
        Participant participant2 = new Participant("Emma", "e@gmail.com","IBAN000","BIC000");

        List<Participant> participants = new ArrayList<>();
        participants.add(participant1);
        participants.add(participant2);

        Color color = new Color(1,1,1);
        Tag tag = new Tag(color,"food");

        Expense expense = new Expense("e1",date1,tag,participant1,400,"EUR",participants);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(expense);

        Event event1 = new Event(date1,date2,"Event1",participants,expenses,null);
        Event event2 = new Event(date1,date2,"Event1",participants,expenses,null);

        assertEquals(event1.hashCode(),event2.hashCode());
    }

    @Test
    public void setterTest(){
        Date date1 = new Date(2024, Calendar.JANUARY,13);
        Date date2 = new Date(2024, Calendar.JANUARY,21);

        Participant participant1 = new Participant("John", "j@gmail.com","IBAN000","BIC000");
        Participant participant2 = new Participant("Emma", "e@gmail.com","IBAN000","BIC000");

        List<Participant> participants = new ArrayList<>();
        participants.add(participant1);
        participants.add(participant2);

        Color color = new Color(1,1,1);
        Tag tag = new Tag(color,"food");

        Expense expense = new Expense("e1",date1,tag,participant1,400,"EUR",participants);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(expense);

        Event event1 = new Event(date1,date2,"Event1",participants,expenses,null);
        Event event2 = new Event(null,null,null,null,null,null);

        event1.setDateOfCreation(null);
        event1.setLastUpdate(null);
        event1.setName(null);
        event1.setPrtList(null);
        event1.setExpList(null);

        assertEquals(event1,event2);
    }
    @Test
    public void getterTest(){
        Date date1 = new Date(2024, Calendar.JANUARY,13);
        Date date2 = new Date(2024, Calendar.JANUARY,21);

        Participant participant1 = new Participant("John", "j@gmail.com","IBAN000","BIC000");
        Participant participant2 = new Participant("Emma", "e@gmail.com","IBAN000","BIC000");

        List<Participant> participants = new ArrayList<>();
        participants.add(participant1);
        participants.add(participant2);

        Color color = new Color(1,1,1);
        Tag tag = new Tag(color,"food");

        Expense expense = new Expense("e1",date1,tag,participant1,400,"EUR",participants);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(expense);

        Event event1 = new Event(date1,date2,"Event1",participants,expenses,null);

        Date dateC = event1.getDateOfCreation();
        Date last = event1.getLastUpdate();
        String name = event1.getName();
        List<Participant> prt = event1.getPrtList();
        List<Expense> exp = event1.getExpList();
        String invite = event1.getInviteCode();

        Event event2 = new Event(dateC,last,name,prt,exp,null);

        assertEquals(event1,event2);
    }

    @Test
    public void generateInviteCodeTest() {
        Event first = new Event(null,null,"Event1",null,null,null);
        assertNull(first.getInviteCode());
        first.setId((long)0);
        assertEquals(first.getInviteCode(),"b");
        first.setId((long)(35));
        assertEquals(first.getInviteCode(),"Z");
        first.setId((long)(2198734));
        assertEquals(first.getInviteCode(),"YJ4B1");
        first.setId((long)(-2198734));
        assertEquals(first.getInviteCode(),"aYJ4B1");
        first.setId((long)(-1));
        assertEquals(first.getInviteCode(),"a1");
    }

}
