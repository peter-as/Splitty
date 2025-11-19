package commons;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Date date;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Tag tag;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Participant whoPaid;
    private int amountPaid;
    private String currency;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Participant> participants;


    /**
     * Constructor for the Expense class.
     * @param date The date at which the expense was made.
     * @param tag The tag which is added onto the expense.
     * @param whoPaid The participant who paid and thus made and expense.
     * @param amountPaid The amount of money that was paid.
     * @param currency the currency of the amount of money that was paid.
     * @param participants the participants that are a part of the expense.
     */
    public Expense(String name, Date date, Tag tag, Participant whoPaid, int amountPaid,
                   String currency, List<Participant> participants) {
        this.name = name;
        this.date = date;
        this.tag = tag;
        this.whoPaid = whoPaid;
        this.amountPaid = amountPaid;
        this.currency = currency;
        this.participants = participants;
    }

    /**
     * Constructor for Expense class.
     */
    public Expense() {

    }

    /**
     * Getter for the name of the expense
     * @return the name of the expense
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name of the expense
     * @param name the name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the generated ID.
     * @return the generated ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * setter for expense id
     * @param id id of expense
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * The getter for the date attribute.
     * @return the date of the expense.
     */
    public Date getDate() {
        return date;
    }

    /**
     * The getter for the tags attribute.
     * @return the tag added on the expense.
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * The getter for the whoPaid attribute.
     * @return the whoPaid attribute of the expense.
     */
    public Participant getWhoPaid() {
        return whoPaid;
    }

    /**
     * The getter for the amountPaid attribute.
     * @return the amount paid on the expense.
     */
    public int getAmountPaid() {
        return amountPaid;
    }

    /**
     * The getter for the currency attribute.
     * @return The type of currency with which the expense was paid.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * The getter for the list of participants who were a part of this expense.
     * @return The list of participants who are a part of this expense.
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * The setter for the date attribute.
     * @param date the new date to update the date attribute to.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * The setter for the tags attribute.
     * @param tag the new list of tags to set the tags attribute to.
     */
    public void setTag(Tag tag) {
        this.tag = tag;
    }

    /**
     * The setter for the whoPaid attribute.
     * @param whoPaid the new participant who paid the expense.
     */
    public void setWhoPaid(Participant whoPaid) {
        this.whoPaid = whoPaid;
    }

    /**
     * The setter for the amountPaid attribute.
     * @param amountPaid the new amount paid to update the amountPaid attribute to.
     */
    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    /**
     * The setter for the currency attribute.
     * @param currency the new currency to set the currency attribute to.
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * The setter for the participants attribute.
     * @param participants the new list of participants who are a part of the expense.
     */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    /**
     * Equals method for the expense class.
     * @param obj The object to check equality against.
     * @return a boolean value on whether they are equal or not.
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Hashcode method to generate the hashcode of a given expense.
     * @return the generated hashcode.
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
