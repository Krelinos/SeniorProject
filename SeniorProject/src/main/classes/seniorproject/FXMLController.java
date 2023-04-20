package seniorproject;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
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
    void openDialog_importXML( ActionEvent e)
    {
        try
        {
            Waveform wave = Waveform.readWaveformXML( new File("D:\\waveforms\\JS-002 (6.8pF)_20221003110909.result.xml") );
            XYChart.Series<Double, Double> plotThePoints = new XYChart.Series<>();
            plotThePoints.setName("This is a waveform");
            
            for( Point2D p : wave.waveformData )
                plotThePoints.getData().add( new XYChart.Data<>(p.getX(), p.getY()) );
        
            waveformChart.getData().add( plotThePoints );
            waveformChart.autosize();
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
    
    public void initialize() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        //label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
        
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
