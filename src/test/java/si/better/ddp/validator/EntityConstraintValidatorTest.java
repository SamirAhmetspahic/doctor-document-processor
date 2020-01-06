package si.better.ddp.validator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import si.better.ddp.model.participant.Patient;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author ahmetspahics
 */
public class EntityConstraintValidatorTest
{

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeClass
    public static void createValidator()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void close()
    {
        validatorFactory.close();
    }

    @Test
    public void corectNameAndNoViolations()
    {
        // 1) given name is OK:
        // 2) internalId is NULL !
        Patient patient = new Patient();
        patient.setName("Aaa");

        //when:
        Set<ConstraintViolation<Patient>> violations
            = validator.validate(patient);

        //then:
        assertEquals(violations.size(), 1);
    }

    @Test
    public void detectIncorectNameLength()
    {
        // 1) given name is to short !:
        // 2) internalId is NULL !
        Patient patient = new Patient();
        patient.setName("A");

        //when:
        Set<ConstraintViolation<Patient>> violations
            = validator.validate(patient);

        //then:
        assertEquals(violations.size(), 2);
    }
}
