package parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;

public class SerializerImpl implements Serializer {

    @Override
    public <T> T serialize(String jsonString, Class<T> resultType) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);

        Constructor<T> defaultConstructor = resultType.getDeclaredConstructor((Class<?>[]) null);
        T result = defaultConstructor.newInstance((Object[]) null);

        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);

            Field field = resultType.getDeclaredField(key);
            field.setAccessible(true);

            Object o = parseField(value, field);
            field.set(result, o);
        }

        return result;
    }

    private Object parseField(Object value, Field field) throws Exception {
        if (ParserUtils.isPrimitiveOrWrapper(field.getType())) {
            return parsePrimitive(value, field.getType());
        } else if (field.getType() == String.class) {
            return value;
        } else if (Collection.class.isAssignableFrom(field.getType())) {
            return parseArray(value, field);
        } else {
            return serialize(value.toString(), field.getType());
        }
    }

    private Object parsePrimitive(Object value, Class<?> fieldType) {
        if (fieldType == int.class || fieldType == Integer.class) {
            return value;
        } else if (fieldType == double.class || fieldType == Double.class) {
            if (value instanceof BigDecimal) {
                return ((BigDecimal) value).doubleValue();
            }
            return value;
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return value;
        }

        throw new UnsupportedOperationException("can't parse type");
    }

    private Object parseArray(Object value, Field field) throws Exception {
        Class<?> fieldType = field.getType();

        Collection collection = createCollection(fieldType);

        JSONArray asArray = (JSONArray) value;

        for (Object element : asArray) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                for (Type actualTypeArgument : parameterizedType.getActualTypeArguments()) {
                    collection.add(serialize(element.toString(), Class.forName(actualTypeArgument.getTypeName())));
                }
            }
        }

        return collection;
    }

    private Collection createCollection(Class<?> fieldType) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Collection collection;
        if (fieldType.isInterface()) {
            if (List.class.isAssignableFrom(fieldType)) {
                collection = new ArrayList();
            } else if (Set.class.isAssignableFrom(fieldType)) {
                collection = new HashSet();
            } else {
                throw new UnsupportedOperationException("unsupported collection");
            }
        } else {
            Constructor<?> declaredConstructor = fieldType.getDeclaredConstructor((Class<?>[]) null);
            collection = (Collection) declaredConstructor.newInstance(null);
        }

        return collection;
    }
}
