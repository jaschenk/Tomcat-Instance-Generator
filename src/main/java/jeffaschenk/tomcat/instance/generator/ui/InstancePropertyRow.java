package jeffaschenk.tomcat.instance.generator.ui;

import javafx.beans.property.SimpleStringProperty;

/**
 * InstancePropertyRow
 *
 * Created by jeffaschenk@gmail.com on 2/18/2017.
 */
public class InstancePropertyRow {

    public SimpleStringProperty propertyName = new SimpleStringProperty();
    public SimpleStringProperty propertyValue = new SimpleStringProperty();
    
    public String getPropertyName() {
        return propertyName.get();
    }

    public SimpleStringProperty propertyNameProperty() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName.set(propertyName);
    }

    public String getPropertyValue() {
        return propertyValue.get();
    }

    public SimpleStringProperty propertyValueProperty() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue.set(propertyValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstancePropertyRow)) return false;

        InstancePropertyRow that = (InstancePropertyRow) o;

        return getPropertyName() != null ? getPropertyName().equals(that.getPropertyName()) : that.getPropertyName() == null;
    }

    @Override
    public int hashCode() {
        return getPropertyName() != null ? getPropertyName().hashCode() : 0;
    }
}
