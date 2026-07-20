package io.senftresearch.openrd.encoding.commands.types;

import io.senftresearch.openrd.RDPoint;
import io.senftresearch.openrd.encoding.commands.AbstractRDCommand;
import io.senftresearch.openrd.encoding.commands.RDCommandHex;

public class RDBoundaryPointsCommand extends AbstractRDCommand {
    private RDPoint minPoint;
    private RDPoint maxPoint;

    @Override
    public Object[] getCommandArgs() {
        return new Object[]{RDCommandHex.RD_MIN_ARRAY_POINT.getHexCode(), minPoint.x(), minPoint.y(), RDCommandHex.RD_MAX_ARRAY_POINT.getHexCode(), maxPoint.x(), maxPoint.y()};
    }

    public static class RDBoundaryPointsCommandBuilder extends AbstractRDCommandBuilder<RDBoundaryPointsCommandBuilder>{
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

        public RDBoundaryPointsCommandBuilder withMinPoint(int minX, int minY){
            this.minPoint = new RDPoint(minX, minY);
            return this;
        }

        public RDBoundaryPointsCommandBuilder withMaxPoint(int maxX, int maxY){
            this.maxPoint = new RDPoint(maxX, maxY);
            return this;
        }
    }
}
