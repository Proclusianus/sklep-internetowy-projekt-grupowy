package wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods;

import jakarta.persistence.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.Wallet;

@Entity
@Table(name = "card_data")
public class CardData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number_masked")
    private String maskedNumber;

    // Relacje
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    public CardData() {}

    // Getters
    public Long getId() { return id; }
    public String getMaskedNumber() { return maskedNumber; }
    public Wallet getWallet() { return wallet; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setMaskedNumber(String maskedNumber) { this.maskedNumber = maskedNumber; }
    public void setWallet(Wallet wallet) { this.wallet = wallet; }
}
