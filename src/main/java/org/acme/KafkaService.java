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
import java.util.Arrays;
import java.util.Properties;

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

    public Properties consumerConfig(){
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

    public void publish(WorkOrder obj){

        switch (obj.status){
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

    public String getSingle(Long id) {

        String response = " ";
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig())) {
            // List of topics to subscribe to
            consumer.subscribe(Arrays.asList("pending"));

            try {
                consumer.seekToBeginning(consumer.assignment());
                ConsumerRecords<String, String> records = consumer.poll(100);
                //TODO consumer.seek(consumer.assignment(), 22);
                for (ConsumerRecord<String, String> record : records) {
                    if(record.offset() == id) {
                        response = record.value();
                    }
                    System.out.printf("Offset = %d\n", record.offset());
                    System.out.printf("Key    = %s\n", record.key());
                    System.out.printf("Value  = %s\n", record.value());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return response;
    }

    public String getAll(String topicId) {
        String response = " ";
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig())) {
            // List of topics to subscribe to
            consumer.subscribe(Arrays.asList(topicId));

            try {
                consumer.seekToBeginning(consumer.assignment());
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Offset = %d\n", record.offset());
                    System.out.printf("Key    = %s\n", record.key());
                    System.out.printf("Value  = %s\n", record.value());
                    response += record.value();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return response;
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
}
