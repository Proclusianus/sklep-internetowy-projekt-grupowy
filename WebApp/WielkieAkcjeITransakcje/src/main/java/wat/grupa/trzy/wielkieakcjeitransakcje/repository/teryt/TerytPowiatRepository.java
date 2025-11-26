package wat.grupa.trzy.wielkieakcjeitransakcje.repository.teryt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt.TerytPowiat;

import java.util.List;

@Repository
public interface TerytPowiatRepository extends JpaRepository<TerytPowiat,Integer> {
    List<TerytPowiat> findByVoivodeshipCodeOrderByNameAsc(String voivodeshipCode);
}
