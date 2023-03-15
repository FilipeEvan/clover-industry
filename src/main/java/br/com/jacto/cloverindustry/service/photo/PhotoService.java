package br.com.jacto.cloverindustry.service.photo;

import br.com.jacto.cloverindustry.ValidationException;
import br.com.jacto.cloverindustry.dto.photo.PhotoResponseDto;
import br.com.jacto.cloverindustry.model.photo.Photo;
import br.com.jacto.cloverindustry.model.product.Product;
import br.com.jacto.cloverindustry.repository.photo.PhotoRepository;
import br.com.jacto.cloverindustry.repository.product.ProductRepository;
import br.com.jacto.cloverindustry.util.PhotoUtils;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private ProductRepository productRepository;

    public String uploadPhoto(UUID productId, List<MultipartFile> files) throws IOException {
        // Busca o produto correspondente pelo ID
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            return "Produto não encontrado";
        }
        Product product = optionalProduct.get();

        List<String> messages = new ArrayList<>();
        for (MultipartFile file : files) {
            // Verifica se o tamanho do arquivo é menor que o tamanho máximo permitido
            long maxSize = 10000000; // Tamanho máximo permitido em bytes (10 MB)
            if (file.getSize() > maxSize) {
                return "Tamanho do arquivo muito grande. O tamanho máximo permitido é " + maxSize + " bytes.";
            }

            // Verifica se o produto já tem a imagem com o mesmo nome
            if (product.getPhotos().stream().anyMatch(photo -> photo.getFilename().equals(file.getOriginalFilename()))) {
                messages.add("O produto já possui a imagem " + file.getOriginalFilename());
                continue; // Pula para a próxima imagem
            }

            // Verifica se o tipo do arquivo não está vazio
            if (StringUtils.isEmpty(file.getContentType())) {
                messages.add("Tipo do arquivo não especificado.");
                continue; // Pula para a próxima imagem
            }

            Photo photo = photoRepository.save(Photo.builder()
                    .filename(file.getOriginalFilename())
                    .type(file.getContentType())
                    .photo(PhotoUtils.compressPhoto(file.getBytes()))
                    .product(product)
                    .created_at(LocalDateTime.now(ZoneId.of("UTC"))).build());
            if (photo != null) {
                messages.add("foto carregada com sucesso : " + file.getOriginalFilename());
            }
        }
        String message = String.join("\n", messages);
        return message;
    }

    public byte[] downloadPhoto(String filename, UUID productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (!optionalProduct.isPresent()) {
            throw new ValidationException("Produto não encontrado");
        }

        Optional<Photo> optionalPhoto = photoRepository.findByProductAndFilename(optionalProduct.get(), filename);

        if (!optionalPhoto.isPresent()) {
            throw new ValidationException("Foto não encontrada");
        }

        byte[] photo = PhotoUtils.decompressPhoto(optionalPhoto.get().getPhoto());
        return photo;
    }

    public List<PhotoResponseDto> getAllPhotosByProductId(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ValidationException("Produto não encontrado"));

        List<Photo> photos = photoRepository.findByProductId(productId);

        if (photos.isEmpty()) {
            throw new ValidationException("Nenhuma imagem encontrada para o produto " + productId);
        }

        List<PhotoResponseDto> photoResponseDtos = new ArrayList<>();
        for (Photo photo : photos) {
            PhotoResponseDto dto = new PhotoResponseDto(
                    photo.getId(),
                    photo.getFilename(),
                    photo.getType(),
                    photo.getCreated_at()
            );
            photoResponseDtos.add(dto);
        }

        return photoResponseDtos;
    }

    public void deletePhotoByProductId(String filename, UUID productId) {
        // Busca o produto correspondente pelo ID
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
            throw new ValidationException("Produto não encontrado");
        }

        // Busca a foto correspondente pelo nome
        Optional<Photo> optionalPhoto = photoRepository.findByProductAndFilename(optionalProduct.get(), filename);
        if (!optionalPhoto.isPresent()) {
            throw new ValidationException("Foto não encontrada");
        }

        Photo photo = optionalPhoto.get();
        // Exclui a foto no banco de dados
        photoRepository.delete(photo);
    }

}
