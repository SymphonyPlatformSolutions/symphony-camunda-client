package camunda.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class StreamIdValue {
    public StreamIdValue() {
    }

    private ValueType streamId;

    public StreamIdValue(String value) {
        ValueType streamId = new ValueType(value);
        this.streamId = streamId;
    }

    public ValueType getStreamId() {
        return streamId;
    }

    public void setStreamId(ValueType streamId) {
        this.streamId = streamId;
    }

}


