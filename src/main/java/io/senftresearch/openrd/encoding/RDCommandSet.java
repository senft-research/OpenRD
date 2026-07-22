package io.senftresearch.openrd.encoding;

import io.senftresearch.openrd.encoding.commands.RDCommand;

import java.util.ArrayList;
import java.util.List;

public class RDCommandSet {
    private List<RDCommand> commands = new ArrayList<>();

    public List<RDCommand> getCommands() {
        return commands;
    }

    public void setCommands(List<RDCommand> commands) {
        this.commands = commands;
    }

}
