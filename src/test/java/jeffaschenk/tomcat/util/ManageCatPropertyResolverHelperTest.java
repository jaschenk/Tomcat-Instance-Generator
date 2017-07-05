package jeffaschenk.tomcat.util;


import jeffaschenk.tomcat.instance.generator.builders.TomcatInstanceBuilderHelper;
import jeffaschenk.tomcat.knowledgebase.DefaultDefinitions;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * ManageCatPropertyResolverHelperTest
 *
 * Created by jeffaschenk@gmail.com on 7/03/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ManageCatPropertyResolverHelperTest {

    private static final String TEST_DUMMY_MANAGECAT_LICENSE_KEY =
            "1234567890abcdefghijklmnopqrstuv";

    @Test
    public void test01() throws Exception {
        /**
         * Prepare for test by setting up External Property for Test...
         */
        System.setProperty(DefaultDefinitions.MANAGECAT_LICENSE_KEY_PROPERTY_NAME,
                TEST_DUMMY_MANAGECAT_LICENSE_KEY);
        /**
         * Perform Test
         */
         Map<String,String> results =
                 TomcatInstanceBuilderHelper.resolvedExternalManagementProperties();
         assertNotNull(results);
         assertEquals(1, results.size());
         assertEquals(TEST_DUMMY_MANAGECAT_LICENSE_KEY,
                 results.get(DefaultDefinitions.MANAGECAT_LICENSE_KEY_INTERNAL_PROPERTY_NAME));
    }

    @Test
    public void test02() throws Exception {
        /**
         * Prepare for test by setting up External Property for Test...
         */
        System.clearProperty(DefaultDefinitions.MANAGECAT_LICENSE_KEY_PROPERTY_NAME);
        /**
         * Perform Test
         */
        Map<String,String> results =
                TomcatInstanceBuilderHelper.resolvedExternalManagementProperties();
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(DefaultDefinitions.DEFAULT_MANAGECAT_LICENSE_KEY_VALUE,
                results.get(DefaultDefinitions.MANAGECAT_LICENSE_KEY_INTERNAL_PROPERTY_NAME));
    }


}
