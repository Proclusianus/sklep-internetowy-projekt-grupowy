package wat.grupa.trzy.wielkieakcjeitransakcje.services.teryt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.teryt.DTO_Country;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.teryt.DTO_CountryAdminUnitLabels;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.teryt.DTO_Teryt;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt.Countries;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt.TerytPowiat;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt.TerytVoivodeship;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.teryt.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TerytServiceTest {

    @Mock private CountriesRepository countriesRepository;
    @Mock private TerytStreetRepository terytStreetRepository;
    @Mock private TerytLocalityRepository terytLocalityRepository;
    @Mock private TerytGminaRepository terytGminaRepository;
    @Mock private TerytPowiatRepository terytPowiatRepository;
    @Mock private TerytVoivodeshipRepository terytVoivodeshipRepository;

    @InjectMocks
    private TerytService terytService;

    @Test
    @DisplayName("getAllCountries: Powinien zwrócić listę krajów z Polską na pierwszym miejscu")
    void getAllCountries_ShouldReturnSortedList() {
        // GIVEN
        Countries germany = new Countries();
        germany.setNamePl("Niemcy");
        germany.setCodeIsoAlpha2("DE");

        Countries poland = new Countries();
        poland.setNamePl("POLSKA");
        poland.setCodeIsoAlpha2("PL");

        Countries albania = new Countries();
        albania.setNamePl("Albania");
        albania.setCodeIsoAlpha2("AL");

        when(countriesRepository.findAllCountriesSortedByName())
                .thenReturn(Arrays.asList(albania, germany, poland)); // Kolejność z bazy może być alfabetyczna

        // WHEN
        List<DTO_Country> result = terytService.getAllCountries();

        // THEN
        assertEquals(3, result.size());
        assertEquals("POLSKA", result.get(0).getNamePl(), "Polska musi być pierwsza");
        assertEquals("Albania", result.get(1).getNamePl(), "Reszta powinna być alfabetycznie");
        assertEquals("Niemcy", result.get(2).getNamePl());
    }

    @Test
    @DisplayName("getAdminLabelsForCountry: Powinien zwrócić etykiety dla istniejącego kraju")
    void getAdminLabelsForCountry_ShouldReturnLabels() {
        // GIVEN
        String countryCode = "US";
        Countries us = new Countries();
        us.setCodeIsoAlpha2("US");
        us.setAdminAreaLevel1Label("Stan");
        us.setAdminAreaLevel2Label("Hrabstwo");

        when(countriesRepository.findByCodeIsoAlpha2("US")).thenReturn(Optional.of(us));

        // WHEN
        Optional<DTO_CountryAdminUnitLabels> result = terytService.getAdminLabelsForCountry(countryCode);

        // THEN
        assertTrue(result.isPresent());
        assertEquals("Stan", result.get().getLevel1());
        assertEquals("Hrabstwo", result.get().getLevel2());
    }

    @Test
    @DisplayName("getAllVoivodeships: Powinien zmapować encje na DTO")
    void getAllVoivodeships_ShouldMapToDto() {
        // GIVEN
        TerytVoivodeship mazowieckie = new TerytVoivodeship();
        mazowieckie.setCode("14");
        mazowieckie.setName("MAZOWIECKIE");

        when(terytVoivodeshipRepository.findAllByOrderByNameAsc())
                .thenReturn(Collections.singletonList(mazowieckie));

        // WHEN
        List<DTO_Teryt> result = terytService.getAllVoivodeships();

        // THEN
        assertEquals(1, result.size());
        assertEquals("14", result.get(0).getCode());
        assertEquals("MAZOWIECKIE", result.get(0).getName());
    }

    @Test
    @DisplayName("findPowiatsInVoivodeship: Powinien pobrać powiaty dla danego kodu województwa")
    void findPowiatsInVoivodeship_ShouldReturnPowiats() {
        // GIVEN
        String voivodeshipCode = "14";
        TerytPowiat powiat = new TerytPowiat();
        powiat.setCode("1401");
        powiat.setName("białobrzeski");

        when(terytPowiatRepository.findByVoivodeshipCodeOrderByNameAsc(voivodeshipCode))
                .thenReturn(Collections.singletonList(powiat));

        // WHEN
        List<DTO_Teryt> result = terytService.findPowiatsInVoivodeship(voivodeshipCode);

        // THEN
        assertEquals(1, result.size());
        assertEquals("1401", result.get(0).getCode());
        assertEquals("białobrzeski", result.get(0).getName());
    }
}