package wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods;

import jakarta.persistence.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.Wallet;

@Entity
@Table(name = "bank_Account_data")
public class BankAccountData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iban")
    private String iban;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    public BankAccountData() {}

    public Long getId() { return id; }
    public String getIban() { return iban; }
    public Wallet getWallet() { return wallet; }

    public void setId(Long id) { this.id = id; }
    public void setIban(String iban) { this.iban = iban; }
    public void setWallet(Wallet wallet) { this.wallet = wallet; }
}
