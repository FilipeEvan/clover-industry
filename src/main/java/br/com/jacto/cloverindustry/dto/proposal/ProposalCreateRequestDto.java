package br.com.jacto.cloverindustry.dto.proposal;

import br.com.jacto.cloverindustry.dto.client.ClientCreateRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class ProposalCreateRequestDto {

    @NotNull
    @Valid
    private ClientCreateRequestDto client;

    @NotEmpty(message = "A lista de produtos não pode estar vazia")
    private List<UUID> products;

    public ClientCreateRequestDto getClient() {
        return client;
    }

    public void setClient(ClientCreateRequestDto client) {
        this.client = client;
    }

    public List<UUID> getProducts() {
        return products;
    }

    public void setProducts(List<UUID> products) {
        this.products = products;
    }
}
