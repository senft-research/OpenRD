package io.senftresearch.openrd.encoding.commands.types.process;

import io.senftresearch.openrd.encoding.RDEncodingType;

public record RDCommandArg(Object value, RDEncodingType type) {
}
