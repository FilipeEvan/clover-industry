package br.com.jacto.cloverindustry.service.proposal;

import br.com.jacto.cloverindustry.ValidationException;
import br.com.jacto.cloverindustry.dto.client.ClientUpdateRequestDto;
import br.com.jacto.cloverindustry.dto.proposal.ProposalCreateRequestDto;
import br.com.jacto.cloverindustry.dto.proposal.ProposalListResponseDto;
import br.com.jacto.cloverindustry.dto.proposal.ProposalUpdateRequestDto;
import br.com.jacto.cloverindustry.dto.proposal.ProposalDetailResponseDto;
import br.com.jacto.cloverindustry.model.client.Client;
import br.com.jacto.cloverindustry.model.product.Product;
import br.com.jacto.cloverindustry.model.proposal.Proposal;
import br.com.jacto.cloverindustry.repository.client.ClientRepository;
import br.com.jacto.cloverindustry.repository.product.ProductRepository;
import br.com.jacto.cloverindustry.repository.proposal.ProposalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class ProposalService {

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public ProposalDetailResponseDto postProposal(ProposalCreateRequestDto proposalCreateRequestDto) {
        Proposal proposal = new Proposal();
        Client client = null;

        if (proposalCreateRequestDto.getClient() != null
                && proposalCreateRequestDto.getClient().getId() != null) {
            // Verifica se já existe um cliente com o mesmo ID
            Optional<Client> optionalClient = clientRepository.findById(proposalCreateRequestDto.getClient().getId());

            if (optionalClient.isPresent()) {
                // Se o cliente já existe, utiliza o registro existente
                client = optionalClient.get();
            }
        }

        if (client == null
                && proposalCreateRequestDto.getClient() != null
                && proposalCreateRequestDto.getClient().getName() != null
                && proposalCreateRequestDto.getClient().getEmail() != null
                && proposalCreateRequestDto.getClient().getPhone() != null) {

            // Verifica se já existe um cliente com as mesmas informações
            client = clientRepository.findByNameAndEmailAndPhone(
                    proposalCreateRequestDto.getClient().getName(),
                    proposalCreateRequestDto.getClient().getEmail(),
                    proposalCreateRequestDto.getClient().getPhone());
        }

        if (client == null) {
            // Cria um novo cliente se o cliente não existe
            client = new Client();
            client.setName(proposalCreateRequestDto.getClient().getName());
            client.setEmail(proposalCreateRequestDto.getClient().getEmail());
            client.setPhone(proposalCreateRequestDto.getClient().getPhone());
            client.setCreated_at(LocalDateTime.now(ZoneId.of("UTC")));

            // Salva o cliente no banco de dados
            client = clientRepository.save(client);
        }

        // Atribui o cliente à proposta
        proposal.setClient(client);

        // Define produtos
        if (proposalCreateRequestDto.getProducts() != null) {
            List<Product> products = productRepository.findAllById(proposalCreateRequestDto.getProducts());
            proposal.setProducts(products);
        }

        if (proposal.getProducts().isEmpty()) {
            throw new ValidationException("Não é possível salvar a proposta sem produtos.");
        }

        proposal = proposalRepository.save(proposal);

        return new ProposalDetailResponseDto(proposal);
    }

    public Page<ProposalListResponseDto> getAllProposals(@PageableDefault(page = 0, size = 10)Pageable pageable) {
        Page<Proposal> proposals = proposalRepository.findAll(pageable);
        List<ProposalListResponseDto> proposalListResponseDtos = new ArrayList<>();
        for (Proposal proposal : proposals) {
            proposalListResponseDtos.add(new ProposalListResponseDto(proposal));
        }
        return new PageImpl<>(proposalListResponseDtos, pageable, proposals.getTotalElements());
    }

    public List<ProposalListResponseDto> getAllProposalsByClientId(UUID clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ValidationException("Cliente não encontrado");
        }

        List<Proposal> proposals = proposalRepository.findByClientId(clientId);
        List<ProposalListResponseDto> proposalListResponseDtos = new ArrayList<>();
        for (Proposal proposal : proposals) {
            proposalListResponseDtos.add(new ProposalListResponseDto(proposal));
        }
        return proposalListResponseDtos;
    }

    public ProposalDetailResponseDto getProposalById(UUID id) {
        Proposal proposal = proposalRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Proposta não encontrada"));

        return new ProposalDetailResponseDto(proposal);
    }

    @Transactional
    public ProposalDetailResponseDto putProposalById(UUID id, ProposalUpdateRequestDto proposalUpdateRequestDto) {
        // Busca a proposta pelo ID
        Proposal proposal = proposalRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Proposta não encontrada"));

        // Atualiza informações do cliente
        if (proposalUpdateRequestDto.getClient() != null) {
            Client client = proposal.getClient();
            ClientUpdateRequestDto clientUpdateRequestDto = proposalUpdateRequestDto.getClient();

            if (clientUpdateRequestDto.getId() != null) {
                // Busca o cliente pelo ID
                Optional<Client> optionalClient = clientRepository.findById(proposalUpdateRequestDto.getClient().getId());

                if (optionalClient.isPresent()) {
                    // Se o cliente já existe, utiliza o registro existente
                    client = optionalClient.get();
                }
            } else {
                System.out.println(clientUpdateRequestDto.getName());
                System.out.println(client.getName());
                // Verifica se o nome do cliente está presente no JSON
                if (clientUpdateRequestDto.getName() != null) {
                    client.setName(clientUpdateRequestDto.getName());
                }

                // Verifica se o email do cliente está presente no JSON
                if (clientUpdateRequestDto.getEmail() != null) {
                    client.setEmail(clientUpdateRequestDto.getEmail());
                }

                // Verifica se o telefone do cliente está presente no JSON
                if (clientUpdateRequestDto.getPhone() != null) {
                    client.setPhone(clientUpdateRequestDto.getPhone());
                }
            }

            // Atualiza o cliente na proposta
            proposal.setClient(client);
        }

        // Adiciona ou remove produtos
        if (proposalUpdateRequestDto.getProducts() != null) {
            List<Product> products = productRepository.findAllById((proposalUpdateRequestDto.getProducts()));
            proposal.setProducts(products);
        }
//        else {
//            proposal.setProducts(Collections.emptyList());
//        }

        // Atualiza a proposta no banco de dados
        proposal = proposalRepository.save(proposal);

        // Retorna a resposta da proposta atualizada
        return  new ProposalDetailResponseDto(proposal);
    }

    @Transactional
    public void deleteProposalById(UUID id) {
        // Remove todas as associações entre a Proposta e os Produtos
        Proposal proposal = proposalRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Proposta não encontrada"));

        // Remove a associação entre o Cliente e a Proposta
        Client client = proposal.getClient();
        client.getProposals().remove(proposal);
        clientRepository.save(client);

        // Apaga as relações entre a proposta e os produtos
        proposal.getProducts().clear();
        // Exclui as informações da proposta no banco de dados
        proposalRepository.delete(proposal);
    }
}
