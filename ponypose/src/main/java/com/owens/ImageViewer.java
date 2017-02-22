package com.owens;



import javax.swing.*;
import java.awt.*;
import org.apache.commons.math4.ml.clustering.*;
import org.apache.commons.imaging.formats.jpeg.decoder.*;
import org.apache.commons.imaging.common.bytesource.*;
import java.awt.image.BufferedImage;


public class ImageViewer extends JPanel {

    private ImageClustering clustering;

    public ImageViewer() {
        clustering = null;
    }

    public void clusterImage( String file ) {
        clustering = ImageClusterer.cluster( file );
        repaint();
    }

    @Override
    public void paintComponent( Graphics g ) {
        if ( clustering == null )
            return;
        Graphics2D g2d = ( Graphics2D ) g;
        g2d.drawImage(clustering.getSourceImage(), clustering, 0, 0);
    }   


}

