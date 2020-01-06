package si.better.ddp.constant;

/**
 * @author ahmetspahics
 */
public class ApplicationConstants
{
    public static final String XML_FILE = "doctor.xml";
    public static final String JSON_FILE = "doctor.json";

    public static class Controller
    {
        //Baze request mappings
        public static final String ROOT_REQUEST_MAPPING = "/better/ddp";
        public static final String API_VERSION = "/v1";
        public static final String DOCTOR_REQUEST_BASE = "/doctor";
        public static final String PATIENT_REQUEST_BASE = "/patient";
        //Doctor specific GET request mappings
        public static final String DOCTOR = "{department}/{externalId}";
        public static final String DOCTORS = "/all";
        //Upload request mapping
        public static final String XML = "/xml";
        public static final String JSON = "/json";
        //Upload response
        public static final String UPLOAD_RESPONSE_OK = "<strong>Message sended to processing...<strong/>";
    }

    public static class Integration
    {
        public static final String MESSAGE_HEADER_REPORT_KEY = "DOCUMENT-REPORT-KEY";
        public static final String TEMP_PATH_KEY = "TEMPORARY-FILE-PATH";
        public static final String MESSAGE_ERROR_FLAG = "HAS-ERROR";
        public static final String FILE_PATH ="FILE-PATH";
        public static final String DOCTOR_ID = "DOCTOR-ID";
    }
}
