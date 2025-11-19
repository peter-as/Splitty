import client.Exchange;
import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeTest {
    private Date date;
    private Date yesterday;
    private Date oldDate;
    private Exchange exchange;

    @BeforeEach
    void setup() {
        Calendar myCalendar = new GregorianCalendar(2023, Calendar.FEBRUARY, 11);
        oldDate = myCalendar.getTime();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 1);
        date = cal.getTime();
        exchange = new Exchange(new ServerUtils());
        yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
    }

    @Test
    public void extractTest() {
        assertEquals(exchange.extractDate(oldDate),"2023-02-11");
    }

    @Test
    public void extractEarlyTest() {
        assertEquals(exchange.extractDate(date), exchange.extractDate(yesterday));
    }
}
