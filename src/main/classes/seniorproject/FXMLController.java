package seniorproject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import org.xml.sax.SAXException;


public class FXMLController {
    
//    private Map<String, Waveform> waveforms;
    private List<Waveform> waveforms;
    private Waveform primaryWaveform;       // The waveform whose significant points and metadata are shown on the graph and table.
                                            // Selected by the user when clicking on the waveform toolbar's name text field.
    private Map<String, Boolean> waveformVisibility;
    
    static Map<Integer, String> metadataHumanReadable = new HashMap<>();    //Ex: Run Date
    static Map<Integer, String> metadataValue = new HashMap<>();            //Ex: runDate@result
    
    @FXML
    private VBox ApplicationMain;
    
    @FXML
    private VBox waveformLayers;
    
    @FXML
    private ScatterChart waveformChart;
    
    @FXML
    private MenuItem settings_fontsAndColors;
    
    @FXML
    private MenuItem importXMLButton;
    
    @FXML
    private Label labelWaveformName;
    
    @FXML
    private Label labelIP1;
    
    @FXML
    private Label labelIP2;
    
    @FXML
    private Label labelFWHM1;
    
    @FXML
    private Label labelFWHM2;
    
    @FXML
    private Label labelTr10;
    
    @FXML
    private Label labelTr90;
    
    @FXML
    private AnchorPane anchorPaneScatterAndInfo;
    
    @FXML
    private Rectangle waveformPointsBG;
    
    @FXML
    private TableView metadataTable;
    
    @FXML
    private TableColumn metadataTableProperty;
    
    @FXML
    private TableColumn metadataTableValue;
    
