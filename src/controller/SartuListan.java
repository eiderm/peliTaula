/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import model.Pelikula;

/**
 *
 * @author aitor
 */
public class SartuListan
{

    static FileReader frPeli = null;
    static FileReader frEgoera = null;
    static BufferedReader br = null;

    public static ArrayList<Pelikula> cargarDatosPeli()
    {

        try {
            //Strima irekitzen dugu.
            frPeli = new FileReader("Pelikulak.txt");
            br = new BufferedReader(frPeli);
            String aux;
            String[] arrString;
            ArrayList<Pelikula> arrPeli= new ArrayList();
            while ((aux = br.readLine()) != null) {
                if (!"".equals(aux)) {
                    arrString = aux.split(",");
                    //izena-direktorea-generoa-egoera
                    arrPeli.add(new Pelikula(arrString[0], arrString[1], arrString[2], arrString[3], arrString[4]));
                }
            }
            br.close();
            return arrPeli;
        } catch (FileNotFoundException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Artxiboa ez da aurkitu.");
            error.showAndWait();
            System.exit(-1);
            return null;
        } catch (IOException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Errorea egon da irakurketan.");
            error.showAndWait();
            System.exit(-1);
            return null;
        }
    }

    public static ArrayList<String> cargarDatosEgoera()
    {

        try {
            //Strima irekitzen dugu.
            frEgoera = new FileReader("Egoera.txt");
            br = new BufferedReader(frEgoera);
            String strAux;
            String[] arrAux;
            ArrayList<String> arrGenero= new ArrayList();
            while ((strAux = br.readLine()) != null) {
               arrAux= strAux.split(",");
               for(int i = 0; i < arrAux.length; i++){
                   arrGenero.add(arrAux[i]);
               }
            }
            br.close();
            return arrGenero;
        } catch (FileNotFoundException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Artxiboa ez da aurkitu.");
            error.showAndWait();
            return null;
        } catch (IOException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Errorea egon da irakurketan.");
            error.showAndWait();
            return null;
        }
    }
}
