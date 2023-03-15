package br.com.jacto.cloverindustry.dto.proposal;

import br.com.jacto.cloverindustry.dto.client.ClientResponseDto;
import br.com.jacto.cloverindustry.dto.product.ProductDetailResponseDto;
import br.com.jacto.cloverindustry.model.proposal.Proposal;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProposalDetailResponseDto {

    private UUID id;
    private ClientResponseDto client;
    private List<ProductDetailResponseDto> products;

    public ProposalDetailResponseDto(Proposal proposal) {
        this.id = proposal.getId();
        this.client = new ClientResponseDto(proposal.getClient());
        if (proposal.getProducts() != null) {
            this.products = proposal.getProducts()
                    .stream()
                    .map(ProductDetailResponseDto::new)
                    .collect(Collectors.toList());
        }
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

    public List<ProductDetailResponseDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDetailResponseDto> products) {
        this.products = products;
    }
}
