package io.senftresearch.openrd.encoding.commands.types.layer;

import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDLayerSpeedCommand extends AbstractRDCommand {
    private int layerNumber;
    private int speed;

    public RDLayerSpeedCommand(int layerNumber, int speed){
        this.layerNumber = layerNumber;
        this.speed = speed;
    }

    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_LAYER_SPEED.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(layerNumber, RDEncodingType.BYTE),
                new RDCommandArg(speed, RDEncodingType.NUMBER)
        };
    }
}
