package camunda.model;

import java.util.Map;

public class VariablesObject {

    private Map<String,ValueType> variables;

    public Map<String, ValueType> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, ValueType> variables) {
        this.variables = variables;
    }
}


