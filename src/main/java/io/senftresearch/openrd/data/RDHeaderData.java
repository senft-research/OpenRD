package io.senftresearch.openrd.data;

import io.senftresearch.openrd.RDBoundingBox;
import io.senftresearch.openrd.RDLayer;
import io.senftresearch.openrd.RDPoint;
import io.senftresearch.openrd.encoding.commands.RDCommandSet;
import io.senftresearch.openrd.encoding.RDEncoder;
import io.senftresearch.openrd.encoding.commands.RDCommand;
import io.senftresearch.openrd.encoding.commands.RDLayerOffsetCommand;
import io.senftresearch.openrd.encoding.commands.RDPenOffsetCommand;
import io.senftresearch.openrd.encoding.commands.types.*;
import io.senftresearch.openrd.encoding.commands.types.array.*;
import io.senftresearch.openrd.encoding.commands.types.boundaries.RDDocumentPointCommand;
import io.senftresearch.openrd.encoding.commands.types.boundaries.RDFeedRepeatCommand;
import io.senftresearch.openrd.encoding.commands.types.boundaries.RDProcessBoundingBoxCommand;
import io.senftresearch.openrd.encoding.commands.types.element.*;
import io.senftresearch.openrd.encoding.commands.types.layer.RDLayerSpeedCommand;
import io.senftresearch.openrd.encoding.commands.types.process.ProcessType;
import io.senftresearch.openrd.encoding.commands.types.process.RDProcessCommand;
import io.senftresearch.openrd.encoding.commands.types.process.RDProcessRepeatCommand;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

            RDCommand initCommand = new RDPartInitCommand.RDPartInitCommandBuilder()
                    .withMaxLayerArg(layers.size()-1)
                    .build();

            RDCommandSet headerInitSet = new RDCommandSet.RDCommandSetBuilder()
                    .withCommand(initCommand)
                    .build();

            setBoundingBoxData(headerData);
            setLayerHeaders(layers, headerData);
            headerData.write(RDEncoder.encode(headerInitSet));

            RDCommandSet offsetCommandSet = new RDCommandSet.RDCommandSetBuilder()
                    .withCommand(new RDPenOffsetCommand())
                    .withCommand(new RDLayerOffsetCommand())
                    .build();
            headerData.write(RDEncoder.encode(offsetCommandSet));

            RDCommandSet boundariesCommandSet = new RDCommandSet.RDCommandSetBuilder()
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

    private void setLayerHeaders(List<RDLayer> layers, ByteArrayOutputStream stream) throws IOException {
        int layerNumber = 0;
        for (RDLayer layer : layers) {
            List<RDPoint> powerArray = new ArrayList<>();
            //TODO this can be moved out to a method to set power, set RGB etc.
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
            stream.write(RDEncoder.encode("-bp-bp", "c6 31", layerNumber, powerOne.x(), "c6 32", layerNumber, powerOne.y()));
            stream.write(RDEncoder.encode("-bp-bp", "c6 41", layerNumber, powerTwo.x(), "c6 42", layerNumber, powerTwo.y()));
            stream.write(RDEncoder.encode("-bp-bp", "c6 35", layerNumber, powerThree.x(), "c6 36", layerNumber, powerThree.y()));
            stream.write(RDEncoder.encode("-bp-bp", "c6 37", layerNumber, powerFour.x(), "c6 38", layerNumber, powerFour.y()));

            setLayerBoundingBoxes(layer, layerNumber, stream);
            layerNumber++;
        }
    }

    private void setLayerBoundingBoxes(RDLayer layer, int layerNumber, ByteArrayOutputStream stream) throws IOException{
        int boundBoxTopLeftX = layer.boundingBox().topLeft().x();
        int boundBoxTopLeftY = layer.boundingBox().topLeft().y();
        int boundBoxBottomRightX = layer.boundingBox().bottomRight().x();
        int boundBoxBottomRightY = layer.boundingBox().bottomRight().y();
        stream.write(RDEncoder.encode("-bc-bb-bnn-bnn-bnn-bnn-",
                "ca 06", layerNumber, layer.colour().getRGBArray(),
                "ca 41", layerNumber, 0,
                "e7 52", layerNumber, boundBoxTopLeftX, boundBoxTopLeftY,
                "e7 53", layerNumber, boundBoxBottomRightX, boundBoxBottomRightY,
                "e7 61", layerNumber, boundBoxTopLeftX, boundBoxTopLeftY,
                "e7 62", layerNumber, boundBoxBottomRightX, boundBoxBottomRightY,""));
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