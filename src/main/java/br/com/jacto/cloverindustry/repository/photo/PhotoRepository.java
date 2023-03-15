package br.com.jacto.cloverindustry.repository.photo;

import br.com.jacto.cloverindustry.model.photo.Photo;
import br.com.jacto.cloverindustry.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, UUID> {

    @Query("""
            select p from Photo p
            where
            p.product = :product
            and
            p.filename = :filename
            """)
    Optional<Photo> findByProductAndFilename(Product product, String filename);

    List<Photo> findByProductId(UUID productId);
}
