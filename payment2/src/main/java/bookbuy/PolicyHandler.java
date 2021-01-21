package bookbuy;

import bookbuy.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @Autowired
    Payment2Repository payment2Repository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverBuyCancelled_(@Payload BuyCancelled buyCancelled){

        if(buyCancelled.isMe()){
            System.out.println("##### listener  : " + buyCancelled.toJson());
            if(buyCancelled.isMe()) {
                System.out.println("##### listener  : " + buyCancelled.toJson());


                payment2Repository.findById(buyCancelled.getBookId()).ifPresent(Payment2 -> {
                    Payment2.setReqState(buyCancelled.getReqState());
                    payment2Repository.save(Payment2);
                });
                System.out.println("##### listener Ship : " + buyCancelled.toJson());

            }
        }
    }

}
