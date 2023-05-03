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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Emmer
 */
public class SettingsController
{
    static File settingsFile;
    static Map<String, String> settings;
    
    static Map<String, String> alteredSettings;
    
    public void initialize()
    {
        ControllerMediator.getInstance().registerSettingsController(this);
        prepSettingsMap();
        
        if( settings.get( "defaultWaveformColor" ) != null )
        {
            Color c = Color.web( settings.get("defaultWaveformColor") );
            defaultWaveformColorPicker.setValue( c );
        }
        
        if( settings.get( "graphBackgroundColor" ) != null )
        {
            Color c = Color.web( settings.get("graphBackgroundColor") );
            graphBackgroundColorPicker.setValue( c );
        }
        
        if( Integer.parseInt( settings.get("importWaveformDirectoryAuto") ) == 1 )
        {
            waveformInputDirectoryAuto.setSelected( true );
        }
        
        if( Integer.parseInt( settings.get("exportImageDirectoryAuto") ) == 1 )
        {
            imageOutputDirectoryAuto.setSelected( true );
        }
        
        if( Integer.parseInt( settings.get("exportCSVDirectoryAuto") ) == 1 )
            csvOutputDirectoryAuto.setSelected( true );
        
        waveformInputDirectoryPath.setText( settings.get("defaultImportWaveformDirectory") );
        
        imageOutputDirectoryPath.setText( settings.get("defaultSaveImageDirectory") );
        
        csvOutputDirectoryPath.setText( settings.get("defaultSaveCSVDirectory") );
        
        waveformPointsBGOpacity.adjustValue( Double.parseDouble( settings.get("waveformPointsBGOpacity") ) );
        waveformPointsBGOpacity.valueProperty().addListener((ObservableValue <? extends Number >
                observable, Number oldValue, Number newValue) ->
        {
            alteredSettings.put("waveformPointsBGOpacity", String.valueOf( newValue.doubleValue() ));
        });
        
    }
    
    static void prepSettingsMap()
    {
        try
        {
//            settingsFile = new File( getClass().getResource("resources/settings.txt").getFile() );
            settings = new HashMap<>();
            alteredSettings = new HashMap<>();
            settingsFile = new File( System.getProperty("user.dir") + "\\build\\modules\\main\\seniorproject\\resources\\settings.txt" );
            System.out.println(settingsFile.getAbsolutePath());
            Scanner scanner = new Scanner(settingsFile);
            
            while( scanner.hasNext() )
            {
                String line = scanner.nextLine();
                String[] keyAndValue = line.split("=");
                if( keyAndValue.length == 2 )
                {
                    settings.put( keyAndValue[0], keyAndValue[1] );
                    System.out.println( keyAndValue[0] + " " + keyAndValue[1] );
                }
                else
                {
                    settings.put( keyAndValue[0], "" );
                    System.out.println( keyAndValue[0] + " " + "" );
                }
            }
        }
        catch( NullPointerException | FileNotFoundException ex )
        { 
            ex.printStackTrace();
        }
        
        if( "".equals( (String)settings.get("defaultImportWaveformDirectory") ) )
            settings.put("defaultImportWaveformDirectory", System.getProperty("user.dir"));
        if( "".equals( (String)settings.get("defaultSaveImageDirectory") ) )
            settings.put("defaultSaveImageDirectory", System.getProperty("user.dir"));
        if( "".equals( (String)settings.get("defaultSaveCSVDirectory") ) )
            settings.put("defaultSaveCSVDirectory", System.getProperty("user.dir"));
        
        ControllerMediator.getInstance().FXMLControllerUpdateSettings(settings);
    }
    
    
    @FXML
    Pane settingsPane;
    @FXML
    ColorPicker graphBackgroundColorPicker;
    @FXML
    ColorPicker defaultWaveformColorPicker;
    @FXML
    TextField waveformInputDirectoryPath;
    @FXML
    TextField imageOutputDirectoryPath;
    @FXML
    TextField csvOutputDirectoryPath;
    @FXML
    CheckBox waveformInputDirectoryAuto;
    @FXML
    CheckBox imageOutputDirectoryAuto;
    @FXML
    CheckBox csvOutputDirectoryAuto;
    @FXML
    Button waveformInputDirectoryBrowse;
    @FXML
    Button imageOutputDirectoryBrowse;
    @FXML
    Button csvOutputDirectoryBrowse;
    @FXML
    Slider waveformPointsBGOpacity;
    
