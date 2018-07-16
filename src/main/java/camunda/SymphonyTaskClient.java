package camunda;

import camunda.model.CamundaConfig;
import camunda.model.parameter.MessageParameters;
import camunda.model.parameter.RoomParameters;
import clients.SymBotClient;
import exceptions.SymClientException;
import model.*;
import org.camunda.bpm.client.ExternalTaskClient;

import javax.ws.rs.core.NoContentException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymphonyTaskClient {

    private SymBotClient botClient;
    private ExternalTaskClient camundaClient;
    private CamundaConfig config;


    public SymphonyTaskClient(SymBotClient botClient, CamundaConfig config) {
        this.botClient = botClient;
        this.config = config;
        this.camundaClient = ExternalTaskClient.create()
                .baseUrl(config.getCamundaURL())
                .build();
    }

    public void initCreateRoomTask(){
        camundaClient.subscribe("symphony-create-room")
                .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    Room room = new Room();
                    room.setName(externalTask.getVariable(RoomParameters.NAME));
                    room.setDescription(externalTask.getVariable(RoomParameters.DESCRIPTION));
                    if(externalTask.getVariable(RoomParameters.MEMBERSCANINVITE)!=null){
                        room.setMembersCanInvite((Boolean) externalTask.getVariableTyped(RoomParameters.MEMBERSCANINVITE).getValue());
                    }
                    if(externalTask.getVariable(RoomParameters.DISCOVERABLE)!=null){
                        room.setDiscoverable((Boolean) externalTask.getVariableTyped(RoomParameters.DISCOVERABLE).getValue());
                    }
                    if(externalTask.getVariable(RoomParameters.CROSSPOD)!=null){
                        room.setCrossPod((Boolean) externalTask.getVariableTyped(RoomParameters.CROSSPOD).getValue());
                    }
                    if(externalTask.getVariable(RoomParameters.VIEWHISTORY)!=null){
                        room.setViewHistory((Boolean) externalTask.getVariableTyped(RoomParameters.VIEWHISTORY).getValue());
                    }
                    if(externalTask.getVariable(RoomParameters.COPYPROTECTED)!=null){
                        room.setCopyProtected((Boolean) externalTask.getVariableTyped(RoomParameters.COPYPROTECTED).getValue());
                    }
                    if(externalTask.getVariable(RoomParameters.PUBLIC)!=null){
                        room.setMembersCanInvite((Boolean)externalTask.getVariableTyped(RoomParameters.PUBLIC).getValue());
                    }
                    if(externalTask.getVariable(RoomParameters.READONLY)!=null){
                        room.setMembersCanInvite((Boolean)externalTask.getVariableTyped(RoomParameters.READONLY).getValue());
                    }
                    List<String> usersToAdd = null;
                    if(externalTask.getVariableTyped(RoomParameters.MEMBERSTOADD)!=null){
                        usersToAdd = (List<String>) externalTask.getVariableTyped(RoomParameters.MEMBERSTOADD).getValue();
                    }
                    RoomInfo roomInfo;
                    try {
                        roomInfo = botClient.getStreamsClient().createRoom(room);
                        Map<String, Object> outVariables = new HashMap<>();
                        outVariables.put(RoomParameters.ID, roomInfo.getRoomSystemInfo().getId());
                        if(usersToAdd!=null) {
                            for (String user : usersToAdd) {
                                botClient.getStreamsClient().addMemberToRoom(roomInfo.getRoomSystemInfo().getId(), botClient.getUsersClient().getUserFromEmail(user, false).getId());
                            }
                        }
                        externalTaskService.complete(externalTask,outVariables);
                    } catch (SymClientException | NoContentException e) {
                        e.printStackTrace();
                    }
                }).open();
    }

    public void initSendRoomMessageTask(){
        camundaClient.subscribe("symphony-send-room-message")
                .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    OutboundMessage message = new OutboundMessage();
                    message.setMessage(externalTask.getVariable(MessageParameters.MESSAGECONTENT));
                    try {
                        InboundMessage inboundMessage = botClient.getMessagesClient().sendMessage(externalTask.getVariable(MessageParameters.ROOMMESSAGERECIPIENTID),message);
                        Map<String, Object> outVariables = new HashMap<>();
                        outVariables.put("sentMessageId", inboundMessage.getMessageId());
                        externalTaskService.complete(externalTask,outVariables);
                    } catch (SymClientException e) {
                        e.printStackTrace();
                    }
                }).open();
    }

    public void initSendIMMessageTask(){
        camundaClient.subscribe("symphony-send-im-message")
                .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    OutboundMessage message = new OutboundMessage();
                    message.setMessage(externalTask.getVariable(MessageParameters.MESSAGECONTENT));
                    try {
                        InboundMessage inboundMessage = botClient.getMessagesClient().sendMessage(botClient.getStreamsClient().getUserIMStreamId(botClient.getUsersClient().getUserFromEmail(externalTask.getVariable(MessageParameters.IMMESSAGERECIPIENTEMAIL),false).getId()),message);
                        Map<String, Object> outVariables = new HashMap<>();
                        outVariables.put("sentMessageId", inboundMessage.getMessageId());
                        externalTaskService.complete(externalTask,outVariables);
                    } catch (SymClientException e) {
                        e.printStackTrace();
                    } catch (NoContentException e) {
                        e.printStackTrace();
                    }
                }).open();
    }

    public void initManageRoomTask(){
        camundaClient.subscribe("symphony-manage-room")
                .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {
                    String roomId = externalTask.getVariable(RoomParameters.ID);
                    Room room = new Room();
                    if(externalTask.getVariable(RoomParameters.NAME)!=null){
                        room.setName(externalTask.getVariable(RoomParameters.NAME));
                    }
                    if(externalTask.getVariable(RoomParameters.DESCRIPTION)!=null){
                        room.setDescription(externalTask.getVariable(RoomParameters.DESCRIPTION));
                    }
                    if(externalTask.getVariable(RoomParameters.MEMBERSCANINVITE)!=null){
                        room.setMembersCanInvite((Boolean) externalTask.getVariableTyped(RoomParameters.MEMBERSCANINVITE).getValue());
                    }
                    if(externalTask.getVariable(RoomParameters.DISCOVERABLE)!=null){
                        room.setDiscoverable((Boolean) externalTask.getVariableTyped(RoomParameters.DISCOVERABLE).getValue());
                    }
                    if(externalTask.getVariable(RoomParameters.CROSSPOD)!=null){
                        room.setCrossPod((Boolean) externalTask.getVariableTyped(RoomParameters.CROSSPOD).getValue());
                    }
                    if(externalTask.getVariable(RoomParameters.VIEWHISTORY)!=null){
                        room.setViewHistory((Boolean) externalTask.getVariableTyped(RoomParameters.VIEWHISTORY).getValue());
                    }
                    if(externalTask.getVariable(RoomParameters.COPYPROTECTED)!=null){
                        room.setCopyProtected((Boolean) externalTask.getVariableTyped(RoomParameters.COPYPROTECTED).getValue());
                    }
                    List<String> usersToAdd = null;
                    if(externalTask.getVariableTyped(RoomParameters.MEMBERSTOADD)!=null){
                        usersToAdd = (List<String>) externalTask.getVariableTyped(RoomParameters.MEMBERSTOADD).getValue();
                    }
                    List<String> usersToRemove = null;
                    if(externalTask.getVariableTyped(RoomParameters.MEMBERSTOREMOVE)!=null){
                        usersToRemove = (List<String>) externalTask.getVariableTyped(RoomParameters.MEMBERSTOREMOVE).getValue();
                    }
                    RoomInfo roomInfo;
                    try {
                        roomInfo = botClient.getStreamsClient().updateRoom(roomId, room);
                        if(usersToAdd!=null) {
                            for (String user : usersToAdd) {
                                botClient.getStreamsClient().addMemberToRoom(roomInfo.getRoomSystemInfo().getId(), botClient.getUsersClient().getUserFromEmail(user, false).getId());
                            }
                        }
                        if(usersToRemove!=null) {
                            for (String user : usersToRemove) {
                                botClient.getStreamsClient().removeMemberFromRoom(roomInfo.getRoomSystemInfo().getId(), botClient.getUsersClient().getUserFromEmail(user, false).getId());
                            }
                        }
                        externalTaskService.complete(externalTask);
                    } catch (SymClientException | NoContentException e) {
                        e.printStackTrace();
                    }
                }).open();
    }
}
