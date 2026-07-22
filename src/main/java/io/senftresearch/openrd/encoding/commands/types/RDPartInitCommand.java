package io.senftresearch.openrd.encoding.commands.types;

import io.senftresearch.openrd.encoding.EncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDPartInitCommand extends AbstractRDCommand {
    private final RDCommandHex commandHex = RDCommandHex.RD_INIT_COMMAND;
    private int maxLayerArg;

    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(commandHex.getHexCode(), EncodingType.HEX),
                new RDCommandArg(maxLayerArg, EncodingType.NUMBER)};
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
