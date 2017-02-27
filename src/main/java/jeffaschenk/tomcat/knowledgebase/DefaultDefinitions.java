package jeffaschenk.tomcat.knowledgebase;

import jeffaschenk.tomcat.instance.generator.builders.TomcatArchive;
import jeffaschenk.tomcat.instance.generator.ui.InstanceJVMOptionRow;
import jeffaschenk.tomcat.instance.generator.ui.InstancePropertyRow;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jeffaschenk.tomcat.knowledgebase.ReplacementDefinitions.TOMCAT_INSTANCE_HOSTNAME_TAG;
import static jeffaschenk.tomcat.knowledgebase.ReplacementDefinitions.TOMCAT_INSTANCE_NAME_TAG;
import static jeffaschenk.tomcat.knowledgebase.ReplacementDefinitions.TOMCAT_PRIMARY_PORT_TAG;

/**
 * DefaultDefinitions
 *
 * Created by jeffaschenk@gmail.com on 2/15/2017.
 */
public final class DefaultDefinitions {
    /**
     * Constants
     */
    protected static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    protected static SimpleDateFormat DATE_TIME_ZONE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a z");

    /**
     * ManageCat License Key for Managing Tomcat Instances.
     */
    public static final String MANAGECAT_LICENSE_KEY =
            "00000000000000000000000000000000";

    /**
     * Apache Mirrors URL
     */
    public static final String APACHE_MIRRORS_HEAD_URL =
            "http://apache.mirrors.pair.com/tomcat";

    /**
     * Apache Tomcat Mirror Version Folders
     */
    public static final String APACHE_TOMCAT_8_MIRROR_HEAD = "tomcat-8";
    public static final String APACHE_TOMCAT_9_MIRROR_HEAD = "tomcat-9";

    /**
     * Default Environments
     */
    public static final String[] DEFAULT_ENVIRONNMENT =
            {
                    "DEV",
                    "STAGING",
                    "TEST",
                    "PRODUCTION"
            };
    public static final String DEFAULT_ENVIRONMENT_SELECTED = DEFAULT_ENVIRONNMENT[0];

    /**
     * Default Tomcat Versions
     */
    public static final String[] DEFAULT_TOMCAT_VERSIONS =
            {
                    "v8.5.11", "v9.0.0.M17"
            };
    public static final String DEFAULT_TOMCAT_VERSION_SELECTED = DEFAULT_TOMCAT_VERSIONS[0];

    /**
     * Tomcat Full Version to Short Have Name
     */
    public static final Map<String,String> TOMCAT_VERSIONS_TO_SHORT_NAME = new HashMap<>();
    static {
            TOMCAT_VERSIONS_TO_SHORT_NAME.put(DEFAULT_TOMCAT_VERSIONS[0], "v85");
            TOMCAT_VERSIONS_TO_SHORT_NAME.put(DEFAULT_TOMCAT_VERSIONS[1], "v90");
    }

    /**
     * Tomcat Version Archive Sizes, to validate the archive is good,
     * checking checksums and size in the number of bytes are checked.
     *
     * To Add a New version, simply add the Archive Name and Length in Bytes of Archive.
     *
     */
    public static final List<TomcatArchive> DEFAULT_TOMCAT_ARCHIVES = new ArrayList<>(1);
    static {

             DEFAULT_TOMCAT_ARCHIVES.add(new TomcatArchive("apache-tomcat-8.5.11.zip",
                     9858689L, "16529edcce866ee493b11e315816213b24492d28"));
    }

    /**
     * Default Ports
     */
    public static final Integer DEFAULT_PRIMARY_PORT = 18080;
    public static final Integer DEFAULT_SECURE_PORT = 18433;
    public static final Integer DEFAULT_SHUTDOWN_PORT = 18005;
    public static final Integer DEFAULT_AJP_PORT = 18009;

