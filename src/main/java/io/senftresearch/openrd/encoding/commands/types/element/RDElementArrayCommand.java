package io.senftresearch.openrd.encoding.commands.types.element;

import io.senftresearch.openrd.RDPoint;
import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDElementArrayCommand extends AbstractRDCommand {
    private RDPoint absCoord;

    public RDElementArrayCommand(RDPoint absCoord){
        this.absCoord = absCoord;
    }
    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_ELEMENT_ARRAY.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(absCoord.x(), RDEncodingType.NUMBER),
                new RDCommandArg(absCoord.y(), RDEncodingType.NUMBER)
        };
    }
}
