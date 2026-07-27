package io.senftresearch.openrd.encoding.commands.types.part;

import io.senftresearch.openrd.RDBoundingBox;
import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDPartPointsExCommand extends AbstractRDCommand {
    private final RDBoundingBox boundingBox;
    private final int layerNumber;

    public RDPartPointsExCommand(RDBoundingBox boundingBox, int layerNumber){
        this.boundingBox = boundingBox;
        this.layerNumber = layerNumber;
    }

    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_PART_MIN_POINT_EX.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(this.layerNumber, RDEncodingType.BYTE),
                new RDCommandArg(this.boundingBox.topLeft().x(), RDEncodingType.NUMBER),
                new RDCommandArg(this.boundingBox.topLeft().y(), RDEncodingType.NUMBER),
                new RDCommandArg(RDCommandHex.RD_PART_MAX_POINT_EX.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(this.layerNumber, RDEncodingType.BYTE),
                new RDCommandArg(this.boundingBox.bottomRight().x(), RDEncodingType.NUMBER),
                new RDCommandArg(this.boundingBox.bottomRight().y(), RDEncodingType.NUMBER)
        };
    }
}
