package jeffaschenk.tomcat.util;

import jeffaschenk.tomcat.util.ResourceHelper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * ResourceHelperTest
 *
 * Created by jeffaschenk@gmail.com on 2/17/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ResourceHelperTest {

    static final String CONFIG_DOCUMENT =
            "SHUTDOWN: ${SHUTDOWN_PORT_NUMBER} PRIMARY: ${PRIMARY_PORT_NUMBER} SECURE: ${SECURE_PORT_NUMBER} AJP: ${AJP_PORT_NUMBER}";

    static final String RESULATANT_DOCUMENT =
            "SHUTDOWN: 18005 PRIMARY: 18080 SECURE: 18443 AJP: 18009";

    /**
     * Test the String Builder Data Replacement.
     */
    @Test
    public void test01() {

        StringBuilder sb = new StringBuilder(CONFIG_DOCUMENT);

        Map<String,String> replacementMap = new HashMap<>();
        replacementMap.put("${SHUTDOWN_PORT_NUMBER}","18005");
        replacementMap.put("${PRIMARY_PORT_NUMBER}", "18080");
        replacementMap.put("${SECURE_PORT_NUMBER}", "18443");
        replacementMap.put("${AJP_PORT_NUMBER}", "18009");

        ResourceHelper.replace(sb, replacementMap);
        /**
         * Validate the Filtered Resource has been created...
         */
        assertEquals(RESULATANT_DOCUMENT, sb.toString());
    }


}
