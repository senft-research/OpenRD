package io.senftresearch.openrd.data;

import java.io.ByteArrayOutputStream;

/**
 * Interface representing the general functionality of an instance that provides data required during the creation of
 * a {@code .rd} file.
 */
public interface RDData {
    /**
     * Gets the Output Stream representing the data of the instance to be used in file creation.
     * @return The instance's output data.
     */
    ByteArrayOutputStream getData();
}
