package wat.grupa.trzy.wielkieakcjeitransakcje.controllers.REST;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.payment.PaymentService;

@RestController
@RequestMapping("/api")
public class REST_PaymentController {
    private final PaymentService paymentService;

    public REST_PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
