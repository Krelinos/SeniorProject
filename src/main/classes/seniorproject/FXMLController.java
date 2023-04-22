package seniorproject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
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
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
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
        for( File f : fileChooser.showOpenMultipleDialog(null) )
            addWaveform( f );
        
        //File filePath = selectedFile.getAbsolutePath();
        
    }
    
    @FXML
    void openDialog_exportCSV( ActionEvent e ) throws IOException
    {
        
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV FILES (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);
        System.out.print(file);

        FileWriter writer = new FileWriter(file);

        writer.write("Time(ps)" + ","+ "Current(A)" + "\n");
        
        for( Waveform w : waveforms )
            for( Data data : w.waveformXYChart.getData() )
                    writer.write( String.format("%f,%f\n", (Double)data.getXValue(), (Double)data.getYValue()) );
                    
                //writer.write(Arrays.toString(ychords[i]) + "\n");
                
        writer.close();
        
    }
    
    @FXML
    void openDialog_fontsAndColors( ActionEvent e ){
        try
        {
            Pane bruh = FXMLLoader.load(getClass().getResource("resources/settingsDialogFontsAndColors.fxml"));
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
    
    public void initialize()
    {
        ControllerMediator.getInstance().registerFXMLController(this);
        
        waveforms = new ArrayList<>();

        NumberAxis h = (NumberAxis)waveformChart.getYAxis();
        h.setUpperBound( 2.5 );
        h.setLowerBound( -5.0 );
        
        NumberAxis g = (NumberAxis)waveformChart.getXAxis();
        g.setUpperBound( 4000 );
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
            
            // Sets all points to black. Eventually, TODO: I want to add an option for the user to select a default color
            for( Data d : wave.waveformXYChart.getData() )
                    d.getNode().setStyle( String.format("-fx-background-color: %s;", "#000000") );
            refreshWaveformGraph();
            
            // Removes a waveform series and its toolbar from the graph
            Button closeButton = (Button)t.getItems().get(0);       // "Why index 0?" you ask. If you look at WaveformLayer.fxml,
                                                                    // The closeButton is the first <Button> item in the <Toolbar><Items>
            closeButton.setOnAction( (ActionEvent e1) ->
            {
                waveforms.remove( wave );
                waveformLayers.getChildren().remove( t );
                waveformChart.getData().remove( wave.waveformXYChart );
            } );
            
            // Moves the waveform series and its toolbar up one layer
            Button upButton = (Button)t.getItems().get(2);
            upButton.setOnAction( (ActionEvent el) ->
            {
                if( waveforms.get(0) == wave )                      // If this waveform is at the front of the array list, it is already on top of all layers.
                    return;
                
                int currentIndex = waveforms.indexOf( wave );
                swapWaveformLayers( currentIndex, currentIndex-1 );
                
            } );
            
            // Moves the waveform series and its toolbar down one layer
            Button downButton = (Button)t.getItems().get(3);
            downButton.setOnAction( (ActionEvent el) ->
            {
                if( waveforms.get( waveforms.size()-1 ) == wave )  // If this waveform is at the end of the array list, it is already on bottom of all layers.
                    return;
                
                int currentIndex = waveforms.indexOf( wave );
                swapWaveformLayers( currentIndex, currentIndex+1 );
                
            } );
            
            // Changes the plot points of the waveform series.
            ColorPicker colorPicker = (ColorPicker)t.getItems().get(4);
            colorPicker.setOnAction( (ActionEvent el) ->
            {
                for( Data d : wave.waveformXYChart.getData() )
                    d.getNode().setStyle( String.format("-fx-background-color: %s;", formatColorIntoStyle( colorPicker.getValue() )) );
            } );
            
        }
        catch( SAXException | IOException ex )
        {
            ex.printStackTrace();
        }
    }
    
    void updateSettings( Map<String, String> settings )
    {
//        System.out.println("Yay!");
        if( settings.containsKey("graphBackgroundColor") )
        {
            waveformChart.lookup( ".chart-plot-background" ).setStyle( String.format("-fx-background-color: %s;", settings.get("graphBackgroundColor")) );
        }
    }
    
    private String formatColorIntoStyle( Color c ) {
        int r = (int) (255 * c.getRed()) ;
        int g = (int) (255 * c.getGreen()) ;
        int b = (int) (255 * c.getBlue()) ;
        
        return String.format("#%02x%02x%02x", r, g, b);
    }
    
    private void swapWaveformLayers( int fromIndex, int toIndex )
    {
        Waveform temp = waveforms.get( fromIndex );
        waveforms.set( fromIndex, waveforms.get( toIndex ) );
        waveforms.set( toIndex, temp );

        refreshWaveformGraph();

        // https://stackoverflow.com/questions/17761415/how-to-change-order-of-children-in-javafx
        ObservableList<Node> workingCollection = FXCollections.observableArrayList(waveformLayers.getChildren());
        Collections.swap( workingCollection, fromIndex, toIndex );
        waveformLayers.getChildren().setAll( workingCollection );
    }
    
    private void refreshWaveformGraph()
    {
        // Couldn't find any other way to rearrange order of series except clearing and adding everything back in.
        waveformChart.getData().clear();
        for( int i = waveforms.size()-1; i >= 0; i-- )
            waveformChart.getData().add( waveforms.get(i).waveformXYChart );
    }
}
