/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package seniorproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Emmer
 */
public class SettingsController
{
    Map<String, String> settings;
    File settingsFile;
    
    public void initialize()
    {
        ControllerMediator.getInstance().registerSettingsController(this);
        
        settings = new HashMap<>();
        
        try
        {
            settingsFile = new File( getClass().getResource("resources/settings.txt").getFile() );
            System.out.println(settingsFile.getAbsolutePath());
            Scanner scanner = new Scanner(settingsFile);
            
            while( scanner.hasNext() )
            {
                String line = scanner.nextLine();
                String[] keyAndValue = line.split("=");
                settings.put( keyAndValue[0], keyAndValue[1] );
                System.out.println( keyAndValue[0] + " " + keyAndValue[1] );
            }
        }
        catch( NullPointerException | FileNotFoundException ex )
        { 
            ex.printStackTrace();
        }
        
        if( settings.get( "graphBackgroundColor" ) != null )
        {
            Color c = Color.web( settings.get("graphBackgroundColor") );
            graphBackgroundColorPicker.setValue( c );
        }
        
    }
    
    
    @FXML
    Pane settingsPane;
    @FXML
    ColorPicker graphBackgroundColorPicker;
    @FXML
    ColorPicker defaultWaveformColorPicker;
    
    @FXML
    void defaultWaveformColorChanged( ActionEvent e )
    {
        settings.put( "graphBackgroundColor", formatColorIntoStyle( defaultWaveformColorPicker.getValue() ) );
    }
    
    @FXML
    void graphBackgroundColorChanged( ActionEvent e )
    {
        settings.put( "graphBackgroundColor", formatColorIntoStyle( graphBackgroundColorPicker.getValue() ) );
    }
    
    @FXML
    void applyAndCloseButtonClicked( ActionEvent e )
    {
        saveSettings();
        ControllerMediator.getInstance().FXMLControllerUpdateSettings( settings );
        
        Stage settingsWindow = (Stage)settingsPane.getScene().getWindow();
        settingsWindow.close();
    }
    
    @FXML
    void applyButtonClicked( ActionEvent e )
    {
        saveSettings();
        ControllerMediator.getInstance().FXMLControllerUpdateSettings( settings );
    }
    
    @FXML
    void cancelButtonClicked( ActionEvent e )
    {
        Stage settingsWindow = (Stage)settingsPane.getScene().getWindow();
        settingsWindow.close();
    }
    
    private void saveSettings()
    {
        try( FileWriter settingsWriter = new FileWriter( getClass().getResource("resources/settings.txt").getFile(), false ) )
        {
            for( Entry aSetting : settings.entrySet() )
            {
                System.out.println( aSetting.getKey() + "=" + aSetting.getValue() );
                settingsWriter.write( aSetting.getKey() + "=" + aSetting.getValue() + "\n");
            }
            settingsWriter.close();
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }
    }
    
    private String formatColorIntoStyle( Color c ) {
        int r = (int) (255 * c.getRed()) ;
        int g = (int) (255 * c.getGreen()) ;
        int b = (int) (255 * c.getBlue()) ;
        
        return String.format("#%02x%02x%02x", r, g, b);
    }
    void beep()
    {
        System.out.println("Boop");
    }
}
