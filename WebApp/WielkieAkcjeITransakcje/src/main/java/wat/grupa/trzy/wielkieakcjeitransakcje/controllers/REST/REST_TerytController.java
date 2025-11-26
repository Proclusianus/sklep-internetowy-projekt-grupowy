package wat.grupa.trzy.wielkieakcjeitransakcje.controllers.REST;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.teryt.DTO_Country;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.teryt.DTO_CountryAdminUnitLabels;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.teryt.DTO_Teryt;
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

    @GetMapping("/countries/{countryCode}/admin-labels")
    public ResponseEntity<DTO_CountryAdminUnitLabels> getCountryLabels(@PathVariable String countryCode) {
        return terytService.getAdminLabelsForCountry(countryCode.toUpperCase())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("teryt/voivodeships")
    public List<DTO_Teryt> getVoivodeships() {
        return terytService.getAllVoivodeships();
    }

    @GetMapping("teryt/powiats")
    public List<DTO_Teryt> getPowiats(@RequestParam String voivodeshipCode) {
        return terytService.findPowiatsInVoivodeship(voivodeshipCode);
    }

    @GetMapping("teryt/gminas")
    public List<DTO_Teryt> getGminas(@RequestParam String powiatCode) {
        return terytService.findGminasInPowiat(powiatCode);
    }

    @GetMapping("teryt/localities")
    public List<DTO_Teryt> getLocalities(@RequestParam String gminaCode) {
        return terytService.findLocalitiesInGmina(gminaCode);
    }

    @GetMapping("teryt/streets")
    public List<DTO_Teryt> getStreets(@RequestParam String localitySymbol) {
        return terytService.findStreetsInLocality(localitySymbol);
    }
}
