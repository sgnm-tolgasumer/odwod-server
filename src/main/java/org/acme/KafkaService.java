package org.acme;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class KafkaService {

    @Inject
    @Channel("pending")
    Emitter<WorkOrder> pendingEmitter;

    @Inject
    @Channel("in_progress")
    Emitter<WorkOrder> in_progressEmitter;

    @Inject
    @Channel("done")
    Emitter<WorkOrder> doneEmitter;

    @Inject
    @Channel("expired")
    Emitter<WorkOrder> expiredEmitter;

    public Properties consumerConfig() {
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092");
        // Just a user-defined string to identify the consumer group
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        // Enable auto offset commit
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "999999");
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        return props;
    }

    /**
     * It publishes the work order according to its status into the kafka topics.
     */
    public void publish(WorkOrder obj) {

        switch (obj.status) {
            case 0:
                pendingEmitter.send(obj);
                break;
            case 1:
                in_progressEmitter.send(obj);
                break;
            case 2:
                doneEmitter.send(obj);
                break;
            case 3:
                expiredEmitter.send(obj);
                break;
        }
    }

    /**
     * It searches and returns single work order by its id in specified topic.
     */
    public WorkOrder getSingle(String workOrderId, String topic) {

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig())) {
            // List of topics to subscribe to
            consumer.subscribe(Collections.singletonList(topic));

            try {
                consumer.seekToBeginning(consumer.assignment());
                ConsumerRecords<String, String> records = consumer.poll(100);

                for (ConsumerRecord<String, String> record : records) {

                    ObjectMapper objectMapper = new ObjectMapper();
                    WorkOrder workOrder = objectMapper.readValue(record.value(), WorkOrder.class);

                    if (workOrder.workOrderId.equals(workOrderId)) {
                        System.out.println(workOrder.toString());
                        return workOrder;

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    /**
     * It searches and returns all work orders according to worker's unique id.
     */
    public List<WorkOrder> getAllByWorkerId(String workerId) {

        List<WorkOrder> workOrders = new ArrayList<>();
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig())) {
            // List of topics to subscribe to
            consumer.subscribe(Collections.singletonList("in_progress"));

            try {
                consumer.seekToBeginning(consumer.assignment());
                ConsumerRecords<String, String> records = consumer.poll(100);

                for (ConsumerRecord<String, String> record : records) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    WorkOrder workOrder = objectMapper.readValue(record.value(), WorkOrder.class);

                    if (workOrder.workerId != null && workOrder.workerId.equals(workerId)) {
                        System.out.println(workOrder.toString());
                        workOrders.add(workOrder);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return workOrders;
    }

    /**
     * It searches and returns all work orders according to customer's unique id.
     */
    public List<WorkOrder> getAllByCustomerId(String userId) {
        final int giveUp = 5;
        int noRecordsCount = 0;
        List<WorkOrder> workOrders = new ArrayList<>();

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig())) {
            // List of topics to subscribe to
            consumer.subscribe(Collections.singletonList("pending"));

            try {
                consumer.seekToBeginning(consumer.assignment());
                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(100);

                    if (records.count() == 0) {
                        noRecordsCount++;
                        if (noRecordsCount > giveUp) break;
                        else continue;
                    }

                    for (ConsumerRecord<String, String> record : records) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        WorkOrder workOrder = objectMapper.readValue(record.value(), WorkOrder.class);
                        if (workOrder.userId.equals(userId)) {
                            System.out.println(workOrder.toString());
                            workOrders.add(workOrder);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                consumer.close();
            }

        }
        return workOrders;
    }

    /**
     * It transfers the given work order to one Kafka topic to another according to the given parameters.
     */
    public void transferWorder(String workOrderId, String sourceTopic, String targetTopic, String workerId) {
        WorkOrder objToTransfer = getSingle(workOrderId, sourceTopic);
        System.out.println(objToTransfer);

        switch (targetTopic) {
            case "pending":
                pendingEmitter.send(objToTransfer);
                break;
            case "in_progress":
                objToTransfer.workerId = workerId;
                in_progressEmitter.send(objToTransfer);
                break;
            case "done":
                ObjectMapper objectMapper = new ObjectMapper();
                DoneWorkOrderEntity doneWorkOrderEntity = objectMapper.convertValue(objToTransfer, DoneWorkOrderEntity.class);
                doneEmitter.send(objToTransfer);
                doneWorkOrderEntity.persist();
                break;
            case "expired":
                expiredEmitter.send(objToTransfer);
                break;
        }

    }

    /**
     * It gets the all work orders from specific Kafka topic according to given parameter.
     */
    public List<WorkOrder> getAll(String topicId) {
        List<WorkOrder> workOrders = new ArrayList<>();
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig())) {
            // List of topics to subscribe to
            consumer.subscribe(Collections.singletonList(topicId));

            try {
                consumer.seekToBeginning(consumer.assignment());
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    WorkOrder workOrder = objectMapper.readValue(record.value(), WorkOrder.class);

                    workOrders.add(workOrder);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return workOrders;
    }

       /* public void publishAsJson(Object obj, int channelId) {
        String objJson = null;
        try {
            objJson = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // Push created Json to Kafka stream
        //employeesEmitter.send(objJson);
        //TODO switch
        if(channelId == 0) {
            pendingEmitter.send(objJson);
        }
        else if(channelId == 1){
            in_progressEmitter.send(objJson);
        }
        else if(channelId == 2){
            doneEmitter.send((objJson));
        }
        else{
            expiredEmitter.send((objJson));
        }
    }*/
   /*
   System.out.printf("Offset = %d\n", record.offset());
   System.out.printf("Key    = %s\n", record.key());
   System.out.printf("Value  = %s\n", record.value());
   */
}
