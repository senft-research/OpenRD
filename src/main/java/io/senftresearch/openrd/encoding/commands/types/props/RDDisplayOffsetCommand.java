package io.senftresearch.openrd.encoding.commands.types.props;

import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDDisplayOffsetCommand extends AbstractRDCommand {
    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_DISPLAY_OFFSET.getHexCode(), RDEncodingType.HEX)
        };
    }
}
