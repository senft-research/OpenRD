package io.senftresearch.openrd.data;

import io.senftresearch.openrd.RDBoundingBox;
import io.senftresearch.openrd.RDLayer;
import io.senftresearch.openrd.RDPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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