    @FXML
    void openDialog_importXML( ActionEvent e ) throws IOException
    {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML FILES (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        
        
        fileChooser.setInitialDirectory( new File( (String)SettingsController.settings.get("defaultImportWaveformDirectory") ) );
        
        List<File> files = fileChooser.showOpenMultipleDialog( ApplicationMain.getScene().getWindow() );
        if( files == null )
            return;
        //Show save file dialog
        for( File f : files )
            addWaveform( f );
        
        //File filePath = selectedFile.getAbsolutePath();
        
    }
    
    // exporting to an image in either jpeg or png format
    @FXML
    void toImage(ActionEvent e) throws IOException
    {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory( new File( (String)SettingsController.settings.get("defaultSaveImageDirectory") ) );
        
        // image format filters
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPEG (*.jpg,*.jpeg,*.jpe,*.jiff)", "*.jpg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG (*.png)", "*.png");

        // adding our filters
        fileChooser.getExtensionFilters().add(jpgFilter);
//        fileChooser.getExtensionFilters().add(pngFilter);

        // showing the save pop up
        File file = fileChooser.showSaveDialog( ApplicationMain.getScene().getWindow() );
        
        WritableImage image = waveformChart.snapshot(new SnapshotParameters(), null);
        if( file != null )
            ImageIO.write( SwingFXUtils.fromFXImage( image, null ), "png", file );
    }
    
    @FXML
    void toImageWithPoints(ActionEvent e) throws IOException
    {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory( new File( (String)SettingsController.settings.get("defaultSaveImageDirectory") ) );
        
        // image format filters
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPEG (*.jpg,*.jpeg,*.jpe,*.jiff)", "*.jpg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG (*.png)", "*.png");

        // adding our filters
        fileChooser.getExtensionFilters().add(jpgFilter);
//        fileChooser.getExtensionFilters().add(pngFilter);

        // showing the save pop up
        File file = fileChooser.showSaveDialog( ApplicationMain.getScene().getWindow() );
        
        WritableImage image = anchorPaneScatterAndInfo.snapshot(new SnapshotParameters(), null);
        if( file != null )
            ImageIO.write( SwingFXUtils.fromFXImage( image, null ), "png", file );
    }
    
    @FXML
    void openDialog_exportCSV( ActionEvent e ) throws IOException
    {
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory( new File( (String)SettingsController.settings.get("defaultSaveCSVDirectory") ) );
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV FILES (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog( ApplicationMain.getScene().getWindow() );
//        System.out.print(file);
        if( file == null )
            return;

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
    
    @FXML
    void openDialog_metadataTable( ActionEvent e )
    {
        try
        {
            Pane bruh = FXMLLoader.load(getClass().getResource("resources/settingsMetadataTable.fxml"));
            Scene settingsDialog = new Scene( bruh );
            Stage s = new Stage( StageStyle.UTILITY );
            s.setScene( settingsDialog );
            
            s.setResizable(false);
            s.setTitle("Metadata Table Content");
            s.initModality(Modality.APPLICATION_MODAL);
            s.show();
        }
        catch( IOException eIO )
        {
            eIO.printStackTrace();
        }
    }
    
    private DoubleProperty zoom = new SimpleDoubleProperty( 1.0 );
    
//    @FXML
//    void waveformChart_OnScroll( ScrollEvent se )
//    {
//        if( se.getDeltaY() == 0 )
//            return;
//        
//        double ZOOM_DELTA = MainApp.ZOOM_DELTA;
//        
//        double scaleFactor = ( se.getDeltaY() > 0 ) ? ZOOM_DELTA : 1/ZOOM_DELTA;
//        zoom.setValue( zoom.getValue() * scaleFactor );
//        System.out.println( zoom.toString() );
////        waveformChart.setScaleX( waveformChart.getScaleX() * scaleFactor );
//        
//    }
    
    public void initialize()
    {
        ControllerMediator.getInstance().registerFXMLController(this);
        
        waveforms = new ArrayList<>();
        
        waveformVisibility = new HashMap<>();
        
        SettingsController.prepSettingsMap();

        NumberAxis xAxis = (NumberAxis)waveformChart.getXAxis();
        xAxis.setUpperBound( 4000 );
        xAxis.setLowerBound( 0 );
        
        NumberAxis yAxis = (NumberAxis)waveformChart.getYAxis();
        yAxis.setUpperBound( 2.5 );
        yAxis.setLowerBound( -5.0 );
        
        metadataTableProperty.setCellValueFactory( new PropertyValueFactory<>("propertyName") );
        metadataTableValue.setCellValueFactory( new PropertyValueFactory<>("propertyValue") );
        
        SettingsMetadataTableController.translateSettingsFileToHashMaps();
//        metadataTable.setItems(ol);
    }
    
    private void addWaveform( File file )
    {
        try
        {
            Waveform wave = Waveform.readWaveformXML( file );
            
            waveformChart.getData().add( wave.waveformXYChart );
            waveformChart.getData().add( wave.waveformXYChartSignificantPoints );
            ToolBar t = FXMLLoader.load(getClass().getResource("resources/WaveformLayer.fxml"));
            t.setId( wave.ID );
            
            waveforms.add( wave );
            waveformVisibility.put( wave.ID , Boolean.TRUE );
            waveformLayers.getChildren().add( t );
            
            // Sets all points to black. Eventually, TODO: I want to add an option for the user to select a default color
            for( Data d : wave.waveformXYChart.getData() )
            {
                    d.getNode().setStyle( String.format("-fx-background-color: %s; -fx-background-radius: 1px; -fx-padding: 2px", SettingsController.settings.get("defaultWaveformColor")) );
            }
            for( Data d : wave.waveformXYChartSignificantPoints.getData() )
            {
//                    d.getNode().setStyle( String.format("-fx-background-color: %s;",  "#FF0000") );
//                    d.getNode().setStyle( String.format("-fx-background-radius: 1px; -fx-stroke: #123456") );
//                    d.getNode().lookup( ".chart-symbol" ).setStyle( String.format("-fx-shape: \"M 20.0 20.0  v24.0 h 10.0  v-24   Z\"; -fx-padding: 7px 7px 7px 7px;") );
                    d.getNode().lookup( ".chart-symbol" ).setStyle( String.format("-fx-background-color: #FF5D5D; -fx-background-radius: 5px; -fx-padding: 5px;") );
            }
            refreshWaveformGraph();
            
            // Removes a waveform series and its toolbar from the graph
            Button closeButton = (Button)t.getItems().get(0);       // "Why index 0?" you ask. If you look at WaveformLayer.fxml,
                                                                    // The closeButton is the first <Button> item in the <Toolbar><Items>
            closeButton.setOnMousePressed( (MouseEvent e1) ->
            {
                if( e1.isControlDown() )
                {
                    waveforms = new ArrayList<>();
                    waveforms.add(wave);
                    waveformLayers.getChildren().clear();
                    waveformLayers.getChildren().add( t );
                    boolean temp = waveformVisibility.get(wave.ID);
                    waveformVisibility.clear();
                    waveformVisibility.put( wave.ID, temp );
                    waveformChart.getData().clear();
                    waveformChart.getData().add( wave.waveformXYChart );
                    if( wave == primaryWaveform )
                        waveformChart.getData().add( wave.waveformXYChartSignificantPoints );
                }
                else
                {
                    waveforms.remove( wave );
                    waveformLayers.getChildren().remove( t );
                    waveformVisibility.remove( wave.ID );
                    waveformChart.getData().remove( wave.waveformXYChart );
                    waveformChart.getData().remove( wave.waveformXYChartSignificantPoints );
                }
            } );
            
            TextField nameTextField = (TextField)t.getItems().get(1);
            
            nameTextField.setText( wave.name );
            
            nameTextField.setOnMouseClicked( (MouseEvent eh) ->
            {
                if( primaryWaveform == wave )                       // This waveform is already the primary, which implies its information is shown.
                    return;
                
                primaryWaveform = wave;
                refreshWaveformGraph();
                refreshMetadataTable();
                
                labelWaveformName.setText( String.format("%s", primaryWaveform.name) );
                labelIP1.setText( String.format("  IP1:\t\t\t%.3f A @ %.3f nS", primaryWaveform.IP1.getY(), primaryWaveform.IP1.getX()) );
                labelIP2.setText( String.format("  IP2:\t\t\t%.3f A @ %.3f nS", primaryWaveform.IP2.getY(), primaryWaveform.IP2.getX()) );
                labelFWHM1.setText( String.format("  FWHM1:\t\t%.3f A @ %.3f nS", primaryWaveform.FWHM_T1.getY(), primaryWaveform.FWHM_T1.getX()) );
                labelFWHM2.setText( String.format("  FWHM2:\t\t%.3f A @ %.3f nS", primaryWaveform.FWHM_T2.getY(), primaryWaveform.FWHM_T2.getX()) );
                labelTr10.setText( String.format("  Tr10:\t\t%.3f A @ %.3f nS", primaryWaveform.RT_10.getY(), primaryWaveform.RT_10.getX()) );
                labelTr90.setText( String.format("  Tr90:\t\t%.3f A @ %.3f nS", primaryWaveform.RT_90.getY(), primaryWaveform.RT_90.getX()) );
                
            } );
            
            nameTextField.setOnKeyTyped((KeyEvent el) ->
            {
                wave.name = nameTextField.getText();
                if( primaryWaveform == wave )
                    labelWaveformName.setText( String.format("%s", primaryWaveform.name) );
            } );
            
            // Moves the waveform series and its toolbar up one layer
            Button upButton = (Button)t.getItems().get(2);
            upButton.setOnMousePressed((MouseEvent el) ->
            {
                if( waveforms.get(0) == wave )                      // If this waveform is at the front of the array list, it is already on top of all layers.
                    return;
                
                int currentIndex = waveforms.indexOf( wave );
                if( el.isControlDown() )
                    swapWaveformLayers( currentIndex, 0 );
                else
                    swapWaveformLayers( currentIndex, currentIndex-1 );
                
            } );
            
            // Moves the waveform series and its toolbar down one layer
            Button downButton = (Button)t.getItems().get(3);
            downButton.setOnMousePressed( (MouseEvent el) ->
            {
                if( waveforms.get( waveforms.size()-1 ) == wave )  // If this waveform is at the end of the array list, it is already on bottom of all layers.
                    return;
                
                int currentIndex = waveforms.indexOf( wave );
                if( el.isControlDown() )
                    swapWaveformLayers( currentIndex, waveforms.size()-1 );
                else
                    swapWaveformLayers( currentIndex, currentIndex+1 );
                
            } );
            
            // Changes the plot points of the waveform series.
            ColorPicker colorPicker = (ColorPicker)t.getItems().get(4);
            colorPicker.setValue( Color.web( SettingsController.settings.get("defaultWaveformColor") ) );
            colorPicker.setOnAction( (ActionEvent el) ->
            {
                for( Data d : wave.waveformXYChart.getData() )
//                    d.getNode().setStyle( String.format("-fx-background-color: %s;", formatColorIntoStyle( colorPicker.getValue() )) );
                    d.getNode().setStyle(String.format( "-fx-background-color: %s; -fx-background-radius: 1px; -fx-padding: 2px", formatColorIntoStyle( colorPicker.getValue() ) ));
            } );
            
            CheckBox visibility = (CheckBox)t.getItems().get(5);
            visibility.setOnMousePressed( (MouseEvent el) ->
            {
                if( el.isControlDown() )
                {
                    for( Node n : waveformLayers.getChildren() )
                    {
                        ToolBar m = (ToolBar) n;
                        CheckBox b = (CheckBox) m.getItems().get(5);
                        b.setSelected(false);
                        waveformVisibility.put( m.getId(), false );
                    }
                    visibility.setSelected(true);
                    waveformVisibility.put(wave.ID, true );
                }
                else
                    waveformVisibility.put(wave.ID, !visibility.isSelected() );
                
                System.out.println("Beeep");
                refreshWaveformGraph();
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
        
        waveformPointsBG.setOpacity( Double.parseDouble(settings.get("waveformPointsBGOpacity"))/100 );
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
        {
            if( Objects.equals(waveformVisibility.get( waveforms.get(i).ID ), Boolean.FALSE) )
                continue;
            
            waveformChart.getData().add( waveforms.get(i).waveformXYChart );
            
            if( waveforms.get(i) == primaryWaveform )
                waveformChart.getData().add( waveforms.get(i).waveformXYChartSignificantPoints );
        }
    }
    
    private void refreshMetadataTable()
    {
        metadataTable.setItems( primaryWaveform.metadataForTableView );
    }
}
