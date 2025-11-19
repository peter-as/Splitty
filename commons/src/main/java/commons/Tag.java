package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import java.awt.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonSerialize(using = ColorSerializer.class)
    @JsonDeserialize(using = ColorDeserializer.class)
    private Color color;
    private String name;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Event tagEvent;

    /**
     * Constructor for Tag class.
     * @param color The Tag's color
     * @param name The Tag's name
     */
    public Tag(Color color, String name) {
        this.color = color;
        this.name = name;
    }

    /**
     * Constructor for Tag class.
     */
    public Tag() {

    }

    /**
     * Setter for the id.
     * @param id the new id to set the id to.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * setter for the color.
     * @param color the color to set to.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Setter for the name
     * @param name the name to set to.
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
     * Setter
     * @return The Tag's color
     */
    @JsonIgnore
    public Color getColor() {
        return color;
    }

    /**
     * Getter
     * @return The Tag's name
     */
    public String getName() {
        return name;
    }

    /**
     * Equals method for Tag class
     * @param obj Object to compare with
     * @return true if objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, "tagEvent");
    }

    /**
     * Hash code method for Tag object
     * @return Hash code of the object
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Getter
     * @return tag's event
     */
    public Event getTagEvent() {
        return tagEvent;
    }

    /**
     * Setter
     * @param tagEvent tag's event
     */
    public void setTagEvent(Event tagEvent) {
        this.tagEvent = tagEvent;
    }
}
