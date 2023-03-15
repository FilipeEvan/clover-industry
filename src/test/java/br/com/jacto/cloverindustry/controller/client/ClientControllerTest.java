package br.com.jacto.cloverindustry.controller.client;

import br.com.jacto.cloverindustry.dto.client.ClientCreateRequestDto;
import br.com.jacto.cloverindustry.dto.client.ClientResponseDto;
import br.com.jacto.cloverindustry.dto.culture.CultureRequestDto;
import br.com.jacto.cloverindustry.dto.culture.CultureResponseDto;
import br.com.jacto.cloverindustry.model.client.Client;
import br.com.jacto.cloverindustry.model.culture.Culture;
import br.com.jacto.cloverindustry.service.client.ClientService;
import br.com.jacto.cloverindustry.service.culture.CultureService;
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

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ClientControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClientService clientService;

    @Test
    @DisplayName("Deve retornar uma lista paginada de clientes com código http 200")
    void getAllClients() throws Exception {
        // Cria um objeto de resposta do serviço para simular a resposta do ClientService
        Page<ClientResponseDto> clientResponseDtoPage = new PageImpl<>(List.of(new ClientResponseDto(new Client())), PageRequest.of(0, 1), 1);
        // Configura o ClientService para retornar o objeto de detalhes do cliente criado acima
        when(clientService.getAllClients(any())).thenReturn(clientResponseDtoPage);

        // Executa uma requisição GET para obter a lista paginada de culturas
        var response = mvc.perform(
                get("/clients") // Rota da requisição
                        .contentType(MediaType.APPLICATION_JSON) // Configura o tipo de conteúdo da requisição
                        .param("page", "0")
                        .param("size", "1")
        ).andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 200 (OK)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        // Verifica se o corpo da resposta é o JSON esperado contendo a lista paginada de clientes
        var json = new ObjectMapper().writeValueAsString(clientResponseDtoPage);
        assertThat(response.getContentAsString()).isEqualTo(json);
    }

    @Test
    @DisplayName("Deve deletar o cliente existente e retornar código http 204")
    @Transactional // Indica que o teste deve ser executado em uma transação
    void deleteClientById() throws Exception {
        UUID id = UUID.randomUUID();

        // Executa a requisição DELETE para deletar o cliente com o id especificado
        var response = mvc.perform(delete("/clients/{id}", id))
                .andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 204 (No Content)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // Verifica se o método deleteClientById() do ClientService foi chamado com o id correto
        verify(clientService, times(1)).deleteClientById(id);
    }
}