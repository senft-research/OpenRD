package io.senftresearch.openrd.encoding.commands.types.array;

import io.senftresearch.openrd.RDPoint;
import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDArrayAddCommand extends AbstractRDCommand {
    private RDPoint point;

    public RDArrayAddCommand(RDPoint point){
        this.point = point;
    }

    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_ARRAY_ADD.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(point.x(), RDEncodingType.NUMBER),
                new RDCommandArg(point.y(), RDEncodingType.NUMBER)
        };
    }
}
