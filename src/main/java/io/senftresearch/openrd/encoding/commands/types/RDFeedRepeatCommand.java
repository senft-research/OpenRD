package io.senftresearch.openrd.encoding.commands.types;

import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDFeedRepeatCommand extends AbstractRDCommand {
    private double feedRepeatOne;
    private double feedRepeatTwo;

    public RDFeedRepeatCommand(double feedRepeatOne, double feedRepeatTwo){
        this.feedRepeatOne = feedRepeatOne;
        this.feedRepeatTwo = feedRepeatTwo;
    }

    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{
                new RDCommandArg(RDCommandHex.RD_FEED_REPEAT.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(feedRepeatOne, RDEncodingType.NUMBER),
                new RDCommandArg(feedRepeatTwo, RDEncodingType.NUMBER)
        };
    }
}
