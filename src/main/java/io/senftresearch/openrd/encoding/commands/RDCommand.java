package io.senftresearch.openrd.encoding.commands;

import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public interface RDCommand {
    RDCommandArg[] getArgs();
}
