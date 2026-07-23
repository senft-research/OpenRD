package io.senftresearch.openrd.encoding.commands.types.boundaries;

import io.senftresearch.openrd.RDBoundingBox;
import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDProcessBoundingBoxCommand extends AbstractRDCommand {

    private RDBoundingBox boundingBox;

    public RDProcessBoundingBoxCommand(RDBoundingBox boundingBox){
        this.boundingBox = boundingBox;
    }


    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_PROCESS_TOP_LEFT.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(boundingBox.topLeft().x(), RDEncodingType.NUMBER),
                new RDCommandArg(boundingBox.topLeft().y(), RDEncodingType.NUMBER),
                new RDCommandArg(RDCommandHex.RD_PROCESS_BOTTOM_RIGHT.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(boundingBox.bottomRight().x(), RDEncodingType.NUMBER),
                new RDCommandArg(boundingBox.bottomRight().y(), RDEncodingType.NUMBER)
        };
    }
}
