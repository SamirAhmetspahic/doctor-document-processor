package si.better.dpp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author ahmetspahics
 */
@Entity
@DiscriminatorValue("Doctor")
@Data
@NoArgsConstructor
public class Doctor extends Participant
{
}
