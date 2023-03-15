package br.com.jacto.cloverindustry.dto.product;

import br.com.jacto.cloverindustry.model.product.Product;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProductListResponseDto {

    private UUID id;
    private String name;
    private LocalDateTime created_at;

    public ProductListResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.created_at = product.getCreated_at();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
