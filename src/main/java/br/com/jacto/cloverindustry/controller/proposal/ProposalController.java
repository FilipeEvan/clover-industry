package br.com.jacto.cloverindustry.controller.proposal;


import br.com.jacto.cloverindustry.dto.proposal.ProposalCreateRequestDto;
import br.com.jacto.cloverindustry.dto.proposal.ProposalListResponseDto;
import br.com.jacto.cloverindustry.dto.proposal.ProposalUpdateRequestDto;
import br.com.jacto.cloverindustry.dto.proposal.ProposalDetailResponseDto;
import br.com.jacto.cloverindustry.service.proposal.ProposalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("proposals")
public class ProposalController {

    @Autowired
    private ProposalService proposalService;

    @PostMapping
    public ResponseEntity<ProposalDetailResponseDto> postProposal(@Valid @RequestBody ProposalCreateRequestDto proposalCreateRequestDto) {
        ProposalDetailResponseDto proposalDetailResponseDto = proposalService.postProposal(proposalCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(proposalDetailResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<ProposalListResponseDto>> getAllProposals(Pageable pageable) {
        Page<ProposalListResponseDto> proposalListResponseDtos = proposalService.getAllProposals(pageable);
        return ResponseEntity.ok(proposalListResponseDtos);
    }

    @GetMapping("/clients/{clientId}")
    public ResponseEntity<List<ProposalListResponseDto>> getAllProposalsByClientId(@PathVariable UUID clientId) {
        List<ProposalListResponseDto> proposalListResponseDtos = proposalService.getAllProposalsByClientId(clientId);
        if (proposalListResponseDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(proposalListResponseDtos);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProposalDetailResponseDto> getProposalGetById(@PathVariable UUID id) {
        ProposalDetailResponseDto proposalDetailResponseDto = proposalService.getProposalById(id);
        return ResponseEntity.ok(proposalDetailResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProposalById(@PathVariable UUID id) {
        proposalService.deleteProposalById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProposalDetailResponseDto> putProposalById(@PathVariable UUID id,
                                                                     @RequestBody ProposalUpdateRequestDto proposalUpdateRequestDto) {
        ProposalDetailResponseDto proposalDetailResponseDto = proposalService.putProposalById(id, proposalUpdateRequestDto);
        return ResponseEntity.ok(proposalDetailResponseDto);
    }
}
