package camunda.model;

import java.util.List;

public class ProcessInstanceRequest {

    private List<Variable> variables;

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }
}
