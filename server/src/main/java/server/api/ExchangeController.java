package server.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    private final String postsApiUrl = "https://openexchangerates.org/api/historical/";
    private final String json = ".json";
    private final String apiKey = "?app_id=cd0faf7075a74519bfe27c253af7d5ab";

    /**
     * Constructor for the ExchangeController
     */
    public ExchangeController() {}

    /**
     * Gets the live exchange rates from openexchangerates.org.
     * @param date the date of the exchange rates
     * @return the exchange rates fetched from the server.
     */
    @GetMapping("/{date}")
    public ResponseEntity<JsonNode> getRates(@PathVariable("date") String date) {
        String requestLink = postsApiUrl + date + json + apiKey;
        HttpResponse<String> response;
        try {
            HttpClient client = HttpClient.newHttpClient();
            //Sends a get request to the server to retrieve exchange rates
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .header("accept", "application/json")
                    .uri(URI.create(requestLink))
                    .build();
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            client.close();
            String rates = response.body(); //Retrieves the body of the get request
            ObjectMapper mapper = new ObjectMapper();
            //Turns the body of the request into a json object,
            // and extracts only the exchange rates themselves
            return ResponseEntity.ok(mapper.readTree(rates).get("rates"));
        } catch (Exception e) {
            System.out.println("Exchange error");
            return null;
        }

    }
}
