package wat.grupa.trzy.wielkieakcjeitransakcje.controllers.WebsiteMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MAP_UserController {

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
