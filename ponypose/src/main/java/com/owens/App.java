package com.owens;

import javax.swing.*;


/**
 * This system reads images of formats handled by the Apache Imaging library and 
 * attempts to identify objects via a form of canopy clustering.
 *
 */
public class App extends JFrame {

    private ImageViewer imageViewer;

    public App( String input ) {
        super("Image Canopy");
        setSize(640, 640);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        imageViewer = new ImageViewer();
        add(imageViewer);
        imageViewer.clusterImage( input );
    }


    public static void main( String[] args ) {
        if ( args.length < 1 ) {
            System.out.println("Required args: <filename>");
            System.exit(-1);
        }
        new App( args[0] );
    }
}
