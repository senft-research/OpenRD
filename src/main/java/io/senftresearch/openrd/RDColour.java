package io.senftresearch.openrd;

public record RDColour(int red, int green, int blue) {
    public RDColour {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("RGB values must be between 0 and 255");
        }
    }
}
