package br.com.jacto.cloverindustry.dto.product;

import br.com.jacto.cloverindustry.dto.culture.CultureResponseDto;
import br.com.jacto.cloverindustry.model.product.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductDetailResponseDto {

    private UUID id;
    private String name;
    private int area_size;
    private String description;
    private LocalDateTime created_at;
    private List<CultureResponseDto> cultures;
//    private List<PhotoResponseDto> photos;

    public ProductDetailResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.area_size = product.getArea_size();
        this.description = product.getDescription();
        this.created_at = product.getCreated_at();
        if (product.getCultures() != null) {
            this.cultures = product.getCultures().stream().map(CultureResponseDto::new).collect(Collectors.toList());
        }
    }

    public ProductDetailResponseDto(UUID id, String name, int area_size, String description, List<CultureResponseDto> cultures) {
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

    public int getArea_size() {
        return area_size;
    }

    public void setArea_size(int area_size) {
        this.area_size = area_size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public List<CultureResponseDto> getCultures() {
        return cultures;
    }

    public void setCultures(List<CultureResponseDto> cultures) {
        this.cultures = cultures;
    }

//    public List<PhotoResponseDto> getPhotos() {
//        return photos;
//    }
//
//    public void setPhotos(List<PhotoResponseDto> photos) {
//        this.photos = photos;
//    }
}
