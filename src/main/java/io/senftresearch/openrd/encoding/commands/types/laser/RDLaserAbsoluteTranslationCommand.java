package io.senftresearch.openrd.encoding.commands.types.laser;

import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDLaserAbsoluteTranslationCommand extends AbstractRDCommand {

    private final int xCoord;
    private final int yCoord;
    private final RDCommandHex commandHex;


    public RDLaserAbsoluteTranslationCommand(int xCoord, int yCoord, boolean isTravel){
        commandHex = isTravel ? RDCommandHex.RD_ABSOLUTE_MOVE : RDCommandHex.RD_ABSOLUTE_CUT;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(commandHex.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(xCoord, RDEncodingType.NUMBER),
                new RDCommandArg(yCoord, RDEncodingType.NUMBER)
        };
    }
}
