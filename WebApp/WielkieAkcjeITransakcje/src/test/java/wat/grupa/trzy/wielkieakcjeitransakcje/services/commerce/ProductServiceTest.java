package wat.grupa.trzy.wielkieakcjeitransakcje.services.commerce;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.commerce.DTO_AddProduct;
import wat.grupa.trzy.wielkieakcjeitransakcje.dto.commerce.DTO_Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.commerce.Product;
import wat.grupa.trzy.wielkieakcjeitransakcje.entities.user.UserData;
import wat.grupa.trzy.wielkieakcjeitransakcje.repository.commerce.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProducts_ShouldReturnDtoList() {
        // GIVEN
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("Product 1");
        p1.setPrice(BigDecimal.TEN);
        p1.setDescription("Description");
        p1.setCategory("Inne");

        when(productRepository.findAll()).thenReturn(List.of(p1));

        // WHEN
        List<DTO_Product> result = productService.getAllProducts();

        // THEN
        assertEquals(1, result.size());
        assertEquals("Product 1", result.get(0).name());
    }

    @Test
    void addProduct_ShouldSaveProductWithSeller() {
        // GIVEN
        DTO_AddProduct dto = new DTO_AddProduct();
        dto.setName("Rower");
        dto.setCena(BigDecimal.valueOf(500));
        dto.setDescription("Szybki");
        dto.setKategoria("Sport");

        UserData seller = new UserData();
        seller.setId(99L);

        // WHEN
        productService.addProduct(dto, seller);

        // THEN
        // Przechwytujemy obiekt, który został przekazany do metody save()
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());

        Product savedProduct = productCaptor.getValue();
        assertEquals("Rower", savedProduct.getName());
        assertEquals(seller, savedProduct.getSeller()); // Kluczowe: czy sprzedawca został przypisany
    }

    @Test
    void searchProducts_ShouldCallCorrectRepositoryMethod() {
        // GIVEN
        String query = "Laptop";
        when(productRepository.findByNameContainingIgnoreCase(query)).thenReturn(List.of());

        // WHEN
        productService.searchProducts(query);

        // THEN
        verify(productRepository).findByNameContainingIgnoreCase(query);
    }
}