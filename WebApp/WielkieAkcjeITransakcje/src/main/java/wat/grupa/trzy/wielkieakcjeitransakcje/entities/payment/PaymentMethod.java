package wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_method")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nazwa_metody", nullable = false)
    private String nazwaMetody;

    public PaymentMethod() {}
}
