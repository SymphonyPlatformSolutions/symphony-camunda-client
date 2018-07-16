# symphony-camunda-client
Library for integration with Camunda REST API for Symphony bots that follow a workflow.

Provides access to the Camunda REST APIs in order to gather the process instance that belongs a to a user in Symphony Stream and if a user task is present action it.

### Install using maven
        <dependency>
                    <groupId>com.symphony.platformsolutions</groupId>
                    <artifactId>symphony-camunda-client</artifactId>
                    <version>1.0.1</version>
        </dependency>
        
## Configuration
Create a camunda-config.json file in your project which includes the following property

    {
      "camundaURL": "http://localhost:8080/engine-rest"
    }
    
## Usage

1. First step is deploying your bpmn files to the camunda engine.
2. Add this code to your message handler in the Symphony bot code:

        ProcessInstance instance = processInstanceClient.getProcessInstance(inboundMessage.getStream().getStreamId().replace("_", "%"));
        if (instance == null) {
    
            if (action != null) {
                ProcessDefinitionInfo info = processInstanceClient.getProcessDefinitionIdFromDeploymentName(action.getAction());
                Map<String, ValueType> variableMap = new HashMap<>();
                variableMap.put("streamId", new ValueType(inboundMessage.getStream().getStreamId().replace("_", "%")));
                variableMap.put("email", new ValueType(inboundMessage.getUser().getEmail()));
                for (String parameter : action.getParameters().keySet()) {
                    variableMap.put(parameter, new ValueType(action.getParameters().get(parameter)));
                }
                processInstanceClient.startProcessInstance(variableMap, info.getId());
            }
    
        } else {
            if(action!=null){
                processInstanceClient.setProcessVariable(instance.getId(),action.getAction(),"true");
            }
    
            for (String parameter : action.getParameters().keySet()) {
                processInstanceClient.setProcessVariable(instance.getId(),parameter,action.getParameters().get(parameter));
            }
            Task task = taskClient.getTask(instance.getId(), inboundMessage.getUser().getEmail());
            System.out.println(task.getId());
            taskClient.completeTask(task.getId());
