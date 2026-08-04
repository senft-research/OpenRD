package io.senftresearch.openrd;

/**
 * The boundaries of a specific "job" (represented by a box that is defined by the x,y coordinates of the box's top left
 * and bottom right vertices.
 * @param topLeft The top left vertex of the box.
 * @param bottomRight The bottom right vertex of the box.
 */
public record RDBoundingBox(RDPoint topLeft, RDPoint bottomRight) {
}
