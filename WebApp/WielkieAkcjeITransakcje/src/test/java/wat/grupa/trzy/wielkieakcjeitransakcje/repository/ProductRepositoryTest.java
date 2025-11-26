package wat.grupa.trzy.wielkieakcjeitransakcje.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // Konfiguruje bazę H2 i tylko warstwę repozytoriów
@ActiveProfiles("test") // Używa application-test.properties
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldFindProductsByCategory() {
        // GIVEN
        Product p1 = new Product();
        p1.setName("Laptop");
        p1.setPrice(BigDecimal.valueOf(2000));
        p1.setCategory("Elektronika");
        productRepository.save(p1);

        Product p2 = new Product();
        p2.setName("Buty");
        p2.setPrice(BigDecimal.valueOf(200));
        p2.setCategory("Moda");
        productRepository.save(p2);

        // WHEN
        List<Product> elektronika = productRepository.findByCategory("Elektronika");

        // THEN
        assertThat(elektronika).hasSize(1);
        assertThat(elektronika.get(0).getName()).isEqualTo("Laptop");
    }
}