    /**
     * Available Protocols
     */
    public static final String[] DEFAULT_CATALINA_PROTOCOLS =
            {
                    "HTTP/1.1", "org.apache.coyote.http11.Http11NioProtocol"
            };
    public static final String DEFAULT_CATALINA_PROTOCOL_SELECTED = DEFAULT_CATALINA_PROTOCOLS[0];
    public static final String DEFAULT_CATALINA_SECURE_PROTOCOL_SELECTED = DEFAULT_CATALINA_PROTOCOLS[1];

    /**
     * Java JVM Heap Sizes
     */
    public static final String[] DEFAULT_JVM_MEMORY_OPTIONS =
            {
              "None", "64m", "128m", "192m", "256m", "320m", "384m", "448m", "512m",
                    "640m", "768m", "896m",
                    "1g", "1280m", "1536m",
                    "2g", "2560m",
                    "3g", "3584m",
                    "4g", "4608m",
                    "5g", "5632m",
                    "6g", "6656m",
                    "7g", "7680m",
                    "8g"
            };
    public static final String DEFAULT_JVM_MEMORY_OPTIONS_SELECTED = DEFAULT_JVM_MEMORY_OPTIONS[15];
    public static final String DEFAULT_JVM_MEMORY_SS_OPTION_SELECTED = DEFAULT_JVM_MEMORY_OPTIONS[4];
    public static final String DEFAULT_JVM_MEMORY_METASPACE_OPTION_SELECTED = DEFAULT_JVM_MEMORY_OPTIONS[10];

    /**
     * Default Management Properties
     */
    public static final List<InstancePropertyRow> DEFAULT_MANAGEMENT_PROPERTY_ROWS = new ArrayList<>();
    static {
        ADD_DEFAULT_VALUE_TO_PROPERTIES(DEFAULT_MANAGEMENT_PROPERTY_ROWS,
                "# com.managecat.controller.url","<MANAGECAT_SERVER>/controller/");

        ADD_DEFAULT_VALUE_TO_PROPERTIES(DEFAULT_MANAGEMENT_PROPERTY_ROWS,
                "# com.managecat.collector.url","<MANAGECAT_SERVER>/collector/");

        ADD_DEFAULT_VALUE_TO_PROPERTIES(DEFAULT_MANAGEMENT_PROPERTY_ROWS,
                "# com.managecat.console.agent.groupname","Development");

        ADD_DEFAULT_VALUE_TO_PROPERTIES(DEFAULT_MANAGEMENT_PROPERTY_ROWS,
                "# com.managecat.console.agent.servername", TOMCAT_INSTANCE_NAME_TAG);

        ADD_DEFAULT_VALUE_TO_PROPERTIES(DEFAULT_MANAGEMENT_PROPERTY_ROWS,
                "# com.managecat.console.agent.hostname", TOMCAT_INSTANCE_HOSTNAME_TAG);

        ADD_DEFAULT_VALUE_TO_PROPERTIES(DEFAULT_MANAGEMENT_PROPERTY_ROWS,
                "# com.managecat.console.agent.port", TOMCAT_PRIMARY_PORT_TAG);

        ADD_DEFAULT_VALUE_TO_PROPERTIES(DEFAULT_MANAGEMENT_PROPERTY_ROWS,
                "# com.managecat.console.agent.contextname","console");

        ADD_DEFAULT_VALUE_TO_PROPERTIES(DEFAULT_MANAGEMENT_PROPERTY_ROWS,
                "# com.managecat.console.agent.secure","false");

        ADD_DEFAULT_VALUE_TO_PROPERTIES(DEFAULT_MANAGEMENT_PROPERTY_ROWS,
                "# com.managecat.console.agent.accountkey",MANAGECAT_LICENSE_KEY);

        ADD_DEFAULT_VALUE_TO_PROPERTIES(DEFAULT_MANAGEMENT_PROPERTY_ROWS,
                "# com.sun.management.jmxremote","");

        ADD_DEFAULT_VALUE_TO_PROPERTIES(DEFAULT_MANAGEMENT_PROPERTY_ROWS,
                "# com.sun.management.jmxremote.port","65420"); 

        ADD_DEFAULT_VALUE_TO_PROPERTIES(DEFAULT_MANAGEMENT_PROPERTY_ROWS,
                "# com.sun.management.jmxremote.ssl","false");

        ADD_DEFAULT_VALUE_TO_PROPERTIES(DEFAULT_MANAGEMENT_PROPERTY_ROWS,
                "# com.sun.management.jmxremote.authenticate","false");
    }

