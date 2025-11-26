package wat.grupa.trzy.wielkieakcjeitransakcje.repository.teryt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt.TerytGmina;

import java.util.List;

@Repository
public interface TerytGminaRepository extends JpaRepository<TerytGmina, Integer> {
    List<TerytGmina> findByPowiatCodeOrderByNameAsc(String powiatCode);
}
