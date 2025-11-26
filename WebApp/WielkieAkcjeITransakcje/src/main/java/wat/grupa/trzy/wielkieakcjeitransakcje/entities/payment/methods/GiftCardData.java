package wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods;

import jakarta.persistence.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.TransactionData;

import java.util.List;

@Entity
@Table(name = "gift_card_data")
public class GiftCardData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "amount_to_add", nullable = false)
    private Long amountToAdd;

    // Relacje
    @OneToMany(mappedBy = "usedGiftCard", fetch = FetchType.LAZY)
    private List<TransactionData> transactions;

    public GiftCardData() {}

    // Getters
    public Long getId() { return id; }
    public String getCode() { return code; }
    public Long getAmountToAdd() { return amountToAdd; }
    public List<TransactionData> getTransactions() { return transactions; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setAmountToAdd(Long amountToAdd) { this.amountToAdd = amountToAdd; }
    public void setTransactions(List<TransactionData> transactions) { this.transactions = transactions; }
}
