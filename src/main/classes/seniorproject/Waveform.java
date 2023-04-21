/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package seniorproject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.geometry.Point2D;
import javafx.scene.chart.XYChart;
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
    
    String ID;
    XYChart.Series<Double, Double> waveformXYChart;     // All points of a waveform, ready to be inserted into a Scatter Graph.
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
        
        ID = UUID.randomUUID().toString();
        waveformXYChart = new XYChart.Series<>();
        metadata = new HashMap<>();
        metadataPreferredPropertyNames = new HashMap<>();
        
        // Integrate all coordinates into waveformXYChart
        Iterator<Double> xCoordinatesIterator = xCoordinates.iterator();
        Iterator<Double> yCoordinatesIterator = yCoordinates.iterator();
        
        while( xCoordinatesIterator.hasNext() && yCoordinatesIterator.hasNext() )
        {
            BigDecimal xToPicoseconds = new BigDecimal( xCoordinatesIterator.next() );
            xToPicoseconds = xToPicoseconds.scaleByPowerOfTen(12);
            Double y = yCoordinatesIterator.next();
            
            if( xToPicoseconds.doubleValue() < 0 )
                continue;
            
            waveformXYChart.getData().add( new XYChart.Data<>( xToPicoseconds.doubleValue(), y ) );
        }
    
    }
    
    void analyzeWaveform()
    {
        //  Ryan(Kasper) said he'd work on sorting to find IP1 and IP2. I will leave it to him.
    }
}
