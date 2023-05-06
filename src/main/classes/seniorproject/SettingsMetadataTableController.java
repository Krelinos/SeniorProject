/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package seniorproject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import static seniorproject.SettingsController.alteredSettings;
import static seniorproject.SettingsController.settings;

/**
 *
 * @author Emmer
 */
public class SettingsMetadataTableController
{
    static ObservableList<WaveformMetadata> getListForWaveform( Waveform wave )
    {
        ObservableList<WaveformMetadata> tableData = FXCollections.observableArrayList();
        
        
        
        return tableData;
    }
    
    
    
    public void initialize()
    {
//        pushHashMapDataToDesiredAttributesTextArea();
        translateHashMapsToDesiredAttribtuesTextArea();
    }
    
    private void translateDesiredAttributesTextAreaToHashMaps()
    {
        int index = 0;
        FXMLController.metadataHumanReadable.clear();
        FXMLController.metadataValue.clear();
        
        Scanner s = new Scanner( desiredAttributes.getText() );
        while( s.hasNextLine() )
        {
            String desiredData = s.nextLine();
            String[] keyAndValue = desiredData.split("=");
            
            if( keyAndValue.length < 2 )        // Malformed property name and attribute
                continue;                       // Probably looks something like "=operator" or "Start Time="
            
            // An Entry in the hashmap would look like ["Temperature"]=metadata@Temperature
            FXMLController.metadataHumanReadable.put( index, keyAndValue[0] );
            FXMLController.metadataValue.put( index++, keyAndValue[1] );
        }
        
        System.out.println( "yee" + FXMLController.metadataHumanReadable.toString() );
        System.out.println( "wow" + FXMLController.metadataValue.toString() );
    }
    
    static void translateSettingsFileToHashMaps()
    {
        String desiredInSettings = SettingsController.settings.get("desiredMetadata");
        System.out.println("^v^" + SettingsController.settings.toString());
        System.out.println("owo" + desiredInSettings);
        String[] desiredSplit = desiredInSettings.split("&&&&");                            // &&&& is illegal in XML, which makes it perfect as a means to split the settings
                                                                                            // I just hope no one uses &&& in their property name.
        FXMLController.metadataHumanReadable.clear();
        FXMLController.metadataValue.clear();
        int index = 0;
        
        for ( String aDesiredAttribute : desiredSplit )
        {
            System.out.println("uwu" + aDesiredAttribute);
            String[] desiredPropNameAndValue = aDesiredAttribute.split("&&&");                // I hope no one uses &&& either.
            
            if( desiredPropNameAndValue.length >= 2)
            {
                FXMLController.metadataHumanReadable.put( index, desiredPropNameAndValue[0] );
                FXMLController.metadataValue.put( index++, desiredPropNameAndValue[1] );
            }
        }
        System.out.println( FXMLController.metadataHumanReadable.toString() );
        System.out.println( FXMLController.metadataValue.toString() );
    }
    
    private void translateHashMapsToSettingsFile()
    {
        String metadataToSettingLine = "";
        int index = 0;
        while( FXMLController.metadataValue.containsKey(index) )
            metadataToSettingLine += FXMLController.metadataHumanReadable.get(index) + "&&&" + FXMLController.metadataValue.get(index++) + "&&&&";
        
        settings.put( "desiredMetadata", metadataToSettingLine );
        saveSettings();
    }
    
    private void translateHashMapsToDesiredAttribtuesTextArea()
    {
        int index = 0;
        String input = "";
        while( FXMLController.metadataValue.containsKey(index) )
            input += FXMLController.metadataHumanReadable.get(index) + "=" + FXMLController.metadataValue.get(index++) + "\n";
        
        desiredAttributes.setText( input );
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
    
    
    @FXML
    Pane settingsPane;
    
    @FXML
    TextArea desiredAttributes;
    
    
    @FXML
    void applyAndCloseButtonClicked( ActionEvent e )
    {
        translateDesiredAttributesTextAreaToHashMaps();
        translateHashMapsToSettingsFile();
                
        Stage settingsWindow = (Stage)settingsPane.getScene().getWindow();
        settingsWindow.close();
    }
    
    @FXML
    void cancelButtonClicked( ActionEvent e )
    {
        Stage settingsWindow = (Stage)settingsPane.getScene().getWindow();
        settingsWindow.close();
    }
    
    @FXML
    void restoreDefaultButtonClicked( ActionEvent e )
    {
        desiredAttributes.setText("""
                                  Run Date=runDate@result
                                  Temperature=Temperature@metadata
                                  Humidity (Rel)=RelativeHumidity@metadata
                                  First Peak Current=IP1""");
    }
    
    private class WaveformMetadata
    {
        private final SimpleStringProperty propertyName;
        private final SimpleStringProperty propertyValue;
        
        WaveformMetadata( String name, String value )
        {
            propertyName = new SimpleStringProperty(name);
            propertyValue = new SimpleStringProperty(name);
        }
    }
}
