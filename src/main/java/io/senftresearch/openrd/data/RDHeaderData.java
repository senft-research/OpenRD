package io.senftresearch.openrd.data;

import io.senftresearch.openrd.RDBoundingBox;
import io.senftresearch.openrd.RDLayer;
import io.senftresearch.openrd.RDPoint;
import io.senftresearch.openrd.encoding.commands.RDCommandSet;
import io.senftresearch.openrd.encoding.RDEncoder;
import io.senftresearch.openrd.encoding.commands.types.layer.RDLayerOffsetCommand;
import io.senftresearch.openrd.encoding.commands.types.part.*;
import io.senftresearch.openrd.encoding.commands.types.props.RDPenOffsetCommand;
import io.senftresearch.openrd.encoding.commands.types.array.*;
import io.senftresearch.openrd.encoding.commands.types.boundaries.RDDocumentPointCommand;
import io.senftresearch.openrd.encoding.commands.types.boundaries.RDFeedRepeatCommand;
import io.senftresearch.openrd.encoding.commands.types.boundaries.RDProcessBoundingBoxCommand;
import io.senftresearch.openrd.encoding.commands.types.element.*;
import io.senftresearch.openrd.encoding.commands.types.layer.RDLayerColourSetCommand;
import io.senftresearch.openrd.encoding.commands.types.layer.RDLayerSpeedCommand;
import io.senftresearch.openrd.encoding.commands.types.process.ProcessType;
import io.senftresearch.openrd.encoding.commands.types.process.RDProcessCommand;
import io.senftresearch.openrd.encoding.commands.types.process.RDProcessRepeatCommand;
import io.senftresearch.openrd.encoding.commands.types.props.RDDisplayOffsetCommand;
import io.senftresearch.openrd.encoding.commands.types.props.RDEnableBlockCuttingCommand;
import io.senftresearch.openrd.encoding.commands.types.props.RDRefPointModeCommand;
import io.senftresearch.openrd.encoding.commands.types.props.RDRefPointSetCommand;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data representing the "Header" of an {@code .rd} file. [Confirmed]The header is broken into the primary header (which
 * establishes the origin and boundaries of the job) and individual headers for each layer (which define the bounding
 * boxes and power levels for each layer).
 */
public class RDHeaderData implements RDData {
    private RDBoundingBox boundingBox;
    private final ByteArrayOutputStream headerData;

    public RDHeaderData(List<RDLayer> layers, RDBoundingBox globalBoundingBox) {

        layers.forEach(layer -> this.boundingBox = combineBoundingBoxes(this.boundingBox, layer));
        this.headerData = new ByteArrayOutputStream();
        try {

            RDCommandSet headerStartSet = new RDCommandSet.RDCommandSetBuilder()
                    .withCommand(new RDRefPointModeCommand
                            .RDRefPointModeCommandBuilder()
                            .withPointMode(0)
                            .build())
                    .withCommand(new RDRefPointSetCommand())
                    .withCommand(new RDEnableBlockCuttingCommand())
                    .withCommand(new RDProcessCommand(ProcessType.START))
                    .build();

            headerData.write(Objects.requireNonNull(RDEncoder.encode(headerStartSet)));

            RDCommandSet headerInitSet = new RDCommandSet.RDCommandSetBuilder()
                    .withCommand(new RDPartInitCommand.RDPartInitCommandBuilder()
                            .withMaxLayerArg(layers.size()-1)
                            .build())
                    .build();

            setBoundingBoxData(headerData);
            setLayerHeaders(layers, headerData);
            headerData.write(RDEncoder.encode(headerInitSet));

            RDCommandSet boundariesCommandSet = new RDCommandSet.RDCommandSetBuilder()
                    .withCommand(new RDPenOffsetCommand())
                    .withCommand(new RDLayerOffsetCommand())
                    .withCommand(new RDDisplayOffsetCommand())
                    .withCommand(new RDElementMaxIndexCommand())
                    .withCommand(new RDElementNameMaxIndexCommand())
                    .withCommand(new RDElementIndexAndNameCommand())
                    .withCommand(new RDElementArrayBoundariesCommand(boundingBox))
                    .withCommand(new RDElementArrayAddCommand(boundingBox.topLeft()))
                    .withCommand(new RDElementArrayCommand(boundingBox.bottomRight()))
                    .withCommand(new RDArrayStartCommand())
                    .withCommand(new RDSetCurrentElementIndexCommand())
                    .withCommand(new RDArrayBoundariesCommand(boundingBox))
                    .withCommand(new RDArrayAddCommand(boundingBox.topLeft()))
                    .withCommand(new RDArrayMirrorCommand())
                    .withCommand(new RDArrayRepeatCommand(boundingBox.bottomRight()))
                    .build();

            headerData.write(RDEncoder.encode(boundariesCommandSet));

        } catch (IOException e) {
            throw new RuntimeException("Failed to assemble header binary streams", e);
        }
    }

