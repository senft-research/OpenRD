package io.senftresearch.openrd;

import java.util.List;

/**
 * Record that represents the properties of a singular Layer of an Rudia file (a Layer is the geometry of a cut-job that
 * has specific power and speed settings, allowing for different effects to other cuts on the same job).
 * @param paths The various paths the cutter is to traverse in the layer.
 * @param speed The speed of the laser cutter (in mm/s) for all operations of this layer.
 * @param power The power levels of this layer (a point representing min and max power).
 * @param colour The colour that the Layer will be represented as when viewed in desktop applications.
 * @param frequency [More information Required]
 * @param boundingBox The bounding box of the layer (i.e. the boundaries of the layer (the top left and bottom right
 *                   points of the layer's boundary box).
 */
public record RDLayer(List<List<RDPoint>> paths, int speed, RDPoint power, RDColour colour,
                      float frequency, RDBoundingBox boundingBox){
}
