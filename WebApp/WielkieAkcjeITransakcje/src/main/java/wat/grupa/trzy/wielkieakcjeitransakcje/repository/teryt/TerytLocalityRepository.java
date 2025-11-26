package wat.grupa.trzy.wielkieakcjeitransakcje.repository.teryt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt.TerytLocality;

import java.util.List;

@Repository
public interface TerytLocalityRepository extends JpaRepository<TerytLocality,Integer> {
    List<TerytLocality> findByGminaCodeOrderByNameAsc(String gminaCode);
}
