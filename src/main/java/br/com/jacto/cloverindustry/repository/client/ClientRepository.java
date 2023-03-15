package br.com.jacto.cloverindustry.repository.client;

import br.com.jacto.cloverindustry.model.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Client findByNameAndEmailAndPhone(String name, String email, String phone);
}
