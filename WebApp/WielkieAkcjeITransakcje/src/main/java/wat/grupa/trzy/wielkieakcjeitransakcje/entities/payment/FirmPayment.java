package wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment;

import jakarta.persistence.*;

@Entity
@Table(name = "firm_payment")
public class FirmPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    public FirmPayment() {}
}
