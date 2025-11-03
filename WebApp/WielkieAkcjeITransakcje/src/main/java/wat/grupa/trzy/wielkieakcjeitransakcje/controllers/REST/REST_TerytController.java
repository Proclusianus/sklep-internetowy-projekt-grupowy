package wat.grupa.trzy.wielkieakcjeitransakcje.controllers.REST;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.teryt.DTO_Country;
import wat.grupa.trzy.wielkieakcjeitransakcje.services.teryt.TerytService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class REST_TerytController {
    private final TerytService terytService;

    public REST_TerytController(TerytService countryService) {
        this.terytService = countryService;
    }

    @GetMapping("/countries")
    public List<DTO_Country> getCountries() {
        return terytService.getAllCountries();
    }
}
