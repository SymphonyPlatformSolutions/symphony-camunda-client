package camunda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

public abstract class CamundaClient {


    private final Logger logger = LoggerFactory.getLogger(CamundaClient.class);

    void handleError(Response response) throws CamundaClientException {
        try {
            if (response.getStatus() == 400){
                logger.error("Client error occurred");
                throw new CamundaClientException(response.getStatusInfo().getReasonPhrase());
            } else if (response.getStatus() == 401){
                logger.error("User unauthorized");
                throw new CamundaClientException(response.getStatusInfo().getReasonPhrase());
            } else if (response.getStatus() == 403){
                logger.error("Forbidden");
                throw new CamundaClientException(response.getStatusInfo().getReasonPhrase());
            } else if (response.getStatus() == 500) {
                logger.error("Internal server error");
                throw new CamundaClientException(response.getStatusInfo().getReasonPhrase());
            }
        } catch (Exception e){
            logger.error("Unexpected error");
            e.printStackTrace();
        }

    }
}
