package client;

import java.util.Arrays;
import javafx.scene.image.Image;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class LanguageNode {
    private String text;
    private Image image;

    /**
     * Constructor for LanguageNode
     * @param text the text it displays
     * @param image the image it displays
     */
    public LanguageNode(String text, Image image) {
        this.text = text;
        this.image = image;
    }

    /**
     * Getter for the text
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Getter for the image
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Setter for the text
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Setter for the image
     * @param image the image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * ToString method, which is empty so no text is displayed for the selected language
     * @return an empty string
     */
    @Override
    public String toString() {
        return "";
    }

    /**
     * equals method for the languagenode
     * @param obj the other object
     * @return if they are equal
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, Arrays.asList("id", "inviteCode"));
    }

    /**
     * Hashcode for the languagenode
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
