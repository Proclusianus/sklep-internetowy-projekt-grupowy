package wat.grupa.trzy.wielkieakcjeitransakcje.repository.teryt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.teryt.TerytVoivodeship;

import java.util.List;

@Repository
public interface TerytVoivodeshipRepository extends JpaRepository<TerytVoivodeship,Integer> {
    List<TerytVoivodeship> findAllByOrderByNameAsc();
}
