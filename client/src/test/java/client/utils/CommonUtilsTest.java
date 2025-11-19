package client.utils;

import client.utils.CommonUtils;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;


public class CommonUtilsTest {

    @Test
    public void readTest() {
        List<String> possible = new ArrayList<>();
        possible.add("EUR");
        possible.add("USD");
        possible.add("CHF");
        assertTrue(possible.contains(CommonUtils.readCurrency(Paths.get("src/main/resources/client/config.txt"))));
    }

    @Test
    public void moneyTest() {
        assertEquals("12.75",CommonUtils.moneyToText(1275));
    }

    @Test
    public void moneyTestZero() {
        assertEquals("0.00",CommonUtils.moneyToText(0));
    }

    @Test
    public void readError() {
        assertNull(CommonUtils.readCurrency(Paths.get("noPath")));
    }
}
