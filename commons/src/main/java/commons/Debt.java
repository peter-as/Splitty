package commons;

import java.util.Date;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Debt {
    private Participant person1;
    private Participant person2;
    private int amt1Owes;
    private int amt2Owes;
    private Date datePaid;

    /**
     * Constructor for the debt class
     * A debt relationship is between 2 individuals who owe each other money
     * @param person1 - the first participant in the relation
     * @param person2 - the second participant in the relation
     * @param amt1Owes - the amount the first participant owes the second
     * @param amt2Owes - the amount the second participant owes the first
     * @param datePaid - the date the debt was settled
     */
    public Debt(Participant person1, Participant person2,
                int amt1Owes, int amt2Owes, Date datePaid) {
        this.person1 = person1;
        this.person2 = person2;
        this.amt1Owes = amt1Owes;
        this.amt2Owes = amt2Owes;
        this.datePaid = datePaid;
    }

    /**
     * Default constructor
     */
    public Debt() {}

    /**
     * Getter method for participant 1
     * @return - the first participant in the relation
     */
    public Participant getPerson1() {
        return person1;
    }

    /**
     * Getter method for participant 2
     * @return - the second participant in the relation
     */
    public Participant getPerson2() {
        return person2;
    }

    /**
     * Getter method for the amount participant 1 owes 2
     * @return - the int amount (in cents) that is owed from 1 to 2
     */
    public int getAmt1Owes() {
        return amt1Owes;
    }

    /**
     * Getter method for the amount participant 2 owes 1
     * @return - the int amount (in cents) that is owed from 2 to 1
     */
    public int getAmt2Owes() {
        return amt2Owes;
    }

    /**
     * Getter method for the date the debt was paid/settled
     * @return - the date the debt was paid
     */
    public Date getDatePaid() {
        return datePaid;
    }

    /**
     * Setter method to set participant 1
     * @param person1 - participant 1 to be set
     */
    public void setPerson1(Participant person1) {
        this.person1 = person1;
    }

    /**
     * Setter method to set participant 2
     * @param person2 - participant 2 to be set
     */
    public void setPerson2(Participant person2) {
        this.person2 = person2;
    }

    /**
     * Setter method to set the amount 1 owes 2
     * @param amt1Owes - amount owed to be set
     */
    public void setAmt1Owes(int amt1Owes) {
        this.amt1Owes = amt1Owes;
    }

    /**
     * Setter method to set the amount 2 owes 1
     * @param amt2Owes - amount owed to be set
     */
    public void setAmt2Owes(int amt2Owes) {
        this.amt2Owes = amt2Owes;
    }

    /**
     * Setter method to set the date the debt is paid
     * @param datePaid - the date paid to be set
     */
    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
    }

    /**
     * Equals method for debt class
     * @param obj Object to compare with
     * @return true if objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Hash code method for debt object
     * @return Hash code of the object
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
