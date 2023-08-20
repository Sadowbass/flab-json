import parser.Deserializer;
import parser.DeserializerImpl;
import parser.Serializer;
import parser.SerializerImpl;

public class Main {

    public static void main(String[] args) throws Exception {
        String json = "{\n" +
                "    \"stringField\": \"asd\",\n" +
                "    \"intField\": 1,\n" +
                "    \"doubleField\": 1.0,\n" +
                "    \"objectField\": {\n" +
                "        \"stringField\": \"new asd\",\n" +
                "        \"doubleField\": 2.0,\n" +
                "        \"objectField\": {\n" +
                "            \"stringField\": \"newnew\",\n" +
                "            \"intField\": 3\n" +
                "        }\n" +
                "    },\n" +
                "    \"listField\": [\n" +
                "        {\n" +
                "            \"objectField\": {\n" +
                "                \"stringField\": \"newnew\",\n" +
                "                \"intField\": 3\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"stringField\": \"newnew\",\n" +
                "            \"intField\": 3\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        Serializer serializer = new SerializerImpl();
        TargetObject serResult = serializer.serialize(json, TargetObject.class);
        System.out.println(serResult);

        Deserializer deserializer = new DeserializerImpl();
        String desResult = deserializer.deserialize(serResult);
        System.out.println(desResult);
    }
}
