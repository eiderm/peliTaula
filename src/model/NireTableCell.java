/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.SartuListan;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;

/**
 *
 * @author Casa
 */
public class NireTableCell extends TableCell<Pelikula, String>
{

    private ComboBox<String> comboBox;

    public NireTableCell()
    {
    }

    @Override
    public void startEdit()
    {
        if (!isEmpty()) {
            super.startEdit();
            createComboBox();
            setText(null);
            setGraphic(comboBox);
        }
    }

    @Override
    public void cancelEdit()
    {
        super.cancelEdit();

        setText(getEgoera());
        setGraphic(null);
    }

    @Override
    public void updateItem(String item, boolean empty)
    {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (comboBox != null) {
                    comboBox.setValue(getEgoera());
                }
                setText(getEgoera());
                setGraphic(comboBox);
            } else {
                setText(getEgoera());
                setGraphic(null);
            }
        }
    }

    private void createComboBox()
    {
        comboBox = new ComboBox<>(FXCollections.observableArrayList(SartuListan.cargarDatosEgoera()));
        comboBoxConverter(comboBox);
        comboBox.valueProperty().set(getEgoera());
        comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        comboBox.setOnAction((e) -> {
            System.out.println("Committed: " + comboBox.getSelectionModel().getSelectedItem());
            commitEdit(comboBox.getSelectionModel().getSelectedItem());
        });
    }

    private void comboBoxConverter(ComboBox<String> comboBox)
    {
        // Define rendering of the list of values in ComboBox drop down. 
        comboBox.setCellFactory((c) -> {
            return new ListCell<String>()
            {
                @Override
                protected void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };
        });
    }

    private String getEgoera()
    {
        return getItem() == null ? "" : getItem();
    }
}
