/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package seniorproject;

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
        translateDesiredAttributesToHashMaps();
    }
    
    private void translateDesiredAttributesToHashMaps()
    {
        Scanner s = new Scanner( desiredAttributes.getText() );
        while( s.hasNextLine() )
        {
            String desiredData = s.nextLine();
            String[] keyAndValue = desiredData.split("=");
            
            if( keyAndValue.length < 2 )        // Malformed property name and attribute
                continue;                       // Probably looks something like "=operator" or "Start Time="
            
            // An Entry in the hashmap would look like ["Temperature"]=metadata@Temperature
            FXMLController.metadataValue.put( keyAndValue[0] , keyAndValue[1] );
        }
        
//        System.out.println( FXMLController.metadataValue.toString() );
    }
    
    private void translateSettingsFileToDesiredAttributes()
    {
        String desiredInSettings = SettingsController.settings.get("desiredMetadata");
        String[] desiredSplit = desiredInSettings.split("&&&");                             // &&& is illegal in XML, which makes it perfect as a means to split the settings
                                                                                            // I just hope no one uses &&& in their property name.
        FXMLController.metadataValue.clear();
        for ( String aDesiredAttribute : desiredSplit )
        {
            String[] desiredPropNameAndValue = aDesiredAttribute.split("=");                // I hope no one uses = either.
            FXMLController.metadataValue.put( desiredPropNameAndValue[0] , desiredPropNameAndValue[1] );
        }
    }
    
    private void translateHashMapsToSettingsFile()
    {
        String metadataToSettingLine = "";
        for( Entry<String, String> e : FXMLController.metadataValue.entrySet() )
            metadataToSettingLine += e.getKey() + "=" + e.getValue() + "&&&";
        
        settings.put( "desiredMetadata", metadataToSettingLine );
    }
    
    
    @FXML
    Pane settingsPane;
    
    @FXML
    TextArea desiredAttributes;
    
    
    @FXML
    void applyAndCloseButtonClicked( ActionEvent e )
    {
        translateDesiredAttributesToHashMaps();
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
