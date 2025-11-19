package commons;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpenseTest {
    /**
     * Test to test the constructor of the expense class.
     */
    @Test
    public void constructorTest(){
        Date date = new Date(2024, Calendar.FEBRUARY,18);

        Participant whoPaid = new Participant("Mark", "mark@gmail.com","IBAN123","BIC123");
        List<Participant> participants = getParticipants();

        Color color = new Color(1,1,1);
        Tag tag = new Tag(color,"food");

        Expense expense1 = new Expense("e1",date, tag, whoPaid, 1234, "EUR", participants);
        Expense expense2 = new Expense("e1",date, tag, whoPaid, 1234, "EUR", participants);

        assertEquals(expense1, expense2);
    }

    /**
     * Test to test the hashcode function of the expense class.
     */
    @Test
    public void hashTest(){
        Date date = new Date(2024, Calendar.FEBRUARY,18);

        Participant whoPaid = new Participant("Mark", "mark@gmail.com","IBAN123","BIC123");
        List<Participant> participants = getParticipants();

        Color color = new Color(1,1,1);
        Tag tag = new Tag(color,"food");

        Expense expense1 = new Expense("e1",date, tag, whoPaid, 1234, "EUR", participants);
        Expense expense2 = new Expense("e1",date, tag, whoPaid, 1234, "EUR", participants);

        assertEquals(expense1.hashCode(), expense2.hashCode());
    }

    /**
     * Test to test the setter functions of the expense class.
     */
    @Test
    public void setterTest(){
        Date date = new Date(2024, Calendar.FEBRUARY,18);

        Participant whoPaid = new Participant("Mark", "mark@gmail.com","IBAN123","BIC123");
        List<Participant> participants = getParticipants();

        Color color = new Color(1,1,1);
        Tag tag = new Tag(color,"food");

        Expense expense1 = new Expense("e1",date, tag, whoPaid, 1234, "EUR", participants);
        Expense expense2 = new Expense(null,null, null, null, 0, null, null);

        expense1.setName(null);
        expense1.setDate(null);
        expense1.setTag(null);
        expense1.setWhoPaid(null);
        expense1.setAmountPaid(0);
        expense1.setCurrency(null);
        expense1.setParticipants(null);


        assertEquals(expense1,expense2);
    }

    /**
     * Test to test the getter functions of the expense class.
     */
    @Test
    public void getterTest(){
        Date date = new Date(2024, Calendar.FEBRUARY,18);

        Participant whoPaid = new Participant("Mark", "mark@gmail.com","IBAN123","BIC123");
        List<Participant> participants = getParticipants();

        Color color = new Color(1,1,1);
        Tag tag = new Tag(color,"food");

        Expense expense1 = new Expense("e1",date, tag, whoPaid, 1234, "EUR", participants);
        Expense expense2 = new Expense(expense1.getName(), expense1.getDate(), expense1.getTag(), expense1.getWhoPaid(),
                expense1.getAmountPaid(), expense1.getCurrency(), expense1.getParticipants());

        assertEquals(expense1, expense2);
    }

    /**
     * Test to test the equals function of the expense class.
     */
    @Test
    public void equalsTest(){
        Date date = new Date(2024, Calendar.FEBRUARY,18);

        Participant whoPaid = new Participant("Mark", "mark@gmail.com","IBAN123","BIC123");
        List<Participant> participants = getParticipants();

        Color color = new Color(1,1,1);
        Tag tag = new Tag(color,"food");

        Expense expense1 = new Expense("e1",date, tag, whoPaid, 1234, "EUR", participants);
        Expense expense2 = new Expense("e1",date, tag, whoPaid, 1234, "EUR", participants);

        assertTrue(expense1.equals(expense2));
    }

    private static List<Participant> getParticipants() {
        Participant participant1 = new Participant("Rick", "rick@gmail.com","IBAN123","BIC123");
        Participant participant2 = new Participant("Levi", "levi@gmail.com","IBAN123","BIC123");

        List<Participant> participants = new ArrayList<>();
        participants.add(participant1);
        participants.add(participant2);
        return participants;
    }
}
