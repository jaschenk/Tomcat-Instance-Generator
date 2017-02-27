package jeffaschenk.tomcat.instance.generator.ui;

import javafx.beans.property.SimpleStringProperty;

/**
 * InstanceJVMOptionRow
 *
 * Created by jeffaschenk@gmail.com on 2/18/2017.
 */
public class InstanceJVMOptionRow {

    public SimpleStringProperty jvmOption = new SimpleStringProperty();
    
    public String getJvmOption() {
        return jvmOption.get();
    }

    public SimpleStringProperty jvmOptionProperty() {
        return jvmOption;
    }

    public void setJvmOption(String jvmOption) {
        this.jvmOption.set(jvmOption);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstanceJVMOptionRow)) return false;

        InstanceJVMOptionRow that = (InstanceJVMOptionRow) o;

        return getJvmOption() != null ? getJvmOption().equals(that.getJvmOption()) : that.getJvmOption() == null;
    }

    @Override
    public int hashCode() {
        return getJvmOption() != null ? getJvmOption().hashCode() : 0;
    }
}
