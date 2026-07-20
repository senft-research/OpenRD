package io.senftresearch.openrd.encoding.commands;

public class RDPartInitCommand extends AbstractRDCommand{
    private final RDCommandHex commandHex = RDCommandHex.RD_INIT_COMMAND;
    private int maxLayerArg;
    @Override
    public Object[] getCommandArgs() {
        return new Object[]{commandHex.getHexCode(), maxLayerArg};
    }

    public static class RDPartInitCommandBuilder extends AbstractRDCommandBuilder<RDPartInitCommandBuilder>{
        private Integer maxLayerArg;
        @Override
        protected AbstractRDCommand create() {
            RDPartInitCommand command = new RDPartInitCommand();
            command.maxLayerArg = maxLayerArg;
            return command;

        }

        @Override
        protected boolean requiredArgsInitialized() {
            return maxLayerArg != null;
        }

        public RDPartInitCommandBuilder withMaxLayerArg(int maxLayerArg){
            this.maxLayerArg = maxLayerArg;
            return this;
        }
    }

}
