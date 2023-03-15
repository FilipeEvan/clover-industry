package br.com.jacto.cloverindustry.controller.photo;

import br.com.jacto.cloverindustry.dto.photo.PhotoRequestDto;
import br.com.jacto.cloverindustry.dto.photo.PhotoResponseDto;
import br.com.jacto.cloverindustry.service.photo.PhotoService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class PhotoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<PhotoRequestDto> photoRequestDtoJson;

    @Autowired
    private JacksonTester<PhotoResponseDto> photoResponseDtoJson;

    @Autowired
    private JacksonTester<List<PhotoResponseDto>> photoResponseDtoListJson;

    @MockBean
    private PhotoService photoService;

    @Test
    @DisplayName("Deve criar uma nova foto para um produto existente")
    @Transactional
    void uploadPhoto() throws Exception {
        UUID productId = UUID.randomUUID();

        // Cria uma lista de arquivos de imagem de teste
        List<MultipartFile> files = new ArrayList<>();
        byte[] imageData = "Teste de imagem".getBytes();
        files.add(new MockMultipartFile("file1", "image1.jpg", "image/jpeg", imageData));
        files.add(new MockMultipartFile("file2", "image2.png", "image/png", imageData));

        // Configura o serviço de fotos para retornar uma mensagem de sucesso
        String message = "Fotos carregadas com sucesso.";
        when(photoService.uploadPhoto(eq(productId), anyList())).thenReturn(message);

        // Executa a requisição POST para criar uma nova foto
        var response = mvc.perform(
                MockMvcRequestBuilders.multipart("/products/{productId}/photos", productId)
                        .file("photos", files.get(0).getBytes())
                        .file("photos", files.get(1).getBytes())
        ).andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 201 (CREATED)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        // Verifica se a mensagem de sucesso retornada pelo serviço de fotos está no corpo da resposta
        assertThat(response.getContentAsString()).isEqualTo(message);
    }

    @Test
    @DisplayName("Deve retornar uma lista de fotos por id de produto")
    void getAllPhotosByProductId() throws Exception {
        UUID productId = UUID.randomUUID();

        // Cria uma lista de fotos de teste
        List<PhotoResponseDto> photos = new ArrayList<>();
        photos.add(new PhotoResponseDto(UUID.randomUUID(), "photo1.jpg", "image/jpeg", LocalDateTime.now()));
        photos.add(new PhotoResponseDto(UUID.randomUUID(), "photo2.png", "image/png", LocalDateTime.now()));
        photos.add(new PhotoResponseDto(UUID.randomUUID(), "photo3.jpg", "image/jpeg", LocalDateTime.now()));

        // Configura o serviço de fotos para retornar a lista de fotos de teste
        when(photoService.getAllPhotosByProductId(eq(productId))).thenReturn(photos);

        // Executa a requisição GET para obter a lista de fotos
        var response = mvc.perform(
                get("/products/{productId}/photos", productId)
        ).andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 200 (OK)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        // Converte o corpo da resposta em uma lista de objetos PhotoResponseDto
        List<PhotoResponseDto> returnedPhotos = photoResponseDtoListJson.parse(response.getContentAsString()).getObject();

        // Verifica se o número de fotos retornadas é igual ao número de fotos de teste
        assertThat(returnedPhotos.size()).isEqualTo(photos.size());

        // Verifica se as informações das fotos de teste correspondem às informações das fotos retornadas
        for (int i = 0; i < photos.size(); i++) {
            assertThat(returnedPhotos.get(i)).usingRecursiveComparison().isEqualTo(photos.get(i));
        }
    }


    @Test
    @DisplayName("Deve retornar uma foto existente")
    void downloadPhoto() throws Exception {
        UUID productId = UUID.randomUUID();
        String filename = "image1.png";
        byte[] photo = "Teste de imagem".getBytes();

        // Configura o serviço de fotos para retornar a foto de teste
        when(photoService.downloadPhoto(eq(filename), eq(productId))).thenReturn(photo);

        // Executa a requisição GET para obter a foto
        var response = mvc.perform(
                MockMvcRequestBuilders.get("/products/{productId}/photos/{filename}", productId, filename)
        ).andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 200 (OK)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        // Verifica se o tipo do conteúdo da resposta é "image/png"
        assertThat(response.getContentType()).isEqualTo("image/png");

        // Verifica se o corpo da resposta é igual à foto de teste
        assertThat(response.getContentAsByteArray()).isEqualTo(photo);
    }

    @Test
    @DisplayName("Deve excluir uma foto pelo id do produto e nome do arquivo")
    void deletePhotoByProductId() throws Exception {
        UUID productId = UUID.randomUUID();
        String filename = "test.jpg";

        // Executa a requisição DELETE para excluir a foto
        var response = mvc.perform(
                MockMvcRequestBuilders.delete("/products/{productId}/photos/{filename}", productId, filename)
        ).andReturn().getResponse();

        // Verifica se o código HTTP de resposta é 204 (NO CONTENT)
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // Verifica se o serviço de fotos foi chamado com os parâmetros corretos
        verify(photoService).deletePhotoByProductId(filename, productId);
    }
}