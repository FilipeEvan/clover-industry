package br.com.jacto.cloverindustry.model.client;

import br.com.jacto.cloverindustry.model.proposal.Proposal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clients")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private String email;

    private String phone;

    private LocalDateTime created_at;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Proposal> proposals;
}
