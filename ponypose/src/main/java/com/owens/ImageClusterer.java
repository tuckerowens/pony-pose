package com.owens;

import java.io.File;
import org.apache.commons.math4.ml.clustering.*;
import org.apache.commons.imaging.formats.jpeg.decoder.*;
import org.apache.commons.imaging.common.bytesource.*;
import org.apache.commons.math4.ml.distance.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public class ImageClusterer {


    public static ImageClustering cluster( String file ) {
        File in = new File(file);
        if ( !in.exists() )
            throw new IllegalArgumentException("File " + file + " does not exists!");

        BufferedImage input = imageDecoder(in);
        List<ImagePoint> points = new ArrayList<>( input.getTileWidth() * input.getTileHeight() );

        for ( int x = 0; x < input.getTileWidth(); x++ ) {
            for ( int y = 0; y < input.getTileHeight(); y++ ) {
                int color = input.getRGB( x, y );
                points.add( new ImageClusterer.ImagePoint( x, y, color & 0xFF, (color >> 8) & 0xFF, (color >> 16) * 0xFF ) ); 
            }
        }
        KMeansPlusPlusClusterer<ImagePoint> clusterer = new KMeansPlusPlusClusterer<ImagePoint>(2, -1, new DistanceMeasure() {
            private double[] weights = new double[] {
                1f,
                1f,
                0.8f,
                0.8f,
                0.8f
            };
            public double compute( double[] a, double[] b ) {
                double sum = 0;
                for ( int i = 0; i < a.length; i++ )
                    sum += Math.pow( (a[i] - b[i]) * weights[i], 2 );
                return Math.sqrt(sum);
            }
        });
        
        int size = (int) Math.sqrt(input.getTileWidth() * input.getTileHeight());
        DBSCANClusterer<ImagePoint> clusterer2 = new DBSCANClusterer<ImagePoint>(5, size, new DistanceMeasure() {
            private double[] weights = new double[] {
                1f,
                1f,
                0.8f,
                0.8f,
                0.8f
            };
            public double compute( double[] a, double[] b ) {
                System.out.println("Called");
                double sum = 0;
                for ( int i = 0; i < a.length; i++ )
                    sum += Math.pow( (a[i] - b[i]) * weights[i], 2 );
                return Math.sqrt(sum);
            }
        });

         MultiKMeansPlusPlusClusterer<ImagePoint> clusterer3 = new MultiKMeansPlusPlusClusterer( clusterer, 100 );

        return new ImageClustering(input, clusterAdapter( clusterer3.cluster(points) ));
    }

    private static List<Cluster<ImagePoint>> clusterAdapter( List a ) {
        List<Cluster<ImagePoint>> out = new ArrayList<>( a.size() );
        for ( Object o : a ) {
            if ( o instanceof Cluster )
                out.add( (Cluster<ImagePoint>) o );
        }
        return out;
    }


    private static BufferedImage imageDecoder( File file ) {
        if ( file.getName().endsWith("jpg") || file.getName().endsWith("jpeg") ) {
            try {
                return (new JpegDecoder()).decode(new ByteSourceFile( file )); 
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to decode: " + file);
            }
        }
        throw new IllegalArgumentException("Only JPEGs are supported");
    }


    public static class ImagePoint implements Clusterable {

        private double[] data;

        public ImagePoint( int x, int y, int r, int g, int b ) {
            data = new double[] { (double) x, (double) y, (double) r, (double) g, (double) b };
        }

        public double[] getPoint() { return data; }

        public int getX() { return (int) data[0]; }
        public int getY() { return (int) data[1]; }
        public int getR() { return (int) data[2]; }
        public int getG() { return (int) data[3]; }
        public int getB() { return (int) data[4]; }
    }


}


