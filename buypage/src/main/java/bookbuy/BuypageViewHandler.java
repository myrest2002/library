package bookbuy;

import bookbuy.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
public class BuypageViewHandler {


    @Autowired
    private BuypageRepository buypageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBought_then_CREATE_1 (@Payload Bought bought) {
        try {
            if (bought.isMe()) {
                System.out.println("##### listener in : " + bought.toJson());
                // view 객체 생성
                Buypage mypage = new Buypage();
                // view 객체에 이벤트의 Value 를 set 함
                mypage.setId(bought.getId());
                mypage.setMemberId(bought.getMemberId());
                mypage.setBookId(bought.getBookId());
                mypage.setBookStatus(bought.getReqState());
                // view 레파지토리 save
                buypageRepository.save(mypage);
                // Mypage Create 되었단 메시지 출력.. 삭제해도됨.
                System.out.println("##### listener out : " + bought.toJson());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBuyCancelled_then_UPDATE_1(@Payload BuyCancelled buyCancelled) {
        try {
            System.out.println("########## listener: " + buyCancelled.toJson());
            if (buyCancelled.isMe()) {
                // view 객체 조회
                System.out.println("############ listener in : " + buyCancelled.toJson());
                Optional<Buypage> mypageOptional = buypageRepository.findById(buyCancelled.getBookId());
                Buypage mypage = mypageOptional.get();

                //for(Mypage mypage  : mypageList){
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                mypage.setBookStatus(buyCancelled.getBookStatus());
                // view 레파지 토리에 save
                buypageRepository.save(mypage);
                //}

                // Mypage Update 되었단 메시지 출력.. 삭제해도됨.
                System.out.println("##### listener Update : " + buyCancelled.toJson());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}