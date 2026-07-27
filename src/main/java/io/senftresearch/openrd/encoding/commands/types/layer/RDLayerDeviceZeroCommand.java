package io.senftresearch.openrd.encoding.commands.types.layer;

import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDLayerDeviceZeroCommand extends AbstractRDCommand {
    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_LAYER_DEVICE_ZERO.getHexCode(), RDEncodingType.HEX)
        };
    }
}
