package io.senftresearch.openrd.encoding.commands.types;

import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDPenOffsetCommand extends AbstractRDCommand {

    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_PEN_OFFSET.getHexCode(), RDEncodingType.HEX)
        };
    }
}
