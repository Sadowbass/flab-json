import java.util.List;

public class TargetObject {

    private String stringField;
    private int intField;
    private double doubleField;
    private TargetObject objectField;
    private List<TargetObject> listField;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TargetObject{");
        sb.append("stringField='").append(stringField).append('\'');
        sb.append(", intField=").append(intField);
        sb.append(", doubleField=").append(doubleField);
        sb.append(", objectField=").append(objectField);
        sb.append(", listField=").append(listField);
        sb.append('}');
        return sb.toString();
    }
}
