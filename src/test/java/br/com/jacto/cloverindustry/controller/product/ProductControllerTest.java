package br.com.jacto.cloverindustry.controller.product;

import br.com.jacto.cloverindustry.dto.culture.CultureResponseDto;
import br.com.jacto.cloverindustry.dto.product.ProductCreateRequestDto;
import br.com.jacto.cloverindustry.dto.product.ProductDetailResponseDto;
import br.com.jacto.cloverindustry.dto.product.ProductListResponseDto;
import br.com.jacto.cloverindustry.dto.product.ProductUpdateRequestDto;
import br.com.jacto.cloverindustry.model.product.Product;
import br.com.jacto.cloverindustry.service.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<ProductCreateRequestDto> productCreateRequestDtoJson;

    @Autowired
    private JacksonTester<ProductDetailResponseDto> productDetailResponseDtoJson;

    @MockBean
    private ProductService productService;

    @Test //Indica que esse é um método de teste
    @DisplayName("Deve devolver código http 400 quando informações estão inválidas")
    @Transactional // Indica que o teste deve ser executado em uma transação
    void postProductScenario1() throws Exception {
        var response = mvc.perform(post("/products"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test //Indica que esse é um método de teste
    @DisplayName("Deve devolver código http 200 quando informações estão válidas")
    @Transactional // Indica que o teste deve ser executado em uma transação
    void postProductScenario2() throws Exception {
        List<CultureResponseDto> cultures = new ArrayList<>();
        cultures.add(new CultureResponseDto(1L, "Cultura 1"));
        cultures.add(new CultureResponseDto(2L, "Cultura 2"));

        // Cria um objeto de detalhes do produto para simular a resposta do ProductService
        var productDetailResponseDto = new ProductDetailResponseDto(null, "Produto de teste", 50, "Este é um produto de teste.", cultures);

        // Configura o ProductService para retornar o objeto de detalhes do produto criado acima
        when(productService.postProduct(any())).thenReturn(productDetailResponseDto);

        // Executa uma requisição POST para criar um novo produto
        var response = mvc
                .perform(
                        post("/products") // Rota da requisição
                                .contentType(MediaType.APPLICATION_JSON) // Configura o tipo de conteúdo da requisição
                                .content(productCreateRequestDtoJson.write( // Configura o corpo da requisição com um objeto de criação de produto em formato JSON
                                        new ProductCreateRequestDto(
                                                "Produto de teste",
                                                50,
                                                "Este é um produto de teste.",
                                                Arrays.asList(1L, 2L)
                                            )
                                ).getJson())
                            )
                            .andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 200 (OK)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        // Verifica se o corpo da resposta é o JSON esperado contendo as informações do produto criado
        var json = productDetailResponseDtoJson.write(
                productDetailResponseDto
        ).getJson();

        assertThat(response.getContentAsString()).isEqualTo(json);
    }

    @Test
    @DisplayName("Deve retornar uma lista paginada de produtos com código http 200")
    void getAllProducts() throws Exception {
        // Cria um objeto de resposta do serviço para simular a resposta do ProductService
        Page<ProductListResponseDto> productListResponseDtoPage = new PageImpl<>(List.of(new ProductListResponseDto(new Product())), PageRequest.of(0, 1), 1);
        // Configura o ProductService para retornar o objeto de detalhes do produto criado acima
        when(productService.getAllProducts(any())).thenReturn(productListResponseDtoPage);

        // Executa uma requisição GET para obter a lista paginada de produtos
        var response = mvc.perform(
                get("/products") // Rota da requisição
                        .contentType(MediaType.APPLICATION_JSON) // Configura o tipo de conteúdo da requisição
                        .param("page", "0")
                        .param("size", "1")
        ).andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 200 (OK)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        // Verifica se o corpo da resposta é o JSON esperado contendo a lista paginada de produtos
        var json = new ObjectMapper().writeValueAsString(productListResponseDtoPage);
        assertThat(response.getContentAsString()).isEqualTo(json);
    }

    @Test
    @DisplayName("Deve retornar um produto pelo ID com código http 200")
    void getProductById() throws Exception {
        // Cria um objeto de resposta do serviço para simular a resposta do ProductService
        UUID productId = UUID.randomUUID();
        List<CultureResponseDto> cultures = new ArrayList<>();
        cultures.add(new CultureResponseDto(1L, "Cultura 1"));
        cultures.add(new CultureResponseDto(2L, "Cultura 2"));
        ProductDetailResponseDto productDetailResponseDto = new ProductDetailResponseDto(productId, "Produto de teste", 50, "Este é um produto de teste.", cultures);

        // Configura o ProductService para retornar o objeto de detalhes do produto criado acima
        when(productService.getProductById(productId)).thenReturn(productDetailResponseDto);

        // Executa uma requisição GET para obter o produto pelo ID
        MvcResult result = mvc.perform(get("/products/" + productId))
                .andExpect(status().isOk())
                .andReturn();

        // Verifica se o corpo da resposta é o JSON esperado contendo as informações do produto
        var json = productDetailResponseDtoJson.write(productDetailResponseDto).getJson();
        assertThat(result.getResponse().getContentAsString()).isEqualTo(json);
    }

    @Test
    @DisplayName("Deve retornar o produto atualizado com código http 200")
    @Transactional // Indica que o teste deve ser executado em uma transação
    void putProductById() throws Exception {
        UUID productId = UUID.randomUUID();
        List<CultureResponseDto> cultures = new ArrayList<>();
        cultures.add(new CultureResponseDto(1L, "Cultura 1"));
        cultures.add(new CultureResponseDto(2L, "Cultura 2"));

        // Cria um objeto de detalhes do produto para simular a resposta do ProductService
        var productDetailResponseDto = new ProductDetailResponseDto(
                productId,
                "Produto de teste",
                50,
                "Este é um produto de teste.",
                cultures
        );

        // Configura o ProductService para retornar o objeto de detalhes do produto criado acima
        when(productService.putProductById(eq(productId), any())).thenReturn(productDetailResponseDto);

        // Executa uma requisição PUT para atualizar o produto
        var response = mvc.perform(
                put("/products/{id}", productId) // Rota da requisição
                        .contentType(MediaType.APPLICATION_JSON) // Configura o tipo de conteúdo da requisição
                        .content(new ObjectMapper().writeValueAsString(
                                new ProductUpdateRequestDto(
                                        "Produto atualizado",
                                        100,
                                        "Este é um produto atualizado.",
                                        Arrays.asList(1L)
                                )
                        ))
        ).andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 200 (OK)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        // Verifica se o corpo da resposta é o JSON esperado contendo as informações do produto atualizado
        var json = productDetailResponseDtoJson.write(
                productDetailResponseDto
        ).getJson();

        assertThat(response.getContentAsString()).isEqualTo(json);
    }

    @Test
    @DisplayName("Deve deletar um produto existente e retornar código http 204")
    @Transactional // Indica que o teste deve ser executado em uma transação
    void deleteProductById() throws Exception {
        UUID productId = UUID.randomUUID();

        // Executa a requisição DELETE para deletar o produto com o id especificado
        var response = mvc.perform(delete("/products/{id}", productId))
                .andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 204 (No Content)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // Verifica se o método deleteProductById() do ProductService foi chamado com o id correto
        verify(productService, times(1)).deleteProductById(productId);
    }
}