package jeffaschenk.tomcat.instance.generator.model;

import java.util.HashMap;
import java.util.Map;

/**
 * TomcatAvailableArchives
 * 
 * Created by jeffaschenk@gmail.com on 7/3/2017.
 */
public class TomcatAvailableArchives {

    /**
     * Apache Tomcat Mirror Head URL to be used to
     * pull an existing Archive from Distribution site when
     * local copy not available.
      */
    private String apacheMirrorHeadUrl;

    /**
     * Apache Tomcat Versions Available from the above Site.
     */
    private Map<String, TomcatArchive> archives = new HashMap<>();

    /**
     * Default Constructor
     */
    public TomcatAvailableArchives() {
    }

    /**
     * Default Constructor to load Object from a Yaml composed Map.
     * @param mapFromYaml Map from Parsed YAML data
     */
    public TomcatAvailableArchives(Map<String, Object> mapFromYaml) {
         if (mapFromYaml != null && !mapFromYaml.isEmpty()) {
              for(String name : mapFromYaml.keySet()) {
                  /**
                   * Process the Mirror Head Url.
                   */
                  if (name.equalsIgnoreCase("apacheMirrorHeadUrl")) {
                      this.apacheMirrorHeadUrl = (String)mapFromYaml.get(name);
                      continue;
                  }
                  /**
                   * Process any Tomcat Version Specifications...
                   */
                  if (!name.toLowerCase().startsWith("tomcatversion_")) {
                      // for now Ignore anything else that is not a tomcatVersion element ...
                      continue;
                  }
                  String[] valuesParsed = ((String)mapFromYaml.get(name)).split(" ");
                  if (valuesParsed.length != 5) {
                      // Skip this element, as we should have at least 4 Values for each element.
                      // ------Name----------  --0----------------- --1---- --2- --3---  --4-------------------------------------
                      // tomcatVersion_8.5.16: apache-tomcat-8.5.16 v8.5    true 9992463 13bf717a94a7b8d5296e678a70004a65f0c0409f
                      continue;
                  }
                  /**
                   * Instantiate the Archive POJO and place in our Archives...
                   */
                  TomcatArchive tomcatArchive = new TomcatArchive(valuesParsed[0], valuesParsed[1],
                          Long.parseLong(valuesParsed[3]), valuesParsed[4], Boolean.parseBoolean(valuesParsed[2]));
                  this.getArchives().put(tomcatArchive.getName(), tomcatArchive);
              }
         }
    }

    /**
     * Constructor with all available parameters
     * @param apacheMirrorHeadUrl Head URL to Pull Archives from...
     * @param archives Archives
     */
    public TomcatAvailableArchives(String apacheMirrorHeadUrl, Map<String, TomcatArchive> archives) {
        this.apacheMirrorHeadUrl = apacheMirrorHeadUrl;
        this.archives = archives;
    }

    public String getApacheMirrorHeadUrl() {
        return apacheMirrorHeadUrl;
    }

    public void setApacheMirrorHeadUrl(String apacheMirrorHeadUrl) {
        this.apacheMirrorHeadUrl = apacheMirrorHeadUrl;
    }

    public Map<String, TomcatArchive> getArchives() {
        return archives;
    }

    public void setArchives(Map<String, TomcatArchive> archives) {
        this.archives = archives;
    }

    /**
     * Obtain an Available Archive by Short Name.
     *
     * Archive must be available.
     *
     * Only have one available version available at a time, or you will not get
     * the appropriate version.
     *
     * @param shortName Name to look up.
     * @return TomcatArchive based upon Short Name which is available.
     */
    public TomcatArchive getAvailableArchiveByShortName(String shortName) {
        for(TomcatArchive archive : this.getArchives().values()) {
             if (archive.isAvailable() && archive.getShortName().equalsIgnoreCase(shortName)) {
                  return archive;
             }
        }
        /**
         * No Tomcat Version archive Found to be available.
         */
        return null;
    }
}
