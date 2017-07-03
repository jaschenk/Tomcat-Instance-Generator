package jeffaschenk.tomcat.instance.generator.model;

import jeffaschenk.tomcat.knowledgebase.DefaultDefinitions;

import java.io.File;
import java.util.*;

import static jeffaschenk.tomcat.knowledgebase.ReplacementDefinitions.*;

/**
 * TomcatInstance
 * <p>
 * Created by jeffaschenk@gmail.com on 2/16/2017.
 */
public final class TomcatInstance {
    /**
     * Tomcat GUID
     */
    private final String tomcatInstanceUUID;

    /**
     * Tomcat Instance Name
     */
    private final String instanceName;

    /**
     * Tomcat Environment Name
     */
    private final String environmentName;

    /**
     * Tomcat Version
     */
    private final String tomcatVersion;

    /**
     * Destination Folder
     */
    private final File destinationFolder;

    /**
     * Compress Indicator, if True the resulting Tomcat Instance will be Zipped up...
     */
    private final boolean compressed;

    /**
     * Tomcat Primary non-SSL Port.
     */
    private final Integer primaryPort;

    /**
     * Tomcat Non-SSL Port Protocol.
     */
    private final String protocolPrimaryPort;

    /**
     * Tomcat Shutdown Port.
     */
    private final Integer shutdownPort;

    /**
     * Tomcat AJP Port
     */
    private final Integer ajpPort;

    /**
     * Indicates that a Secure Instance should be built.
     */
    private final boolean secureInstance;

    /**
     * Secure Instance SSL/TLS Port
     */
    private final Integer securePort;

    /**
     * Secure Instance SSL/TLS Port Protocol
     */
    private final String protocolSecurePort;

    /**
     * Tomcat Instance Secure Interface Keystore Filename.
     */
    private final String keystoreSourceFilename;

    /**
     * Tomcat Instance Secure Interface Keystore Credentials.
     */
    private final String keystoreCredentials;

    /**
     * JVM Option Minimum Heap.
     */
    private final String jvmOptionXms;

    /**
     * JVM Option Maximum Heap.
     */
    private final String jvmOptionXmx;

    /**
     * JVM Option Thread Stack Size.
     */
    private final String jvmOptionXss;

    /**
     * JVM Option Java8 Metaspace Size
     */
    private final String jvmOptionXXMaxMetaspaceSize;

    /**
     * Additional JVM Options
     */
    private final List<String> jvmOptions;

    /**
     * Indicator that Instance Management should be established
     */
    private final boolean instanceManagement;

    /**
     * Instance Management Properties to be set during Build...
     */
    private final List<TomcatInstanceProperty> instanceManagementProperties;

    /**
     * Instance Properties to be set during Build...
     */
    private final List<TomcatInstanceProperty> instanceProperties;

    /**
     * Archive Associated to this Tomcat Instance.
     */
    private TomcatArchive tomcatArchive;

    /**
     * Tomcat Instance Default Constructor
     *
     * @param instanceName      Name of Tomcat Instance
     * @param environmentName   Environment Name
     * @param tomcatVersion     Tomcat Version
     * @param destinationFolder Destination Folder for Instance
     * @param compressed        Indicator
     */
    public TomcatInstance(String instanceName, String environmentName, String tomcatVersion, File destinationFolder,
                          boolean compressed) {
        this.tomcatInstanceUUID = UUID.randomUUID().toString();
        this.instanceName = instanceName;
        this.environmentName = environmentName;
        this.tomcatVersion = tomcatVersion;
        this.destinationFolder = destinationFolder;
        this.compressed = compressed;
        /**
         * Set Remainder with Defaults...
         */
        this.primaryPort = DefaultDefinitions.DEFAULT_PRIMARY_PORT;
        this.protocolPrimaryPort = DefaultDefinitions.DEFAULT_CATALINA_PROTOCOL_SELECTED;
        this.shutdownPort = DefaultDefinitions.DEFAULT_SHUTDOWN_PORT;
        this.ajpPort = DefaultDefinitions.DEFAULT_AJP_PORT;
        /**
         * Not a Secure Instance
         */
        this.secureInstance = false;
        this.securePort = DefaultDefinitions.DEFAULT_SECURE_PORT;
        this.protocolSecurePort = DefaultDefinitions.DEFAULT_CATALINA_SECURE_PROTOCOL_SELECTED;
        this.keystoreSourceFilename = null;
        this.keystoreCredentials = null;
        /**
         * Set Default JVM Options.
         */
        this.jvmOptionXms = DefaultDefinitions.DEFAULT_JVM_MEMORY_OPTIONS_SELECTED;
        this.jvmOptionXmx = DefaultDefinitions.DEFAULT_JVM_MEMORY_OPTIONS_SELECTED;
        this.jvmOptionXss = DefaultDefinitions.DEFAULT_JVM_MEMORY_OPTIONS[0];
        this.jvmOptionXXMaxMetaspaceSize = DefaultDefinitions.DEFAULT_JVM_MEMORY_OPTIONS[0];
        this.jvmOptions = new ArrayList<>(0);
        /**
         * Set the Instance Management as not used.
         */
        this.instanceManagement = false;
        this.instanceManagementProperties = new ArrayList<>(0);
        /**
         * Set Additional Instance Properties...
         */
        this.instanceProperties = new ArrayList<>(0);
    }