    /**
     * Sets the {@linkplain RDBoundingBox Boundaries} of the entire {@code .rd} file ,defining the area (in terms of
     * x,y coordinates) in which the job will be conducted.
     * @param stream The byte array stream representing the {@code .rd} file content.
     * @throws IOException Thrown if any write operation to the byte stream fails.
     */
    private void setBoundingBoxData(ByteArrayOutputStream stream) throws IOException {
        RDCommandSet boundingBoxSet = new RDCommandSet.RDCommandSetBuilder()
                .withCommand(new RDFeedRepeatCommand(0,0))
                .withCommand(new RDProcessBoundingBoxCommand(this.boundingBox))
                .withCommand(new RDDocumentPointCommand(boundingBox))
                .withCommand(new RDProcessRepeatCommand(0, 0))
                .withCommand(new RDArrayDirectionCommand())
                .build();
        stream.write(RDEncoder.encode(boundingBoxSet));
    }

    /**
     * Sets the headers for each {@linkplain RDLayer Layer}, defining both their power levels, speed, and boundaries.
     * @param layers The layers to create headers for.
     * @param stream The byte array stream representing the {@code .rd} file content.
     * @throws IOException Thrown if any write operation to the byte stream fails.
     */
    private void setLayerHeaders(List<RDLayer> layers, ByteArrayOutputStream stream) throws IOException {
        int layerNumber = 0;
        for (RDLayer layer : layers) {
            setLayerSpeedAndPowerLevels(layer, stream, layerNumber);
            setLayerBoundingBoxes(layer, layerNumber, stream);
            layerNumber++;
        }
    }

    /**
     * Sets both the power levels and speed of the layer within the Header.
     * @param layer The layer to set.
     * @param stream The byte array stream representing the {@code .rd} file content.
     * @param layerNumber The number of the layer in relation to its ordering in the file (layers need to define their
     *                   numerical value within the RD file, hence need to specify their number.
     * @throws IOException Thrown if any write operation to the byte stream fails.
     */
    private void setLayerSpeedAndPowerLevels(RDLayer layer, ByteArrayOutputStream stream, int layerNumber) throws IOException {
        List<RDPoint> powerArray = new ArrayList<>();
        while (powerArray.size() < 8) {
            powerArray.add(layer.power());
        }
        int speed = layer.speed();

        RDPoint powerOne = powerArray.get(0);
        RDPoint powerTwo = powerArray.get(1);
        RDPoint powerThree = powerArray.get(2);
        RDPoint powerFour = powerArray.get(3);
        RDCommandSet speedSet = new RDCommandSet.RDCommandSetBuilder()
                .withCommand(new RDLayerSpeedCommand(layerNumber, speed))
                .build();

        stream.write(RDEncoder.encode(speedSet));

        RDCommandSet powerSet = new RDCommandSet.RDCommandSetBuilder()
                .withCommand(new RDPartPowerCommand.RDPartPowerCommandBuilder()
                        .withPower(powerOne, 1)
                        .withPower(powerTwo, 2)
                        .withPower(powerThree, 3)
                        .withPower(powerFour, 4)
                        .withLayerNumber(layerNumber)
                        .build()).build();
        stream.write(RDEncoder.encode(powerSet));
    }

    /**
     * Sets the {@linkplain RDBoundingBox boundaries} of a specified layer, defining the boundaries in which the layer
     * will perform its job.
     * @param layer The layer to set the boundaries of.
     * @param layerNumber The number of the layer in relation to its ordering in the file (layers need to define their
     *                   numerical value within the RD file, hence need to specify their number.
     * @param stream The byte array stream representing the {@code .rd} file content.
     * @throws IOException Thrown if any write operation to the byte stream fails.
     * */
    private void setLayerBoundingBoxes(RDLayer layer, int layerNumber, ByteArrayOutputStream stream) throws IOException{

        RDCommandSet layerBoundingBoxSet = new RDCommandSet.RDCommandSetBuilder()
                .withCommand(new RDLayerColourSetCommand(layer.colour(), layerNumber))
                .withCommand(new RDPartWorkModeCommand(layerNumber))
                .withCommand(new RDPartPointsCommand(layer.boundingBox(), layerNumber))
                .withCommand(new RDPartPointsExCommand(layer.boundingBox(), layerNumber))
                .build();

        stream.write(RDEncoder.encode(layerBoundingBoxSet));
    }

    private RDBoundingBox combineBoundingBoxes(RDBoundingBox boundingBox, RDLayer layer) {
        if(boundingBox == null) return layer.boundingBox();
        RDBoundingBox layerBox = layer.boundingBox();
        int x0 = Math.min(boundingBox.topLeft().x(), layerBox.topLeft().x());
        int y0 = Math.min(boundingBox.topLeft().y(), layerBox.topLeft().y());

        int x1 = Math.max(boundingBox.bottomRight().x(), layerBox.bottomRight().x());
        int y1 = Math.max(boundingBox.bottomRight().y(), layerBox.bottomRight().y());

        return new RDBoundingBox(new RDPoint(x0, y0), new RDPoint(x1, y1));
    }

    @Override
    public ByteArrayOutputStream getData() {
        return this.headerData;
    }
}