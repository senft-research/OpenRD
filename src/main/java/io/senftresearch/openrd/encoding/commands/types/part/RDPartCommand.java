package io.senftresearch.openrd.encoding.commands.types.part;

import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDPartCommand extends AbstractRDCommand {
    private final int layerNumber;

    public RDPartCommand(int layerNumber){
        this.layerNumber = layerNumber;
    }
    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_PART.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(layerNumber, RDEncodingType.BYTE)
        };
    }
}
