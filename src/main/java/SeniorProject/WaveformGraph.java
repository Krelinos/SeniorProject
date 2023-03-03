package SeniorProject;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Emmer
 */
public class WaveformGraph
{
    ArrayList<Waveform> displayedWaveforms = new ArrayList<>();
    
    double zoomLevel = 1;
    
    JPanel waveformGraphPanel = new JPanel(){
        @Override
        public void paintComponent( Graphics g )
        {
            super.paintComponent(g);
            
            Iterator<Waveform> readWaveData = displayedWaveforms.iterator();
            while( readWaveData.hasNext() )
            {
                Waveform aWaveform = readWaveData.next();
                Iterator<Point2D.Double> waveformPoints = aWaveform.waveformPoints.iterator();

                System.out.println("This waveform has " + aWaveform.waveformPoints.size() + " elements");

                while( waveformPoints.hasNext() )
                {
                    Point2D.Double waveformPoint = waveformPoints.next();

                    // p.x * 10000000000000 was too large for the int data type, so I just divided
                    // by 0.0000000000005 to scale the X axis.
                    // Obviously this should also be revamped.
                    g.fillRect(
                        (int)( ((waveformPoint.x/0.000000000005) + 300)*zoomLevel )
                        , (int)( ( (waveformPoint.y*50)+300 )*zoomLevel )
                        , 3
                        , 3
                    );
                }
            }
        }
    };
    
}
