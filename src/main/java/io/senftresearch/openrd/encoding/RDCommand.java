package io.senftresearch.openrd.encoding;

public enum RDCommand {
    PART_INIT("-b-", "ca 22 %s e7 54 00 00 00 00 00 00 e7 54 01 00 00 00");
    private RDCommand(String format, String encoding){
        this.format = format;
        this.encoding = encoding;
    }
    private String format;
    private String encoding;

    public String getFormat(){
        return this.format;
    }

    public String getEncoding(){
        return this.encoding;
    }
}
