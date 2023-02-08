/*

This code is hella messy and honestly should be scrapped and redone, but it works.
We can make a new one once everyone has seen this file.
Until then, this file has plenty of comments so that everyone can understand how I
managed to plot the waveform.

*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.parsers.SAXParser;  
import javax.xml.parsers.SAXParserFactory;  
import org.xml.sax.Attributes;  
import org.xml.sax.SAXException;  
import org.xml.sax.helpers.DefaultHandler;  

public class XMLReader extends JPanel
{  
    // All of the read waveform files are kept in a class called WaveformData.
    // That class stores the info of the point coordinates, lowestY, and highestY.
    public static ArrayList<WaveformData> waveforms = new ArrayList<WaveformData>();
    
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
            <waveformContainer>
        <waveform>
        ...
    */
    // xCoords and yCoords save the x and y values from the <dp>s in the <rawWaveForm> tag
    public static ArrayList<Double> xCoords = new ArrayList<Double>();
    public static ArrayList<Double> yCoords = new ArrayList<Double>();
    
    // The lowest and highest Y value in a waveform. Ideally used for automatic zoom scaling.
    public static double lowestY = Double.MAX_VALUE;
    public static double highestY = Double.MIN_VALUE;
    
    // Multiplier used to crudly zoom in and out.
    public static double zoomLevel = 1;
    
    @Override
    public void paintComponent( Graphics g )
    {
        super.paintComponent(g);
        boolean color = false;
        
        // Iterate through all read waveform XMLs.
        Iterator<WaveformData> readWaveData = waveforms.iterator();
        while( readWaveData.hasNext() )
        {
            System.out.println("Reading a waveform");
            
            // There is only two colors that are used.
            // When this is remade, we'll need a way to scale it to handle
            // more than 2 and with customizable coloring.
            if( !color )
            {
                g.setColor(Color.CYAN);
                System.out.println("Color is CYAN");
                color = true;
            }
            else
            {
                g.setColor(Color.MAGENTA);
                System.out.println("Color is MAGENTA");
                color = false;
            }
            
            WaveformData aWaveform = readWaveData.next();
            Iterator<Point2D.Double> waveformPoints = aWaveform.coordinates.iterator();
            
            System.out.println("This waveform has " + aWaveform.coordinates.size() + " elements");
            
            while( waveformPoints.hasNext() )
            {
                Point2D.Double p = waveformPoints.next();
                
                // p.x * 10000000000000 was too large for the int data type, so I just divided
                // by 0.0000000000005 to scale the X axis.
                // Obviously this should also be revamped.
                g.fillRect( (int)( ((p.x/0.000000000005) + 300)*zoomLevel ), (int)( ( (p.y*50)+300 )*zoomLevel ), 3, 3 );
            }
        }
        
    }
    
    // This whole main function was built from the example provided at the following link:
    // https://www.javatpoint.com/how-to-read-xml-file-in-java
    public static void main(String args[])   
    {  
        try   
        {  
            SAXParserFactory factory = SAXParserFactory.newInstance();  
            SAXParser saxParser = factory.newSAXParser();  
            
            DefaultHandler handler = new DefaultHandler()   
            {
                boolean dp = false; // Doesn't actually do anything, just a holdover from the sample file.
                
                //parser starts parsing a specific element inside the document    
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException   
                {  
                    System.out.println("Start Element :" + qName);  
                    if( qName.equalsIgnoreCase("DP") )  // <dp ... /> is the coordinates in the XML file
                    {
                        dp = true;
                        
                        double x = Double.parseDouble( attributes.getValue("x") );
                        double y = Double.parseDouble( attributes.getValue("y") );
                        
                        if( y > highestY )
                            highestY = y;
                        if( y < lowestY )
                            lowestY = y;
                        
                        System.out.println("dp -> x: " + x + " y: " + y);
                        
                        xCoords.add(x);
                        yCoords.add(y);
                    }
                }  
                  
                //reads the text value of the currently parsed element  
                @Override
                public void characters(char ch[], int start, int length) throws SAXException   
                {  
                    // Keeping this for reference.
//                    if (id)   
//                    {  
//                        System.out.println("ID : " + new String(ch, start, length));  
//                        id = false;  
//                    }
                }  
                //parser ends parsing the specific element inside the document  
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException   
                {  
                    System.out.println("End Element:" + qName);
                }
            };
            
            saxParser.parse("D:\\waveforms\\JS-002 (6.8pF)_20221003110909.result.xml", handler);
            waveforms.add( new WaveformData( xCoords, yCoords, lowestY, highestY ) );               // Writing the static data to a nonstatic WaveformData object.

            xCoords.clear();    // Prep for the next waveform
            yCoords.clear();
            
            saxParser.parse("D:\\waveforms\\JS-002 (6.8pF)_20221003111023.result.xml", handler);  
            waveforms.add( new WaveformData( xCoords, yCoords, lowestY, highestY ) );
            
            // Basic app window stuff in Java
            XMLReader xmlReader = new XMLReader();
            JFrame app = new JFrame("XML Reader");
            app.add( xmlReader, BorderLayout.CENTER );
            app.setSize(1000, 800);
            app.setLocationRelativeTo(null);
            app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            //BufferedImage zoomIcons = ImageIO.read(new File("D:\\waveforms\\zoom-icons.png"));
            JPanel zoomButtons = new JPanel();
            
            JButton zoomIn = new JButton("Zoom In");
            JButton zoomOut = new JButton("Zoom Out");
            
            zoomButtons.add( zoomIn, BorderLayout.WEST );
            zoomButtons.add( zoomOut, BorderLayout.EAST );
            
            zoomIn.addActionListener( new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    System.out.println("Zoom in");
                    zoomLevel = zoomLevel * 1.1;
                    app.repaint();
                }
            });
            
            zoomOut.addActionListener( new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    System.out.println("Zoom out");
                    zoomLevel = zoomLevel / 1.1;
                    app.repaint();
                }
            });
            
            app.add( zoomButtons, BorderLayout.SOUTH );
            app.setVisible(true);
        }   
        catch (Exception e)   
        {
            e.printStackTrace();  
        }  
    }  
}  

class WaveformData
{
    double lowestY;
    double highestY;
    
    ArrayList<Point2D.Double> coordinates = new ArrayList<>();
    
    WaveformData( ArrayList<Double> setXCoords, ArrayList<Double> setYCoords, double setLowestY, double setHighestY )
    {
        lowestY = setLowestY;
        highestY = setHighestY;
        
        // ArrayList is not a primative, so I need to create a copy for both axises (axii?).
        Iterator<Double> xIterator = setXCoords.iterator();
        Iterator<Double> yIterator = setYCoords.iterator();
        
        while( xIterator.hasNext() && yIterator.hasNext() )
        {
            Point2D.Double aCoordinate = new Point2D.Double();
            aCoordinate.setLocation( xIterator.next() , yIterator.next() );
            coordinates.add( aCoordinate );
        }
    }
}