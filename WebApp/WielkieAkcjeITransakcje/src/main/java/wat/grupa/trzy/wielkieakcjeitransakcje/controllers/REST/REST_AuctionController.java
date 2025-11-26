package wat.grupa.trzy.wielkieakcjeitransakcje.controllers.REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Auction;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.UserDataRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.AuctionService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Collections;

@Controller
@RequestMapping("/auction")
public class REST_AuctionController {

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private UserDataRepository userDataRepository;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("cartItemCount", 0);
        model.addAttribute("cartItemsPreview", Collections.emptyList());
    }

    @GetMapping("/szczegoly/{id}")
    public String szczegolyAukcji(@PathVariable Long id, Model model) {

        Auction auction = auctionService.getAuctionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowe ID aukcji: " + id));


        auctionService.sprawdzCzyZakonczona(auction);

        model.addAttribute("auction", auction);
        model.addAttribute("offers", auction.getOferty());

        return "auctionSzczegoly";
    }

    @PostMapping("/licytuj/{id}")
    public String licytuj(@PathVariable Long id,
                          @RequestParam BigDecimal kwota,
                          Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        UserData user = userDataRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/auction/szczegoly/" + id + "?error=BladUzytkownika";
        }

        boolean sukces = auctionService.zlozOferte(id, user.getId(), kwota);

        if (!sukces) {

            return "redirect:/auction/szczegoly/" + id + "?error=OfertaOdrzucona";
        }

        return "redirect:/auction/szczegoly/" + id + "?success=OfertaPrzyjeta";
    }
}