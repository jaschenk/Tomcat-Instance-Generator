package jeffaschenk.tomcat.instance.generator.builders;

/**
 * TomcatArchive
 *
 * Created by jeffaschenk@gmail.com on 2/22/2017.
 */
public class TomcatArchive {

    private final String name;

    private final Long size;

    private final String checkSum;

    public TomcatArchive(String name, Long size, String checkSum) {
        this.name = name;
        this.size = size;
        this.checkSum = checkSum;
    }

    public String getName() {
        return name;
    }

    public Long getSize() {
        return size;
    }

    public String getCheckSum() {
        return checkSum;
    }
}
