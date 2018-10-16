/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import model.Pelikula;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import view.MainWindow;

/**
 *
 * @author aitor
 */
public class SartuListan {

    public static ArrayList<Pelikula> cargarDatosPeli(File pelifile) throws ParserConfigurationException, SAXException
    {

        try {            
            
            ArrayList<Pelikula> arrPeli= new ArrayList();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(pelifile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("pelikula");
            for(int temp = 0; temp < nList.getLength(); temp++)
            {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element element = (Element) node;
                    String izena = element.getElementsByTagName("izena").item(0).getTextContent();
                    String direktorea = element.getElementsByTagName("direktorea").item(0).getTextContent();
                    String urtea = element.getElementsByTagName("urtea").item(0).getTextContent();
                    String generoa = element.getElementsByTagName("generoa").item(0).getTextContent();
                    String egoera = element.getElementsByTagName("egoera").item(0).getTextContent();
                    arrPeli.add(new Pelikula(izena,direktorea,urtea,generoa,egoera));
                }
            }
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

    public static ArrayList<String> cargarDatosEgoera() throws ParserConfigurationException, SAXException
    {

        try {
            File egofile = new File("Egoera.xml");
            ArrayList<String> arrEgoera= new ArrayList();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(egofile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("egoera");
            for(int temp = 0; temp < nList.getLength(); temp++)
            {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    arrEgoera.add(node.getTextContent());
                }
            }
            return arrEgoera;
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
    
    
    public static void guardar(TableView<Pelikula> table, File pelifile) throws TransformerConfigurationException, TransformerException{
        
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            // definimos el elemento raíz del documento
            
            Element eRaiz = doc.createElement("pelikulak");
            doc.appendChild(eRaiz);
            for(int i = 0; i < table.getItems().size(); i++){           
                // definimos el nodo que contendrá los elementos
                Element ePeli = doc.createElement("pelikula");
                eRaiz.appendChild(ePeli);

                // definimos cada uno de los elementos y le asignamos un valor
                Element eNombre = doc.createElement("izena");
                eNombre.appendChild(doc.createTextNode(table.getItems().get(i).getIzena()));
                ePeli.appendChild(eNombre);

                Element eModelo = doc.createElement("direktorea");
                eModelo.appendChild(doc.createTextNode(table.getItems().get(i).getDirek()));
                ePeli.appendChild(eModelo);

                Element eEspecie = doc.createElement("urtea");
                eEspecie.appendChild(doc.createTextNode(table.getItems().get(i).getUrte()));
                ePeli.appendChild(eEspecie);

                Element eElemento = doc.createElement("generoa");
                eElemento.appendChild(doc.createTextNode(table.getItems().get(i).getGenero()));
                ePeli.appendChild(eElemento);

                Element eDebilidad = doc.createElement("egoera");
                eDebilidad.appendChild(doc.createTextNode(table.getItems().get(i).getEgoera()));
                ePeli.appendChild(eDebilidad);
            }
            // clases necesarias finalizar la creación del archivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //txukunagoa gorde
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(pelifile);

            transformer.transform(source, result);
            
            }
        catch(Exception e) {
            e.printStackTrace();
          }  
    
    }
}
