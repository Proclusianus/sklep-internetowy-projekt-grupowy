package wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce;

import jakarta.persistence.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.Sell_type;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Lob
    private String description;

    @Column
    private String category;


    @Column(name = "image_url")
    private String imageUrl;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private UserData seller;

    @Enumerated(EnumType.STRING)
    @Column(name = "sell_type")
    private Sell_type sellType;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Auction auction;

    public Product() {}



    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }


    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }


    public UserData getSeller() { return seller; }
    public void setSeller(UserData seller) { this.seller = seller; }

    public Sell_type getSellType() { return sellType; }
    public void setSellType(Sell_type sellType) { this.sellType = sellType; }

    public Auction getAuction() { return auction; }
    public void setAuction(Auction auction) { this.auction = auction; }
}