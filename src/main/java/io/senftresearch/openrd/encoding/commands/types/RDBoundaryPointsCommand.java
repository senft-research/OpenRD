package io.senftresearch.openrd.encoding.commands.types;

import io.senftresearch.openrd.RDPoint;
import io.senftresearch.openrd.encoding.RDEncodingType;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;
import io.senftresearch.openrd.encoding.commands.types.process.RDCommandArg;

public class RDBoundaryPointsCommand extends AbstractRDCommand {
    private RDPoint minPoint;
    private RDPoint maxPoint;


    @Override
    public RDCommandArg[] getArgs() {
        return new RDCommandArg[]{

                new RDCommandArg(RDCommandHex.RD_MIN_ARRAY_POINT.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(minPoint.x(), RDEncodingType.NUMBER),
                new RDCommandArg(minPoint.y(), RDEncodingType.NUMBER),
                new RDCommandArg(RDCommandHex.RD_MAX_ARRAY_POINT.getHexCode(), RDEncodingType.HEX),
                new RDCommandArg(maxPoint.x(), RDEncodingType.NUMBER),
                new RDCommandArg(maxPoint.y(), RDEncodingType.NUMBER)
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
