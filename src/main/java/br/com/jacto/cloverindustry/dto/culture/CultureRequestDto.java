package br.com.jacto.cloverindustry.dto.culture;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class CultureRequestDto {

    @NotBlank(message = "A cultura não pode estar em branco")
    private String culture;

    public CultureRequestDto() {}

    public CultureRequestDto(String culture) {
        this.culture = culture;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }
}
