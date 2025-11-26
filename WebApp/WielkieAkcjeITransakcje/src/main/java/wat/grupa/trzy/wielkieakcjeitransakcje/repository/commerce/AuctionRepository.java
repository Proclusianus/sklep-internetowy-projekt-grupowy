package wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce;


import org.springframework.data.jpa.repository.JpaRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
