package io.senftresearch.openrd.encoding.commands.types;

import io.senftresearch.openrd.RDPoint;
import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

import java.util.Arrays;
import java.util.stream.Stream;

public class RDPartPowerCommand extends AbstractRDCommand {
    private RDPoint powerOne;
    private RDPoint powerTwo;
    private RDPoint powerThree;
    private RDPoint powerFour;
    private int layerNumber;

    @Override
    public RDCommandArg[] getArgs() {
        return Stream.of(
                        createPowerArgs(RDCommandHex.RD_PART_POWER_ONE_MIN, RDCommandHex.RD_PART_POWER_ONE_MAX, powerOne),
                        createPowerArgs(RDCommandHex.RD_PART_POWER_TWO_MIN, RDCommandHex.RD_PART_POWER_TWO_MAX, powerTwo),
                        createPowerArgs(RDCommandHex.RD_PART_POWER_THREE_MIN, RDCommandHex.RD_PART_POWER_THREE_MAX, powerThree),
                        createPowerArgs(RDCommandHex.RD_PART_POWER_FOUR_MIN, RDCommandHex.RD_PART_POWER_FOUR_MAX, powerFour)
                )
                .flatMap(Arrays::stream)
                .toArray(RDCommandArg[]::new);
    }

    private RDCommandArg[] createPowerArgs(RDCommandHex minHex,
                                           RDCommandHex maxHex,
                                           RDPoint power) {
        return new RDCommandArg[] {
                new RDCommandArg(minHex.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(layerNumber, RDEncodingType.BYTE),
                new RDCommandArg(power.x(), RDEncodingType.PERCENT),

                new RDCommandArg(maxHex.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(layerNumber, RDEncodingType.BYTE),
                new RDCommandArg(power.y(), RDEncodingType.PERCENT)
        };
    }

    public static class RDPartPowerCommandBuilder extends AbstractRDCommandBuilder<RDPartPowerCommandBuilder>{
        private RDPoint powerOne;
        private RDPoint powerTwo;
        private RDPoint powerThree;
        private RDPoint powerFour;
        private int layerNumber;

        @Override
        protected AbstractRDCommand create() {
            RDPartPowerCommand command = new RDPartPowerCommand();
            command.powerOne = this.powerOne;
            command.powerTwo = this.powerTwo;
            command.powerThree = this.powerThree;
            command.powerFour = this.powerFour;
            command.layerNumber = layerNumber;
            return command;
        }

        @Override
        protected boolean requiredArgsInitialized() {
            return powerOne != null && powerTwo != null && powerThree != null && powerFour != null;
        }

        public RDPartPowerCommandBuilder withPower(RDPoint power, int powerNumber){
            switch(powerNumber){
                case 1:
                    this.powerOne = power;
                    break;
                case 2:
                    this.powerTwo = power;
                    break;
                case 3:
                    this.powerThree = power;
                    break;
                case 4:
                    this.powerFour = power;
                    break;
                default:
                    throw new IllegalArgumentException("The power value (" + powerNumber + ") is not a valid power!");
            }
            return this;
        }

        public RDPartPowerCommandBuilder withLayerNumber(int layerNumber){
            this.layerNumber = layerNumber;
            return this;
        }
    }
}
