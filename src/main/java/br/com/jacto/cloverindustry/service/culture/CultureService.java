package br.com.jacto.cloverindustry.service.culture;

import br.com.jacto.cloverindustry.ValidationException;
import br.com.jacto.cloverindustry.dto.culture.CultureRequestDto;
import br.com.jacto.cloverindustry.dto.culture.CultureResponseDto;
import br.com.jacto.cloverindustry.model.culture.Culture;
import br.com.jacto.cloverindustry.repository.culture.CultureRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

@Service
public class CultureService {

    @Autowired
    private CultureRepository cultureRepository;

    @Transactional
    public CultureResponseDto postCulture(CultureRequestDto cultureRequestDto) {
        // Verifica se já existe uma cultura com o mesmo nome
        Culture existingCulture = cultureRepository.findByCulture(cultureRequestDto.getCulture());
        if (existingCulture != null) {
            throw new ValidationException("A cultura " + cultureRequestDto.getCulture() + " já está cadastrada.");
        }

        Culture culture = new Culture(cultureRequestDto);
        cultureRepository.save(culture);

        return new CultureResponseDto(culture);
    }

    public Page<CultureResponseDto> getAllCultures(@PageableDefault(page = 0, size = 10, sort = {"culture"}) Pageable pageable) {
        var page = cultureRepository.findAll(pageable).map(CultureResponseDto::new);
        return page;
    }

    public CultureResponseDto getCultureById(Long id) {
        // Busca a cultura correspondente pelo ID
        Culture culture = cultureRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Cultura não encontrado"));

        return new CultureResponseDto(culture);
    }

    @Transactional
    public CultureResponseDto putCultureById(Long id, CultureRequestDto cultureRequestDto) {
        // Verifica se já existe uma cultura com o mesmo nome
        Culture existingCulture = cultureRepository.findByCulture(cultureRequestDto.getCulture());
        if (existingCulture != null) {
            throw new ValidationException("A cultura " + cultureRequestDto.getCulture() + " já está cadastrada.");
        }

        // Busca a cultura correspondente pelo ID
        Culture culture = cultureRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Cultura não encontrado"));

        // Atualiza a informação de cultura
        if (cultureRequestDto.getCulture() != null) {
            culture.setCulture(cultureRequestDto.getCulture());
        }

        // Salva a informação atualizada de cultura no banco de dados
        culture = cultureRepository.save(culture);

        return new CultureResponseDto(culture);
    }

    @Transactional
    public void deleteCultureById(Long id) {
        // Busca a cultura correspondente pelo ID
        Culture culture = cultureRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Cultura não encontrada"));
        // Verifica se a cultura está associada a algum produto
        if (!culture.getProducts().isEmpty()) {
            throw new ValidationException("Não é possível excluir esta cultura porque ela está associada a um ou mais produtos.");
        }
        // Exclui as informações da cultura do banco de dados
        cultureRepository.delete(culture);
    }
}
