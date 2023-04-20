package seniorproject;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

public class FXMLController {
    
    @FXML
    private Label label;
    
    @FXML
    private VBox waveformLayers;
    
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
