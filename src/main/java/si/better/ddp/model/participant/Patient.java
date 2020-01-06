package si.better.ddp.model.participant;

import lombok.Data;
import lombok.NoArgsConstructor;
import si.better.ddp.converter.String2ListConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author ahmetspahics
 */
@Entity
@DiscriminatorValue("Patient")
@Data
@NoArgsConstructor
public class Patient extends Participant
{
    /**
     * name - patient name.
     */
    @Column(name = "NAME")
    @Size(min = 2, max = 16)
    private String name;

    /**
     * lastName - patient last name.
     */
    @Column(name = "LASTNAME")
    @Size(min = 2, max = 16)
    private String lastName;

    /**
     * doctor - FK
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="DOCTOR_IID")
    private Doctor doctor;

    /**
     * diseases - patient diseases collection
     */
    @Column(name = "DISEASES")
    @Convert(converter = String2ListConverter.class)
    private List<String> diseasesCollection;
}
