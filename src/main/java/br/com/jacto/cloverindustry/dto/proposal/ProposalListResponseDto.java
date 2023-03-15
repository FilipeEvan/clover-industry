package br.com.jacto.cloverindustry.dto.proposal;

import br.com.jacto.cloverindustry.dto.client.ClientResponseDto;
import br.com.jacto.cloverindustry.dto.product.ProductListResponseDto;
import br.com.jacto.cloverindustry.model.proposal.Proposal;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProposalListResponseDto {

    private UUID id;
    private ClientResponseDto client;
    private List<ProductListResponseDto> products;

    public ProposalListResponseDto(Proposal proposal) {
        this.id = proposal.getId();
        this.client = new ClientResponseDto(proposal.getClient());
        if (proposal.getProducts() != null) {
            this.products = proposal.getProducts()
                    .stream()
                    .map(ProductListResponseDto::new)
                    .collect(Collectors.toList());
        }
    }

    public ProposalListResponseDto() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ClientResponseDto getClient() {
        return client;
    }

    public void setClient(ClientResponseDto client) {
        this.client = client;
    }

    public List<ProductListResponseDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductListResponseDto> products) {
        this.products = products;
    }
}
