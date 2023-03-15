package br.com.jacto.cloverindustry.model.photo;


import br.com.jacto.cloverindustry.model.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Photo")
@Table(name = "photos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private  String filename;

    private String type;

    private LocalDateTime created_at;

    @Lob
    @Column(name = "photo", length = 1000)
    public byte[] photo() {
        return this.photo;
    }

    @JsonIgnore
    private byte[] photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public Photo(UUID id, String filename, String type, LocalDateTime created_at) {
    }
}
