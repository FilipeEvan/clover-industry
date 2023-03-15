package br.com.jacto.cloverindustry.model.product;

import br.com.jacto.cloverindustry.dto.culture.CultureResponseDto;
import br.com.jacto.cloverindustry.model.proposal.Proposal;
import br.com.jacto.cloverindustry.model.culture.Culture;
import br.com.jacto.cloverindustry.model.photo.Photo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "products")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private int area_size;

    private String description;

    private LocalDateTime created_at;

    private Boolean status;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "cultures_products",
            joinColumns = {
                @JoinColumn(name = "product_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                @JoinColumn(name = "culture_id", referencedColumnName = "id")
            }
    )
    private List<Culture> cultures;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Photo> photos;

    @ManyToMany(mappedBy = "products")
    private List<Proposal> proposals = new ArrayList<>();

    public Product(String name, Integer areaSize, String description, List<CultureResponseDto> cultureResponseDtos) {
    }
}
