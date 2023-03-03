package SeniorProject;

/*
    This is the main class of the project. It is the foundation for everything else.
    It holds the JFrame that contains all of the UI elements.
*/
import javax.swing.*;

public class WaveformProject {
    
    public static void main( String args[] )
    {
        
        WaveformGraph waveformGraph = new WaveformGraph();
        
        Waveform test = WaveformXMLReader.readXML( "D:\\waveforms\\JS-002 (6.8pF)_20221003111023.result.xml" );
        
        JFrame mainWindow = new JFrame("WaveformProject");
        mainWindow.setSize( 600, 400 );
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        waveformGraph.displayedWaveforms.add( test );
        waveformGraph.waveformGraphPanel.repaint();
        mainWindow.add( waveformGraph.waveformGraphPanel );
        
        mainWindow.setVisible(true);
        
    }
    
}