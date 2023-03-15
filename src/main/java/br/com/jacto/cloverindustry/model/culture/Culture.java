package br.com.jacto.cloverindustry.model.culture;

import br.com.jacto.cloverindustry.dto.culture.CultureRequestDto;
import br.com.jacto.cloverindustry.model.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cultures")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Culture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String culture;

    @ManyToMany(mappedBy = "cultures")
    private List<Product> products = new ArrayList<>();

    public Culture(CultureRequestDto data) {
        this.culture = data.getCulture();
    }
}
