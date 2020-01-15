package virtual_machine;

import java.io.IOException;
import java.io.Reader;

public class Utils {

    public static String readString(Reader r, int len) throws IOException {

        StringBuilder buf = new StringBuilder();
        while (len-- > 0) buf.append((char)r.read());
        return buf.toString();
    }

    public static int readByte(Reader r) throws IOException { return Integer.parseInt(readString(r, 2), 16);}

    public static int readWord(Reader r) throws IOException { return Integer.parseInt(readString(r, 6), 16);}
}
