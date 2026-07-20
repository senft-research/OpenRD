package io.senftresearch.openrd.encoding.commands.types;

import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;

public class RDEnableBlockCuttingCommand extends AbstractRDCommand {

    @Override
    public Object[] getCommandArgs() {
        return new Object[]{"f1 02 00"};
    }
}
