package wat.grupa.trzy.wielkieakcjeitransakcje.services.teryt;

import org.springframework.stereotype.Service;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.teryt.DTO_Country;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt.Countries;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.TerytRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TerytService {
    private final TerytRepository terytRepository;

    public TerytService(TerytRepository terytRepository) {
        this.terytRepository = terytRepository;
    }

    public List<DTO_Country> getAllCountries() {
        List<Countries> countries = terytRepository.findAllByOrderByNamePlAsc();

        return countries.stream()
                .map(country -> new DTO_Country(country.getNamePl(), country.getCodeIsoAlpha2()))
                .collect(Collectors.toList());
    }
}
