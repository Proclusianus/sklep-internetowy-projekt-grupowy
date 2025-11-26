package wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment;

import jakarta.persistence.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Auction;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.ShopOrder;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.BankAccountData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.CardData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.GiftCardData;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.TRANSACTION_TYPE;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "transaction_history")
public class TransactionData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "description")
    private String desc;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "balance_after")
    private Long balanceAfter;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TRANSACTION_TYPE transactionType;

    // Relacje
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_order_id")
    private ShopOrder shopOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_bank_account_id")
    private BankAccountData usedBankAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_card_id")
    private CardData usedCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giftcard_id")
    private GiftCardData usedGiftCard;

    public TransactionData() {}

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        }
    }

    // Getters
    public Long getId() { return id; }
    public Long getAmount() { return amount; }
    public String getDesc() { return desc; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public Long getBalanceAfter() { return balanceAfter; }
    public Wallet getWallet() { return wallet; }
    public TRANSACTION_TYPE getTransactionType() { return transactionType; }
    public ShopOrder getShopOrder() { return shopOrder; }
    public BankAccountData getUsedBankAccount() { return usedBankAccount; }
    public CardData getUsedCard() { return usedCard; }
    public GiftCardData getUsedGiftCard() { return usedGiftCard; }


    // Setters
    public void setAmount(Long amount) { this.amount = amount; }
    public void setDesc(String desc) { this.desc = desc; }
    public void setBalanceAfter(Long balanceAfter) { this.balanceAfter = balanceAfter; }
    public void setWallet(Wallet wallet) { this.wallet = wallet; }
    public void setTransactionType(TRANSACTION_TYPE transactionType) { this.transactionType = transactionType; }
    public void setShopOrder(ShopOrder shopOrder) { this.shopOrder = shopOrder; }
    public void setUsedBankAccount(BankAccountData usedBankAccount) { this.usedBankAccount = usedBankAccount; }
    public void setUsedCard(CardData usedCard) { this.usedCard = usedCard; }
    public void setUsedGiftCard(GiftCardData usedGiftCard) { this.usedGiftCard = usedGiftCard; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}