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

/**
 *
 * @author Emmer
 */
public class Waveform
{
    //==========STATIC STUFF
    static DocumentBuilder documentBuilder;
    
    static      // I just learned that you can do this for static classes - Emmer 20 April 2023
    {
        try
        {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch ( ParserConfigurationException ex )
        {
            ex.printStackTrace();
        }
    }
    
    static Waveform readWaveformXML( File file ) throws SAXException
    {
        try
        {
            Document document = documentBuilder.parse( file );
            
            NodeList bruhSFX = document.getChildNodes().item(0).getChildNodes();
            if ( document.hasChildNodes() )
                return printNodeList( document.getChildNodes() );
                
            
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    static Waveform printNodeList(NodeList nodeList) {

        int n = 0;
        int a = 0;
        int b = 0;
        
        List<Double> xcoords = new ArrayList<>();
        List<Double> ycoords = new ArrayList<>();
        /////////////LOOP THROUGH XML FILE AND CREATE AN ARRAY WITH ALL X CHORDS AND ANOTHER ARRAY WITH ALL Y CHORDS

        for (int count = 0; count < nodeList.getLength(); count++) {
            Node elemNode = nodeList.item(count);
            if (elemNode.getNodeType() == Node.ELEMENT_NODE) {

                {
                    NamedNodeMap nodeMap = elemNode.getAttributes();

                    for (int i = 0; i < nodeMap.getLength(); i++) {
                        Node node = nodeMap.item(i);
                        //if the node name is x or y

                        /////////////////////////////////////////CHECK FOR TAG "X"
                        if( "x".equals(node.getNodeName()) ) {
                            /////////DISPLAY VALUES TO CONSOLE
                            System.out.println("attr name : " + node.getNodeName());
                            System.out.println("attr value x  : " + node.getNodeValue());
                            //PUT THE VALUE INTO THE ARRAY
                            xcoords.add( Double.valueOf(node.getNodeValue()) );
                            b++;
                        }
                        /////////////////////////////////////////CHECK FOR TAG Y
                        else if( "y".equals(node.getNodeName()) ) {
                            /////////DISPLAY VALUES TO CONSOLE
                            System.out.println("attr name : " + node.getNodeName());
                            System.out.println("attr value y  : " + node.getNodeValue());
                            //PUT THE VALUE INTO THE ARRAY
                            ycoords.add( Double.valueOf(node.getNodeValue()) );

                            n++;
                        }

                    }

                }

                if (elemNode.hasChildNodes()) {
//recursive call if the node has child nodes
                    printNodeList(elemNode.getChildNodes());
                }
            }
        }
//out of loops
        
        return new Waveform( xcoords, ycoords );
    }
    
    //========NON STATIC STUFF
    
    List<Point2D> waveformData; // All points of a waveform 
    Point2D IP1;                // Peak positive current - Maximum measured current on the first peak, measured in Amps.
    Point2D IP2;                // Peak negative current on second peak – Maximum current on the second peak, measured in Amps.
    Point2D FWHM_T1;            // Full Width at Half Maximum – The timing at half of IP1 for t1 to t2, measured in ps (Pico Seconds).
    Point2D FWHM_T2;
    Point2D RT_10;              // Rise Time - The timing measured between 10% and 90% of peak current of IP1, measured in ps (picoseconds).
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
        metadata = new HashMap<>();
        
        // Integrate all coordinates into the Point2D array list.
        Iterator<Double> yCoordinatesIterator = yCoordinates.iterator();
        for( Double anXCoordinate : xCoordinates )
            waveformData.add( new Point2D( anXCoordinate, yCoordinatesIterator.next() ) );
        
        
    }
    
    void analyzeWaveform()
    {
        //  Ryan(Kasper) said he'd work on sorting to find IP1 and IP2. I will leave it to him.
    }
}
