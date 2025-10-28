package wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment;

import jakarta.persistence.*;

@Entity
@Table(name = "personal_payment")
public class PersonalPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    public PersonalPayment() {}
}
