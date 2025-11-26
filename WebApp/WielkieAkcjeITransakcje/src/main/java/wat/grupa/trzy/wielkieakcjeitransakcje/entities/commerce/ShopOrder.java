package wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce;

import jakarta.persistence.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.TransactionData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.BusinessData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shop_order")
public class ShopOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private UserData buyer;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private String status;

    @Column(name = "delivery_address_snapshot", columnDefinition = "TEXT")
    private String deliveryAddressSnapshot;

    @OneToMany(mappedBy = "shopOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "shopOrder", fetch = FetchType.LAZY)
    private List<TransactionData> transactions = new ArrayList<>();

    public ShopOrder() {}

    // GETTERY I SETTERY
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserData getBuyer() {
        return buyer;
    }

    public void setBuyer(UserData buyer) {
        this.buyer = buyer;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliveryAddressSnapshot() {
        return deliveryAddressSnapshot;
    }

    public void setDeliveryAddressSnapshot(String deliveryAddressSnapshot) {
        this.deliveryAddressSnapshot = deliveryAddressSnapshot;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<TransactionData> getTransactions() {
        return transactions;
    }
    public void setTransactions(List<TransactionData> transactions) {
        this.transactions = transactions;
    }
}