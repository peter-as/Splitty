package commons;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


public class DebtTest {
    @Test
    void testConstructor() {
        Participant person1 = new Participant("name", "email", "iban", "bic");
        Participant person2 = new Participant("name2", "email", "iban", "bic");
        Date date = new Date(2024, 2, 21);
        Debt debt = new Debt(person1, person2, 0, 500, date);
        assertEquals(debt.getPerson1(), person1);
        assertEquals(debt.getPerson2(), person2);
        assertEquals(debt.getAmt1Owes(), 0);
        assertEquals(debt.getAmt2Owes(), 500);
        assertEquals(debt.getDatePaid(), date);
    }

    @Test
    void testEquals() {
        Participant person1 = new Participant("name", "email", "iban", "bic");
        Participant person2 = new Participant("name2", "email", "iban", "bic");
        Date date = new Date(2024, 2, 21);
        Debt debt1 = new Debt(person1, person2, 0, 500, date);
        Debt debt2 = new Debt(person1, person2, 0, 500, date);

        assertEquals(debt1, debt2);
    }

    @Test
    void testHashcode() {
        Participant person1 = new Participant("name", "email", "iban", "bic");
        Participant person2 = new Participant("name2", "email", "iban", "bic");
        Date date = new Date(2024, 2, 21);
        Debt debt1 = new Debt(person1, person2, 0, 500, date);
        Debt debt2 = new Debt(person1, person2, 0, 500, date);

        assertEquals(debt1.hashCode(), debt2.hashCode());
    }
    
    @Test
    void testSetters() {
        Participant person1 = new Participant("name", "email", "iban", "bic");
        Participant person2 = new Participant("name2", "email", "iban", "bic");
        Date date = new Date(2024, 2, 21);
        Date date1 = new Date(2020, 3, 20);
        Debt debt = new Debt(person1, person2, 0, 500, date);

        debt.setPerson1(person2);
        debt.setPerson2(person1);
        debt.setAmt1Owes(500);
        debt.setAmt2Owes(0);
        debt.setDatePaid(date1);

        Debt debt1 = new Debt(person2, person1, 500, 0, date1);

        assertEquals(debt, debt1);
    }

}
