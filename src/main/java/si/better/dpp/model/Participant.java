package si.better.dpp.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ahmetspahics
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PARTICIPANT_TYPE",
    discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
public class Participant implements Serializable
{
    /**
     * id - Unique identifier, auto generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    @Setter(AccessLevel.NONE)
    private long id;

    /**
     * name - Participant name
     */
    @Column(name = "NAME")
    private String name;

    /**
     * timeCreated - Participant creation time
     */
    @Column(name = "TIME_CREATED")
    private LocalDateTime timeCreated;

    /**
     * timeModified - Participant modified time
     */
    @Column(name = "TIME_MODIFIED")
    private LocalDateTime timeModified;
}
