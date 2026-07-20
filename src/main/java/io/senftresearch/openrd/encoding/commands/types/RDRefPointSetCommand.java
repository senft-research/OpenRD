package io.senftresearch.openrd.encoding.commands.types;

import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;

public class RDRefPointSetCommand extends AbstractRDCommand {

    @Override
    public Object[] getCommandArgs() {
        return new Object[]{RDCommandHex.RD_REF_POINT_SET.getHexCode()};
    }
}
