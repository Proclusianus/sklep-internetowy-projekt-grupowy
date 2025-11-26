package wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Product; // Zmiana importu
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ZMIANA: name -> Name
    List<Product> findByNameContainingIgnoreCase(String name);

    // ZMIANA: kategoria -> Category
    List<Product> findByCategory(String category);

    // ZMIANA: kategoria -> category, Product -> Product
    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.category IS NOT NULL ORDER BY p.category")
    List<String> findDistinctCategories();
}