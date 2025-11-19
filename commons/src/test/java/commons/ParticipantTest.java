package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {
	@Test
	void testConstructor() {
		Participant a = new Participant("Ash","ash@gmail.com","NL123412341234","IDK123456");
		assertEquals(a.getName(),"Ash");
		assertEquals(a.getEmail(),"ash@gmail.com");
		assertEquals(a.getIban(),"NL123412341234");
		assertEquals(a.getBic(),"IDK123456");
	}
	@Test
	void testEquals() {
		Participant a = new Participant("Ash","ash@gmail.com","NL123412341234","IDK123456");
		Participant b = new Participant("Ash","ash@gmail.com","NL123412341234","IDK123456");
		assertEquals(a,b);
	}

	@Test
	void testHashCode() {
		Participant a = new Participant("Ash","ash@gmail.com","NL123412341234","IDK123456");
		Participant b = new Participant("Ash","ash@gmail.com","NL123412341234","IDK123456");
		assertEquals(a.hashCode(),b.hashCode());
	}
	@Test
	void testNotEquals() {
		Participant a = new Participant("Ash","ash@gmail.com","NL123412341234","IDK123456");
		Participant b = new Participant("Bob","ash@gmail.com","NL123412341234","IDK123456");
		assertNotEquals(a,b);
	}
	@Test
	void testSetters() {
		Participant a = new Participant("Ash","ash@gmail.com","NL123412341234","IDK123456");
		a.setName("Bob");
		a.setEmail("bob@gmail.com");
		a.setIban("NL432143214321");
		a.setBic("IDK654321");
		Participant b = new Participant("Bob","bob@gmail.com","NL432143214321","IDK654321");
		assertEquals(a,b);
	}
}