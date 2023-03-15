package br.com.jacto.cloverindustry.controller.product;

import br.com.jacto.cloverindustry.dto.product.ProductCreateRequestDto;
import br.com.jacto.cloverindustry.dto.product.ProductListResponseDto;
import br.com.jacto.cloverindustry.dto.product.ProductDetailResponseDto;
import br.com.jacto.cloverindustry.dto.product.ProductUpdateRequestDto;
import br.com.jacto.cloverindustry.service.product.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDetailResponseDto> postProduct(@RequestBody @Valid ProductCreateRequestDto productCreateRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.postProduct(productCreateRequestDto));
    }

    @GetMapping
    public ResponseEntity<Page<ProductListResponseDto>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok().body(productService.getAllProducts(pageable));
    }

    @GetMapping("/{id}")
    public ProductDetailResponseDto getProductById(@PathVariable UUID id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDetailResponseDto> putProductById(
            @PathVariable UUID id,
            @RequestBody @Valid ProductUpdateRequestDto productUpdateRequestDto) {
        return ResponseEntity.ok().body(productService.putProductById(id, productUpdateRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable UUID id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
