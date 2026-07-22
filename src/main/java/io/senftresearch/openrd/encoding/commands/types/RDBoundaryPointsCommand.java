package io.senftresearch.openrd.encoding.commands.types;

import io.senftresearch.openrd.RDPoint;
import io.senftresearch.openrd.encoding.EncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDBoundaryPointsCommand extends AbstractRDCommand {
    private RDPoint minPoint;
    private RDPoint maxPoint;


    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{

                new RDCommandArg(RDCommandHex.RD_MIN_ARRAY_POINT.getHexCode(), EncodingType.HEX),
                new RDCommandArg(minPoint.x(), EncodingType.NUMBER),
                new RDCommandArg(minPoint.y(), EncodingType.NUMBER),
                new RDCommandArg(RDCommandHex.RD_MAX_ARRAY_POINT.getHexCode(), EncodingType.HEX),
                new RDCommandArg(maxPoint.x(), EncodingType.NUMBER),
                new RDCommandArg(maxPoint.y(), EncodingType.NUMBER)
        };
    }


    public static class RDBoundaryPointsCommandBuilder extends AbstractRDCommandBuilder<RDBoundaryPointsCommandBuilder> {
        private RDPoint minPoint;
        private RDPoint maxPoint;


        @Override
        protected AbstractRDCommand create() {
            RDBoundaryPointsCommand command = new RDBoundaryPointsCommand();
            command.minPoint = minPoint;
            command.maxPoint = maxPoint;
            return command;
        }

        @Override
        protected boolean requiredArgsInitialized() {
            return (minPoint != null) && (maxPoint != null);
        }

        public RDBoundaryPointsCommandBuilder withMinPoint(int minX, int minY) {
            this.minPoint = new RDPoint(minX, minY);
            return this;
        }

        public RDBoundaryPointsCommandBuilder withMaxPoint(int maxX, int maxY) {
            this.maxPoint = new RDPoint(maxX, maxY);
            return this;
        }
    }
}
