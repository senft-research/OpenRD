package io.senftresearch.openrd.encoding.commands.types.process;

import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;

public class RDProcessCommand extends AbstractRDCommand {

    private RDCommandHex commandHex;
    private final RDEncodingType encodingTypeArray = RDEncodingType.HEX;

    public RDProcessCommand(ProcessType processType){
        switch (processType){
            case START -> commandHex = RDCommandHex.RD_START_PROCESS;
            case STOP -> commandHex = RDCommandHex.RD_STOP_PROCESS;
            case PAUSE -> commandHex = RDCommandHex.RD_PAUSE_PROCESS;
            case RESTORE -> commandHex = RDCommandHex.RD_RESTORE_PROCESS;
        }
    }


    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{new RDCommandArg(this.commandHex.getHexCode(), this.encodingTypeArray)};
    }
}
