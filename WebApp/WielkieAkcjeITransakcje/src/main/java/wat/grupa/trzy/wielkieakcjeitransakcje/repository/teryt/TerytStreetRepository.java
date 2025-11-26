package wat.grupa.trzy.wielkieakcjeitransakcje.repository.teryt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt.TerytStreet;

import java.util.List;

@Repository
public interface TerytStreetRepository extends JpaRepository<TerytStreet,Integer> {
    List<TerytStreet> findByLocalitySymbolOrderByStreetNameAsc(String localitySymbol);
}
