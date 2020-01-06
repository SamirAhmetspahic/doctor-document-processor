package si.better.ddp.model.participant;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import si.better.ddp.converter.String2MapConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

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
     * internalId - Internal unique identifier, auto generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IID", updatable = false, nullable = false)
    @Setter(AccessLevel.NONE)
    private long internalId;

    /**
     * id - External identifier, retrieved from xml file.
     */
    @NotNull
    @Column(name = "ID")
    private String id;

    /**
     * mapId - External unique identifier, composed from participant id and department.
     */
    @Column(name = "EXTERNAL_ID")
    @Convert(converter = String2MapConverter.class)
    private Map<String, String> mapId;

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

    /**
     * createdBy -
     */
    @Column(name = "CREATED_BY")
    private String createdBy;

    /**
     * modifiedBy -
     */
    @Column(name = "MODIFIED_BY")
    private String modifiedBy;
}
