package com.example.linechart2;

import javafx.application.Application;
import javafx.css.Style;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

//My part was to custom made background color and line plot. Bryan Rocha
//The base code was made by Raul
//my imports
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.beans.value.ChangeListener;
import javafx.scene.chart.LineChart;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;

public class JavaFXLineChartExample extends Application {

    public ColorPicker myColorPicker;

    //ARRAYS FOR X AND Y CHORDS
    static double[] xchords = new double[10000];
    static double[] ychords = new double[10000];



    public void start(Stage s) {
        s.setTitle("Current Wave Form");

//x and y axis representation
        NumberAxis x = new NumberAxis();
        x.setLabel("X AXIS");
        NumberAxis y = new NumberAxis();
        y.setLabel("Y AXIS");
//line chart syntax
        LineChart ll = new LineChart(x, y);
        XYChart.Series sr = new XYChart.Series();


        sr.setName("WaveForm 1");



        for (int i = 0; i < 10000; i++) {

            //////////////////SET THE ARRAY INTO THE LINECHART OBJECT THING..iterate through array with loop
            sr.getData().add(new XYChart.Data( xchords[i], ychords[i]));

        }
        ////////////////////HARD CODING TEST VALUES
        //sr.getData().add(new XYChart.Data( 4, 290));
        //sr.getData().add(new XYChart.Data(3, 200));
        ll.getData().add(sr);






        VBox vbox = new VBox(ll);
        Scene sc = new Scene(vbox, 800, 400);
        //This 4 lines creates the color pickers
        final ColorPicker colorPicker = new ColorPicker();
        final ColorPicker colorLine = new ColorPicker();
        colorPicker.setValue(Color.TRANSPARENT);
        colorLine.setValue(Color.ORANGE);

        //This for make a new background and line color
        ll.setBackground(new Background(new BackgroundFill(colorPicker.getValue(),null,null)));
        ChangeListener<Color> listener = (obs, oldColor, newColor) ->
                updateStyles(ll, colorLine.getValue());
        colorLine.valueProperty().addListener(listener);
        vbox.getChildren().addAll(colorPicker);
        vbox.getChildren().addAll(colorLine);

        colorPicker.setOnAction((event) -> {
            vbox.getStylesheets().add("colors.css");
            ll.setBackground(Background.fill(colorPicker.getValue()));


        });


        s.setScene(sc);
        s.setHeight(550);
        s.setWidth(1250);
        s.show();


    }
    private void updateStyles(Chart ll, Color color1) {
        ll.setStyle(String.format("CHART_COLOR_1: %s ; ", format(color1)));
    }
    private String format(Color c) {
        int r = (int) (255 * c.getRed()) ;
        int g = (int) (255 * c.getGreen()) ;
        int b = (int) (255 * c.getBlue()) ;

        return String.format("#%02x%02x%02x", r, g, b);
    }



    public static void main(String[] args) {
        try
        {

            File file = new File("Your file path");
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            //System.out.println("Root element: "+ document.getDocumentElement().getNodeName());
            if (document.hasChildNodes())
            {
                printNodeList(document.getChildNodes());
            }
        }

        catch (Exception b){}
        Application.launch(args);
    }
    public static void printNodeList(NodeList nodeList) {


        int n = 0;
        int a = 0;
        int b = 0;
        /////////////LOOP THROUGH XML FILE AND CREATE AN ARRAY WITH ALL X CHORDS AND ANOTHER ARRAY WITH ALL Y CHORDS

        for (int count = 0; count < nodeList.getLength(); count++) {
            Node elemNode = nodeList.item(count);
            if (elemNode.getNodeType() == Node.ELEMENT_NODE) {

                {
                    NamedNodeMap nodeMap = elemNode.getAttributes();

                    for (int i = 0; i < nodeMap.getLength(); i++) {
                        Node node = nodeMap.item(i);
                        //if the node name is x or y

                        /////////////////////////////////////////CHECK FOR TAG "X"
                        if (node.getNodeName() == "x") {
                            /////////DISPLAY VALUES TO CONSOLE
                            System.out.println("attr name : " + node.getNodeName());
                            System.out.println("attr value x  : " + node.getNodeValue());
                            //PUT THE VALUE INTO THE ARRAY
                            xchords[b] = Double.parseDouble(node.getNodeValue());
                            b++;
                        }
                        /////////////////////////////////////////CHECK FOR TAG Y
                        else if (node.getNodeName() == "y") {
                            /////////DISPLAY VALUES TO CONSOLE
                            System.out.println("attr name : " + node.getNodeName());
                            System.out.println("attr value y  : " + node.getNodeValue());
                            //PUT THE VALUE INTO THE ARRAY
                            ychords[n] = Double.parseDouble(node.getNodeValue());

                            n++;
                        }

                    }

                }

                if (elemNode.hasChildNodes()) {
//recursive call if the node has child nodes
                    printNodeList(elemNode.getChildNodes());
                }
            }
        }
//out of loops

    }
}