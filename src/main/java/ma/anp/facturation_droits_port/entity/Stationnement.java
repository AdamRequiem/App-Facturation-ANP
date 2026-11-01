package ma.anp.facturation_droits_port.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.anp.facturation_droits_port.enums.TypeRistourne;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="stationnement")
public class Stationnement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStationnement;
    private boolean embarquement;
    private boolean abonnement;
    private boolean exoneration;

    @Enumerated(EnumType.STRING)
    private TypeRistourne typeRistourne;

    @ManyToOne
    @JoinColumn(name="id_navire")
    private Navire navire;

    @ManyToOne
    @JoinColumn(name="id_marchandise")
    private Marchandise marchandise;

    @ManyToOne
    @JoinColumn(name="id_escale")
    private Escale escale;
}
