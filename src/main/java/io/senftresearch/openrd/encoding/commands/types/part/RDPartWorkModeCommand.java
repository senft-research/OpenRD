package io.senftresearch.openrd.encoding.commands.types.part;

import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDPartWorkModeCommand extends AbstractRDCommand {
    private final int layerNumber;
    // Dev note: Added as a field in-case other work modes actually mean something in future.
    private final int workMode = 0;

    public RDPartWorkModeCommand(int layerNumber){
        this.layerNumber = layerNumber;
    }
    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_PART_WORK_MODE.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(layerNumber, RDEncodingType.BYTE),
                new RDCommandArg(workMode, RDEncodingType.BYTE)
        };
    }
}
