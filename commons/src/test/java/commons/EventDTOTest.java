package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventDTOTest {

    @Test
    void test() {
        Event ev = new Event();
        boolean f = false;
        EventDTO dto = new EventDTO(f, ev);
        dto.setEvent(ev);
        dto.setDeleted(true);
        assertEquals(dto.getEvent(), ev);
        assertSame(dto.isDeleted(), true);
    }
}