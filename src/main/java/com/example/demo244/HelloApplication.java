package com.example.demo244;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;

import static com.example.demo244.HelloController.*;

public class HelloApplication extends Application {
    static double[] xchords = new double[10000];
    static double[] ychords = new double[10000];

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 750);
        stage.setTitle("Wave Form Reader");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }



    public static void printNodeList(NodeList nodeList) {



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

                            //System.out.println("attr name : " + node.getNodeName());
                            // System.out.println("attr value x  : " + node.getNodeValue());
                            //PUT THE VALUE INTO THE ARRAY as a double from string
                            xchords[b] = Double.parseDouble(node.getNodeValue());
                            b++;

                        }
                        /////////////////////////////////////////CHECK FOR TAG Y
                        if (node.getNodeName() == "y") {

                            //System.out.println("attr name : " + node.getNodeName());
                            //System.out.println("attr value y  : " + node.getNodeValue());
                            //PUT THE VALUE INTO THE ARRAY as a double from string
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

    }//end printNodeList

    //method to get the lowest element from the array ychords (IP2)
    public static void get_ip2(){
        double min = ychords[0];
        for (int i = 0; i < ychords.length; i++) {
            //Compare elements of array with min
            if(ychords[i] <min)
                min = ychords[i];
        }

System.out.println("IP2 is " + min);
    }
//method to get the highest element from the array ychords (IP1)
public static void get_ip1(){

    double temp, size;
    size = ychords.length;

    for(int i = 0; i<size; i++ ){
        for(int j = i+1; j<size; j++){
            if(ychords[i]>ychords[j]){
                temp = ychords[i];
                ychords[i] = ychords[j];
                ychords[j] = temp;
            }
        }
    }
    System.out.println("Ip1 is "+ychords[(int) (size-1)]);
    }



}



