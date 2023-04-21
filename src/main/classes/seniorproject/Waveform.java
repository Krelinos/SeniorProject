/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package seniorproject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.geometry.Point2D;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.SAXParser;  
import javax.xml.parsers.SAXParserFactory;  
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;  

/**
 *
 * @author Emmer
 */
public class Waveform
{
    //==========STATIC STUFF
    private static SAXParser saxParser;
    
    static      // I just learned that you can do this for static classes - Emmer 20 April 2023
    {
        try
        {
            saxParser = SAXParserFactory.newInstance().newSAXParser();
        }
        catch ( ParserConfigurationException | SAXException ex )
        {
            ex.printStackTrace();
        }
        
        
    }
    
    static Waveform readWaveformXML( File file ) throws SAXException, IOException
    {
        List<Double> xCoords = new ArrayList<>();
        List<Double> yCoords = new ArrayList<>();
        
        DefaultHandler handler = new DefaultHandler()
        {
            @Override
            public void startElement( String uri, String localName, String qName, Attributes attributes ) throws SAXException
            {
                
                if( "dp".equalsIgnoreCase(qName) )  // <dp x="..." y="..." /> is the coordinates in the XML file
                {
                    xCoords.add( Double.valueOf( attributes.getValue("x") ) );
                    yCoords.add( Double.valueOf( attributes.getValue("y") ) );
                }
                
            }

            @Override
            public void characters(char ch[], int start, int length) throws SAXException   
            {

            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException   
            {  
//                System.out.println("End Element:" + qName);
            }
        };
        
        saxParser.parse( file, handler );
        
        return new Waveform( xCoords, yCoords );
    }
    
    //========NON STATIC STUFF
    
    List<Point2D> waveformData; // All points of a waveform 
    Point2D IP1;                // Peak positive current - Maximum measured current on the first peak, measured in Amps.
    Point2D IP2;                // Peak negative current on second peak – Maximum current on the second peak, measured in Amps.
    Point2D FWHM_T1;            // Full Width at Half Maximum – The timing at half of IP1 for t1 to t2, measured in ps (Pico Seconds).
    Point2D FWHM_T2;
    Point2D RT_10;              // Rise Time - The timing measured between 10% and 90% of peak current of IP1, measured in ps (picoseconds or x10^-9).
    Point2D RT_90;
    
    Map<String, String> metadata;                           // Data from the XML to be displayed in the TableView.
    Map<String, String> metadataPreferredPropertyNames;     // Container to hold human-readable names of metadata, if needed.
    
    Waveform( List<Double> xCoordinates, List<Double> yCoordinates )
    {
        // Sanity check. Error out if somehow there are unequal amounts of x and y points.
        // Eventually, this should be a dialog box in the application.
        if( xCoordinates.size() != yCoordinates.size() )
            System.err.println( String.format("Somehow, there is a different number of x and y coords. x count: %d  y count: %d", xCoordinates.size(), yCoordinates.size()) );
        
        //----------
        
        waveformData = new ArrayList<>();
        metadata = new HashMap<>();
        metadataPreferredPropertyNames = new HashMap<>();
        
        // Integrate all coordinates into the Point2D array list.
        Iterator<Double> yCoordinatesIterator = yCoordinates.iterator();
        for( Double anXCoordinate : xCoordinates )
            waveformData.add( new Point2D( anXCoordinate, yCoordinatesIterator.next() ) );
        
        System.out.println("In Waveform, waveformData has " + waveformData.size() + " elements");
    }
    
    void analyzeWaveform()
    {
        //  Ryan(Kasper) said he'd work on sorting to find IP1 and IP2. I will leave it to him.
    }
}
