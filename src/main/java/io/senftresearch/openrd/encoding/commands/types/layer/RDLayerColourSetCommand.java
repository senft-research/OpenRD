package io.senftresearch.openrd.encoding.commands.types.layer;

import io.senftresearch.openrd.RDColour;
import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDLayerColourSetCommand extends AbstractRDCommand {

    private final RDColour colour;
    private final int layerNumber;

    public RDLayerColourSetCommand(RDColour colour, int layerNumber){
        this.colour = colour;
        this.layerNumber = layerNumber;
    }

    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_LAYER_COLOUR_SET, RDEncodingType.HEX),
                new RDCommandArg(this.layerNumber, RDEncodingType.BYTE),
                new RDCommandArg(this.colour.getRGBArray(), RDEncodingType.COLOUR)
        };
    }
}
