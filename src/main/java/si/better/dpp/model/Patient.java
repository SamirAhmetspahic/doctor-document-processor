package si.better.dpp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author ahmetspahics
 */
@Entity
@DiscriminatorValue("Patient")
@Data
@NoArgsConstructor
public class Patient extends Participant
{
}
