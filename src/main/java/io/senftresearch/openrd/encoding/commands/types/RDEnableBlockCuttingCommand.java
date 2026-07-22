package io.senftresearch.openrd.encoding.commands.types;

import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDEnableBlockCuttingCommand extends AbstractRDCommand {


    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{new RDCommandArg("f1 02 00", RDEncodingType.HEX)};
    }
}
