package io.senftresearch.openrd.encoding.commands.types.process;

import io.senftresearch.openrd.encoding.EncodingType;

public record RDCommandArg(Object value, EncodingType type) {
}
