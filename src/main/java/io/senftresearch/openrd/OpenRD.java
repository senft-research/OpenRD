package io.senftresearch.openrd;

import io.senftresearch.openrd.data.RDBodyData;
import io.senftresearch.openrd.data.RDHeaderData;
import io.senftresearch.openrd.data.RDTrailerData;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//TODO need to clean up the logic in this class (taking out the odo calcs, the scramble logic, the bounding box calcs etc)
public class OpenRD {
    private static OpenRD instance;
    public static OpenRD getInstance(){
        if(instance == null){
            instance = new OpenRD();
        }
        return instance;
    }
    private OpenRD(){
        this.forceAbs = FORCE_ABS;
    }
    private List<RDLayer> layers = new ArrayList<>();
    private RDOdometer odometer;
    private final int FORCE_ABS = 100;
    private int forceAbs;
    private RDBoundingBox globalBoundingBox;
    private RDHeaderData headerData;
    private RDBodyData bodyData;
    private RDTrailerData trailerData;

    public void write(boolean shouldScramble, String filePath){
        if(layers.isEmpty()){
            throw new RuntimeException("Layers not initialised!");
        }
        layers = layers.stream()
                .map(layer -> layer.boundingBox() == null
                        ? new RDLayer(layer.paths(), layer.speed(), layer.power(), layer.colour(), layer.frequency(), boundingbox(layer.paths()))
                        : layer)
                .toList();
        double[] odo = new double[]{0.0, 0.0};
        for(RDLayer layer : layers){
            double[] odoToAdd = odoMeter(layer.paths(), false);
            odo[0] += odoToAdd[0];
            odo[1] += odoToAdd[1];
        }

        headerData = new RDHeaderData(layers, globalBoundingBox);
        bodyData = new RDBodyData(layers, globalBoundingBox);
        trailerData = new RDTrailerData(odo);

        if(this.headerData == null){
            throw new RuntimeException("Header not initialised!");
        }
        if(this.bodyData == null){
            throw new RuntimeException("Body not initialised!");
        }

        if(this.trailerData == null){
            throw  new RuntimeException("Trailer not initialised!");
        }

        try(FileOutputStream fd = new FileOutputStream(filePath)){
            ByteArrayOutputStream combined = new ByteArrayOutputStream();
            headerData.getData().writeTo(combined);
            bodyData.getData().writeTo(combined);
            trailerData.getData().writeTo(combined);

            if(shouldScramble){
                //scrambleStreamInPlace(combined);
            }
            combined.writeTo(fd);
        }
        catch (IOException e) {

        }

    }
    public void set(RDLayer layer){
        set(layer, this.FORCE_ABS);
    }
    public void set(RDLayer layer, int forceAbs){
        this.forceAbs = forceAbs;
        if (layer == null) {
            throw new IllegalArgumentException("Layer cannot be null");
        }

        if (layer.paths() == null) {
            throw new IllegalArgumentException("Paths cannot be null");
        }
        if (layer.power() == null) {
            throw new IllegalArgumentException("Power cannot be null");
        }
        if (layer.colour() == null) {
            throw new IllegalArgumentException("Colour cannot be null");
        }

        this.layers.add(layer);
    }

    public RDBoundingBox boundingbox(List<List<RDPoint>> paths) {
        if (paths == null || paths.isEmpty()) {
            throw new IllegalArgumentException("No points provided to calculate bounding box.");
        }

        List<RDPoint> points = new ArrayList<>();
        paths.forEach(points::addAll);

        RDPoint firstPoint = points.get(0);
        int xmin = firstPoint.x();
        int xmax = firstPoint.x();
        int ymin = firstPoint.y();
        int ymax = firstPoint.y();

        for (RDPoint point : points) {
            if (point.x() > xmax) xmax = point.x();
            if (point.x() < xmin) xmin = point.x();
            if (point.y() > ymax) ymax = point.y();
            if (point.y() < ymin) ymin = point.y();
        }

        return new RDBoundingBox(new RDPoint(xmin, ymin), new RDPoint(xmax, ymax));
    }
    public int getForceAbs() {
        return forceAbs;
    }

    public void setForceAbs(int forceAbs) {
        this.forceAbs = forceAbs;
    }
    public void scrambleStreamInPlace(ByteArrayOutputStream existingStream) {
        if (existingStream == null || existingStream.size() == 0) return;

        byte[] originalBytes = existingStream.toByteArray();

        existingStream.reset();

        for (byte b : originalBytes) {
            int unsignedByte = b & 0xFF;
            existingStream.write(scramble(unsignedByte));
        }
    }

    private int scramble(int b) {
        int fb = b & 0x80;
        int lb = b & 1;

        int resB = b - fb - lb;

        resB |= (lb << 7);
        resB |= (fb >>> 7);

        resB ^= 0x88;
        resB += 1;

        if (resB > 0xFF) {
            resB -= 0x100;
        }

        return resB;
    }

    private double[] odoMeter(List<List<RDPoint>> paths, boolean returnHome){
        return odometer(paths, new double[]{0.0,0.0}, returnHome);
    }
    private double[] odometer(List<List<RDPoint>> paths, double[] init, boolean returnHome){
        if(paths == null || paths.isEmpty()){
            return null;
        }
        double cutDistance = 0;
        double travelDistance = 0;
        boolean travelling;
        double[] xy = init.clone();
        for(List<RDPoint> path : paths){
            travelling = true;
            for(RDPoint point : path){
                double[] pointArray = new double[]{point.x(),point.y()};
                if(travelling){
                    travelDistance += distXY(xy, pointArray);
                    xy=pointArray;
                    travelling = false;
                }
                else{
                    cutDistance += distXY(xy, pointArray);
                    xy = pointArray;
                }
            }
        }
        if(returnHome){
            travelDistance += distXY(xy, init);
        }

        return new double[]{cutDistance, travelDistance};


    }

    private double distXY(double[] point1, double[] point2){
        double dx = point2[0] - point1[0];
        double dy = point2[1] - point1[1];
        return Math.sqrt((dx*dx)+(dy*dy));
    }



}
