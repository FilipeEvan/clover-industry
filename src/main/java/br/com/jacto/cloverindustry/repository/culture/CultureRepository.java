package br.com.jacto.cloverindustry.repository.culture;

import br.com.jacto.cloverindustry.model.culture.Culture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CultureRepository extends JpaRepository<Culture, Long> {
    List<Culture> findByProducts_Id(UUID id);

    Culture findByCulture(String culture);
}
