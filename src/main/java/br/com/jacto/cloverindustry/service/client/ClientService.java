package br.com.jacto.cloverindustry.service.client;

import br.com.jacto.cloverindustry.ValidationException;
import br.com.jacto.cloverindustry.dto.client.ClientResponseDto;
import br.com.jacto.cloverindustry.model.client.Client;
import br.com.jacto.cloverindustry.model.proposal.Proposal;
import br.com.jacto.cloverindustry.repository.client.ClientRepository;
import br.com.jacto.cloverindustry.repository.proposal.ProposalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    public Page<ClientResponseDto> getAllClients(@PageableDefault(page = 0, size = 10, sort = {"name"}) Pageable pageable) {
        var page = clientRepository.findAll(pageable).map(ClientResponseDto::new);
        return page;
    }

    @Transactional
    public void deleteClientById(UUID id) {
        // Busca o cliente correspondente pelo ID
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Cliente não encontrado"));

        // Busca a proposta associada ao cliente (se houver)
        List<Proposal> proposals = proposalRepository.findByClientId(id);

        // Verifica se a proposta existe e adiciona uma mensagem se necessário
        if (!proposals.isEmpty()) {
            throw new ValidationException("Não é possível excluir o cliente porque ele tem uma proposta associada.");
        }
        // Exclui as informações de cliente do banco de dados
        clientRepository.delete(client);
    }
}
