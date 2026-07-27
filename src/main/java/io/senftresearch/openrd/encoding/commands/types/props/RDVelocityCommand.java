package io.senftresearch.openrd.encoding.commands.types.props;

import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDVelocityCommand extends AbstractRDCommand {
    private final int speed;

    public RDVelocityCommand(int speed){
        this.speed = speed;
    }

    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_VELOCITY.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(speed, RDEncodingType.NUMBER)
        };
    }
}
