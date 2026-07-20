package io.senftresearch.openrd.encoding.commands.types;

import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;

public class RDRefPointModeCommand extends AbstractRDCommand {
    private int refPointMode = 0;

    @Override
    public Object[] getCommandArgs() {
        RDCommandHex commandHex = switch (refPointMode) {
            case 1 -> RDCommandHex.RD_REF_POINT_MODE_ONE;
            case 2 -> RDCommandHex.RD_REF_POINT_MODE_TWO;
            default -> RDCommandHex.RD_REF_POINT_MODE_ZERO;
        };

        return new Object[]{commandHex.getHexCode()};
    }

    public static class RDRefPointModeCommandBuilder extends AbstractRDCommandBuilder<RDRefPointModeCommandBuilder>{
        private int refPointMode;


        @Override
        protected AbstractRDCommand create() {
            RDRefPointModeCommand command = new RDRefPointModeCommand();
            command.refPointMode = refPointMode;
            return command;
        }

        @Override
        protected boolean requiredArgsInitialized() {
            return true;
        }

        public RDRefPointModeCommandBuilder withPointMode(int refPointMode){
            this.refPointMode = refPointMode;
            return this;
        }
    }
}
