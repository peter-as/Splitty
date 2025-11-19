package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String iban;
    private String bic;
    private int netDebt;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Event partEvent;

    /**
     * Constructor for the Participant class
     * @param name First name of the participant
     * @param email Email address of the participant
     * @param iban IBAN of the participant's bank account
     * @param bic BIC of the bank account of the participant
     */
    public Participant(String name, String email, String iban, String bic) {
        this.name = name;
        this.email = email;
        this.iban = iban;
        this.bic = bic;
        this.netDebt = 0;
    }

    /**
     * No-arg constructor.
     */
    public Participant() {

    }

    /**
     * Getter
     * @return Participant's net debt
     */
    public int getNetDebt() {
        return netDebt;
    }

    /**
     * Setter
     * @param netDebt Participant's net debt
     */
    public void setNetDebt(int netDebt) {
        this.netDebt = netDebt;
    }

    /**
     * Getter
     * @return Participant's name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter
     * @param id Id to set participant's id to
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Setter
     * @param name Participant's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter
     * @return Participant's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter
     * @param email Participant's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter
     * @return Participant's IBAN
     */
    public String getIban() {
        return iban;
    }

    /**
     * Setter
     * @param iban Participant's IBAN
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     * Getter
     * @return Participant's BIC
     */
    public String getBic() {
        return bic;
    }

    /**
     * Setter
     * @param bic Participant's BIC
     */
    public void setBic(String bic) {
        this.bic = bic;
    }

    /**
     * Getter
     * @return Participant's automatically generated ID
     */
    public Long getId() {
        return this.id;
    }
    /**
     * Equals method for participant class
     * @param obj Object to compare with
     * @return true if objects are equal
     */

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Hash code method for participant object
     * @return Hash code of the object
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Getter
     * @return participant's event
     */
    public Event getPartEvent() {
        return partEvent;
    }

    /**
     * Setter
     * @param partEvent participant's event
     */
    public void setPartEvent(Event partEvent) {
        this.partEvent = partEvent;
    }
}