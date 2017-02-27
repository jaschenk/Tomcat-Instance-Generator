package jeffaschenk.tomcat.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * ValidationHelper
 *
 * Created by jeffaschenk@gmail.com on 2/17/2017.
 */
public class ValidationHelper {

    /**
     * Regular Express to Validate a Property Name.
     */
    protected static final String VALIDATE_PROPERTY_NAME_REGEX =
            "^[a-zA-Z0-9_\\.]*$";
    protected static final Pattern VALIDATE_PROPERTY_NAME_PATTERN = Pattern.compile(VALIDATE_PROPERTY_NAME_REGEX);

    protected static final String VALIDATE_JVM_OPTION_REGEX =
            "^[\\-]+[a-zA-Z0-9_\\.\\-:;=]*$";
    protected static final Pattern VALIDATE_JVM_OPTION_PATTERN = Pattern.compile(VALIDATE_JVM_OPTION_REGEX);

    protected static final String VALIDATE_INSTANCE_NAME_REGEX =
            "^[a-zA-Z0-9_\\-\\.]*$";
    protected static final Pattern VALIDATE_INSTANCE_NAME_PATTERN = Pattern.compile(VALIDATE_INSTANCE_NAME_REGEX);

    /**
     * Validate Numeric Data
     *
     * @param textField Field to Validate as Numeric
     * @return Integer containing transformed value or Null, if invalid Data!
     */
    public static Integer validateNumericData(String textField) {
        if (textField == null || textField.isEmpty()) {
            return null;
        }
        try {
            return  Integer.parseInt(textField);
        } catch(NumberFormatException nfe) {
            return null;
        }
    }

    /**
     * Validate Numeric Data
     *
     * @param textField Field to Validate as Numeric and Valid Port Range
     * @return Integer containing transformed value or Null, if invalid Data!
     */
    public static Integer validatePort(String textField) {
        Integer value = validateNumericData(textField);
        if (value == null || value < 1024 || value > 65535) {
            return null;
        } else {
            return value;
        }
    }

    /**
     * Validate Property Names
     * @param propertyName to be Validated
     * @return boolean to indicate if the Name is valid or not.
     */
    public static boolean validatePropertyName(String propertyName) {
        if (propertyName == null || propertyName.isEmpty()) {
            return false;
        } else if (propertyName.startsWith("#")) {
            return true;
        }
        return VALIDATE_PROPERTY_NAME_PATTERN.matcher(propertyName).find();
    }

    /**
     * Validate JVM Option
     * @param jvmOption to be Validated
     * @return boolean to indicate if the Name is valid or not.
     */
    public static boolean validateJVMOption(String jvmOption) {
        if (jvmOption == null || jvmOption.isEmpty()) {
            return false;
        } else if (jvmOption.startsWith("#")) {
            return true;
        }
        return VALIDATE_JVM_OPTION_PATTERN.matcher(jvmOption).find();
    }

    /**
     * Validate Instance Name
     * @param instanceName to be Validated
     * @return boolean to indicate if the Name is valid or not.
     */
    public static boolean validateInstanceName(String instanceName) {
        if (instanceName == null || instanceName.isEmpty()) {
            return false;
        }
        return VALIDATE_INSTANCE_NAME_PATTERN.matcher(instanceName).find();
    }

    /**
     * Validate that this list of Ports are Unique.
     *
     * @param ports List of ports to validate all are unique.
     * @return
     */
    public static boolean validatePortsUnique(List<Integer> ports) {
          Set<Integer> duplicates = findDuplicates(ports);
          if (duplicates == null || duplicates.isEmpty()) {
              return true;
          } else {
              return false;
          }
    }

    /**
     * Helper Method to return Duplicates within a Collection.
     * @param list Collection to find Duplicates
     * @return Set of Duplicates found.
     */
    private static Set<Integer> findDuplicates(List<Integer> list) {
        Set<Integer> dups = new HashSet<>();
        Set<Integer> uniques = new HashSet<>();
        for(Integer value : list) {
            if (!uniques.add(value)) {
                dups.add(value);
            }
        }
        return dups;
    }
}
