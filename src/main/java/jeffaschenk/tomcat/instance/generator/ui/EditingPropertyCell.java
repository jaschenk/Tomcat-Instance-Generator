package jeffaschenk.tomcat.instance.generator.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

/**
 * EditingPropertyCell
 * 
 * Created by jeffaschenk@gmail.com on 2/19/2017.
 */
public class EditingPropertyCell extends TableCell<InstancePropertyRow, String> {

    private TextArea textArea;

    public EditingPropertyCell() {
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(textArea);
            textArea.selectAll();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText((String) getItem());
        setGraphic(null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textArea != null) {
                    textArea.setText(getString());
                }
                setText(null);
                setGraphic(textArea);
            } else {
                setText(getString());
                setGraphic(null);
                /**
                 * Determine the Textual Color for this Editable Property Cell ...
                 */
                setTextFillColor(getId());
            }
        }
    }

    private void createTextField() {
        textArea = new TextArea(getString());
        textArea.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
        textArea.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0,
                                Boolean arg1, Boolean arg2) {
                if (!arg2) {
                    commitEdit(textArea.getText());
                }
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }

    private void setTextFillColor(String columnId) {
        if (columnId == null || columnId.isEmpty()) {
            setTextFill(Color.CORNFLOWERBLUE);
            return;
        }
        /**
         * Set the Valid Based upon the Type of Property Cell being Edited.
         */
         switch(columnId) {
             case("instanceManagementNameColumn") :
                 setTextFill(Color.DARKGREEN);
                 break;
             case("instanceManagementValueColumn") :
                 setTextFill(Color.BLUEVIOLET);
                 break;
             case("instancePropertyNameColumn") :
                 setTextFill(Color.DARKGREEN);
                 break;
             case("instancePropertyValueColumn") :
                 setTextFill(Color.INDIGO);
                 break;
             case("instanceJVMOptionsNameColumn") :
                 setTextFill(Color.DARKMAGENTA);
                 break;
             default :
                 setTextFill(Color.CORNFLOWERBLUE);
         }
    }

}
