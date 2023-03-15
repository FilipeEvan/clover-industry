package br.com.jacto.cloverindustry.repository;

import br.com.jacto.cloverindustry.model.culture.Culture;
import br.com.jacto.cloverindustry.model.photo.Photo;
import br.com.jacto.cloverindustry.model.product.Product;
import br.com.jacto.cloverindustry.repository.culture.CultureRepository;
import br.com.jacto.cloverindustry.repository.photo.PhotoRepository;
import br.com.jacto.cloverindustry.repository.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Testa uma interface Repository
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class PhotoRepositoryTest {

    @Autowired
    private PhotoRepository photoRepository; // Autowire do repositório de fotos

    @Autowired
    private ProductRepository productRepository; // Autowire do repositório de produtos

    @Autowired
    private CultureRepository cultureRepository; // Autowire do repositório de culturas

    @Test
    @DisplayName("Deve retornar uma foto pelo produto e nome do arquivo")
    void findByProductAndFilename() throws IOException {
        // Arrange
        // Cria um objeto Culture para associar com o produto
        Culture culture = new Culture();
        culture.setCulture("Cultura de teste");

        // Salva a cultura no banco de dados
        cultureRepository.save(culture);

        // Cria um objeto Product para associar com a foto
        Product product = new Product();
        product.setName("Produto de teste");
        product.setDescription("Este é um produto de teste.");
        product.setCreated_at(LocalDateTime.now());
        product.setStatus(true);

        // Associa a cultura ao produto
        List<Culture> cultures = new ArrayList<>();
        cultures.add(culture);
        product.setCultures(cultures);

        // Salva o produto no banco de dados
        productRepository.save(product);

        // Cria um objeto Photo com valores de teste
        Photo photo = new Photo();
        photo.setProduct(product);
        photo.setFilename("teste.jpg");
        photo.setType("jpg");
        photo.setCreated_at(LocalDateTime.now());
        photo.setPhoto(new byte[] { 0x01, 0x02, 0x03 });

        // Salva a foto no banco de dados
        photoRepository.save(photo);

        // Act
        Optional<Photo> result = photoRepository.findByProductAndFilename(product, "teste.jpg");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(photo.getId(), result.get().getId());
        assertEquals(photo.getProduct().getId(), result.get().getProduct().getId());
        assertEquals(photo.getFilename(), result.get().getFilename());
    }
}