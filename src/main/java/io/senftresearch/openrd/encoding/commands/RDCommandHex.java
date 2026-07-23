package io.senftresearch.openrd.encoding.commands;

public enum RDCommandHex {
    RD_INIT_COMMAND("ca 22"),
    RD_MIN_ELEMENT_ARRAY_POINT("f2 03"),
    RD_MAX_ELEMENT_ARRAY_POINT("f2 04"),
    RD_MIN_ARRAY_POINT("e7 13"),
    RD_MAX_ARRAY_POINT("e7 17"),
    RD_ARRAY_MIRROR("e7 24 00"),
    RD_ARRAY_REPEAT("e7 08 00 01 00 01"),
    RD_ARRAY_ADD("e7 23"),
    RD_REF_POINT_MODE_ZERO("d8 12"),
    RD_REF_POINT_MODE_ONE("d8 11"),
    RD_REF_POINT_MODE_TWO("d8 10"),
    RD_REF_POINT_SET("f0"),
    RD_START_PROCESS("d8 00"),
    RD_STOP_PROCESS("d8 01"),
    RD_PAUSE_PROCESS("d8 02"),
    RD_RESTORE_PROCESS("d8 03"),
    RD_PEN_OFFSET("e7 54 00 00 00 00 00 00 e7 54 01 00 00 00 00 00"),
    RD_LAYER_OFFSET("e7 55 00 00 00 00 00 00 e7 55 01 00 00 00 00 00"),
    RD_DISPLAY_OFFSET("f1 03 00 00 00 00 00 00 00 00 00 00"),
    RD_NAME_MAX_INDEX("f1 01 00"),
    RD_ELEMENT_MAX_INDEX("f1 00 00"),
    RD_ELEMENT_INDEX_AND_NAME("f2 00 00 f2 01 00 f2 02 05 2a 39 1c 41 04 6a 15 08 20"),
    RD_ELEMENT_ARRAY_ADD("f2 06"),
    RD_ELEMENT_ARRAY("f2 07 00 f2 05 00 01 00 01"),
    RD_ARRAY_START("ea 00"),
    RD_ARRAY_END("eb"),
    RD_SET_CURRENT_ARRAY_INDEX("e7 60 00");

    private final String hexCode;
    RDCommandHex(String hexCode){
        this.hexCode = hexCode;
    }

    public String getHexCode() {
        return hexCode;
    }
}
