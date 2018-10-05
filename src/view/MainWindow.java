/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import controller.SartuListan;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.NireTableCell;
import model.Pelikula;

public class MainWindow extends Application
{

    private final TableView<Pelikula> table = new TableView<>();
    final HBox hb = new HBox();

    @Override
    public void start(Stage stage)
    {
        Scene scene = new Scene(new Group());
        
        stage.setTitle("Datuen Taula");
        stage.setWidth(750);
        stage.setHeight(525);

        final Label label = new Label("Pelikulak");

        label.setFont(new Font("Arial", 20));

        table.setEditable(true);
        Callback<TableColumn<Pelikula, String>, TableCell<Pelikula, String>> comboBoxCellFactory
                = (TableColumn<Pelikula, String> param) -> new NireTableCell();

        TableColumn<Pelikula, String> izenCol = new TableColumn<>("Izena");
        izenCol.setMinWidth(125);
        izenCol.setCellValueFactory(new PropertyValueFactory<>("izena"));
        izenCol.setCellFactory(TextFieldTableCell.<Pelikula>forTableColumn());
        izenCol.setOnEditCommit((TableColumn.CellEditEvent<Pelikula, String> t) -> {
            ((Pelikula) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())).setIzena(t.getNewValue());
        });

        TableColumn<Pelikula, String> direkCol = new TableColumn<>("Direktorea");
        direkCol.setMinWidth(125);
        direkCol.setCellValueFactory(new PropertyValueFactory<>("direk"));
        direkCol.setCellFactory(TextFieldTableCell.<Pelikula>forTableColumn());
        direkCol.setOnEditCommit((TableColumn.CellEditEvent<Pelikula, String> t) -> {
            ((Pelikula) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())).setDirek(t.getNewValue());
        });
        
        TableColumn<Pelikula, String> urteCol = new TableColumn<>("Urtea");
        urteCol.setMinWidth(125);
        urteCol.setCellValueFactory(new PropertyValueFactory<>("urte"));
        urteCol.setCellFactory(TextFieldTableCell.<Pelikula>forTableColumn());
        urteCol.setOnEditCommit((TableColumn.CellEditEvent<Pelikula, String> t) -> {
            ((Pelikula) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())).setUrte(t.getNewValue());
        });

        TableColumn<Pelikula, String> generoCol = new TableColumn<>("Genero");
        generoCol.setMinWidth(125);
        generoCol.setCellValueFactory(new PropertyValueFactory<>("genero"));
        generoCol.setCellFactory(TextFieldTableCell.<Pelikula>forTableColumn());
        generoCol.setOnEditCommit((TableColumn.CellEditEvent<Pelikula, String> t) -> {
            ((Pelikula) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())).setGenero(t.getNewValue());
        });

        TableColumn<Pelikula, String> egoeraCol = new TableColumn<>("Egoera");
        egoeraCol.setMinWidth(125);
        egoeraCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEgoera()));
        egoeraCol.setCellFactory(comboBoxCellFactory);
        egoeraCol.setOnEditCommit(
                (TableColumn.CellEditEvent<Pelikula, String> t) -> {
                    ((Pelikula) t.getTableView().getItems()
                            .get(t.getTablePosition().getRow()))
                            .setEgoera(t.getNewValue());
                });

        table.setItems(FXCollections.observableList(SartuListan.cargarDatosPeli()));
        table.getColumns().addAll(izenCol, direkCol, urteCol, generoCol, egoeraCol);
        final TextField txtIzena = new TextField();        
        txtIzena.setMaxWidth(izenCol.getPrefWidth());
        txtIzena.setPromptText("Izena");
        final TextField txtDirek = new TextField();
        txtDirek.setMaxWidth(direkCol.getPrefWidth());
        txtDirek.setPromptText("Direktorea");
        final TextField txtUrte = new TextField();
        txtUrte.setMaxWidth(urteCol.getPrefWidth());
        txtUrte.setPromptText("Urtea");
        final TextField txtGenero = new TextField();
        txtGenero.setMaxWidth(100);
        txtGenero.setPromptText("Generoa");
        final ComboBox cbxEgoera = new ComboBox(FXCollections.observableList(SartuListan.cargarDatosEgoera()));
        cbxEgoera.setMaxWidth(100);
        cbxEgoera.setPromptText("Egoera");

        final Button addButton = new Button("Gehitu");
        addButton.setOnAction((ActionEvent e) -> {
            if ("".equals(txtIzena.getText()) || txtDirek.getText() == "" || txtUrte.getText() == "" || txtGenero.getText() == "" || cbxEgoera.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ezin da gehitu!");
                alert.setContentText("Ezin da daturik gorde taulan guztiak bete barik.");
                alert.showAndWait();
            } else {
                Pelikula p = new Pelikula(txtIzena.getText(), txtDirek.getText(), txtUrte.getText(), txtGenero.getText(), cbxEgoera.getSelectionModel().getSelectedItem().toString());
                table.getItems().add(p);
                txtIzena.clear();
                txtDirek.clear();
                txtUrte.clear();
                txtGenero.clear();
                cbxEgoera.getSelectionModel().clearSelection();
            }
        });

        final Button removeButton = new Button("Ezabatu");
        removeButton.setOnAction((ActionEvent e) -> {
            Pelikula manga = table.getSelectionModel().getSelectedItem();
            table.getItems().remove(manga);
        });
        
        final Button exitButton = new Button ("Irten");
        exitButton.setOnAction((ActionEvent e) ->
        {
            System.exit(-1);
        });

        hb.getChildren().addAll(txtIzena, txtDirek, txtUrte, txtGenero, cbxEgoera, addButton, removeButton, exitButton);
        hb.setSpacing(3);
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        stage.setScene(scene);
        stage.show();
        
        stage.setOnCloseRequest((WindowEvent event)-> {
            try { 
                PrintWriter pw = new PrintWriter("Pelikulak.txt");
                for(int i = 0; i < table.getItems().size(); i++){
                    pw.println(table.getItems().get(i).getIzena()+","
                            +table.getItems().get(i).getDirek()+","
                            +table.getItems().get(i).getUrte()+","
                            +table.getItems().get(i).getGenero()+","
                            +table.getItems().get(i).getEgoera());
                }
                pw.close();
            } catch (FileNotFoundException ex) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setContentText("Ez da artxiboa aurkitu datuak gordetzeko.");
                error.showAndWait();
            }
            
        });

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

}
