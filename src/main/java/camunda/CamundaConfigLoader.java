package camunda;

import camunda.model.CamundaConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class CamundaConfigLoader {


    public static CamundaConfig loadFromFile(String path){
        ObjectMapper mapper = new ObjectMapper();
        CamundaConfig config = null;
        try {
            config = mapper.readValue(new File(path), CamundaConfig.class);
            return config;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }
}