    /**
     * Default Constructor with all applicable Properties to drive the Generation process.
     *
     * @param instanceName                 Name of Tomcat Instance
     * @param environmentName              Environment Name
     * @param tomcatVersion                Tomcat Version
     * @param destinationFolder            Destination Folder for Instance
     * @param compressed                   Indicator
     * @param primaryPort                  Instance Primary Port
     * @param protocolPrimaryPort          Primary Port Protocol
     * @param shutdownPort                 Shutdown Port
     * @param ajpPort                      AJP Port
     * @param secureInstance               Secure Instance?
     * @param securePort                   Secure Port
     * @param protocolSecurePort           Secure Port Protocol
     * @param keystoreSourceFilename       Keystore Filename
     * @param keystoreCredentials          KeyStore Credentials
     * @param jvmOptionXms                 JVM Minimum Heap Size.
     * @param jvmOptionXmx                 JVM Maximum Heap Size.
     * @param jvmOptionXss                 JVM Thread Stack Size.
     * @param jvmOptionXXMaxMetaspaceSize  JVM Metaspace Size.
     * @param jvmOptions                   Additional JVM Options.
     * @param instanceManagement           Indicator to include instance Management Properties.
     * @param instanceManagementProperties Instance Management Properties.
     * @param instanceProperties           Instance Properties.
     */
    public TomcatInstance(String instanceName, String environmentName, String tomcatVersion,
                          File destinationFolder, boolean compressed,
                          Integer primaryPort, String protocolPrimaryPort, Integer shutdownPort, Integer ajpPort,
                          boolean secureInstance, Integer securePort,
                          String protocolSecurePort, String keystoreSourceFilename, String keystoreCredentials,
                          String jvmOptionXms, String jvmOptionXmx, String jvmOptionXss, String jvmOptionXXMaxMetaspaceSize,
                          List<String> jvmOptions,
                          boolean instanceManagement,
                          List<TomcatInstanceProperty> instanceManagementProperties,
                          List<TomcatInstanceProperty> instanceProperties) {
        this.tomcatInstanceUUID = UUID.randomUUID().toString();
        this.instanceName = instanceName;
        this.environmentName = environmentName;
        this.tomcatVersion = tomcatVersion;
        this.destinationFolder = destinationFolder;
        this.compressed = compressed;
        this.primaryPort = primaryPort;
        this.protocolPrimaryPort = protocolPrimaryPort;
        this.shutdownPort = shutdownPort;
        this.ajpPort = ajpPort;
        this.secureInstance = secureInstance;
        this.securePort = securePort;
        this.protocolSecurePort = protocolSecurePort;
        this.keystoreSourceFilename = keystoreSourceFilename;
        this.keystoreCredentials = keystoreCredentials;
        this.jvmOptionXms = jvmOptionXms;
        this.jvmOptionXmx = jvmOptionXmx;
        this.jvmOptionXss = jvmOptionXss;
        this.jvmOptionXXMaxMetaspaceSize = jvmOptionXXMaxMetaspaceSize;
        this.jvmOptions = jvmOptions;
        this.instanceManagement = instanceManagement;
        this.instanceManagementProperties = instanceManagementProperties;
        this.instanceProperties = instanceProperties;
    }

