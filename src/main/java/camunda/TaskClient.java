package camunda;

import camunda.model.Task;
import camunda.model.TaskList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class TaskClient extends CamundaClient {

    private String camundaEngineURL;

    public TaskClient(String camundaEngineURL) {
        this.camundaEngineURL = camundaEngineURL;
    }

    public Task getTask(String processInstanceId, String userEmail){
        Client client = ClientBuilder.newClient();
        Response response
                = client.target(camundaEngineURL)
                .path("/task")
                .queryParam("processInstanceId", processInstanceId)
                .queryParam("assignee", userEmail)
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
            TaskList infoList =  response.readEntity(TaskList.class);
            if(infoList.isEmpty()){
                return null;
            } else{
                return infoList.get(0);

            }
        }
    }

    public void completeTask(String taskId){
        Client client = ClientBuilder.newClient();
        Response response
                = client.target(camundaEngineURL)
                .path("/task/{id}/complete".replace("{id}",taskId))
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity("{}", MediaType.APPLICATION_JSON));
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            try {
                handleError(response);
            } catch (CamundaClientException e) {
                e.printStackTrace();
            }
        }
        System.out.println(response.getStatus());
    }
}
