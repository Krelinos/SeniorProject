package seniorproject;

import java.io.File;
import java.io.IOException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
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
    
    @FXML
    private Label label;
    
    @FXML
    private VBox waveformLayers;
    
    @FXML
    private ScatterChart waveformChart;
    
    @FXML
    private MenuItem settings_fontsAndColors;
    
    @FXML
    private MenuItem importXMLButton;
    
    @FXML
    void openDialog_importXML( ActionEvent e) throws IOException
    {
        try
        {
            Waveform wave = Waveform.readWaveformXML( new File("D:\\waveforms\\JS-002 (6.8pF)_20221003110909.result.xml") );
            
            System.out.println("Waveform has " + wave.waveformData.size() + " elements");
            
            XYChart.Series<Double, Double> plotThePoints = new XYChart.Series<>();
            plotThePoints.setName("This is a waveform");
            
            for( Point2D p : wave.waveformData )
                plotThePoints.getData().add( new XYChart.Data<>(p.getX(), p.getY()) );
        
            waveformChart.getData().add( plotThePoints );
        }
        catch( SAXException ex )
        {
            ex.printStackTrace();
        }
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
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        //label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
        
//        waveformChart.scaleXProperty().bind( zoom );
//        waveformChart.scaleYProperty().bind( zoom );
        
        NumberAxis h = (NumberAxis)waveformChart.getYAxis();
        h.setUpperBound( 2.5 );
        h.setLowerBound( -5.0 );
        
        NumberAxis g = (NumberAxis)waveformChart.getXAxis();
        g.setUpperBound( 0.000000004 );
        g.setLowerBound( 0 );
        
        try
        {
            ToolBar t = FXMLLoader.load(getClass().getResource("resources/WaveformLayer.fxml"));
            waveformLayers.getChildren().add( t );
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
        
    }    
    
}
