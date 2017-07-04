package jeffaschenk.tomcat.util;

import jeffaschenk.tomcat.instance.generator.builders.TomcatInstanceBuilderHelper;
import jeffaschenk.tomcat.instance.generator.model.TomcatArchive;
import jeffaschenk.tomcat.instance.generator.model.TomcatAvailableArchives;
import jeffaschenk.tomcat.knowledgebase.DefaultDefinitions;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

/**
 * TomcatAvailableArchiveHelperTest
 *
 * Created by jeffaschenk@gmail.com on 7/03/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TomcatAvailableArchiveHelperTest {

    @Before
    public void initialize() {
        System.setProperty(DefaultDefinitions.TOMCAT_ARCHIVE_PROPERTY_NAME,
                "src/test/resources/yaml/Test-TomcatKnowledgeBase.yaml");
    }

    /**
     * Test the String Builder Data Replacement.
     */
    @Test
    public void test01() throws Exception {
        TomcatAvailableArchives tomcatAvailableArchives = TomcatInstanceBuilderHelper.loadAvailableArchives();
        assertNotNull(tomcatAvailableArchives);

        assertNotNull(tomcatAvailableArchives.getApacheMirrorHeadUrl());
        assertEquals("http://apache.cs.utah.edu/tomcat", tomcatAvailableArchives.getApacheMirrorHeadUrl());

        assertNotNull(tomcatAvailableArchives.getArchives());
        assertEquals(4, tomcatAvailableArchives.getArchives().size());
           
        TomcatArchive tomcatArchive = tomcatAvailableArchives.getArchives().get("apache-tomcat-8.5.12");
        assertNotNull(tomcatArchive);
        assertEquals("apache-tomcat-8.5.12", tomcatArchive.getName());
        assertEquals("v8.5", tomcatArchive.getShortName());
        assertTrue(tomcatArchive.getSize() == 9927423L);
        assertEquals("dcc5b79f25ffa06d6ae615b48b441870792cb4d4", tomcatArchive.getCheckSum());
        assertFalse(tomcatArchive.isAvailable());

        tomcatArchive = tomcatAvailableArchives.getArchives().get("apache-tomcat-8.5.16");
        assertNotNull(tomcatArchive);
        assertEquals("apache-tomcat-8.5.16", tomcatArchive.getName());
        assertEquals("v8.5", tomcatArchive.getShortName());
        assertTrue(tomcatArchive.getSize() == 9992463L);
        assertEquals("13bf717a94a7b8d5296e678a70004a65f0c0409f", tomcatArchive.getCheckSum());
        assertTrue(tomcatArchive.isAvailable());

    }

    @Test
    public void test02() throws Exception {
        TomcatAvailableArchives tomcatAvailableArchives = TomcatInstanceBuilderHelper.loadAvailableArchives();
        assertNotNull(tomcatAvailableArchives);

        TomcatArchive tomcatArchive = tomcatAvailableArchives.getAvailableArchiveByShortName("v8.5");
        assertNotNull(tomcatArchive);
        assertEquals("v8.5", tomcatArchive.getShortName());
        assertTrue(tomcatArchive.isAvailable());
        assertEquals("apache-tomcat-8.5.16", tomcatArchive.getName());

        assertEquals("8.5.16", tomcatArchive.getShortVersion());
    }


}
