package io.senftresearch.openrd.data;

import io.senftresearch.openrd.OpenRD;
import io.senftresearch.openrd.RDBoundingBox;
import io.senftresearch.openrd.RDLayer;
import io.senftresearch.openrd.RDPoint;
import io.senftresearch.openrd.encoding.RDEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RDBodyData implements RDData{
    private ByteArrayOutputStream bodyData;

    public RDBodyData(List<RDLayer> layers, RDBoundingBox globalBoundingBox){
        ByteArrayOutputStream bodyDataStream = new ByteArrayOutputStream();
        int layerNumber = 0;

        RDPoint lastPoint = null;
        for(RDLayer layer : layers){


            try{
                proLog(bodyDataStream, layerNumber, layer);
                lastPoint = travelLogic(bodyDataStream, layer, lastPoint);
                this.bodyData = bodyDataStream;
            }
            catch (IOException e){

            }

            layerNumber++;
        }

    }

    private void proLog(ByteArrayOutputStream bodyDataStream, int layerNumber, RDLayer layer ) throws IOException{
        List<RDPoint> powerArray = new ArrayList<>();
        //TODO this can be moved out to a method to set power, set RGB etc.
        while (powerArray.size() < 8) {
            powerArray.add(layer.power());
        }
        RDPoint powerOne = powerArray.get(0);
        RDPoint powerTwo = powerArray.get(1);
        RDPoint powerThree = powerArray.get(2);
        RDPoint powerFour = powerArray.get(3);

        int speed = layer.speed();

        bodyDataStream.write(RDEncoder.encode("-b-", "ca 01 00 ca 02", layerNumber, "ca 01 30 ca 01 10 ca 01 13"));
        bodyDataStream.write(RDEncoder.encode("-n-p-p-p-p-p-p-p-p-",
                "c9 02", speed,
                "c6 15 00 00 00 00 00 c6 16 00 00 00 00 00 c6 01", powerOne.x(),
                "c6 02", powerOne.y(),
                "c6 21", powerTwo.x(),
                "c6 22", powerTwo.y(),
                "c6 05", powerThree.x(),
                "c6 06", powerThree.y(),
                "c6 07", powerFour.x(),
                "c6 08", powerFour.y(),
                "ca 03 01 ca 10 00"));
    }

    private RDPoint travelLogic(ByteArrayOutputStream bodyDataStream, RDLayer layer, RDPoint lastPoint) throws IOException{
        int relCounter = 0;


        for(List<RDPoint> path : layer.paths()) {
            boolean travel = true;

            for (RDPoint point : path) {
                int forceAbs = OpenRD.getInstance().getForceAbs();
                if (lastPoint != null && relok(lastPoint, point) && (forceAbs == 0 || relCounter < forceAbs)) {
                    if (forceAbs > 0) relCounter += 1;

                    if (point.y() == lastPoint.y()) {
                        if (travel) {
                            bodyDataStream.write(RDEncoder.encode("-r", "8a", point.x() - lastPoint.x()));
                        } else {
                            bodyDataStream.write(RDEncoder.encode("-r", "aa", point.x() - lastPoint.x()));
                        }
                    } else if (point.x() == lastPoint.x()) {
                        if (travel) {
                            bodyDataStream.write(RDEncoder.encode("-r", "8b", point.y() - lastPoint.y()));
                        } else {
                            bodyDataStream.write(RDEncoder.encode("-r", "ab", point.y() - lastPoint.y()));
                        }
                    } else {
                        if (travel) {
                            bodyDataStream.write(RDEncoder.encode("-rr", "89", point.x() - lastPoint.x(), point.y() - lastPoint.y()));
                        } else {
                            bodyDataStream.write(RDEncoder.encode("-rr", "a9", point.x() - lastPoint.x(), point.y() - lastPoint.y()));
                        }
                    }
                } else {
                    relCounter = 0;
                    if (travel) {
                        bodyDataStream.write(RDEncoder.encode("-nn", "88", point.x(), point.y()));
                    } else {
                        bodyDataStream.write(RDEncoder.encode("-nn", "a8", point.x(), point.y()));
                    }

                }
                lastPoint = point;
                travel = false;
            }
        }
        return lastPoint;
    }
    private boolean relok(RDPoint lastPoint, RDPoint point){
        //TODO need to investigate this a bit more to figure out how maxRel actually functions
        //8.191 encodes as 3f 7f. -8.191 encodes as 40 01
        double maxRel = 8.191;

        if(lastPoint == null){
            return false;
        }
        int dx = Math.abs(point.x() - lastPoint.x());
        int dy = Math.abs(point.y() - lastPoint.y());
        return Math.max(dx, dy) <= maxRel;
    }

    @Override
    public ByteArrayOutputStream getData() {
        return this.bodyData;
    }
}
