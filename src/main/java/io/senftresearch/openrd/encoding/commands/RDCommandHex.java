package io.senftresearch.openrd.encoding.commands;

public enum RDCommandHex {
    RD_INIT_COMMAND("ca 22"),
    RD_MIN_ARRAY_POINT("f2 03"),
    RD_MAX_ARRAY_POINT("f2 04"),
    RD_REF_POINT_MODE_ZERO("d8 12"),
    RD_REF_POINT_MODE_ONE("d8 11"),
    RD_REF_POINT_MODE_TWO("d8 10"),
    RD_REF_POINT_SET("f0"),
    RD_START_PROCESS("d8 00"),
    RD_STOP_PROCESS("d8 01"),
    RD_PAUSE_PROCESS("d8 02"),
    RD_RESTORE_PROCESS("d8 03");

    private final String hexCode;
    RDCommandHex(String hexCode){
        this.hexCode = hexCode;
    }

    public String getHexCode() {
        return hexCode;
    }
}
