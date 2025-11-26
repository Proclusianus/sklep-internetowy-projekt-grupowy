package wat.grupa.trzy.wielkieakcjeitransakcje.controllers.WebsiteMapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.payment.DTO_BankAccount;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.payment.DTO_Card;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.TransactionData;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.payment.Wallet;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.enums.E_TYP_KONTA;
import wat.grupa.trzy.wielkieakcjeitransakcje.other.security.CustomUserDetails;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce.OrderService;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.payment.PaymentService;

import java.awt.print.Pageable;

@Controller
public class MAP_UserController {
    private final OrderService orderService;
    private final PaymentService paymentService;
    public MAP_UserController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    /**
     * Metoda obsługująca stronę z danymi użytkownika.
     * @param userDetails Obiekt wstrzykiwany automatycznie przez Spring Security, zawierający dane zalogowanego użytkownika.
     * @param model Obiekt do przekazywania danych do widoku (pliku HTML).
     * @return Nazwa pliku HTML do wyświetlenia.
     */
    @GetMapping("/userData")
    public String getUserDataPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser());
        return "userpanel/userData";
    }

    @GetMapping("/wallet")
    public String getWalletPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        Wallet wallet = paymentService.getUserWallet(username);
        model.addAttribute("wallet", wallet);
        model.addAttribute("user", userDetails.getUser());

        return "userpanel/wallet";
    }
    @PostMapping("wallet/redeem-giftcard")
    public String redeemGiftCard(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestParam String code,
                                 RedirectAttributes redirectAttributes) {
        try {
            paymentService.redeemGiftCard(userDetails.getUsername(), code);
            redirectAttributes.addFlashAttribute("successMessage", "Pomyślnie zrealizowano kod! Środki dodane.");

        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Wystąpił nieoczekiwany błąd.");
        }

        return "redirect:/wallet";
    }
    @GetMapping("/wallet/add-card")
    public String showAddCardForm(Model model) {
        model.addAttribute("cardDto", new DTO_Card());
        return "userpanel/add-card";
    }
    @PostMapping("/wallet/add-card")
    public String processAddCard(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 DTO_Card cardDto,
                                 RedirectAttributes redirectAttributes) {
        try {
            paymentService.addCreditCard(userDetails.getUsername(), cardDto.getCardNumber());
            redirectAttributes.addFlashAttribute("successMessage", "Karta została pomyślnie dodana.");
            return "redirect:/wallet"; // Wracamy do głównego widoku portfela
        } catch (IllegalArgumentException e) {
            // Błąd walidacji (zły numer lub duplikat)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/wallet/add-card"; // Wracamy do formularza
        }
    }
    @GetMapping("wallet/add-bank-account")
    public String showAddBankAccountForm(Model model) {
        model.addAttribute("bankAccountDto", new DTO_BankAccount());
        return "userpanel/add-bank-account";
    }
    @PostMapping("wallet/add-bank-account")
    public String processAddBankAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        DTO_BankAccount dto,
                                        RedirectAttributes redirectAttributes) {
        try {
            paymentService.addBankAccount(userDetails.getUsername(), dto.getIban());
            redirectAttributes.addFlashAttribute("successMessage", "Konto bankowe zostało dodane.");
            return "redirect:/wallet";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/wallet/add-bank-account";
        }
    }
    @PostMapping("wallet/topup")
    public String topUpWallet(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @RequestParam("amount") Long amount,
                              @RequestParam(value = "methodId", required = false) String methodId, // required=false, żeby obsłużyć null ręcznie
                              RedirectAttributes redirectAttributes) {
        try {
            paymentService.topUpWallet(userDetails.getUsername(), amount, methodId);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Konto doładowane o " + amount + " Bobuxów!");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (SecurityException e) {
            // Poważny błąd bezpieczeństwa
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd autoryzacji metody płatności.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Wystąpił nieoczekiwany błąd.");
        }

        return "redirect:/wallet";
    }
    @PostMapping("wallet/delete-card")
    public String deleteCard(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @RequestParam("cardId") Long cardId,
                             RedirectAttributes redirectAttributes) {
        try {
            paymentService.deleteCard(userDetails.getUsername(), cardId);
            redirectAttributes.addFlashAttribute("successMessage", "Karta została usunięta.");
        } catch (SecurityException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Brak uprawnień do usunięcia tej karty.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Nie można usunąć karty (może być powiązana z historią transakcji).");
        }
        return "redirect:/wallet";
    }
    @PostMapping("wallet/delete-bank-account")
    public String deleteBankAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @RequestParam("accountId") Long accountId,
                                    RedirectAttributes redirectAttributes) {
        try {
            paymentService.deleteBankAccount(userDetails.getUsername(), accountId);
            redirectAttributes.addFlashAttribute("successMessage", "Konto bankowe zostało usunięte.");
        } catch (SecurityException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Brak uprawnień do usunięcia tego konta.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Nie można usunąć konta (może być powiązane z historią transakcji).");
        }
        return "redirect:/wallet";
    }
    @GetMapping("transactionHistory")
    public String getTransactionHistory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestParam(defaultValue = "0") int page, // Domyślnie strona 0 (pierwsza)
                                        Model model) {
        Page<TransactionData> transactionPage = paymentService.getUserTransactions(userDetails.getUsername(), page);
        model.addAttribute("transactionPage", transactionPage);
        model.addAttribute("user", userDetails.getUser());

        return "userpanel/transactionHistory";
    }

    @GetMapping("/activeOrders")
    public String getActiveOrdersPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser());
        return "userpanel/activeOrders";
    }

    @GetMapping("/activeAuctions")
    public String getActiveAuctionsPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser());
        return "userpanel/activeAuctions";
    }

    @GetMapping("/orderHistory")
    public String getOrderHistoryPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("orders", orderService.getUserOrders(userDetails.getUser()));
        return "userpanel/orderHistory";
    }

    @GetMapping("/auctionHistory")
    public String getAuctionHistoryPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser());
        return "userpanel/auctionHistory";
    }

    @GetMapping("/firmStats")
    public String getFirmStatsPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        UserData currentUser = userDetails.getUser();
        if (currentUser.getAccountType() != E_TYP_KONTA.FIRMA) {
            return "redirect:/userData";
        }

        model.addAttribute("user", currentUser);
        return "userpanel/firmStats";
    }

    @GetMapping("/order/details/{id}")
    public String getOrderDetailsPage(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser());
        try {
            model.addAttribute("order", orderService.getOrderDetails(id, userDetails.getUser()));
            return "userpanel/orderDetails";
        } catch (Exception e) {
            return "redirect:/orderHistory";
        }
    }
}