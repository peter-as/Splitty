package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.utils.ServerUtils;

import java.io.IOException;

public class ExchangeControllerTest {

    ExchangeController e;
    ServerUtils server;

    @BeforeEach
    public void setup() {
        e = new ExchangeController();
        server = new ServerUtils();
    }
    @Test
    public void successfulRequest() {
        server.getRates("2024-03-15");
    }
}
