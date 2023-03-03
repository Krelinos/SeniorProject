package SeniorProject;

import java.io.File;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WaveformXMLReader {
    
    static Waveform readXML( File xmlFile )
    {
        Waveform waveformData = new Waveform();
        
        // A brief reminder on the relevent XML tags:
        /*
            ...
            <waveform>
                ...
                <waveformContainer ... >
                    <rawWaveForm>
                        <dp x="..." y="...">
                        <dp x="..." y="...">
                        ...
                        <dp x="..." y="...">
                    </rawWaveForm>
                </waveformContainer>
            </waveform>
            ...
        */
        
        try   
        {  
            SAXParserFactory factory = SAXParserFactory.newInstance();  
            SAXParser saxParser = factory.newSAXParser();  
            
            DefaultHandler handler = new DefaultHandler()   
            {
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException   
                {
                    System.out.println("End Element:" + qName);
                    if( qName.equalsIgnoreCase("DP") )  // <dp ... /> is the coordinates in the XML file
                    {
                        double x = Double.parseDouble( attributes.getValue("x") );
                        double y = Double.parseDouble( attributes.getValue("y") );
                        
                        System.out.println("dp -> x: " + x + " y: " + y);
                        
                        waveformData.waveformPoints.add( new Point2D.Double( x, y ) );
                    }
                }
                
                @Override
                public void characters(char ch[], int start, int length) throws SAXException   
                { /* Nothing */ }
                
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException   
                {  
                    System.out.println("End Element:" + qName);
                }
            };
            
            saxParser.parse(xmlFile, handler);
        }
        catch( Exception e )
        {

        }
        
        return waveformData;
    }
    
    static Waveform readXML( String filePath ){ return WaveformXMLReader.readXML( new File(filePath) ); }
    
}