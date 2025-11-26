package wat.grupa.trzy.wielkieakcjeitransakcje.controllers.WebsiteMapping;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.commerce.DTO_CheckoutDetails;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.ShopOrder;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.Wallet;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.CustomUserDetails;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.user.UserDataRepository;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce.OrderService;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.payment.PaymentService;

@Controller
@RequestMapping("/checkout")
public class MAP_CheckoutController {

    private final OrderService orderService;
    private final UserDataRepository userDataRepository;
    private final PaymentService paymentService;

    public MAP_CheckoutController(OrderService orderService, UserDataRepository userDataRepository, PaymentService paymentService) {
        this.orderService = orderService;
        this.userDataRepository = userDataRepository;
        this.paymentService = paymentService;
    }

    @GetMapping("/details")
    public String getCheckoutDetailsPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        UserData user = userDataRepository.findWithDetailsByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika"));

        DTO_CheckoutDetails details = new DTO_CheckoutDetails();

        if (user.getPersonalData() != null) {
            details.setImie(user.getPersonalData().getName());
            details.setNazwisko(user.getPersonalData().getSurname());
            details.setTelefon(user.getPersonalData().getPhoneNumber());

            if (!user.getPersonalData().getAddresses().isEmpty()) {
                var adres = user.getPersonalData().getAddresses().get(0);
                details.setUlica(adres.getStreet());
                details.setNumerDomu(adres.getHouseNumber());
                details.setKodPocztowy(adres.getZipcode());
                details.setMiasto(adres.getCity());
            }
        }
        model.addAttribute("details", details);
        return "checkout/details";
    }

    @PostMapping("/details")
    public String processDetails(@ModelAttribute DTO_CheckoutDetails details,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 HttpSession session) {
        try {
            Long orderId = orderService.createOrder(userDetails.getUser(), details);
            session.setAttribute("currentOrderId", orderId);
            return "redirect:/checkout/payment";
        } catch (IllegalStateException e) {
            return "redirect:/koszyk";
        }
    }

    @GetMapping("/payment")
    public String getPaymentPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 HttpSession session,
                                 Model model) {
        Long orderId = (Long) session.getAttribute("currentOrderId");
        if (orderId == null) {
            return "redirect:/koszyk";
        }

        ShopOrder order = orderService.getOrderDetails(orderId, userDetails.getUser());

        if ("PAID".equals(order.getStatus()) || "OPLACONE".equals(order.getStatus())) {
            return "redirect:/orderHistory";
        }

        Wallet wallet = paymentService.getUserWallet(userDetails.getUsername());

        model.addAttribute("order", order);
        model.addAttribute("wallet", wallet);

        boolean canAfford = wallet.getBalance() >= order.getTotalAmount().longValue();
        model.addAttribute("canAfford", canAfford);

        return "checkout/payment";
    }

    @PostMapping("/complete")
    public String completeOrder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                HttpSession session,
                                @RequestParam(value = "methodId", required = false) String methodId,
                                RedirectAttributes redirectAttributes) {
        Long orderId = (Long) session.getAttribute("currentOrderId");
        if (orderId == null) {
            return "redirect:/orderHistory";
        }

        try {
            ShopOrder order = orderService.getOrderDetails(orderId, userDetails.getUser());

            paymentService.processPayment(order.getId(), methodId);

            session.removeAttribute("currentOrderId");

            redirectAttributes.addFlashAttribute("successMessage", "Płatność przyjęta! Dziękujemy za zakupy.");
            return "redirect:/orderHistory";

        } catch (IllegalArgumentException | IllegalStateException | SecurityException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd płatności: " + e.getMessage());
            return "redirect:/checkout/payment";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/orderHistory";
        }
    }
}