package br.com.jacto.cloverindustry.controller.client;

import br.com.jacto.cloverindustry.dto.client.ClientResponseDto;
import br.com.jacto.cloverindustry.service.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<Page<ClientResponseDto>> getAllClients(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clientService.getAllClients(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientById(@PathVariable UUID id) {
        clientService.deleteClientById(id);
        return ResponseEntity.noContent().build();
    }
}
