package camunda;

import camunda.model.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProcessInstanceClient extends CamundaClient {

    private String camundaEngineURL;


    public ProcessInstanceClient(String camundaEngineURL) {
        this.camundaEngineURL = camundaEngineURL;
    }

    public ProcessInstance getProcessInstanceFromStreamId(String streamId){
        return getProcessInstance(streamId.replace("_","%"));
    }

    public ProcessInstance getProcessInstance(String streamId){
        ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
        List<Variable> variableList = new ArrayList<>();
        variableList.add(new Variable("streamId","eq",streamId));
        processInstanceRequest.setVariables(variableList);
        Client client = ClientBuilder.newClient();
        Response response
                = client.target(camundaEngineURL)
                .path("/process-instance")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(processInstanceRequest, MediaType.APPLICATION_JSON));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {

            try {
                handleError(response);
            } catch (CamundaClientException e) {
                e.printStackTrace();
            }
            return  null;
        } else {
            ProcessInstanceList list = response.readEntity(ProcessInstanceList.class);
            if(!list.isEmpty()){
                return list.get(0);
            }
            else{
                return null;
            }

        }
    }


    public void startProcessInstance(Map<String,ValueType> variableMap, String processId){

        VariablesObject variableList = new VariablesObject();
        variableList.setVariables(variableMap);
        Client client = ClientBuilder.newClient();
        Response response
                = client.target(camundaEngineURL)
                .path("/process-definition/{processId}/start".replace("{processId}", processId))
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(variableList, MediaType.APPLICATION_JSON));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            try {
                handleError(response);
            } catch (CamundaClientException e) {
                e.printStackTrace();
            }
        }
    }

    public Deployment getDeploymentFromName(String deploymentName){
        Client client = ClientBuilder.newClient();
        Response response
                = client.target(camundaEngineURL)
                .path("deployment")
                .queryParam("name", deploymentName)
                .request(MediaType.APPLICATION_JSON)
                .get();
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            try {
                handleError(response);
            } catch (CamundaClientException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            DeploymentList deploymentList =  response.readEntity(DeploymentList.class);
            System.out.println(deploymentList.get(0).getId());
            return deploymentList.get(0);
        }
    }

    public ProcessDefinitionInfo getProcessDefinitionIdFromDeploymentId(String deployment){
        Client client = ClientBuilder.newClient();
        Response response
                = client.target(camundaEngineURL)
                .path("process-definition/")
                .queryParam("deploymentId", deployment)
                .request(MediaType.APPLICATION_JSON)
                .get();
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            try {
                handleError(response);
            } catch (CamundaClientException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            ProcessDefinitionInfoList infoList =  response.readEntity(ProcessDefinitionInfoList.class);
            System.out.println(infoList.get(0).getId());
            return infoList.get(0);
        }
    }

    public Map<String,ValueType> getProcessVariables(String processInstanceId) {
        Client client = ClientBuilder.newClient();
        Response response
                = client.target(camundaEngineURL)
                .path("process-instance/{instanceId}/variables".replace("{instanceId}",processInstanceId))
                .request(MediaType.APPLICATION_JSON)
                .get();
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            try {
                handleError(response);
            } catch (CamundaClientException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            Map<String,ValueType> variables =  response.readEntity(VariableMap.class);
            return variables;
        }
    }

    public void setProcessVariable(String instanceId, String variable, String value){
        Client client = ClientBuilder.newClient();
        ValueType valueType = new ValueType(value);
        Response response
                = client.target(camundaEngineURL)
                .path("process-instance/{id}/variables/{varName}".replace("{id}",instanceId).replace("{varName}",variable))
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(valueType,MediaType.APPLICATION_JSON));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            try {
                handleError(response);
            } catch (CamundaClientException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(response.getStatus());

        }
    }

    public ProcessDefinitionInfo getProcessDefinitionIdFromDeploymentName(String name) {
        Deployment deployment  = getDeploymentFromName(name);
        return getProcessDefinitionIdFromDeploymentId(deployment.getId());
    }
}
