package io.senftresearch.openrd;

import java.util.List;

public record RDLayer(List<List<RDPoint>> paths, int speed, RDPoint power, RDColour colour,
                      float frequency, RDBoundingBox boundingBox){
}
