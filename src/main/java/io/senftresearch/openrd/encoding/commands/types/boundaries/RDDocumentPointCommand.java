package io.senftresearch.openrd.encoding.commands.types.boundaries;

import io.senftresearch.openrd.RDBoundingBox;
import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDDocumentPointCommand extends AbstractRDCommand {
    private final RDBoundingBox boundingBox;

    public RDDocumentPointCommand(RDBoundingBox boundingBox){
        this.boundingBox = boundingBox;
    }

    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_DOCUMENT_MIN_POINT.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(boundingBox.topLeft().x(), RDEncodingType.NUMBER),
                new RDCommandArg(boundingBox.topLeft().y(), RDEncodingType.NUMBER),
                new RDCommandArg(RDCommandHex.RD_DOCUMENT_MAX_POINT.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(boundingBox.bottomRight().x(), RDEncodingType.NUMBER),
                new RDCommandArg(boundingBox.bottomRight().y(), RDEncodingType.NUMBER)
        };
    }
}
