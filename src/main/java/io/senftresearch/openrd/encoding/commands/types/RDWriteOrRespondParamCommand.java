package io.senftresearch.openrd.encoding.commands.types;

import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDWriteOrRespondParamCommand extends AbstractRDCommand {
    private final double param;

    public RDWriteOrRespondParamCommand(double param){
        this.param = param;
    }

    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_WRITE_OR_RESPOND_PARAM.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(param, RDEncodingType.NUMBER),
                new RDCommandArg(param, RDEncodingType.NUMBER)
        };
    }
}
