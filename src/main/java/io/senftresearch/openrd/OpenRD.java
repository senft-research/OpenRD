package io.senftresearch.openrd;

import io.senftresearch.openrd.data.RDBodyData;
import io.senftresearch.openrd.data.RDHeaderData;
import io.senftresearch.openrd.data.RDTrailerData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

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
    private List<RDLayer> layers;
    private RDOdometer odometer;
    private final int FORCE_ABS = 100;
    private int forceAbs;
    private RDBoundingBox globalBoundingBox;
    private RDHeaderData headerData;
    private RDBodyData bodyData;
    private RDTrailerData trailerData;

    public void write(boolean shouldScramble){
        if(this.headerData == null){
            throw new RuntimeException("Header not initialised!");
        }
        if(this.bodyData == null){
            throw new RuntimeException("Body not initialised!");
        }

        if(this.trailerData == null){
            throw  new RuntimeException("Trailer not initialised!");
        }

        try{
            ByteArrayOutputStream combined = new ByteArrayOutputStream();
            headerData.getData().writeTo(combined);
            bodyData.getData().writeTo(combined);
            trailerData.getData().writeTo(combined);

            if(shouldScramble){

            }
        }
        catch (IOException e){

        }

    }

    public int getForceAbs() {
        return forceAbs;
    }

    public void setForceAbs(int forceAbs) {
        this.forceAbs = forceAbs;
    }


}
