package io.senftresearch.openrd.encoding.commands.types.element;

import io.senftresearch.openrd.RDPoint;
import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDElementArrayAddCommand extends AbstractRDCommand {
    private RDPoint absCoord;

    public RDElementArrayAddCommand(RDPoint absCoord){
        this.absCoord = absCoord;
    }
    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_ELEMENT_ARRAY_ADD.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(absCoord.x(), RDEncodingType.NUMBER),
                new RDCommandArg(absCoord.y(), RDEncodingType.NUMBER)
        };
    }
}
