package io.senftresearch.openrd.encoding.commands;

import java.util.ArrayList;
import java.util.List;

public class RDCommandSet {
    private List<RDCommand> commands = new ArrayList<>();

    private RDCommandSet(){}

    public List<RDCommand> getCommands() {
        return commands;
    }

    public void setCommands(List<RDCommand> commands) {
        this.commands = commands;
    }

    public static class RDCommandSetBuilder{
        private List<RDCommand> commands = new ArrayList<>();

        public RDCommandSet build(){
            RDCommandSet commandSet = new RDCommandSet();
            commandSet.commands.addAll(commands);
            return commandSet;
        }

        public RDCommandSetBuilder withCommand(RDCommand command){
            this.commands.add(command);
            return this;
        }
    }

}
