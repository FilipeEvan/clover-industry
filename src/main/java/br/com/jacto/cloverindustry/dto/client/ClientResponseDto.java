package br.com.jacto.cloverindustry.dto.client;

import br.com.jacto.cloverindustry.model.client.Client;

import java.util.UUID;

public class ClientResponseDto {

    private UUID id;
    private String name;
    private String email;
    private String phone;

    public ClientResponseDto(Client client) {
        if (client != null) {
            this.id = client.getId();
            this.name = client.getName();
            this.email = client.getEmail();
            this.phone = client.getPhone();
        }
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
