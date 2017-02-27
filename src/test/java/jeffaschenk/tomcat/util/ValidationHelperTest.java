package jeffaschenk.tomcat.util;

import jeffaschenk.tomcat.util.ValidationHelper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * ResourceHelperTest
 *
 * Created by jeffaschenk@gmail.com on 2/17/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValidationHelperTest {

    /**
     * Test the Validation of Numeric Data
     */
    @Test
    public void test01() {

        assertNull(ValidationHelper.validateNumericData(""));
        assertNull(ValidationHelper.validateNumericData("89.foo.bar"));
        assertTrue(ValidationHelper.validateNumericData("18080") == 18080);
        assertTrue(ValidationHelper.validateNumericData("-1") == -1);
    }

    /**
     * Test the Validation of Property Names
     */
    @Test
    public void test02() {

        assertFalse(ValidationHelper.validatePropertyName(""));
        assertTrue(ValidationHelper.validatePropertyName("test.property.name"));
        assertFalse(ValidationHelper.validatePropertyName("test property name"));
        assertTrue(ValidationHelper.validatePropertyName("test_property_name.AbCD.xyz.99"));
        assertFalse(ValidationHelper.validatePropertyName("test_property_name.AbCD.xyz 99"));
        assertTrue(ValidationHelper.validatePropertyName("# Just a Test Comment ..."));
    }

    /**
     * Test the Validation of JVM Options
     */
    @Test
    public void test03() {

        assertFalse(ValidationHelper.validateJVMOption(""));
        assertTrue(ValidationHelper.validateJVMOption("-d64"));
        assertTrue(ValidationHelper.validateJVMOption("--X:67ABC"));
        assertTrue(ValidationHelper.validateJVMOption("-server"));
        assertTrue(ValidationHelper.validateJVMOption("# Just a Test Comment ..."));
    }

    /**
     * Test the Validation of Instance Names
     */
    @Test
    public void test04() {

        assertFalse(ValidationHelper.validateInstanceName(""));
        assertTrue(ValidationHelper.validateInstanceName("test.instance.name"));
        assertFalse(ValidationHelper.validateInstanceName("test property name"));
        assertTrue(ValidationHelper.validateInstanceName("test_property_name-AbCD.xyz.99"));
        assertFalse(ValidationHelper.validateInstanceName("test_property_name.AbCD.xyz 99"));
        assertTrue(ValidationHelper.validateInstanceName("--test_property_name.AbCD.xyz-99"));
    }

    /**
     * Test the Validation of Numeric Data
     */
    @Test
    public void test05() {

        assertNull(ValidationHelper.validatePort(""));
        assertNull(ValidationHelper.validatePort("89.foo.bar"));
        assertTrue(ValidationHelper.validatePort("18080") == 18080);
        assertNull(ValidationHelper.validatePort("-1"));
        assertNull(ValidationHelper.validatePort("1023"));
        assertNull(ValidationHelper.validatePort("65536"));
        assertNotNull(ValidationHelper.validatePort("1025"));
        assertNotNull(ValidationHelper.validatePort("65535"));
    }

    /**
     * Test the Validation of Unique Ports
     */
    @Test
    public void test06() {
        List<Integer> ports = new ArrayList<>();
        ports.add(18080);
        ports.add(18081);
        ports.add(18082);
        ports.add(18083);
        ports.add(18084);
        assertTrue(ValidationHelper.validatePortsUnique(ports));

        ports.add(18080);
        assertFalse(ValidationHelper.validatePortsUnique(ports));
    }

}
