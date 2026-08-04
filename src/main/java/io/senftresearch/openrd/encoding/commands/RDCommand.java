package io.senftresearch.openrd.encoding.commands;

import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

/**
 * Interface representing the command to be sent to the Rudia Controller.
 */
public interface RDCommand {
    /**
     * Gets the various args of the command to be encoded, including its hex values.
     * @return The command's args.
     */
    RDCommandArg[] getArgs();
}
