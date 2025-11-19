package commons;

import jakarta.persistence.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class  Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date dateOfCreation;
    private Date lastUpdate;
    private String name;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "partEvent",
            fetch = FetchType.EAGER)
    private List<Participant> prtList;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Expense> expList;
    private String inviteCode;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "tagEvent",
            fetch = FetchType.EAGER)
    private List<Tag> tagList;

    /**
     * Constructor for an Event object.
     * @param dateOfCreation the date the event was created.
     * @param lastUpdate the last date when an update related to the event was made.
     * @param name the name of the event.
     * @param prtList the list of participants in the event.
     * @param expList the list of expenses related to the event.
     */
    public Event(Date dateOfCreation,
                 Date lastUpdate,
                 String name,
                 List<Participant> prtList,
                 List<Expense> expList,
                 List<Tag> tagList
                 ) {
        this.dateOfCreation = dateOfCreation;
        this.lastUpdate = lastUpdate;
        this.name = name;
        this.prtList = prtList;
        this.expList = expList;
        this.tagList = tagList;
        generateInviteCode();
    }


    /**
     * No-arg constructor.
     */
    public Event() {

    }

    /**
     * Getter method for tag list
     * @return list of tags in event
     */
    public List<Tag> getTagList() {
        return tagList;
    }

    /**
     * Sets tag list
     * @param tagList - list of tags
     */
    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    /**
     * Getter method for the date of creation.
     * @return the date when the event was created
     */
    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    /**
     * Getter method for the date of the last update.
     * @return the date of when the event was last updated
     */
    public Date getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Getter method for the name of the event.
     * @return the name of the event.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for the list of participants.
     * @return the list of participants involved in the event.
     */
    public List<Participant> getPrtList() {
        return prtList;
    }

    /**
     * Getter method for the list of expenses.
     * @return the list of expenses related to the event.
     */
    public List<Expense> getExpList() {
        return expList;
    }

    /**
     * Getter method for the invite code of the event.
     * @return the invite code associated to an event.
     */
    public String getInviteCode() {
        return inviteCode;
    }

    /**
     * Getter
     * @return The ID of event
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter
     * @param id ID to set for event
     */
    public void setId(Long id) {
        this.id = id;
        generateInviteCode();
    }

    /**
     * Setter method for the date of creation.
     * @param dateOfCreation the date when the event was created.
     */
    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    /**
     * Setter method for the date of the last update.
     * @param lastUpdate the date of when the event was last updated.
     */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Setter method for the name of the event.
     * @param name the name of the event.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter method for the participants list.
     * @param prtList the list of participants involved in the event.
     */
    public void setPrtList(List<Participant> prtList) {
        this.prtList = prtList;
    }

    /**
     * Setter method for the expenses list.
     * @param expList the list of expenses related to the event.
     */
    public void setExpList(List<Expense> expList) {
        this.expList = expList;
    }

    /**
     * Setter for the invite code, for testing purposes
     * @param inviteCode the invite code of the event
     */
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    /**
     * Equals method that checks if the given object is an Event with identical properties.
     * @param obj the given object that has to be compared with this object
     * @return true iff the two objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, Arrays.asList("id", "inviteCode"));
    }

    /**
     * Hash code method for the Event object.
     * @return the hash code of the object.
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Generates the invite code from the id
     */
    public void generateInviteCode() {
        if (id == null) {
            this.inviteCode = null;
            return;
        }
        StringBuilder ans = new StringBuilder();
        long idd = id;
        if (id < 0) {
            ans.append("a");
            idd = -id;
        } else if (id == 0) {
            ans.append("b");
        }
        String vocab = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        while (idd > 0) {
            ans.append(vocab.charAt((int) (idd % vocab.length())));
            idd /= vocab.length();
        }
        this.inviteCode = ans.toString();
    }

    /**
     * Removes a participant from the participants list of the event
     * @param participant the participant to be removed
     */
    public void removeParticipant(Participant participant) {
        List<Participant> original = getPrtList();
        original.remove(participant);
        setPrtList(original);
    }

    /**
     * Removes an expense from the expense list of the event
     * @param expense the expense to be removed
     */
    public void removeExpense(Expense expense) {
        List<Expense> original = getExpList();
        original.remove(expense);
        setExpList(original);
    }
}
