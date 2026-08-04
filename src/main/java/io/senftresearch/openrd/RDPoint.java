package io.senftresearch.openrd;

/**
 * Simple record that represents a 2-tuple pair (typically x and y coordinates)
 * @param x The first element of the Point (typically the x coordinate).
 * @param y The second element of the Point (typically the y coordinate).
 */
public record RDPoint(int x, int y) {
}
