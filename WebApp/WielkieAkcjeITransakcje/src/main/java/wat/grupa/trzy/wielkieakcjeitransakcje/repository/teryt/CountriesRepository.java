package wat.grupa.trzy.wielkieakcjeitransakcje.repository.teryt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt.Countries;
import java.util.List;
import java.util.Optional;

@Repository
public interface CountriesRepository extends JpaRepository<Countries, Integer> {

    @Query("SELECT c FROM Countries c ORDER BY c.namePl ASC")
    List<Countries> findAllCountriesSortedByName();

    Optional<Countries> findByCodeIsoAlpha2(String codeIsoAlpha2);
}

