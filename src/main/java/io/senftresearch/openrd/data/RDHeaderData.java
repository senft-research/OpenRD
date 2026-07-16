package io.senftresearch.openrd.data;

import io.senftresearch.openrd.RDBoundingBox;
import io.senftresearch.openrd.RDLayer;
import io.senftresearch.openrd.RDPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RDHeaderData implements RDData {
    private RDBoundingBox boundingBox;

    public RDHeaderData(List<RDLayer> layers, RDBoundingBox globalBoundingBox) {
        this.boundingBox = globalBoundingBox;
        layers.forEach(layer -> this.boundingBox = combineBoundingBoxes(this.boundingBox, layer));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            // Not sure what this does
            stream.write(RDEncoder.encodeHex("""
            d8 12           # Red Light on ?
            f0 f1 02 00     # file type ?
            d8 00           # Green Light off ?
            """));

            setBoundingBoxData(stream);

        } catch (IOException e) {
            throw new RuntimeException("Failed to assemble header binary streams", e);
        }
    }

    private void setBoundingBoxData(ByteArrayOutputStream stream) throws IOException {
        int xmin = this.boundingBox.topLeft().x();
        int ymin = this.boundingBox.topLeft().y();
        int xmax = this.boundingBox.bottomRight().x();
        int ymax = this.boundingBox.bottomRight().y();

        stream.write(RDEncoder.encode("-nn", "e7 06", (double) 0, (double) 0));
        stream.write(RDEncoder.encode("-nn", "e7 03", (double) xmin, (double) ymin));
        stream.write(RDEncoder.encode("-nn", "e7 07", (double) xmax, (double) ymax));
        stream.write(RDEncoder.encode("-nn", "e7 50", (double) xmin, (double) ymin));
        stream.write(RDEncoder.encode("-nn", "e7 51", (double) xmax, (double) ymax));
        stream.write(RDEncoder.encode("-nn", "e7 04 00 01 00 01", (double) 0, (double) 0));
        stream.write(RDEncoder.encode("-",   "e7 05 00"));
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
            stream.write(RDEncoder.encode("-bn", "c9 04", layerNumber, speed));

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
        int boundBoxTopLeftY = layer.boundingBox().topLeft().x();
        int boundBoxBottomRightX = layer.boundingBox().bottomRight().x();
        int boundBoxBottomRightY = layer.boundingBox().bottomRight().y();
        stream.write(RDEncoder.encode("-bc-bb-bnn-bnn-bnn-bnn-",
                "c6 06", layerNumber, layer.colour().getRGBArray(),
                "ca 41", layerNumber, 0,
                "e7 52", layerNumber, boundBoxTopLeftX, boundBoxTopLeftY,
                "e7 53", layerNumber, boundBoxBottomRightX, boundBoxBottomRightY,
                "e7 61", layerNumber, boundBoxTopLeftX, boundBoxTopLeftY,
                "e7 62", layerNumber, boundBoxBottomRightX, boundBoxBottomRightY));
    }
    private RDBoundingBox combineBoundingBoxes(RDBoundingBox boundingBox, RDLayer layer) {
        int x0 = Math.min(boundingBox.topLeft().x(), layer.boundingBox().topLeft().x());
        int y0 = Math.min(boundingBox.topLeft().y(), layer.boundingBox().topLeft().y());
        int x1 = Math.max(boundingBox.topLeft().x(), layer.boundingBox().topLeft().x());
        int y1 = Math.max(boundingBox.bottomRight().y(), layer.boundingBox().bottomRight().y());
        return new RDBoundingBox(new RDPoint(x0, y0), new RDPoint(x1, y1));
    }

    @Override
    public String getData() {
        return "";
    }
}