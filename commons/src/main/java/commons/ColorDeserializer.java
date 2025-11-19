package commons;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.awt.Color;
import java.io.IOException;

public class ColorDeserializer extends JsonDeserializer<Color> {

    /**
     * Method to deserialize the JSON and make a color object from it again.
     * @param jsonParser Parsed used for reading JSON content
     * @param deserializationContext Context that can be used to access information about
     *   this deserialization activity.
     *
     * @return The color that has been deserialized.
     * @throws IOException if there is something wrong with IO.
     */
    @Override
    public Color deserialize(JsonParser jsonParser,
                             DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        int red = node.get("red").asInt();
        int green = node.get("green").asInt();
        int blue = node.get("blue").asInt();
        int alpha = node.get("alpha").asInt();
        return new Color(red, green, blue, alpha);
    }
}

