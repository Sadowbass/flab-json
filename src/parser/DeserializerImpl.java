package parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Collection;

public class DeserializerImpl implements Deserializer {

    @Override
    public String deserialize(Object target) throws Exception {
        if (target == null) {
            return null;
        }

        JSONObject jsonObject = new JSONObject();
        Field[] fields = target.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();

            jsonObject.put(fieldName, parseValue(field, target));
        }

        return jsonObject.toString().replace("\\", "").replace("\"{", "{").replace("}\"", "}");
    }

    private Object parseValue(Field field, Object target) throws Exception {
        Class<?> fieldType = field.getType();
        Object value = field.get(target);
        if (ParserUtils.isPrimitiveOrWrapper(fieldType) || fieldType == String.class) {
            return value;
        } else if (Collection.class.isAssignableFrom(fieldType)) {
            return parseCollection(field, target);
        } else {
            return deserialize(value);
        }
    }

    private Object parseCollection(Field field, Object target) throws Exception {
        Collection collection = (Collection) field.get(target);
        if (collection == null) {
            return null;
        }

        JSONArray jsonArray = new JSONArray();

        for (Object element : collection) {
            jsonArray.put(deserialize(element));
        }

        return jsonArray;
    }
}
