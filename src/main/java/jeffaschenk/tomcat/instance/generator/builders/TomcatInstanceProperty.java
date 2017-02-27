package jeffaschenk.tomcat.instance.generator.builders;

import java.util.HashMap;
import java.util.Map;

/**
 * TomcatInstanceProperty
 *
 * Created by jeffaschenk@gmail.com on 2/20/2017.
 */
public class TomcatInstanceProperty {

    private final String propertyName;

    private final String propertyValue;

    public TomcatInstanceProperty(String propertyName, String propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    public TomcatInstanceProperty(Map<String, String> propertyMap) {
        this.propertyName = propertyMap.get("propertyName");
        this.propertyValue = propertyMap.get("propertyValue");
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TomcatInstanceProperty)) return false;

        TomcatInstanceProperty that = (TomcatInstanceProperty) o;

        return getPropertyName() != null ? getPropertyName().equals(that.getPropertyName()) : that.getPropertyName() == null;
    }

    @Override
    public int hashCode() {
        return getPropertyName() != null ? getPropertyName().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TomcatInstanceProperty{" +
                "propertyName='" + propertyName + '\'' +
                ", propertyValue='" + propertyValue + '\'' +
                '}';
    }

    /**
     * Generate a Property Map for YAML Support.
     * @return Map containing Property Name and Value.
     */
    public Map<String,String> map() {
        Map<String, String> propertyMap = new HashMap<>(2);
        propertyMap.put("propertyName", propertyName);
        propertyMap.put("propertyValue", propertyValue);
        return propertyMap;
    }
}
