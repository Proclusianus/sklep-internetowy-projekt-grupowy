package wat.grupa.trzy.wielkieakcjeitransakcje.services.teryt;

import org.springframework.stereotype.Service;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.teryt.DTO_Country;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.teryt.DTO_CountryAdminUnitLabels;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.teryt.DTO_Teryt;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt.Countries;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.teryt.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TerytService {
    private final CountriesRepository countriesRepository;
    private final TerytStreetRepository terytStreetRepository;
    private final TerytLocalityRepository terytLocalityRepository;
    private final TerytGminaRepository terytGminaRepository;
    private final TerytPowiatRepository terytPowiatRepository;
    private final TerytVoivodeshipRepository terytVoivodeshipRepository;


    public TerytService(
            CountriesRepository countriesRepository,
            TerytStreetRepository terytStreetRepository,
            TerytLocalityRepository terytLocalityRepository,
            TerytGminaRepository terytGminaRepository,
            TerytPowiatRepository terytPowiatRepository,
            TerytVoivodeshipRepository terytVoivodeshipRepository
    ){
        this.countriesRepository = countriesRepository;
        this.terytStreetRepository = terytStreetRepository;
        this.terytLocalityRepository = terytLocalityRepository;
        this.terytGminaRepository = terytGminaRepository;
        this.terytPowiatRepository = terytPowiatRepository;
        this.terytVoivodeshipRepository = terytVoivodeshipRepository;
    }

    public List<DTO_Country> getAllCountries() {
        List<Countries> countries = countriesRepository.findAllCountriesSortedByName();
        return countries.stream()
                .map(country -> new DTO_Country(
                        country.getNamePl(),
                        country.getCodeIsoAlpha2()
                ))
                .sorted(
                        Comparator.comparingInt((DTO_Country c) -> c.getNamePl().equalsIgnoreCase("POLSKA") ? 0 : 1)
                                .thenComparing(DTO_Country::getNamePl)
                )
                .collect(Collectors.toList());
    }

    public Optional<DTO_CountryAdminUnitLabels> getAdminLabelsForCountry(String countryCode) {
        return countriesRepository.findByCodeIsoAlpha2(countryCode)
                .map(country -> new DTO_CountryAdminUnitLabels(
                        country.getAdminAreaLevel1Label(),
                        country.getAdminAreaLevel2Label(),
                        country.getAdminAreaLevel3Label(),
                        country.getAdminAreaLevel4Label()
                ));
    }

    public List<DTO_Teryt> getAllVoivodeships() {
        return terytVoivodeshipRepository.findAllByOrderByNameAsc().stream()
                .map(v -> new DTO_Teryt(v.getCode(), v.getName()))
                .collect(Collectors.toList());
    }

    public List<DTO_Teryt> findPowiatsInVoivodeship(String voivodeshipCode) {
        return terytPowiatRepository.findByVoivodeshipCodeOrderByNameAsc(voivodeshipCode).stream()
                .map(p -> new DTO_Teryt(p.getCode(), p.getName()))
                .collect(Collectors.toList());
    }

    public List<DTO_Teryt> findGminasInPowiat(String powiatCode) {
        return terytGminaRepository.findByPowiatCodeOrderByNameAsc(powiatCode).stream()
                .map(g -> new DTO_Teryt(g.getCode(), g.getName()))
                .collect(Collectors.toList());
    }

    public List<DTO_Teryt> findLocalitiesInGmina(String gminaCode) {
        return terytLocalityRepository.findByGminaCodeOrderByNameAsc(gminaCode).stream()
                .map(l -> new DTO_Teryt(l.getSymbol(), l.getName()))
                .collect(Collectors.toList());
    }

    public List<DTO_Teryt> findStreetsInLocality(String localitySymbol) {
        return terytStreetRepository.findByLocalitySymbolOrderByStreetNameAsc(localitySymbol).stream()
                .map(s -> {
                    String streetType = s.getStreetType();
                    String streetName = s.getStreetName();
                    String fullName;
                    if (streetType != null && !streetType.equalsIgnoreCase("rondo")) {
                        fullName = streetType + " " + streetName;
                    } else {
                        // Jeśli typ to "rondo" lub jest null, użyj samej nazwy
                        fullName = streetName;
                    }
                    return new DTO_Teryt(s.getId().toString(), fullName);
                })
                .collect(Collectors.toList());
    }
}
