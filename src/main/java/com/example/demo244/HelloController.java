package com.example.demo244;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import static com.example.demo244.HelloApplication.*;

public class HelloController {

    XYChart.Series series_wave1_plot = new XYChart.Series();
    XYChart.Series series_wave1_plot2 = new XYChart.Series();
    static int n = 0;
    static int b = 0;
    String[] xchords_to_string = new String[xchords.length];
    @FXML
    private LineChart<?, ?> chart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    public void AddWave1(ActionEvent e) {
        try {
            //Open dialog box for user to select a file then put that path into variable selectedFile
            FileChooser fc = new FileChooser();
            File selectedFile = fc.showOpenDialog(null);
            //Use the selected file and call printNodeList method in the main app to read through the xml
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(selectedFile);
            if (document.hasChildNodes()) {
                printNodeList(document.getChildNodes());
            }
        } catch (Exception b) {}


        //Convert xchords double array to a string array called xchords_to_string
        for (int i = 0; i < xchords_to_string.length; i++)
            xchords_to_string[i] = String.valueOf(xchords[i]);

        //omit x values that are negative
        for (int i = 1; i < xchords.length; i++) {
            if (xchords[i] <=0 )//if its negative skip it
            {
                i++;//continue to next iteration
            }

            else
                //add the xchord_to_string value and the corresponding ychord to the series AKA line plot
                series_wave1_plot.getData().add(new XYChart.Data(xchords_to_string[i], ychords[i]));

        }


        series_wave1_plot2.getData().add(new XYChart.Data("4", 1));
        series_wave1_plot2.getData().add(new XYChart.Data("6", 2));

        series_wave1_plot2.setName("Wave 2");

        series_wave1_plot.setName("Wave 1");
        chart.setCreateSymbols(false); //hide dots
        chart.getData().add(series_wave1_plot);
        chart.getData().add(series_wave1_plot2);
        //get_ip1();
        //get_ip2();
    }

    public void clearchart(ActionEvent e) {

        //////not working correctly
        // //////when you clear and add another wave it adds the previous too even though you clear it
        chart.getData().clear();
        //chart.getData().remove(series_wave1_plot2);
        //series_wave1_plot2.getData().removeAll(chart);
        //series_wave1_plot.getData().removeAll(Collections.singleton(chart.getData().setAll()));
        //chart.setAnimated(false);
        //chart.setCreateSymbols(true);



    }

    public void Export(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV FILES (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);
        System.out.print(file);
    }


}