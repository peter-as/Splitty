package commons;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

	@Test
	void testConstructor() {
		Color color = new Color(1,1,1);
		Tag a = new Tag(color,"food");
		assertEquals(a.getColor(),color);
		assertEquals(a.getName(),"food");
	}
	@Test
	void testEquals() {
		Color color = new Color(1,1,1);
		Tag a = new Tag(color,"drinks");
		Tag b = new Tag(color,"drinks");
		assertEquals(a,b);
	}

	@Test
	void testHashCode() {
		Color color = new Color(1,1,1);
		Tag a = new Tag(color,"drinks");
		Tag b = new Tag(color,"drinks");
		assertEquals(a.hashCode(),b.hashCode());
	}
	@Test
	void testNotEquals() {
		Color color = new Color(1,1,1);
		Tag a = new Tag(color,"drinks");
		Tag b = new Tag(color,"food");
		assertNotEquals(a,b);
	}
}