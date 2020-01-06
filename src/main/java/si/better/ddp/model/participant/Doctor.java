package si.better.ddp.model.participant;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author ahmetspahics
 */
@Entity
@DiscriminatorValue("Doctor")
@Data
@NoArgsConstructor
public class Doctor extends Participant
{
    /**
     * patientCollection - Doctor - Patient relation: OneToMany
     */
    @OneToMany(mappedBy="doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
    @ToString.Exclude
    private List<Patient> patients;

    /**
     * department
     */
    @Column(name = "DEPARTMENT")
    @Size(min = 2, max = 32)
    private String department;
}
