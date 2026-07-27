package io.senftresearch.openrd.encoding.commands.types.laser;

import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;
//TODO I am sure I can make this and the Relative Move Command into children of some parent class to reduce repeated code...

public class RDLaserRelativeCutCommand extends AbstractRDCommand {
    private LaserTranslationType translationType;
    private int xCoord;
    private int yCoord;
    private RDLaserRelativeCutCommand(){}


    @Override
    public RDCommandArg[] getArgs() {
        return switch (translationType){
            case COORDINATE -> new RDCommandArg[]{
                    new RDCommandArg(RDCommandHex.RD_RELATIVE_CUT.getHexCode(), RDEncodingType.HEX),
                    new RDCommandArg(xCoord, RDEncodingType.REL_COORD),
                    new RDCommandArg(yCoord, RDEncodingType.REL_COORD)
            };
            case HORIZONTAL -> new RDCommandArg[]{
                    new RDCommandArg(RDCommandHex.RD_RELATIVE_CUT_HORIZONTAL.getHexCode(), RDEncodingType.HEX),
                    new RDCommandArg(xCoord, RDEncodingType.REL_COORD)
            };
            case VERTICAL -> new RDCommandArg[]{
                    new RDCommandArg(RDCommandHex.RD_RELATIVE_CUT_VERTICAL.getHexCode(), RDEncodingType.HEX),
                    new RDCommandArg(yCoord, RDEncodingType.REL_COORD)
            };
        };
    }
    public static class RDLaserRelativeCutCommandBuilder extends AbstractRDCommandBuilder<RDLaserRelativeCutCommandBuilder>{
        private LaserTranslationType translationType;
        private Integer xCoord;
        private Integer yCoord;

        public RDLaserRelativeCutCommandBuilder withTranslationType(LaserTranslationType translationType){
            this.translationType = translationType;
            return this;
        }

        public RDLaserRelativeCutCommandBuilder withXCoord(int xCoord){
            this.xCoord = xCoord;
            return this;
        }

        public RDLaserRelativeCutCommandBuilder withYCoord(int yCoord){
            this.yCoord = yCoord;
            return this;
        }

        @Override
        protected AbstractRDCommand create() {
            RDLaserRelativeCutCommand command = new RDLaserRelativeCutCommand();
            command.translationType = translationType;
            if(xCoord != null) command.xCoord = xCoord;
            if(yCoord != null) command.yCoord = yCoord;
            return command;
        }


        @Override
        protected boolean requiredArgsInitialized() {
            if(this.translationType == null){
                return false;
            }

            return switch(this.translationType){
                case COORDINATE -> this.xCoord != null && this.yCoord != null;
                case HORIZONTAL -> this.xCoord != null;
                case VERTICAL -> this.yCoord != null;
            };
        }
    }
}
