package io.senftresearch.openrd;

/**
 * Record representing an RGB colour (with the respective Red, Green, and Blue values being stored).
 * @param red The red value of the colour.
 * @param green The green value of the colour.
 * @param blue The blue value of the colour.
 */
public record RDColour(int red, int green, int blue) {
    public RDColour {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("RGB values must be between 0 and 255");
        }
    }

    /**
     * Retrieves the RGB values of the colour as an integer array, reprenting red, green, and blue values in that order.
     * @return The Integer array representation of the RGB colour.
     */
    public int[] getRGBArray(){
        return new int[] {red, green, blue};
    }
}
