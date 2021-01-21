package library;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Rental_table")
public class Rental {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;         //예약번호
    private Long memberId;  // 사용자번호
    private Long bookId;    // 책번호
    private String reqState;//요청: "reserve", "cancel", "rental", "return"

    @PostPersist
    public void onPostPersist() {
        if (this.reqState.equals("reserve")) {
            Reserved reserved = new Reserved();
            BeanUtils.copyProperties(this, reserved);
            reserved.publishAfterCommit();


            //Following code causes dependency to external APIs
            // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.
            library.external.Payment payment = new library.external.Payment();
            // mappings goes here
            payment.setId(this.id);
            payment.setMemberId(this.memberId);
            payment.setBookId(this.bookId);
            payment.setReqState("reserve");

            RentalApplication.applicationContext.getBean(library.external.PaymentService.class)
                    .pay(payment);

        } else if (this.reqState.equals("buy")) {
            Bought bought = new Bought();
            BeanUtils.copyProperties(this, bought);
            bought.publishAfterCommit();


            //Following code causes dependency to external APIs
            // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.
            library.external.Payment2 payment2 = new library.external.Payment2();
            // mappings goes here
            payment2.setId(this.id);
            payment2.setMemberId(this.memberId);
            payment2.setBookId(this.bookId);
            payment2.setReqState("buy");

            RentalApplication.applicationContext.getBean(library.external.Payment2Service.class)
                    .pay(payment2);

        }
    }

    @PostUpdate
    public void onPostUpdate(){
        if (this.reqState.equals("cancel") ) {
            Cancelled cancelled = new Cancelled();
            BeanUtils.copyProperties(this, cancelled);
            cancelled.publishAfterCommit();
            System.out.println("cancelled" + cancelled.toJson());
        }  else if (this.reqState.equals("rental") ) {
            Rentaled rentaled = new Rentaled();
            BeanUtils.copyProperties(this, rentaled);
            rentaled.publishAfterCommit();
            System.out.println("rentaled" + rentaled.toJson());
        }  else if (this.reqState.equals("return") ) {
            Returned returned = new Returned();
            BeanUtils.copyProperties(this, returned);
            returned.publishAfterCommit();
            System.out.println("returned" + returned.toJson());
        }
        else if (this.reqState.equals("buycancel") ) {
            BuyCancelled buyCancelled = new BuyCancelled();
            BeanUtils.copyProperties(this, buyCancelled);
            buyCancelled.publishAfterCommit();
            System.out.println("returned" + buyCancelled.toJson());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    public String getReqState() {
        return reqState;
    }

    public void setReqState(String reqState) {
        this.reqState = reqState;
    }

}
