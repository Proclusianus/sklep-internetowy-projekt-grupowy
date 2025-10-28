package wat.grupa.trzy.wielkieakcjeitransakcje.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.UserService;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userData")
    public String getUserDataPage() {
        return "userpanel/userData";
    }

    @GetMapping("/wallet")
    public String getWalletPage() {
        return "userpanel/wallet";
    }

    @GetMapping("/activeOrders")
    public String getActiveOrdersPage() {
        return "userpanel/activeOrders";
    }

    @GetMapping("/activeAuctions")
    public String getActiveAuctionsPage() {
        return "userpanel/activeAuctions";
    }

    @GetMapping("/orderHistory")
    public String getOrderHistoryPage() {
        return "userpanel/orderHistory";
    }

    @GetMapping("/auctionHistory")
    public String getAuctionHistoryPage() {
        return "userpanel/auctionHistory";
    }

    @GetMapping("/firmStats")
    public String getFirmStatsPage() {
        return "userpanel/firmStats";
    }
}
