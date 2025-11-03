package wat.grupa.trzy.wielkieakcjeitransakcje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt.Countries;
import java.util.List;

@Repository
public interface TerytRepository extends JpaRepository<Countries, Integer> {
    List<Countries> findAllByOrderByNamePlAsc();
}

