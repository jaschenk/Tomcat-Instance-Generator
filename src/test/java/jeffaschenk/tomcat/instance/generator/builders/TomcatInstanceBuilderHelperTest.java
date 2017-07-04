package jeffaschenk.tomcat.instance.generator.builders;

import jeffaschenk.tomcat.instance.generator.model.TomcatInstance;
import jeffaschenk.tomcat.instance.generator.logger.GenerationLogger;
import jeffaschenk.tomcat.instance.generator.logger.Log;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * TomcatInstanceBuilderHelperTest
 *
 * Created by jeffaschenk@gmail.com on 2/21/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TomcatInstanceBuilderHelperTest {

    private static final GenerationLogger GENERATION_LOGGER =
            new GenerationLogger(new Log(), "test", true);

    /**
     * Test YAML File.
     */
    private static final String TEST_YAML_FILE =
            "src/test/resources/yaml/Test-TomcatInstance.yaml";

    /**
     * Test the Validation of Reading in a YAML File.
     */
    @Test
    public void test01() throws Exception {

        Yaml yaml = new Yaml(new Constructor(Map.class));
        InputStream input = new FileInputStream(new File(TEST_YAML_FILE));
        Map<String,Object> mapFromYaml = (Map) yaml.load(input);
        assertNotNull(mapFromYaml);

        TomcatInstance tomcatInstance = new TomcatInstance(mapFromYaml);
        assertNotNull(tomcatInstance);
        assertEquals("0c4cf885-11ca-4b5e-8df0-66769626f841", tomcatInstance.getTomcatInstanceUUID());
        System.out.println(tomcatInstance.toString());
    }

    /**
     * Test the Validation of Reading in a YAML File using Helper Methods.
     */
    @Test
    public void test02() throws Exception {
        TomcatInstance tomcatInstance =
                TomcatInstanceBuilderHelper.loadYAMLConfigurationForInstance(TEST_YAML_FILE);
        assertNotNull(tomcatInstance);
        assertEquals("0c4cf885-11ca-4b5e-8df0-66769626f841", tomcatInstance.getTomcatInstanceUUID());
        assertEquals("Test-FooBar-999", tomcatInstance.getInstanceName());
        assertTrue(tomcatInstance.getPrimaryPort() == 18080);
        assertTrue(tomcatInstance.isInstanceManagement());
        assertTrue(tomcatInstance.isSecureInstance());
        assertFalse(tomcatInstance.isCompressed());
        System.out.println(tomcatInstance.toString());
    }

    @Test
    public void test03() throws Exception {
        String hostname = TomcatInstanceBuilderHelper.getThisDefaultInstanceHostName();
        assertNotNull(hostname);
        assertFalse(hostname.equalsIgnoreCase("localhost"));
        System.out.println("Hostname obtained: "+hostname);
    }

    @Test
    public void test04() throws Exception {
        String ipAddress = TomcatInstanceBuilderHelper.getThisDefaultInetAddress();
        assertNotNull(ipAddress);
        assertFalse(ipAddress.equalsIgnoreCase("localhost"));
        System.out.println("IP Address obtained: "+ipAddress);
    }

    @Test
    public void test05() throws Exception {
        assertTrue(TomcatInstanceBuilderHelper.zipFile(GENERATION_LOGGER,
                "target\\test_instance_archive.zip", "src\\main\\resources"));
        File zipFile = new File("target\\test_instance_archive.zip");
        assertTrue(zipFile.exists());
    }

    @Test
    public void test06() throws Exception {
        String value = TomcatInstanceBuilderHelper.getNIXValue("Hello");
        assertNotNull(value);
        assertEquals("Hello", value);

        value = TomcatInstanceBuilderHelper.getNIXValue("\"Hello there!\"");
        assertNotNull(value);
        assertEquals("\\\"Hello there!\\\"", value);

    }

}
