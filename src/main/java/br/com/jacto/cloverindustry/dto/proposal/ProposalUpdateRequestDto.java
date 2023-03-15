package br.com.jacto.cloverindustry.dto.proposal;

import br.com.jacto.cloverindustry.dto.client.ClientUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public class ProposalUpdateRequestDto {

    private ClientUpdateRequestDto client;
    private List<UUID> products;

    public ClientUpdateRequestDto getClient() {
        return client;
    }

    public void setClient(ClientUpdateRequestDto client) {
        this.client = client;
    }

    public List<UUID> getProducts() {
        return products;
    }

    public void setProducts(List<UUID> products) {
        this.products = products;
    }
}
