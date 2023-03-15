package br.com.jacto.cloverindustry.controller.photo;

import br.com.jacto.cloverindustry.dto.photo.PhotoResponseDto;
import br.com.jacto.cloverindustry.service.photo.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("products/{productId}/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping
    public ResponseEntity<?> upload(@PathVariable UUID productId,
                                    @RequestParam("photos") List<MultipartFile> files) throws IOException {
        String message = photoService.uploadPhoto(productId, files);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }

    @GetMapping
    public ResponseEntity<List<PhotoResponseDto>> getAllPhotosByProductId(@PathVariable UUID productId) {
        List<PhotoResponseDto> photoResponseDtos = photoService.getAllPhotosByProductId(productId);
        return ResponseEntity.ok(photoResponseDtos);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<?> downloadPhoto(@PathVariable UUID productId,
                                           @PathVariable String filename) {
        byte[] photo = photoService.downloadPhoto(filename, productId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(photo);
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<Void> deletePhotoByProductId(@PathVariable UUID productId,
                                                       @PathVariable String filename) {
        photoService.deletePhotoByProductId(filename, productId);
        return ResponseEntity.noContent().build();
    }

}
