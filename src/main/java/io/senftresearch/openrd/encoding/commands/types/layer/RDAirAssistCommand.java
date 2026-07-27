package io.senftresearch.openrd.encoding.commands.types.layer;

import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDAirAssistCommand extends AbstractRDCommand {
    private final boolean airAssistOn;

    public RDAirAssistCommand(boolean airAssistOn){
        this.airAssistOn = airAssistOn;
    }
    @Override
    public RDCommandArg[] getArgs() {
        RDCommandHex commandHex = airAssistOn ? RDCommandHex.RD_AIR_ASSIST_ON : RDCommandHex.RD_AIR_ASSIST_OFF;
        return new RDCommandArg[]{
                new RDCommandArg(commandHex.getHexCode(), RDEncodingType.HEX)
        };
    }
}
