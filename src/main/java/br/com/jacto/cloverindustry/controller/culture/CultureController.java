package br.com.jacto.cloverindustry.controller.culture;

import br.com.jacto.cloverindustry.dto.culture.CultureRequestDto;
import br.com.jacto.cloverindustry.dto.culture.CultureResponseDto;
import br.com.jacto.cloverindustry.service.culture.CultureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("cultures")
public class CultureController {

    @Autowired
    private CultureService cultureService;

    @PostMapping
    public ResponseEntity postCulture(@RequestBody @Valid CultureRequestDto cultureRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cultureService.postCulture(cultureRequestDto));
    }

    @GetMapping
    public ResponseEntity<Page<CultureResponseDto>> getAllCultures(Pageable pageable) {
        return ResponseEntity.ok(cultureService.getAllCultures(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity getCultureById(@PathVariable Long id) {
        return ResponseEntity.ok(cultureService.getCultureById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity putCultureById(@PathVariable Long id,
                                          @RequestBody @Valid CultureRequestDto cultureRequestDto) {
        return ResponseEntity.ok(cultureService.putCultureById(id, cultureRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultureById(@PathVariable Long id) {
        cultureService.deleteCultureById(id);
        return ResponseEntity.noContent().build();
    }
}
