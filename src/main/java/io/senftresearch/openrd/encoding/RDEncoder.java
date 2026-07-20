package io.senftresearch.openrd.encoding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class RDEncoder {

    public static byte[] encode(String format, Object... args){
        if(format == null || args == null){
            throw new IllegalArgumentException("Args / format cannot be null");
        }
        int argsLength;

        if(format.length() != args.length){
            throw new IllegalArgumentException("Format '" + format + "' length differs from args length=" + args.length + "(Length of format: " + format.length());
        }

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        try{
            for(int i = 0; i < format.length(); i++){
                char formatCharacter = format.charAt(i);
                Object value = args[i];
                byte[] encoded = switch (formatCharacter) {
                    case '-' -> encodeHex((String) value);
                    // Use ((Number) value).doubleValue() to safely extract the double value
                    case 'n' -> encodeNumber(((Number) value).doubleValue());
                    case 'p' -> encodePercent(((Number) value).doubleValue());
                    case 'r' -> encodeRelCoord(((Number) value).doubleValue());
                    case 'b' -> encodeByte(((Number) value).doubleValue());
                    case 'c' -> encodeColour((int[]) value);
                    default  -> throw new IllegalArgumentException("Unknown character in fmt: " + format);
                };

                byteStream.write(encoded);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteStream.toByteArray();
    }

    public static byte[] encodeHex(String value){
        String cleanedValue = Pattern.compile("#.*$", Pattern.MULTILINE).matcher(value).replaceAll("");
        String[] tokens = cleanedValue.trim().split("\\s+");
        if(tokens.length == 1 && tokens[0].isEmpty()){
            return new byte[0];
        }

        byte[] bytes = new byte[tokens.length];
        for(int i = 0; i < tokens.length; i++){
            bytes[i] = (byte) Integer.parseInt(tokens[i], 16);
        }

        return bytes;
    }

    public static byte[] encodeNumber(double value){
        return encodeNumber(value, 5, 1000);
    }
    public static byte[] encodeNumber(double value, int length, int scale){
        List<Integer> bytes = new ArrayList<>();
        long valueMicroMeters = (long) (value * scale);

        while (valueMicroMeters > 0){
            bytes.add((int) (valueMicroMeters & 0x7F));
            valueMicroMeters >>= 7;
        }

        while(bytes.size() < length){
            bytes.add(0);
        }
        Collections.reverse(bytes);

        byte[] byteArray = new byte[length];
        for(int i = 0; i < length ; i++){
            byteArray[i] = bytes.get(i).byteValue();
        }

        return byteArray;
    }

    public static byte[] encodePercent(double value){
        int a = (int) (value * 16383 * 0.01);
        return new byte[] {
                (byte) (a >> 7),
                (byte) (a & 0x7F)
        };
    }

    public static byte[] encodeRelCoord(double value){
        int relCordMicro = (int) Math.round(value * 1000);
        if (relCordMicro > 8191 || relCordMicro < -8191) {
            throw new IllegalArgumentException("relcoord " + relCordMicro + " mm is out of range. Use abscoords!");
        }
        if(relCordMicro < 0){
            relCordMicro += 16384;
        }
        return encodeNumber(relCordMicro, 2, 1);
    }

    public static byte[] encodeByte(double value){
        return encodeNumber(value, 1, 1);
    }

    public static byte[] encodeColour(int[] value){
        int colourValue = ((value[2] & 0xFF) << 16) + ((value[1] & 0xFF) << 8) + (value[0] & 0xFF);
        return encodeNumber(colourValue, 5, 1);
    }
}
