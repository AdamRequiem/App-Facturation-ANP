package ma.anp.facturation_droits_port.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="touchee")
public class Touchee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTouchees;

    @ManyToOne
    @JoinColumn(name= "id_navire")
    private Navire navire;

    @ManyToOne
    @JoinColumn(name="id_port")
    private Port port;

    private int annee;
    private int nombreTouchees;
}
