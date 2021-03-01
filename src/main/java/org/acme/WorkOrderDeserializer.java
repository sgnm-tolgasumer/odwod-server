package org.acme;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class WorkOrderDeserializer extends ObjectMapperDeserializer<WorkOrder>{
    public WorkOrderDeserializer(){
        // pass the class to the parent.
        super(WorkOrder.class);
    }
}
