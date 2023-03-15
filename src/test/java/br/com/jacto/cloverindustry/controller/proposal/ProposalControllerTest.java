package br.com.jacto.cloverindustry.controller.proposal;

import br.com.jacto.cloverindustry.dto.client.ClientCreateRequestDto;
import br.com.jacto.cloverindustry.dto.client.ClientUpdateRequestDto;
import br.com.jacto.cloverindustry.dto.product.ProductDetailResponseDto;
import br.com.jacto.cloverindustry.dto.proposal.ProposalCreateRequestDto;
import br.com.jacto.cloverindustry.dto.proposal.ProposalDetailResponseDto;
import br.com.jacto.cloverindustry.dto.proposal.ProposalListResponseDto;
import br.com.jacto.cloverindustry.dto.proposal.ProposalUpdateRequestDto;
import br.com.jacto.cloverindustry.model.product.Product;
import br.com.jacto.cloverindustry.model.proposal.Proposal;
import br.com.jacto.cloverindustry.service.proposal.ProposalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ProposalControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<ProposalCreateRequestDto> proposalCreateRequestDtoJson;

    @Autowired
    private JacksonTester<ProposalDetailResponseDto> proposalDetailResponseDtoJson;

    @Autowired
    private JacksonTester<ProposalUpdateRequestDto> proposalRequestDtoJson;

    @MockBean
    private ProposalService proposalService;

    @Test //Indica que esse é um método de teste
    @DisplayName("Deve devolver código http 400 quando informações estão inválidas")
    @Transactional // Indica que o teste deve ser executado em uma transação
    void postProposalScenario1() throws Exception {
        var response = mvc.perform(post("/proposals"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deve devolver código http 201 quando informações estão válidas")
    @Transactional
    void postProposalScenario2() throws Exception {
        // Cria um objeto de resposta do serviço para simular a resposta do ProposalService
        var proposalDetailResponseDto = new ProposalDetailResponseDto(new Proposal());

        // Configura o ProposalService para retornar o objeto de detalhes da proposta criado acima
        when(proposalService.postProposal(any())).thenReturn(proposalDetailResponseDto);

        // Cria uma lista de produtos para a proposta
        List<UUID> products = new ArrayList<>();
        products.add(UUID.randomUUID());
        products.add(UUID.randomUUID());

        // Cria um objeto de criação de proposta com dados válidos
        var proposalCreateRequestDto = new ProposalCreateRequestDto();
        proposalCreateRequestDto.setClient(new ClientCreateRequestDto(UUID.randomUUID(), "João Silva", "joao.silva@example.com", "(11) 99999-9999"));
        proposalCreateRequestDto.setProducts(products);

        // Executa uma requisição POST para criar uma nova proposta
        var response = mvc.perform(
                post("/proposals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(proposalCreateRequestDtoJson.write(proposalCreateRequestDto).getJson())
        ).andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 201 (CREATED)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        // Verifica se o corpo da resposta é o JSON esperado contendo as informações da proposta criada
        var json = proposalDetailResponseDtoJson.write(proposalDetailResponseDto).getJson();
        assertThat(response.getContentAsString()).isEqualTo(json);
    }

    @Test
    @DisplayName("Deve retornar uma lista paginada de propostas com código http 200")
    void getAllProposals() throws Exception {
        // Cria um objeto de resposta do serviço para simular a resposta do ProposalService
        Page<ProposalListResponseDto> proposalListResponseDtoPage = new PageImpl<>(List.of(new ProposalListResponseDto(new Proposal())), PageRequest.of(0, 1), 1);
        // Configura o ProposalService para retornar o objeto de detalhes da proposta criado acima
        when(proposalService.getAllProposals(any())).thenReturn(proposalListResponseDtoPage);

        // Executa uma requisição GET para obter a lista paginada de propostas
        var response = mvc.perform(
                get("/proposals") // Rota da requisição
                        .contentType(MediaType.APPLICATION_JSON) // Configura o tipo de conteúdo da requisição
                        .param("page", "0")
                        .param("size", "1")
        ).andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 200 (OK)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        // Verifica se o corpo da resposta é o JSON esperado contendo a lista paginada de propostas
        var json = new ObjectMapper().writeValueAsString(proposalListResponseDtoPage);
        assertThat(response.getContentAsString()).isEqualTo(json);
    }

    @Test
    @DisplayName("Deve retornar uma lista de propostas com código http 200")
    void getAllProposalsByClientIdScenario1() throws Exception {
        // Cria um objeto de resposta do serviço para simular a resposta do ProposalService
        UUID clientId = UUID.randomUUID();
        List<ProposalListResponseDto> proposalListResponseDtos = new ArrayList<>();
        proposalListResponseDtos.add(new ProposalListResponseDto(new Proposal()));
        when(proposalService.getAllProposalsByClientId(clientId)).thenReturn(proposalListResponseDtos);

        // Executa uma requisição GET para obter a lista de propostas
        var response = mvc.perform(
                get("/proposals/clients/" + clientId) // Rota da requisição
                        .contentType(MediaType.APPLICATION_JSON) // Configura o tipo de conteúdo da requisição
        ).andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 200 (OK)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        // Verifica se o corpo da resposta é o JSON esperado contendo a lista de propostas
        var json = new ObjectMapper().writeValueAsString(proposalListResponseDtos);
        assertThat(response.getContentAsString()).isEqualTo(json);
    }

    @Test
    @DisplayName("Deve retornar uma resposta vazia com código http 204 quando não houver propostas")
    void getAllProposalsByClientIdScenario2() throws Exception {
        // Cria um objeto de resposta do serviço para simular a resposta do ProposalService
        UUID clientId = UUID.randomUUID();
        List<ProposalListResponseDto> proposalListResponseDtos = new ArrayList<>();
        when(proposalService.getAllProposalsByClientId(clientId)).thenReturn(proposalListResponseDtos);

        // Executa uma requisição GET para obter a lista de propostas
        var response = mvc.perform(
                get("/proposals/clients/" + clientId) // Rota da requisição
                        .contentType(MediaType.APPLICATION_JSON) // Configura o tipo de conteúdo da requisição
        ).andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 204 (NO CONTENT)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // Verifica se o corpo da resposta está vazio
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar detalhes de uma proposta existente com código http 200")
    void getProposalById() throws Exception {
        // Cria um objeto de resposta do serviço para simular a resposta do ProposalService
        UUID proposalId = UUID.randomUUID();
        ProposalDetailResponseDto proposalDetailResponseDto = new ProposalDetailResponseDto(new Proposal());
        proposalDetailResponseDto.setId(proposalId);
        // Configura o ProposalService para retornar o objeto de detalhes da proposta criado acima
        when(proposalService.getProposalById(proposalId)).thenReturn(proposalDetailResponseDto);

        // Executa uma requisição GET para obter a proposta pelo seu ID
        var response = mvc.perform(
                get("/proposals/{id}", proposalId) // Rota da requisição
                        .contentType(MediaType.APPLICATION_JSON) // Configura o tipo de conteúdo da requisição
        ).andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 200 (OK)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        // Verifica se o corpo da resposta é o JSON esperado contendo as informações da proposta
        var json = proposalDetailResponseDtoJson.write(proposalDetailResponseDto).getJson();
        assertThat(response.getContentAsString()).isEqualTo(json);
    }

    @Test
    @DisplayName("Deve atualizar uma proposta existente e retornar código http 200")
    void putProposalById() throws Exception {
        // Cria um objeto de atualização de proposta para simular a requisição do cliente
        var proposalUpdateRequestDto = new ProposalUpdateRequestDto();
        proposalUpdateRequestDto.setClient(new ClientUpdateRequestDto(UUID.randomUUID(), "Jo\\u00e3o Silva", "joao.silva@example.com", "(11) 99999-9999"));
        proposalUpdateRequestDto.setProducts(List.of(UUID.randomUUID()));

        // Cria um objeto de resposta do serviço para simular a resposta do ProposalService
        var proposalDetailResponseDto = new ProposalDetailResponseDto(new Proposal());
        proposalDetailResponseDto.getClient().setName(proposalUpdateRequestDto.getClient().getName());
        proposalDetailResponseDto.getClient().setEmail(proposalUpdateRequestDto.getClient().getEmail());
        proposalDetailResponseDto.getClient().setPhone(proposalUpdateRequestDto.getClient().getPhone());
        proposalDetailResponseDto.setProducts(List.of(new ProductDetailResponseDto(new Product())));

        // Configura o ProposalService para retornar o objeto de detalhes da proposta atualizado acima
        when(proposalService.putProposalById(any(UUID.class), any(ProposalUpdateRequestDto.class))).thenReturn(proposalDetailResponseDto);

        // Executa uma requisição PUT para atualizar uma proposta existente
        var response = mvc.perform(
                put("/proposals/{id}", UUID.randomUUID()) // Rota da requisição, passando o ID de uma proposta existente
                        .contentType(MediaType.APPLICATION_JSON) // Configura o tipo de conteúdo da requisição
                        .content(proposalRequestDtoJson.write(proposalUpdateRequestDto).getJson()) // Configura o corpo da requisição com um objeto de atualização de proposta em formato JSON
        ).andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 200 (OK)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        // Verifica se o corpo da resposta é o JSON esperado contendo as informações da proposta atualizada
        var json = proposalDetailResponseDtoJson.write(proposalDetailResponseDto).getJson();
        assertThat(response.getContentAsString()).isEqualTo(json);
    }

        @Test
        @DisplayName("Deve exclui a proposta e retornar código http 204")
        void deleteProposalById() throws Exception {
            // Cria um ID de proposta aleatório
            UUID proposalId = UUID.randomUUID();

            // Executa uma requisição DELETE para excluir a proposta pelo ID
            var response = mvc.perform(delete("/proposals/{id}", proposalId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn().getResponse();

            // Verifica se o código HTTP de resposta é 204 (NO_CONTENT)
            assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());

            // Verifica se o método deleteProposalById foi chamado com o ID correto
            verify(proposalService, times(1)).deleteProposalById(proposalId);
        }
}