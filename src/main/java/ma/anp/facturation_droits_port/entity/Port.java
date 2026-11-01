package ma.anp.facturation_droits_port.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.anp.facturation_droits_port.enums.TypeTerminal;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="port")
public class Port {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPort;
    private String nom;

    @Enumerated(EnumType.STRING)
    private TypeTerminal typeTerminal;

    @OneToMany(mappedBy = "port")
    private List<Escale> escales;

    @OneToMany(mappedBy = "port")
    private List<Tarif> tarifs;

    @OneToMany(mappedBy = "port")
    private List<Touchee> touchees;
}