    @FXML
    void defaultWaveformColorChanged( ActionEvent e )
    {
        alteredSettings.put( "defaultWaveformColor", formatColorIntoStyle( defaultWaveformColorPicker.getValue() ) );
    }
    
    @FXML
    void graphBackgroundColorChanged( ActionEvent e )
    {
        alteredSettings.put( "graphBackgroundColor", formatColorIntoStyle( graphBackgroundColorPicker.getValue() ) );
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
    
    @FXML
    void waveformInputDirectoryAutoClicked( ActionEvent e )
    {
        if( waveformInputDirectoryAuto.isSelected() )
            settings.put("importWaveformDirectoryAuto", "1");
        else
            settings.put("importWaveformDirectoryAuto", "0");
    }
    
    @FXML
    void imageOutputDirectoryAutoClicked( ActionEvent e )
    {
        if( imageOutputDirectoryAuto.isSelected() )
            settings.put("exportImageDirectoryAuto", "1");
        else
            settings.put("exportImageDirectoryAuto", "0");
    }
    
    @FXML
    void csvOutputDirectoryAutoClicked( ActionEvent e )
    {
        if( csvOutputDirectoryAuto.isSelected() )
            settings.put("exportCSVDirectoryAuto", "1");
        else
            settings.put("exportCSVDirectoryAuto", "0");
    }
    
    @FXML
    void waveformInputDirectoryBrowseClicked( ActionEvent e )
    {
        DirectoryChooser dChooser = new DirectoryChooser();
        dChooser.setInitialDirectory( new File( (String)SettingsController.settings.get("defaultImportWaveformDirectory") ) );
        File d = dChooser.showDialog( settingsPane.getScene().getWindow() );
        
        waveformInputDirectoryPath.setText( d.getAbsolutePath() );
        alteredSettings.put("defaultImportWaveformDirectory", d.getAbsolutePath());
    }
    
    @FXML
    void imageOutputDirectoryBrowseClicked( ActionEvent e )
    {
        DirectoryChooser dChooser = new DirectoryChooser();
        dChooser.setInitialDirectory( new File( (String)SettingsController.settings.get("defaultSaveImageDirectory") ) );
        File d = dChooser.showDialog( settingsPane.getScene().getWindow() );
        
        imageOutputDirectoryPath.setText( d.getAbsolutePath() );
        alteredSettings.put("defaultSaveImageDirectory", d.getAbsolutePath());
    }
    
    @FXML
    void csvOutputDirectoryBrowseClicked( ActionEvent e )
    {
        DirectoryChooser dChooser = new DirectoryChooser();
        dChooser.setInitialDirectory( new File( (String)SettingsController.settings.get("defaultSaveCSVDirectory") ) );
        File d = dChooser.showDialog( settingsPane.getScene().getWindow() );
        
        imageOutputDirectoryPath.setText( d.getAbsolutePath() );
        alteredSettings.put("defaultSaveCSVDirectory", d.getAbsolutePath());
    }
    
    @FXML
    void waveformPointsBGOpacityChanged( ActionEvent e )
    {
        System.out.println("Buh");
        alteredSettings.put("waveformPointsBGOpacity", String.valueOf(waveformPointsBGOpacity.getValue()) );
    }
    
    private void saveSettings()
    {
        for( Entry<String, String> aSetting : alteredSettings.entrySet() )
            settings.put( aSetting.getKey() , aSetting.getValue() );
        
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
}