    /**
     * Default JVM Options
     */
    public static final List<InstanceJVMOptionRow> DEFAULT_JVM_OPTION_ROWS = new ArrayList<>();
    static {

        ADD_DEFAULT_VALUE_TO_JVM_OPTIONS(DEFAULT_JVM_OPTION_ROWS,
                "-server");
        
        ADD_DEFAULT_VALUE_TO_JVM_OPTIONS(DEFAULT_JVM_OPTION_ROWS,
                "# -Xdebug");

        ADD_DEFAULT_VALUE_TO_JVM_OPTIONS(DEFAULT_JVM_OPTION_ROWS,
                "# -Xrunjdwp:transport=dt_socket,address=64420,suspend=n,server=y");
    }
    
    /**
     * Add Default Value To Management Properties
     *
     * @param defaultList List of default Entries
     * @param jvmOption of Entry to be Added
     */
    protected static void ADD_DEFAULT_VALUE_TO_JVM_OPTIONS(List<InstanceJVMOptionRow> defaultList,
                                                                     String jvmOption) {
        InstanceJVMOptionRow instanceJVMOptionRow = new InstanceJVMOptionRow();
        instanceJVMOptionRow.setJvmOption(jvmOption);
        defaultList.add(instanceJVMOptionRow);
    }

    /**
     * Default Management Properties
     */
    public static final List<InstancePropertyRow> DEFAULT_INSTANCE_PROPERTY_ROWS = new ArrayList<>();
    static {
        ADD_DEFAULT_VALUE_TO_PROPERTIES(DEFAULT_INSTANCE_PROPERTY_ROWS,
                "welcome.message", "\"Hello to Tomcat Generation Utility\"");
    }

    /**
     * Add Default Value To Management Properties
     *
     * @param defaultList List of default Entries
     * @param name of Entry to be Added
     * @param value of Entry to be Added
     */
    protected static void ADD_DEFAULT_VALUE_TO_PROPERTIES(List<InstancePropertyRow> defaultList,
                                                          String name, String value) {
        InstancePropertyRow instancePropertyRow = new InstancePropertyRow();
        instancePropertyRow.setPropertyName(name);
        instancePropertyRow.setPropertyValue(value);
        defaultList.add(instancePropertyRow);
    }

    /**
     * Additional directories to be Created during Customization Processing ...
     */
    public static final String[] ADDITIONAL_DIRECTORIES_TO_BE_ADDED = {"properties"};

    /**
     * Additional directories to be Created during Customization Processing ...
     */
    public static final String[] EMPTY_DIRECTORIES_TO_BE_SEEDED = {"logs", "properties", "work"};

    /**
     * Additional directories to be Created during Customization Processing ...
     */
    public static final String[] EMPTY_DIRECTORIES_README_CONTENTS = {"* Contains Tomcat Instance logs.",
            "* Contains Tomcat Application Property Files.", "* Contains 'work' Directory for Catalina Engine."};

    /**
     * Additional 'lib' Artifacts to be Written ...
     */
    public static final String[] ADDITIONAL_LIB_ARTIFACTS_TO_BE_ADDED = {};

    /**
     * Additional 'webapps' for Instance Management to be Written ...
     */
    public static final String[] ADDITIONAL_WEBAPPS_TO_BE_ADDED_FOR_MANAGED_INSTANCE = {};

    /**
     * 'conf' Files to be Written and Filtered ...
     */
    public static final String[] CONF_FILES_TO_BE_ADDED_FILTERED = {"catalina.properties", "context.xml",
                    "server.xml", "tomcat-users.xml", "web.xml"};

    /**
     * 'bin' Files to be Written and Filtered ...
     */
    public static final String[] BIN_FILES_TO_BE_ADDED_FILTERED = {"setenv.bat", "setenv.sh" };
    
}
