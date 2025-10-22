package wat.grupa.trzy.wielkieakcjeitransakcje.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "register";
    }
}
