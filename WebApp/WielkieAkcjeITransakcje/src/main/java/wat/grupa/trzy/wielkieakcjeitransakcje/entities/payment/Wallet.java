package wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment;

import jakarta.persistence.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.BankAccountData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.methods.CardData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;

import java.util.List;

@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance", nullable = false)
    private Long balance = 0L;

    @Column(name = "version")
    @Version
    private Long version;

    // Relacje
    @OneToOne(mappedBy = "wallet")
    private UserData userData;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionData> transactionDataList;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankAccountData> ownedBankAccounts;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardData> ownedCards;

    public Wallet() {}

    public void addTransaction(TransactionData td) {
        transactionDataList.add(td);
        td.setWallet(this);
        balance = td.getBalanceAfter();
    }

    // Getters
    public Long getId() { return id; }
    public Long getBalance() { return balance; }
    public Long getVersion() { return version; }
    public UserData getUserData() { return userData; }
    public List<TransactionData> getTransactionDataList() { return transactionDataList; }
    public List<BankAccountData> getOwnedBankAccounts() { return ownedBankAccounts; }
    public List<CardData> getOwnedCards() { return ownedCards; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUserData(UserData userData) { this.userData = userData; }
    public void setTransactionDataList(List<TransactionData> transactionDataList) { this.transactionDataList = transactionDataList; }
    public void setOwnedBankAccounts(List<BankAccountData> ownedBankAccounts) { this.ownedBankAccounts = ownedBankAccounts; }
    public void setOwnedCards(List<CardData> ownedCards) { this.ownedCards = ownedCards; }
}
