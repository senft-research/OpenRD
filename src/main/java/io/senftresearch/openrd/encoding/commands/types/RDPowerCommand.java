package io.senftresearch.openrd.encoding.commands.types;

import io.senftresearch.openrd.RDPoint;
import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

import java.util.Arrays;
import java.util.stream.Stream;

public class RDPowerCommand extends AbstractRDCommand {
    private RDPoint powerOne;
    private RDPoint powerTwo;
    private RDPoint powerThree;
    private RDPoint powerFour;

    @Override
    public RDCommandArg[] getArgs() {
        return Stream.of(
                        createPowerArgs(RDCommandHex.RD_POWER_MIN_ONE, RDCommandHex.RD_POWER_MAX_ONE, powerOne),
                        createPowerArgs(RDCommandHex.RD_POWER_MIN_TWO, RDCommandHex.RD_POWER_MAX_TWO, powerTwo),
                        createPowerArgs(RDCommandHex.RD_POWER_MIN_THREE, RDCommandHex.RD_POWER_MAX_THREE, powerThree),
                        createPowerArgs(RDCommandHex.RD_POWER_MIN_FOUR, RDCommandHex.RD_POWER_MAX_FOUR, powerFour)
                )
                .flatMap(Arrays::stream)
                .toArray(RDCommandArg[]::new);
    }

    private RDCommandArg[] createPowerArgs(RDCommandHex minHex,
                                           RDCommandHex maxHex,
                                           RDPoint power) {
        return new RDCommandArg[] {
                new RDCommandArg(minHex.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(power.x(), RDEncodingType.PERCENT),

                new RDCommandArg(maxHex.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(power.y(), RDEncodingType.PERCENT)
        };
    }

    public static class RDPowerCommandBuilder extends AbstractRDCommandBuilder<RDPowerCommandBuilder>{
        private RDPoint powerOne;
        private RDPoint powerTwo;
        private RDPoint powerThree;
        private RDPoint powerFour;

        @Override
        protected AbstractRDCommand create() {
            RDPowerCommand command = new RDPowerCommand();
            command.powerOne = this.powerOne;
            command.powerTwo = this.powerTwo;
            command.powerThree = this.powerThree;
            command.powerFour = this.powerFour;
            return command;
        }

        @Override
        protected boolean requiredArgsInitialized() {
            return powerOne != null && powerTwo != null && powerThree != null && powerFour != null;
        }

        public RDPowerCommandBuilder withPower(RDPoint power, int powerNumber){
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
    }
}
