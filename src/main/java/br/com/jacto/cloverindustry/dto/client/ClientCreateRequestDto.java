package br.com.jacto.cloverindustry.dto.client;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class ClientCreateRequestDto {

    private UUID id;
    @NotBlank(message = "O nome não pode estar em branco")
    private String name;
    @NotBlank(message = "O email não pode estar em branco")
    private String email;
    @NotBlank(message = "O telefone não pode estar em branco")
    private String phone;

    public ClientCreateRequestDto(UUID id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
