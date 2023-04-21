package seniorproject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.xml.sax.SAXException;


public class FXMLController {
    
//    private Map<String, Waveform> waveforms;
    private List<Waveform> waveforms;
    
    @FXML
    private VBox waveformLayers;
    
    @FXML
    private ScatterChart waveformChart;
    
    @FXML
    private MenuItem settings_fontsAndColors;
    
    @FXML
    private MenuItem importXMLButton;
    
    @FXML
    void openDialog_importXML( ActionEvent e ) throws IOException
    {
        
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML FILES (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File selectedFile = fileChooser.showOpenDialog(null);
        
        //File filePath = selectedFile.getAbsolutePath();
        
        addWaveform(selectedFile);

    }
    
    @FXML
    void openDialog_fontsAndColors( ActionEvent e ){
        System.out.println("uwu");
        try
        {
            Pane bruh = FXMLLoader.load(getClass().getResource("resources/settings_FontsAndColorsDialog.fxml"));
            Scene settingsDialog = new Scene( bruh );
            Stage s = new Stage( StageStyle.UTILITY );
            s.setScene( settingsDialog );
            
            s.setResizable(false);
            s.setTitle("Fonts & Colors");
            s.initModality(Modality.APPLICATION_MODAL);
            s.show();
        }
        catch( IOException eIO )
        {
            eIO.printStackTrace();
        }
        
    }
    
    private DoubleProperty zoom = new SimpleDoubleProperty( 1.0 );
    
    @FXML
    void waveformChart_OnScroll( ScrollEvent se )
    {
        if( se.getDeltaY() == 0 )
            return;
        
        double ZOOM_DELTA = MainApp.ZOOM_DELTA;
        
        double scaleFactor = ( se.getDeltaY() > 0 ) ? ZOOM_DELTA : 1/ZOOM_DELTA;
        zoom.setValue( zoom.getValue() * scaleFactor );
        System.out.println( zoom.toString() );
//        waveformChart.setScaleX( waveformChart.getScaleX() * scaleFactor );
        
    }
    
    public void initialize() {
//        String javaVersion = System.getProperty("java.version");
//        String javafxVersion = System.getProperty("javafx.version");
//        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
        
//        waveformChart.scaleXProperty().bind( zoom );
//        waveformChart.scaleYProperty().bind( zoom );
        
        waveforms = new ArrayList<>();

        NumberAxis h = (NumberAxis)waveformChart.getYAxis();
        h.setUpperBound( 2.5 );
        h.setLowerBound( -5.0 );
        
        NumberAxis g = (NumberAxis)waveformChart.getXAxis();
        g.setUpperBound( 0.000000004 );
        g.setLowerBound( 0 );
        
        
    }    
    
    private void addWaveform( File file )
    {
        try
        {
            Waveform wave = Waveform.readWaveformXML( file );
            
            waveformChart.getData().add( wave.waveformXYChart );
            ToolBar t = FXMLLoader.load(getClass().getResource("resources/WaveformLayer.fxml"));
            t.setId( wave.ID );
            
            waveforms.add( wave );
            waveformLayers.getChildren().add( t );
            
            Button closeButton = (Button)t.getItems().get(0);       // "Why index 0?" you ask. If you look at WaveformLayer.fxml,
                                                                    // The closeButton is the first <Button> item in the <Toolbar><Items>
            closeButton.setOnAction( (ActionEvent e1) ->
            {
                System.out.println("Bruh");
                waveforms.remove( wave );
                waveformLayers.getChildren().remove( t );
                waveformChart.getData().remove( wave.waveformXYChart );
            } );
            
            Button upButton = (Button)t.getItems().get(2);
            upButton.setOnAction( (ActionEvent el) ->
            {
                if( waveforms.get(0) == wave )  // If this waveform is at the front of the array list, it is already on top of all layers.
                    return;
                
                int desiredIndex = waveforms.indexOf( wave ) - 1;
                
                Waveform waveformAtDesiredIndex = waveforms.get( desiredIndex );
                
                waveforms.set( desiredIndex, wave );
                waveforms.set( desiredIndex+1, waveformAtDesiredIndex );
                
                // Couldn't find any other way to rearrange order of series except clearing and adding everything back in.
                waveformChart.getData().clear();
                for( Waveform w : waveforms )
                    waveformChart.getData().add( w.waveformXYChart );
                
                // https://stackoverflow.com/questions/17761415/how-to-change-order-of-children-in-javafx
                ObservableList<Node> workingCollection = FXCollections.observableArrayList(waveformLayers.getChildren());
                Collections.swap(workingCollection, desiredIndex, desiredIndex+1);
                waveformLayers.getChildren().setAll(workingCollection);
            } );
            
            Button downButton = (Button)t.getItems().get(3);
            downButton.setOnAction( (ActionEvent el) ->
            {
                if( waveforms.get( waveforms.size()-1 ) == wave )  // If this waveform is at the end of the array list, it is already on bottom of all layers.
                    return;
                
                int desiredIndex = waveforms.indexOf( wave ) + 1;
                
                Waveform waveformAtDesiredIndex = waveforms.get( desiredIndex );
                
                waveforms.set( desiredIndex, wave );
                waveforms.set( desiredIndex-1, waveformAtDesiredIndex );
                
                // Couldn't find any other way to rearrange order of series except clearing and adding everything back in.
                waveformChart.getData().clear();
                for( Waveform w : waveforms )
                    waveformChart.getData().add( w.waveformXYChart );
                
                // https://stackoverflow.com/questions/17761415/how-to-change-order-of-children-in-javafx
                ObservableList<Node> workingCollection = FXCollections.observableArrayList(waveformLayers.getChildren());
                Collections.swap(workingCollection, desiredIndex, desiredIndex-1);
                waveformLayers.getChildren().setAll(workingCollection);
            } );
            
        }
        catch( SAXException | IOException ex )
        {
            ex.printStackTrace();
        }
    }
}
