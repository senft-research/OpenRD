package io.senftresearch.openrd.data;

import io.senftresearch.openrd.encoding.RDEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RDTrailerData implements RDData{

    private ByteArrayOutputStream trailerData;

    public RDTrailerData(){
        this(new double[]{0.0,0.0});
    }
    public RDTrailerData(double[] odo){
        ByteArrayOutputStream trailerDataStream = new ByteArrayOutputStream();
        try{
            trailerDataStream.write(RDEncoder.encode("-nn-", "eb e7 00 da 01 06 20", odo[0]*0.001, odo[0]*0.001, "d7"));
            this.trailerData = trailerDataStream;
        }
        catch (IOException e){

        }

    }
    @Override
    public ByteArrayOutputStream getData() {
        return this.trailerData;
    }
}
