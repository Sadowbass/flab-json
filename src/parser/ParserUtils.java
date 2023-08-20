package parser;

import java.math.BigDecimal;

class ParserUtils {

    public static boolean isPrimitiveOrWrapper(Class<?> field) {
        return field.isPrimitive() || field == Integer.class || field == BigDecimal.class || field == Boolean.class || field == Double.class;
    }
}
