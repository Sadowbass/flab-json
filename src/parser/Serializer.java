package parser;

public interface Serializer {

    <T> T serialize(String json, Class<T> result) throws Exception;
}
