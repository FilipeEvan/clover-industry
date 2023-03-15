package br.com.jacto.cloverindustry.dto.product;

import java.util.List;

public class ProductUpdateRequestDto {

    public String name;
    public int area_size;
    public String description;
    public List<Long> cultures;

    public ProductUpdateRequestDto(String name, int area_size, String description, List<Long> cultures) {
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

    public List<Long> getCultures() {
        return cultures;
    }

    public void setCultures(List<Long> cultures) {
        this.cultures = cultures;
    }
}