    /**
     * Constructor to read in a Map from a Parsed YAML Configuration File.
     * @param tomcatInstanceMap
     */
    public TomcatInstance(Map<String,Object> tomcatInstanceMap) {
        this.tomcatInstanceUUID = (String) tomcatInstanceMap.get("instanceUUID");
        this.instanceName = (String) tomcatInstanceMap.get("instanceName");
        this.environmentName = (String) tomcatInstanceMap.get("environmentName");
        this.tomcatVersion = (String)  tomcatInstanceMap.get("tomcatVersion");
        this.destinationFolder = new File(tomcatInstanceMap.get("destinationFolder").toString());
        this.compressed = (boolean) tomcatInstanceMap.get("compressed");
        this.primaryPort = (Integer) tomcatInstanceMap.get("primaryPort");
        this.protocolPrimaryPort = (String) tomcatInstanceMap.get("protocolPrimaryPort");
        this.shutdownPort = (Integer) tomcatInstanceMap.get("shutdownPort");
        this.ajpPort = (Integer) tomcatInstanceMap.get("ajpPort");
        this.secureInstance = (boolean) tomcatInstanceMap.get("secureInstance");
        this.securePort = (Integer) tomcatInstanceMap.get("securePort");
        this.protocolSecurePort = (String) tomcatInstanceMap.get("protocolSecurePort");
        this.keystoreSourceFilename = (String) tomcatInstanceMap.get("keystoreSourceFilename");
        this.keystoreCredentials = (String) tomcatInstanceMap.get("keystoreCredentials");
        this.jvmOptionXms = (String) tomcatInstanceMap.get("jvmOptionXms");
        this.jvmOptionXmx = (String) tomcatInstanceMap.get("jvmOptionXmx");
        this.jvmOptionXss = (String) tomcatInstanceMap.get("jvmOptionXss");
        this.jvmOptionXXMaxMetaspaceSize = (String) tomcatInstanceMap.get("jvmOptionXXMaxMetaspaceSize");
        this.jvmOptions = (List<String>) tomcatInstanceMap.get("jvmOptions");
        this.instanceManagement = (boolean) tomcatInstanceMap.get("instanceManagement");

        /**
         * DeSerialize our Instance Management Properties
         */
        this.instanceManagementProperties = new ArrayList<>();
        List<Map<String,String>> instanceManagementPropertiesMapList =
                (List<Map<String,String>>) tomcatInstanceMap.get("instanceManagementProperties");
        for(Map<String,String> mapElement : instanceManagementPropertiesMapList) {
            TomcatInstanceProperty tomcatInstanceProperty = new TomcatInstanceProperty(mapElement);
            instanceManagementProperties.add(tomcatInstanceProperty);
        }
        /**
         * DeSerialize our Instance Properties
         */
        this.instanceProperties = new ArrayList<>();
        List<Map<String,String>> instancePropertiesMapList =
                (List<Map<String,String>>) tomcatInstanceMap.get("instanceProperties");
        for(Map<String,String> mapElement : instancePropertiesMapList) {
            TomcatInstanceProperty tomcatInstanceProperty = new TomcatInstanceProperty(mapElement);
            instanceProperties.add(tomcatInstanceProperty);
        }

    }

    /**
     * ***************************************************
     * Getter Methods
     * ***************************************************
     */

    public String getTomcatInstanceUUID() { return tomcatInstanceUUID; }

