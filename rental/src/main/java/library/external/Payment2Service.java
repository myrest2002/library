
package library.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@FeignClient(name="payment", url="http://payment:8080")
@FeignClient(name="payment2", url="${api.payment2.url}")
public interface Payment2Service {

    @RequestMapping(method= RequestMethod.POST, path="/payment2s")
    public void pay(@RequestBody Payment2 payment2);

}
