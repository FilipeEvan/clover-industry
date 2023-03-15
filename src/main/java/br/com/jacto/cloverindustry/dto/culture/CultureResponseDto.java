package br.com.jacto.cloverindustry.dto.culture;

import br.com.jacto.cloverindustry.model.culture.Culture;

public class CultureResponseDto {

    private Long id;
    private String culture;

    public CultureResponseDto(Culture culture) {
        this.id = culture.getId();
        this.culture = culture.getCulture();
    }

    public CultureResponseDto(Long id, String culture) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }
}