    public String getInstanceName() {
        return instanceName;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public String getTomcatVersion() {
        return tomcatVersion;
    }

    public File getDestinationFolder() {
        return destinationFolder;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public Integer getPrimaryPort() {
        return primaryPort;
    }

    public String getProtocolPrimaryPort() {
        return protocolPrimaryPort;
    }

    public Integer getShutdownPort() {
        return shutdownPort;
    }

    public Integer getAjpPort() {
        return ajpPort;
    }

    public boolean isSecureInstance() {
        return secureInstance;
    }

    public Integer getSecurePort() {
        return securePort;
    }

    public String getProtocolSecurePort() {
        return protocolSecurePort;
    }

    public String getKeystoreSourceFilename() {
        return keystoreSourceFilename;
    }

    public String getKeystoreCredentials() {
        return keystoreCredentials;
    }

    public String getJvmOptionXms() {
        return jvmOptionXms;
    }

    public String getJvmOptionXmx() {
        return jvmOptionXmx;
    }

    public String getJvmOptionXss() {
        return jvmOptionXss;
    }

    public String getJvmOptionXXMaxMetaspaceSize() {
        return jvmOptionXXMaxMetaspaceSize;
    }

    public List<String> getJvmOptions() {
        return jvmOptions;
    }

    public boolean isInstanceManagement() {
        return instanceManagement;
    }

    public List<TomcatInstanceProperty> getInstanceManagementProperties() {
        return instanceManagementProperties;
    }

    public List<TomcatInstanceProperty> getInstanceProperties() {
        return instanceProperties;
    }

    @Override
    public String toString() {
        return "TomcatInstance{" +
                " instanceUUID='" + tomcatInstanceUUID + '\'' +
                ", instanceName='" + instanceName + '\'' +
                ", environmentName='" + environmentName + '\'' +
                ", tomcatVersion='" + tomcatVersion + '\'' +
                ", destinationFolder=" + destinationFolder +
                ", compressed=" + compressed +
                ", primaryPort=" + primaryPort +
                ", protocolPrimaryPort='" + protocolPrimaryPort + '\'' +
                ", shutdownPort=" + shutdownPort +
                ", ajpPort=" + ajpPort +
                ", secureInstance=" + secureInstance +
                ", securePort=" + securePort +
                ", protocolSecurePort='" + protocolSecurePort + '\'' +
                ", keystoreSourceFilename='" + keystoreSourceFilename + '\'' +
                ", keystoreCredentials='" + keystoreCredentials + '\'' +
                ", jvmOptionXms='" + jvmOptionXms + '\'' +
                ", jvmOptionXmx='" + jvmOptionXmx + '\'' +
                ", jvmOptionXss='" + jvmOptionXss + '\'' +
                ", jvmOptionXXMaxMetaspaceSize='" + jvmOptionXXMaxMetaspaceSize + '\'' +
                ", jvmOptions=" + jvmOptions +
                ", instanceManagement=" + instanceManagement +
                ", instanceManagementProperties=" + instanceManagementProperties +
                ", instanceProperties=" + instanceProperties +
                '}';
    }

    /**
     * ***************************************************
     * Helper Methods
     * ***************************************************
     */
    
    /**
     * Return the Original referenced Download Archive.
     *
     * @return String containing fully qualified formulated Download Archive.
     */
    public String referenceDownloadedArchiveFolder() {
        if (tomcatArchive == null) {
            throw new IllegalStateException("No Tomcat Archive has been Selected!");
        }
        return getDestinationFolder().getAbsolutePath() + File.separator + this.tomcatArchive.getName();
    }

    /**
     * Return the formulated Tomcat Instance Folder for this Instance Creation.
     *
     * @return String containing the formulated new Tomcat Instance folder, less a Parent.
     */
    public String referenceTomcatInstanceFolder() {
        if (tomcatArchive == null) {
            throw new IllegalStateException("No Tomcat Archive has been Selected!");
        }
        return getInstanceName() + "-" + getEnvironmentName() + "-" +this.tomcatArchive.getName();
    }

    /**
     * Return the formulated Tomcat Instance YAML configuration File for this Instance Creation.
     *
     * @return String containing the formulated new Tomcat Instance folder, less a Parent.
     */
    public String referenceTomcatInstanceYAML() {
        if (tomcatArchive == null) {
            throw new IllegalStateException("No Tomcat Archive has been Selected!");
        }
        return getInstanceName() + "-" + getEnvironmentName() + "-" +
                this.tomcatArchive.getName() + ".yaml";

    }

    /**
     * Return the File reference of the constructed YAML Configuration File.
     *
     * @return File representing the YAML File.
     */
    public File referenceSourceYAMLFile() {
        return new File(getDestinationFolder().getAbsolutePath() + File.separator + referenceTomcatInstanceYAML());
    }

    /**
     * Return the File reference of the YAML Configuration File in the Instance Directory.
     *
     * @return File representing the YAML File.
     */
    public File referenceDestinationYAMLFile() {
        return new File(getDestinationFolder().getAbsolutePath() + File.separator +
                referenceTomcatInstanceFolder() + File.separator + referenceTomcatInstanceYAML());
    }

    /**
     * Generate a Map representing this Object.
     *
     * @return Map representing this Object.
     */
    public Map<String, Object> map() {
        Map<String, Object> tomcatInstanceMap = new HashMap<>();
        tomcatInstanceMap.put("instanceUUID", tomcatInstanceUUID);
        tomcatInstanceMap.put("instanceName", instanceName);
        tomcatInstanceMap.put("environmentName", environmentName);
        tomcatInstanceMap.put("tomcatVersion", tomcatVersion);
        tomcatInstanceMap.put("destinationFolder", destinationFolder.getAbsolutePath());
        tomcatInstanceMap.put("compressed", compressed);
        tomcatInstanceMap.put("primaryPort", primaryPort);
        tomcatInstanceMap.put("protocolPrimaryPort", protocolPrimaryPort);
        tomcatInstanceMap.put("shutdownPort", shutdownPort);
        tomcatInstanceMap.put("ajpPort", ajpPort);
        tomcatInstanceMap.put("secureInstance", secureInstance);
        tomcatInstanceMap.put("securePort", securePort);
        tomcatInstanceMap.put("protocolSecurePort", protocolSecurePort);
        tomcatInstanceMap.put("keystoreSourceFilename", keystoreSourceFilename);
        tomcatInstanceMap.put("keystoreCredentials", keystoreCredentials);
        tomcatInstanceMap.put("jvmOptionXms", jvmOptionXms);
        tomcatInstanceMap.put("jvmOptionXmx", jvmOptionXmx);
        tomcatInstanceMap.put("jvmOptionXss", jvmOptionXss);
        tomcatInstanceMap.put("jvmOptionXXMaxMetaspaceSize", jvmOptionXXMaxMetaspaceSize);
        tomcatInstanceMap.put("jvmOptions", jvmOptions);
        tomcatInstanceMap.put("instanceManagement", instanceManagement);
        /**
         * Serialize our Instance Management Properties
         */
        List<Map<String,String>> instanceManagementPropertiesMapList =
                new ArrayList<>(instanceManagementProperties.size());
        for(TomcatInstanceProperty tomcatInstanceProperty : instanceManagementProperties) {
            instanceManagementPropertiesMapList.add(tomcatInstanceProperty.map());
        }
        tomcatInstanceMap.put("instanceManagementProperties", instanceManagementPropertiesMapList);
        /**
         * Serialize our Instance Properties
         */
        List<Map<String,String>> instancePropertiesMapList =
                new ArrayList<>(instanceProperties.size());
        for(TomcatInstanceProperty tomcatInstanceProperty : instanceProperties) {
            instancePropertiesMapList.add(tomcatInstanceProperty.map());
        }
        tomcatInstanceMap.put("instanceProperties", instancePropertiesMapList);
        /**
         * Return the Map for Persisting as YAML.
         */
        return tomcatInstanceMap;
    }

    /**
     * Generate a Map representing this Object for Replacement throughout the
     * configuration aspects of the customizations phase.
     *
     * @return Map representing this Object.
     */
    public Map<String, String> configurationReplacementMapPhase1() {
        Map<String, String> tomcatInstanceReplacementMap = new HashMap<>();
        tomcatInstanceReplacementMap.put(TOMCAT_INSTANCE_UUID_TAG, tomcatInstanceUUID);
        tomcatInstanceReplacementMap.put(TOMCAT_INSTANCE_NAME_TAG, instanceName);
        tomcatInstanceReplacementMap.put(TOMCAT_ENVIRONMENT_NAME_TAG, environmentName);
        tomcatInstanceReplacementMap.put(TOMCAT_VERSION_TAG, tomcatVersion);

        tomcatInstanceReplacementMap.put(TOMCAT_PRIMARY_PORT_TAG, primaryPort.toString());
        tomcatInstanceReplacementMap.put(TOMCAT_PRIMARY_PORT_PROTOCOL_TAG, protocolPrimaryPort);
        tomcatInstanceReplacementMap.put(TOMCAT_SHUTDOWN_PORT_TAG, shutdownPort.toString());
        tomcatInstanceReplacementMap.put(TOMCAT_AJP_PORT_TAG, ajpPort.toString());

        tomcatInstanceReplacementMap.put(TOMCAT_SECURE_PORT_TAG, (securePort==null)?"18443":securePort.toString());
        tomcatInstanceReplacementMap.put(TOMCAT_SECURE_PORT_PROTOCOL_TAG, protocolSecurePort);
        tomcatInstanceReplacementMap.put(TOMCAT_KEYSTORE_TAG, keystoreSourceFilename);
        tomcatInstanceReplacementMap.put(TOMCAT_KEYSTORE_CREDENTIALS_TAG, keystoreCredentials);
        
        /**
         * Return the Map for Replacement Phase.
         */
        return tomcatInstanceReplacementMap;
    }

    public TomcatArchive getTomcatArchive() {
        return tomcatArchive;
    }

    public void setTomcatArchive(TomcatArchive tomcatArchive) {
        this.tomcatArchive = tomcatArchive;
    }
}
