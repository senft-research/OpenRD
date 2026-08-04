package io.senftresearch.openrd.data;

import io.senftresearch.openrd.encoding.RDEncoder;
import io.senftresearch.openrd.encoding.commands.RDCommandSet;
import io.senftresearch.openrd.encoding.commands.types.RDEndFileCommand;
import io.senftresearch.openrd.encoding.commands.types.RDWriteOrRespondParamCommand;
import io.senftresearch.openrd.encoding.commands.types.array.RDArrayBlockEndCommand;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
//TODO need to investigate why Trailer was separated from the Body with previous API authors.
public class RDTrailerData implements RDData{

    private ByteArrayOutputStream trailerData;

    public RDTrailerData(){
        this(new double[]{0.0,0.0});
    }
    public RDTrailerData(double[] odo){
        ByteArrayOutputStream trailerDataStream = new ByteArrayOutputStream();
        try{
            RDCommandSet trailerSet = new RDCommandSet.RDCommandSetBuilder()
                    .withCommand(new RDArrayBlockEndCommand())
                    .withCommand(new RDWriteOrRespondParamCommand(odo[0]*0.001))
                    .withCommand(new RDEndFileCommand())
                    .build();
            trailerDataStream.write(RDEncoder.encode(trailerSet));
            this.trailerData = trailerDataStream;
        }
        catch (IOException e){
            throw new RuntimeException("IOException thrown during writing of Trailer Data");
        }

    }
    @Override
    public ByteArrayOutputStream getData() {
        return this.trailerData;
    }
}
