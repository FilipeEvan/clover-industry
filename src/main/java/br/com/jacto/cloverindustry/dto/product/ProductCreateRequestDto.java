package br.com.jacto.cloverindustry.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ProductCreateRequestDto {

    @NotBlank(message = "O nome não pode estar em branco")
    public String name;
    @NotNull(message = "O tamanho da área não pode estar em branco")
    public Integer area_size;
    @NotBlank(message = "A descrição não pode estar em branco")
    public String description;

//    @NotNull(message = "A lista de produtos não pode ser nula")
    @NotEmpty(message = "A lista de produtos não pode estar vazia")
    public List<Long> cultures;

    public ProductCreateRequestDto(String name, Integer area_size, String description, List<Long> cultures) {
        this.name = name;
        this.area_size = area_size;
        this.description = description;
        this.cultures = cultures;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getArea_size() {
        return area_size;
    }

    public void setArea_size(Integer area_size) {
        this.area_size = area_size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getCultures() {
        return cultures;
    }

    public void setCultures(List<Long> cultures) {
        this.cultures = cultures;
    }
}
