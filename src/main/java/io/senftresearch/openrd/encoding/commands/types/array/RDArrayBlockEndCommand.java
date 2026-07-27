package io.senftresearch.openrd.encoding.commands.types.array;

import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDArrayBlockEndCommand extends AbstractRDCommand {
    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_ARRAY_END.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(RDCommandHex.RD_BLOCK_END.getHexCode(), RDEncodingType.HEX)
        };
    }
}
