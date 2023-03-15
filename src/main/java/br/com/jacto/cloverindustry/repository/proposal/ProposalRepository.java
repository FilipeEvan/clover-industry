package br.com.jacto.cloverindustry.repository.proposal;

import br.com.jacto.cloverindustry.model.proposal.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProposalRepository extends JpaRepository<Proposal, UUID> {
    List<Proposal> findByClientId(UUID clientId);
}
