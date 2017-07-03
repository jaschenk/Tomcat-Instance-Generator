package jeffaschenk.tomcat.instance.generator.model;

/**
 * TomcatArchive
 *
 * Created by jeffaschenk@gmail.com on 2/22/2017.
 */
public class TomcatArchive {

    private final String name;

    private final String shortName;

    private final Long size;

    private final String checkSum;

    private final boolean available;

    /**
     * TomcatArchive
     * Defines a Tomcat Archive.
     *
     * @param name Name of Archive, example: 'apache-tomcat-8.5.16'
     * @param shortName Short Name of Archive, example: 'v8.5'
     * @param size size of Archive
     * @param checkSum Checksum of archive as created with the internal tool @see jeffaschenk.tomcat.util.FileCheckSumUtility
     * @param available Boolean indicating if Archive is available or not.
     */
    public TomcatArchive(String name, String shortName, Long size, String checkSum, boolean available) {
        this.name = name;
        this.shortName = shortName;
        this.size = size;
        this.checkSum = checkSum;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public Long getSize() {
        return size;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getShortVersion() {
         if (this.name.lastIndexOf('-')+1 <= this.name.length()) {
             return this.name.substring(this.name.lastIndexOf('-') + 1);
         } else {
             return null;
         }
    }
}
