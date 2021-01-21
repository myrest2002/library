package bookbuy;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Payment2_table")
public class Payment2 {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long bookId;
    private Long memberId;
    private String reqState;
    @PrePersist
    public void onPostPersist(){
        BuyPaid paid = new BuyPaid();
        BeanUtils.copyProperties(this, paid);
        paid.publishAfterCommit();

//        try {
//            Thread.currentThread().sleep((long) (400 + Math.random() * 220));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }



    }
    @PostUpdate
    public void onPostUpdate(){
        BuyCancelled buycancelled = new BuyCancelled();
        BeanUtils.copyProperties(this, buycancelled);
        buycancelled.publishAfterCommit();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getBookId() {
        return bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    public Long getMemberId() {
        return memberId;
    }
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public String getReqState() {
        return reqState;
    }
    public void setReqState(String reqState) {
        this.reqState = reqState;
    }




}
