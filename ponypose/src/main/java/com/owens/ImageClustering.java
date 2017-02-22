package com.owens;


import org.apache.commons.math4.ml.clustering.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.ArrayList;


public class ImageClustering implements BufferedImageOp {

    private List<Cluster<ImageClusterer.ImagePoint>> clusters;
    private BufferedImage source;

    public ImageClustering( BufferedImage source, List<Cluster<ImageClusterer.ImagePoint>> clusters ) {
        this.clusters = clusters;
        this.source = source;
    }


    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
        return src;
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        dest = src;
        Random rand = new Random();
        System.out.println("Cluster Count: " + clusters.size());
        for ( int c = 0; c < clusters.size(); c++ ) {
            int color = rand.nextInt() | 0xFF000000;
            for ( ImageClusterer.ImagePoint ip : clusters.get(c).getPoints() ) {
                dest.setRGB( ip.getX(), ip.getY(), color );
            }
        }
        return dest;
    }

    public Rectangle2D getBounds2D(BufferedImage src) {
        return new Rectangle(0, 0, src.getTileWidth(), src.getTileHeight() );
    }

    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
        dstPt = srcPt;
        return dstPt;
    }

    public RenderingHints getRenderingHints() {
        return new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }


    public BufferedImage getSourceImage() { return source; }

}
