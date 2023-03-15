package br.com.jacto.cloverindustry.model.proposal;

import br.com.jacto.cloverindustry.model.client.Client;
import br.com.jacto.cloverindustry.model.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "proposals")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Proposal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "products_proposals",
            joinColumns = {
                    @JoinColumn(name = "proposal_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "product_id", referencedColumnName = "id")
            }
    )
    private List<Product> products;
}
