package br.com.jacto.cloverindustry.service.product;

import br.com.jacto.cloverindustry.ValidationException;
import br.com.jacto.cloverindustry.dto.product.ProductCreateRequestDto;
import br.com.jacto.cloverindustry.dto.product.ProductListResponseDto;
import br.com.jacto.cloverindustry.dto.product.ProductDetailResponseDto;
import br.com.jacto.cloverindustry.dto.product.ProductUpdateRequestDto;
import br.com.jacto.cloverindustry.model.culture.Culture;
import br.com.jacto.cloverindustry.model.product.Product;
import br.com.jacto.cloverindustry.repository.culture.CultureRepository;
import br.com.jacto.cloverindustry.repository.product.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CultureRepository cultureRepository;

    @Transactional
    public ProductDetailResponseDto postProduct(ProductCreateRequestDto productCreateRequestDto)  {
        // Verifica se já existe um produto com o mesmo nome
        Product existingProduct = productRepository.findByName(productCreateRequestDto.getName());
        if (existingProduct != null) {
            throw new ValidationException("Já existe um produto com o mesmo nome.");
        }

        Product product = new Product();
        // Cria as informações do produto
        product.setName(productCreateRequestDto.getName());
        product.setArea_size(productCreateRequestDto.getArea_size());
        product.setDescription(productCreateRequestDto.getDescription());
        product.setCreated_at(LocalDateTime.now(ZoneId.of("UTC"))); // Salva a data de criação do produto
        product.setStatus(true);

        // Define culturas
        if (productCreateRequestDto.getCultures() != null) {
            List<Culture> cultures = cultureRepository.findAllById(productCreateRequestDto.getCultures());
            product.setCultures(cultures);
        }

        // Salva as informações do produto no banco de dados
        product = productRepository.save(product);

        return new ProductDetailResponseDto(product);
    }

    public Page<ProductListResponseDto> getAllProducts(@PageableDefault(page = 0, size = 10, sort = {"name"}) Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);
        List<ProductListResponseDto> productListResponseDtos = page.getContent().stream().map(ProductListResponseDto::new).collect(Collectors.toList());
        return new PageImpl<>(productListResponseDtos, pageable, page.getTotalElements());
    }

    public ProductDetailResponseDto getProductById(UUID id) {
        // Busca o produto correspondente pelo ID
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Produto não encontrado"));

        return new ProductDetailResponseDto(product);
    }

    @Transactional
    public ProductDetailResponseDto putProductById(UUID id, ProductUpdateRequestDto productUpdateRequestDto) {
        // Busca o produto correspondente pelo ID
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Produto não encontrado"));

        // Verifica se já existe um produto com o mesmo nome
        Product existingProduct = productRepository.findByName(productUpdateRequestDto.getName());
        if (existingProduct != null) {
            throw new ValidationException("Já existe um produto com o mesmo nome.");
        }

        // Atualiza as informações do produto
        if (productUpdateRequestDto.getName() != null) {
            product.setName(productUpdateRequestDto.getName());
        }
        if (productUpdateRequestDto.getArea_size() > 0) {
            product.setArea_size(productUpdateRequestDto.getArea_size());
        }
        if (productUpdateRequestDto.getDescription() != null) {
            product.setDescription(productUpdateRequestDto.getDescription());
        }

        // Verifica se a lista de culturas foi atualizada
        List<Long> cultureIds = productUpdateRequestDto.getCultures();
        if (cultureIds != null) {
            // Apaga as relações entre o produto e suas culturas antigas
            product.getCultures().clear();
            // Adiciona as novas relações entre o produto e suas culturas atualizadas
            for (Long cultureId : cultureIds) {
                Culture culture = cultureRepository.findById(cultureId)
                        .orElseThrow(() -> new ValidationException("Cultura não encontrada"));
                product.getCultures().add(culture);
            }
        }

        // Salva as informações atualizadas do produto no banco de dados
        product = productRepository.save(product);

        return new ProductDetailResponseDto(product);
    }

    @Transactional
    public void deleteProductById(UUID id) {
        // Busca o produto correspondente pelo ID
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Produto não encontrado"));
        // Apaga as relações entre o produto e suas culturas
        product.getCultures().clear();
        // Exclui as informações do produto no banco de dados
        productRepository.delete(product);
    }
}

