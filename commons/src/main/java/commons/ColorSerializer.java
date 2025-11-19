package commons;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.awt.Color;
import java.io.IOException;

public class ColorSerializer extends JsonSerializer<Color> {

    /**
     * Method to serialize color object.
     * @param color color to serialize.
     * @param jsonGenerator Generator used to output resulting Json content
     * @param serializerProvider Provider that can be used to get serializers for
     *   serializing Objects value contains, if any.
     * @throws IOException if there is something wrong with IO.
     */
    @Override
    public void serialize(Color color, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("red", color.getRed());
        jsonGenerator.writeNumberField("green", color.getGreen());
        jsonGenerator.writeNumberField("blue", color.getBlue());
        jsonGenerator.writeNumberField("alpha", color.getAlpha());
        jsonGenerator.writeEndObject();
    }
}
