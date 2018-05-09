package camunda.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ValueType{
    private String value;
    private String type;

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;

    }

    public ValueType(String value) {
        this.value = value;
        this.type = "String";
    }

    public ValueType() {
    }
}
