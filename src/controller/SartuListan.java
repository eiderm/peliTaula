/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javax.json.*;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
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
 * @author DM3-2-22
 */
public class SartuListan {
    
    static FileReader frPeli = null;
    static FileReader frEgoera = null;
    static BufferedReader br = null;
    
    //kargatu
    public static ObservableList<Pelikula> cargarDatos(File pelifile) throws ParserConfigurationException, SAXException, FileNotFoundException
    {        
        String ext = pelifile.getName().substring(pelifile.getName().length() -4);
        if (ext.equals(".txt")) {
            return cargarDatosTxt(pelifile);
        }
        else if (ext.equals(".xml")) {
            return cargarDatosXml(pelifile);
        }
        else if (ext.equals("json")) {
            long pixua = pelifile.length()/1024;
            if(pixua<5) {
                return cargarDatosJson(pelifile);
            }
            else {
                return cargarDatosStringJson(pelifile);
            }
        }
        return null;
    }    
    
    public static ObservableList<Pelikula> cargarDatosTxt(File pelifile)
    {
        ObservableList<Pelikula> lista = FXCollections.observableArrayList();

        try {
            //Strima irekitzen dugu.
            frPeli = new FileReader(pelifile);
            br = new BufferedReader(frPeli);
            String aux;
            String[] arrString;
            while ((aux = br.readLine()) != null) {
                if (!"".equals(aux)) {
                    arrString = aux.split(",");
                    //izena-direktorea-generoa-egoera
                    Pelikula peli = new Pelikula(arrString[0], arrString[1], arrString[2], arrString[3], arrString[4]);
                    lista.add(peli);
                }
            }
            br.close();
            return lista;
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

    public static ObservableList<Pelikula> cargarDatosXml(File pelifile) throws ParserConfigurationException, SAXException
    {
        ObservableList<Pelikula> lista = FXCollections.observableArrayList();
        
        try {        
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(pelifile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("pelikula");
            for(int temp = 0; temp < nList.getLength(); temp++)
            {
                Node nNode = nList.item(temp);
                Element eElement = (Element) nNode;
                Pelikula peli = new Pelikula(eElement.getElementsByTagName("izena").item(0).getTextContent(),
                    eElement.getElementsByTagName("direktorea").item(0).getTextContent(),
                    eElement.getElementsByTagName("urtea").item(0).getTextContent(),
                    eElement.getElementsByTagName("generoa").item(0).getTextContent(),
                    eElement.getElementsByTagName("egoera").item(0).getTextContent());
                lista.add(peli);
                
            }
            return lista;
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

    public static ArrayList<String> cargarDatosEgoeraXml() throws ParserConfigurationException, SAXException
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
    
    private static ObservableList<Pelikula> cargarDatosJson(File pelifile) {
        ObservableList<Pelikula> lista = FXCollections.observableArrayList();
        String a, b, c, d, e;
        
        try {
            JsonReader reader = Json.createReader(new FileInputStream(pelifile));
            JsonArray pelikula = reader.readArray();
            reader.close();

            for (int i = 0; i < pelikula.size(); i++) {
                JsonObject n = pelikula.getJsonObject(i);
                a = n.getString("izena");
                b = n.getString("direktorea");
                c = n.getString("urtea");
                d = n.getString("generoa");
                e = n.getString("egoera");
                lista.add(new Pelikula(a,b,c,d,e));
            }
        } catch (FileNotFoundException ez) {
            
        }
        return lista;       
    }
    
    private static ObservableList<Pelikula> cargarDatosStringJson(File pelifile) throws FileNotFoundException {
        JsonParser parser = Json.createParser(new FileInputStream(pelifile));
        ObservableList<Pelikula> lista = FXCollections.observableArrayList();
        ArrayList<String> identifier = new ArrayList<String>();
        boolean a = true;
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            switch (event) {
                case START_ARRAY:
                case END_ARRAY:
                case START_OBJECT:
                case END_OBJECT:
                case VALUE_FALSE:
                case VALUE_NULL:
                case VALUE_TRUE:
                    if (event.toString().equals("END_OBJECT")) {
                        Pelikula peli = new Pelikula(identifier.get(0), identifier.get(1), identifier.get(2), identifier.get(3), identifier.get(5));
                        lista.add(peli);
                    } else if (event.toString().equals("START_OBJECT")) {
                        identifier.clear();
                    }
                    break;
                case KEY_NAME:
                    break;
                case VALUE_STRING:
                case VALUE_NUMBER:
                    identifier.add(parser.getString());
                    break;
            }
        }
        return lista;
    }
    
    //gorde
    public static void guardarDatos(TableView<Pelikula> table, File pelifile) throws ParserConfigurationException, SAXException, TransformerException, FileNotFoundException, IOException
    {
        String ext = pelifile.getName().substring(pelifile.getName().length() -4);
        if (ext.equals(".txt")) {
            guardarTxt(table, pelifile);
        }
        else if(ext.equals(".xml")) {
            guardarXml(table, pelifile);
        }
        else if (ext.equals("json")) {
            long pixua = pelifile.length()/1024;
            if(pixua<5) {
                guardarJson(table, pelifile);
            }
            else {
                guardarStringJson((ObservableList<Pelikula>) table, pelifile);
            }
            guardarJson(table, pelifile);
        }
    }
    
    public static void guardarXml(TableView<Pelikula> table, File pelifile) throws TransformerConfigurationException, TransformerException{
        
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
    
    public static void guardarTxt(TableView<Pelikula> table, File pelifile) throws FileNotFoundException
    {
        PrintWriter pw = new PrintWriter(pelifile);
        for(int i = 0; i < table.getItems().size(); i++){
            pw.println(table.getItems().get(i).getIzena()+","
                    +table.getItems().get(i).getDirek()+","
                    +table.getItems().get(i).getUrte()+","
                    +table.getItems().get(i).getGenero()+","
                    +table.getItems().get(i).getEgoera());
        }
        pw.close();
    }    

    private static void guardarJson(TableView<Pelikula> table, File pelifile) throws IOException {
        JsonArrayBuilder aBuilder = Json.createArrayBuilder();
        
        try {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            for(int i= 0; i < table.getItems().size(); i++) 
            {
                objectBuilder.add("izena", table.getItems().get(i).getIzena());
                objectBuilder.add("direktorea", table.getItems().get(i).getDirek());
                objectBuilder.add("urtea", table.getItems().get(i).getUrte());
                objectBuilder.add("generoa", table.getItems().get(i).getGenero());
                objectBuilder.add("egoera", table.getItems().get(i).getEgoera());
                JsonObject jsonObject = objectBuilder.build();
                aBuilder.add(jsonObject);
            }
            JsonArray jsonObjects = aBuilder.build();
            JsonWriter writer = Json.createWriter(new FileOutputStream(pelifile));
            writer.writeArray(jsonObjects);
            writer.close();
        }
        catch (Exception ex){
            
        }                
    }

    private static void guardarStringJson(ObservableList<Pelikula> lista, File pelifile) {
        FileWriter writer;
        try {
            JsonGenerator gen = Json.createGenerator(new FileWriter(pelifile));
            gen.writeStartArray();
            for (Pelikula i : lista) {
                gen.writeStartObject()
                        .write("Name", i.getIzena())
                        .write("Description", i.getDirek())
                        .write("Color", i.getUrte())
                        .write("Size", i.getGenero())
                        .write("Flowers", i.getEgoera())
                        .writeEnd();
            }
            gen.writeEnd();
            gen.close();
        } catch (IOException ex) {
        }
    }
    
}
