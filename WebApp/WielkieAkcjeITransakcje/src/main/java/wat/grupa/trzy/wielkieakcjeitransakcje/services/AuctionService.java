package wat.grupa.trzy.wielkieakcjeitransakcje.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Auction;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Oferta;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.AuctionRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.OfertaRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.ProductRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.UserDataRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private OfertaRepository ofertaRepository;
    @Autowired
    private UserDataRepository userDataRepository;
    @Autowired
    private ProductRepository productRepository;

    public Optional<Auction> getAuctionById(Long id) {
        return auctionRepository.findById(id);
    }

    @Transactional
    public void sprawdzCzyZakonczona(Auction auction) {
        if (auction.isAktywna() && auction.getDataZakonczenia() != null) {
            if (LocalDateTime.now().isAfter(auction.getDataZakonczenia())) {
                zakonczAukcje(auction);
            }
        }
    }

    @Transactional
    public boolean zlozOferte(Long auctionId, Long userId, BigDecimal kwota) {
        Auction auction = auctionRepository.findById(auctionId).orElse(null);
        UserData user = userDataRepository.findById(userId).orElse(null);


        if (auction == null || user == null || !auction.isAktywna()) {
            return false;
        }


        if (auction.getDataZakonczenia() != null && LocalDateTime.now().isAfter(auction.getDataZakonczenia())) {
            zakonczAukcje(auction);
            return false;
        }


        BigDecimal aktualnaCena = auction.getAktualnaCena() != null ? auction.getAktualnaCena() : BigDecimal.ZERO;


        if (kwota.compareTo(aktualnaCena) > 0) {
            Oferta nowaOferta = new Oferta(auction, user, kwota);
            ofertaRepository.save(nowaOferta);

            auction.setAktualnaCena(kwota);
            auctionRepository.save(auction);
            return true;
        }
        return false;
    }

    private void zakonczAukcje(Auction auction) {
        auction.setAktywna(false);

        List<Oferta> oferty = auction.getOferty();

        if (!oferty.isEmpty()) {
            Oferta wygranaOferta = oferty.get(0);
            UserData zwyciezca = wygranaOferta.getLicytujacy();
            Product product = auction.getProduct();


            product.setSeller(zwyciezca);


            productRepository.save(product);

            System.out.println("Auction " + auction.getId() + " zakończona. Wygrał: " + zwyciezca.getUsername());
        } else {
            System.out.println("Auction " + auction.getId() + " zakończona bez ofert.");
        }

        auctionRepository.save(auction);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void automatZamykaniaAukcji() {
        List<Auction> aktywneAukcje = auctionRepository.findAll();

        for (Auction auction : aktywneAukcje) {
            if (auction.isAktywna() && auction.getDataZakonczenia() != null) {
                if (LocalDateTime.now().isAfter(auction.getDataZakonczenia())) {
                    zakonczAukcje(auction);
                }
            }
        }
    }
}