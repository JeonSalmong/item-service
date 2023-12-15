package apps.itemservice.web.domain.pay;

import apps.itemservice.platform.pay.PayClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayClient payClient;

    public void pay(int money) {
        payClient.pay(money);
    }
}
