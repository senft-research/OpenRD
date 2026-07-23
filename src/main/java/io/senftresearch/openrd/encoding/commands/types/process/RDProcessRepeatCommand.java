package io.senftresearch.openrd.encoding.commands.types.process;

import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;

public class RDProcessRepeatCommand extends AbstractRDCommand {
    private double repeatFieldOne;
    private double repeatFieldTwo;

    public RDProcessRepeatCommand(double repeatFieldOne, double repeatFieldTwo){
        this.repeatFieldOne = repeatFieldOne;
        this.repeatFieldTwo = repeatFieldTwo;
    }

    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_PROCESS_REPEAT.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(repeatFieldOne, RDEncodingType.NUMBER),
                new RDCommandArg(repeatFieldTwo, RDEncodingType.NUMBER)
        };
    }
}
