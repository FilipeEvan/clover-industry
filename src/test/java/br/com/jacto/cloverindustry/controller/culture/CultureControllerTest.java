package br.com.jacto.cloverindustry.controller.culture;

import br.com.jacto.cloverindustry.dto.culture.CultureRequestDto;
import br.com.jacto.cloverindustry.dto.culture.CultureResponseDto;
import br.com.jacto.cloverindustry.dto.product.ProductDetailResponseDto;
import br.com.jacto.cloverindustry.dto.product.ProductListResponseDto;
import br.com.jacto.cloverindustry.model.culture.Culture;
import br.com.jacto.cloverindustry.model.product.Product;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class CultureControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CultureRequestDto> cultureRequestDtoJson;

    @Autowired
    private JacksonTester<CultureResponseDto> cultureResponseDtoJson;

    @MockBean
    private CultureService cultureService;

    @Test
    @DisplayName("Deve devolver código http 400 quando informações estão inválidas")
    @Transactional // Indica que o teste deve ser executado em uma transação
    void postCultureScenario1() throws Exception {
        var response = mvc.perform(post("/cultures"))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver codigo http 200 quando informações estão validas")
    @Transactional
    void postCultureScenario2() throws Exception {
        // Cria um objeto de Cultura para simular a resposta do CultureService
        var cultureResponseDto = new CultureResponseDto(null, "Cultura 1");

        // Configura o CultureService para retornar o objeto de cultura criado acima
        when(cultureService.postCulture(any())).thenReturn(cultureResponseDto);

        // Executa uma requisição POST para criar um novo produto
        var response = mvc
                .perform(
                        post("/cultures")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(cultureRequestDtoJson.write(
                                        new CultureRequestDto("Cultura 1")
                                ).getJson())
                )
                .andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 200 (OK)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        // Verifica se o corpo da resposta é o JSON esperado contendo as informações do produto criado
        var json = cultureResponseDtoJson.write(
                cultureResponseDto
        ).getJson();
        System.out.println(json);

        assertThat(response.getContentAsString()).isEqualTo(json);
    }

    @Test
    @DisplayName("Deve retornar uma lista paginada de culturas com código http 200")
    void getAllProducts() throws Exception {
        // Cria um objeto de resposta do serviço para simular a resposta do CultureService
        Page<CultureResponseDto> cultureResponseDtoPage = new PageImpl<>(List.of(new CultureResponseDto(new Culture())), PageRequest.of(0, 1), 1);
        // Configura o CultureService para retornar o objeto de detalhes da cultura criado acima
        when(cultureService.getAllCultures(any())).thenReturn(cultureResponseDtoPage);

        // Executa uma requisição GET para obter a lista paginada de culturas
        var response = mvc.perform(
                get("/cultures") // Rota da requisição
                        .contentType(MediaType.APPLICATION_JSON) // Configura o tipo de conteúdo da requisição
                        .param("page", "0")
                        .param("size", "1")
        ).andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 200 (OK)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        // Verifica se o corpo da resposta é o JSON esperado contendo a lista paginada de propostas
        var json = new ObjectMapper().writeValueAsString(cultureResponseDtoPage);
        assertThat(response.getContentAsString()).isEqualTo(json);
    }

    @Test
    @DisplayName("Deve retornar uma cultura pelo ID com código http 200")
    void getCultureById() throws Exception {
        // Cria um objeto de resposta do serviço para simular a resposta do CultureService
        Long id = 1l;
        CultureResponseDto cultureResponseDto = new CultureResponseDto(id, "Cultura 1");

        // Configura o ProductService para retornar o objeto de detalhes do produto criado acima
        when(cultureService.getCultureById(id)).thenReturn(cultureResponseDto);

        // Executa uma requisição GET para obter a cultura pelo ID
        MvcResult result = mvc.perform(get("/cultures/" + id))
                .andExpect(status().isOk())
                .andReturn();

        // Verifica se o corpo da resposta é o JSON esperado contendo as informações do produto
        var json = cultureResponseDtoJson.write(cultureResponseDto).getJson();
        assertThat(result.getResponse().getContentAsString()).isEqualTo(json);
    }

    @Test
    @DisplayName("Deve atualizar uma cultura existente e retornar código http 200")
    @Transactional
    void putCultureById() throws Exception {
        // Cria um objeto de CulturaRequestDto para simular a requisição PUT
        var cultureRequestDto = new CultureRequestDto("Cultura atualizada");

        // Cria um objeto de Cultura para simular a resposta do CultureService
        var culture = new Culture();
        culture.setId(1L);
        culture.setCulture("Cultura original");

        // Configura o CultureService para retornar o objeto de cultura criado acima
        when(cultureService.putCultureById(anyLong(), any())).thenReturn(new CultureResponseDto(culture));

        // Executa uma requisição PUT para atualizar a cultura com ID 1
        var response = mvc.perform(
                put("/cultures/{id}", 1L) // Rota da requisição com o ID da cultura
                        .contentType(MediaType.APPLICATION_JSON) // Configura o tipo de conteúdo da requisição
                        .content(cultureRequestDtoJson.write(cultureRequestDto).getJson()) // Adiciona o corpo da requisição com a cultura atualizada
        ).andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 200 (OK)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        // Verifica se o corpo da resposta é o JSON esperado contendo as informações da cultura atualizada
        var json = cultureResponseDtoJson.write(new CultureResponseDto(culture)).getJson();
        assertThat(response.getContentAsString()).isEqualTo(json);
    }

    @Test
    @DisplayName("Deve deletar uma cultura existente e retornar código http 204")
    @Transactional // Indica que o teste deve ser executado em uma transação
    void deleteCultureById() throws Exception {
        Long id = 1L;

        // Executa a requisição DELETE para deletar a cultura com o id especificado
        var response = mvc.perform(delete("/cultures/{id}", id))
                .andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 204 (No Content)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // Verifica se o método deleteCultureById() do CultureService foi chamado com o id correto
        verify(cultureService, times(1)).deleteCultureById(id);
    }
}