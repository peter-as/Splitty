package client;

import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.module.ResolutionException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LanguageNodeTest {

    Image img;
    Image img2;
    LanguageNode l1;
    LanguageNode l2;

    @BeforeEach
    public void setup() {
        URL u = LanguageNodeTest.class.getClassLoader().getResource("client/flags/English.jpg");
        URL u2 = LanguageNodeTest.class.getClassLoader().getResource("client/flags/Dutch.jpg");
        try (InputStream input = u.openStream(); InputStream input2 = u2.openStream()) {

            img = new Image(input);
            img2 = new Image(input2);
            l1 = new LanguageNode("English",img);
            l2 = new LanguageNode("English",img);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void constructorTest() throws IOException {
        LanguageNode l3 = new LanguageNode("English",img);
    }

    @Test
    public void getterTextTest() {
        assertEquals("English",l1.getText());
    }

    @Test
    public void getterImageTest() {
        assertEquals(img,l1.getImage());
    }

    @Test
    public void setterTextTest() {
        l1.setText("Dutch");
        assertEquals("Dutch",l1.getText());
    }

    @Test
    public void setterImageTest() {
        l1.setImage(img2);
        assertEquals(img2, l1.getImage());
    }

    @Test
    public void toStringTest() {
        assertEquals("",l1.toString());
    }

    @Test
    public void equalsTest() {
        assertEquals(l1,l2);
    }

    @Test
    public void hasTest() {
        assertEquals(l1.hashCode(), l2.hashCode());
    }

}
